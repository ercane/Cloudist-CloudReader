package mree.cloud.music.player.app.scan.impl;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import java.util.ArrayList;
import java.util.List;

import mree.cloud.music.player.app.act.AccountSetActivity;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.report.AnswersImpl;
import mree.cloud.music.player.app.scan.IScan;
import mree.cloud.music.player.app.tasks.RefreshTokenTask;
import mree.cloud.music.player.app.utils.Constants;
import mree.cloud.music.player.app.utils.RestHelper;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.model.SourceInfo;
import mree.cloud.music.player.common.model.box.Entry;
import mree.cloud.music.player.common.model.box.File;
import mree.cloud.music.player.common.model.box.FolderItems;
import mree.cloud.music.player.common.ref.ScanStatus;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.audio.AudioStatus;
import mree.cloud.music.player.rest.ok.BoxOkHttp;
import mree.cloud.music.player.rest.utils.UnauthorizedException;

/**
 * Created by eercan on 03.03.2017.
 */

public class BoxScan implements IScan{
    private static final String TAG = BoxScan.class.getSimpleName();
    private static final String ROOT_ID = "0";
    //private static BoxRestClient restClient;
    private static BoxOkHttp okHttp;
    private static int limit = 100;
    private Context context;
    private SourceInfo sourceInfo;
    private int count;
    private Thread scanThread;
    private String extensions;
    private int folderSongCount = 0;

    public BoxScan(Context context, SourceInfo sourceInfo){
        this.context = context;
        this.sourceInfo = sourceInfo;
        extensions = "flac,mp3,wav,aac,m4a";
        okHttp = RestHelper.getBoxOkHttpClient(sourceInfo.getId(), sourceInfo.getAccessToken());
    }

    @Override
    public void start(){
        scanThread = new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    List<Entry> rootFolder = getAllFolderItems(ROOT_ID);
                    startScan(ROOT_ID, rootFolder);
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });
        scanThread.start();
        AnswersImpl.scanAccountStart(sourceInfo.getId(), sourceInfo.getType());
        Answers.getInstance().logCustom(new CustomEvent("Box Scan")
                .putCustomAttribute("State", ScanStatus.STARTED.getDesc()));
        DbEntryService.updateAccountScanStatus(sourceInfo.getId(), ScanStatus.STARTED);
    }

    private synchronized void startScan(String folderId, List<Entry> entries){

        try {

            for (Entry e : entries) {
                if (e.getType().equals("folder")) {
                    List<Entry> items = getAllFolderItems(e.getId());
                    startScan(e.getId(), items);
                } else if (e.getType().equals("file")) {
                    String name = e.getName().toString();
                    String ext = name.substring(name.lastIndexOf('.') + 1, name.length());
                    if (ext != null && extensions.contains(ext)) {
                        File i = getFileInfo(e.getId());
                        if (i != null) {
                            SongInfo si = new SongInfo();
                            si.setAccountId(sourceInfo.getId());
                            si.setSourceType(SourceType.BOX);
                            si.setAlbum(Constants.DEFAULT_ALBUM);
                            si.setArtist(Constants.DEFAULT_ARTIST);
                            si.setTitle(i.getName());
                            si.setFileName(i.getName());
                            si.setId(i.getId());
                            si.setStatus(AudioStatus.ONLINE);
                            DbEntryService.saveAudio(si);
                            folderSongCount++;
                        }
                    }

                    if (folderSongCount % 5 == 0) {
                        notifyAccountSetActivity();
                    }
                }

            }

            if (ROOT_ID.equals(folderId)) {
                AnswersImpl.scanAccountFinish(sourceInfo.getId(), sourceInfo.getType());
                DbEntryService.updateAccountScanStatus(sourceInfo.getId(), ScanStatus
                        .FINISHED);
                DbEntryService.updateAccountScannedSongs(sourceInfo.getId(), folderSongCount);
                if (AccountSetActivity.getRefreshHandler() != null) {
                    Message m = new Message();
                    Bundle b = new Bundle();
                    b.putSerializable(AccountSetActivity.ACC_ID, sourceInfo.getId());
                    m.setData(b);
                    AccountSetActivity.getRefreshHandler().sendMessage(m);
                }
                Log.e(TAG, "SCAN FINISHED: " + folderSongCount);
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            DbEntryService.updateAccountScanStatus(sourceInfo.getId(), ScanStatus.FAILED);
        }
    }

    @Override
    public void stop() throws NullPointerException{
        if (scanThread != null) {
            if (!scanThread.isInterrupted()) {
                scanThread.interrupt();
            }
        } else {
            throw new NullPointerException("Thread is null");
        }
    }

    @Override
    public void resume() throws InterruptedException{
        if (scanThread != null) {
            if (scanThread.isInterrupted()) {
                scanThread.join();
            }
        }
    }

    private List<Entry> getAllFolderItems(String folderId){
        List<Entry> items = new ArrayList<>();
        try {
            int offset = 0;
            int size = limit;
            while (size >= limit) {
                FolderItems files = okHttp.getFolderItems(folderId, limit, offset);
                items.addAll(files.getEntries());
                offset += limit;
                size = files.getEntries().size();
            }
            return items;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            try {
                UnauthorizedException ue = (UnauthorizedException) e;
                RefreshTokenTask task = new RefreshTokenTask(sourceInfo.getId(), okHttp, "");
                task.run();
                while (!task.isFinished()) {

                }
                int offset = 0;
                int size = limit;
                while (size >= limit) {
                    FolderItems files = okHttp.getFolderItems(folderId, limit, offset);
                    items.addAll(files.getEntries());
                    offset += limit;
                    size = files.getEntries().size();
                }
                return items;
            } catch (Exception e1) {
                Log.e(TAG, e1.getMessage() + "");
                return items;
            }
        }
    }

    private File getFileInfo(String fileId){
        try {
            return okHttp.getFileInfo(fileId);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            try {
                UnauthorizedException ue = (UnauthorizedException) e;
                RefreshTokenTask task = new RefreshTokenTask(sourceInfo.getId(), okHttp, "");
                task.run();
                while (!task.isFinished()) {

                }
                return okHttp.getFileInfo(fileId);
            } catch (Exception e1) {
                Log.e(TAG, e1.getMessage() + "");
                return null;
            }
        }
    }

    @Override
    public boolean isRunning(){
        return false;
    }

    @Override
    public String getAccountId(){
        return null;
    }

    @Override
    public void notifyAccountSetActivity(){
        AccountSetActivity.getRefreshHandler().sendEmptyMessage(25);
    }
}
