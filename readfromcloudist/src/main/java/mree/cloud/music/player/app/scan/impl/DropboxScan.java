package mree.cloud.music.player.app.scan.impl;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import mree.cloud.music.player.app.act.AccountSetActivity;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.report.AnswersImpl;
import mree.cloud.music.player.app.scan.IScan;
import mree.cloud.music.player.app.utils.Constants;
import mree.cloud.music.player.app.utils.RestHelper;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.model.SourceInfo;
import mree.cloud.music.player.common.model.dropbox.Entry;
import mree.cloud.music.player.common.model.dropbox.ListFolder;
import mree.cloud.music.player.common.ref.ScanStatus;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.audio.AudioStatus;
import mree.cloud.music.player.rest.ok.DropboxOkHttp;

/**
 * Created by mree on 09.01.2017.
 */

public class DropboxScan implements IScan{

    private static final String TAG = DropboxScan.class.getSimpleName();
    final List<String> extensions = Arrays.asList(".mp3", ".wav", ".wma", ".acc");
    private boolean isRunning;
    private DropboxOkHttp dropbox;
    private String first = "/";
    private Context context;
    private SourceInfo sourceInfo;
    private int count = 0;
    private Thread scanThread;

    public DropboxScan(Context context, SourceInfo sourceInfo){
        this.context = context;
        this.sourceInfo = sourceInfo;
    }

    @Override
    public void start(){
        String accessToken = sourceInfo.getAccessToken();
        dropbox = RestHelper.getDropboxOkHttp(sourceInfo.getId(), accessToken);
        scanThread = new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    ListFolder response = dropbox.listFolder("", false);
                    response.setPath(first);
                    Log.e(TAG, response + "");
                    startScan(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        scanThread.start();
        DbEntryService.updateAccountScanStatus(sourceInfo.getId(), ScanStatus.STARTED);
        AnswersImpl.scanAccountStart(sourceInfo.getId(), sourceInfo.getType());
    }

    private void startScan(ListFolder folder){
        if (folder != null) {
            try {
                for (Entry e : folder.getEntries()) {
                    if (e.getTag().equals("folder")) {
                        ListFolder response = dropbox.listFolder(e.getPathLower(), false);
                        response.setPath(e.getPathLower());
                        startScan(response);
                    } else if (e.getTag().equals("file")) {
                        String substring = e.getName().substring(e.getName().lastIndexOf("."), e
                                .getName().length());
                        if (extensions.contains(substring)) {
                            SongInfo si = new SongInfo();
                            si.setId(e.getId());
                            si.setFileName(e.getName());
                            si.setTitle(e.getName());
                            si.setArtist(Constants.DEFAULT_ARTIST);
                            si.setAlbum(Constants.DEFAULT_ALBUM);
                            si.setSourceType(SourceType.DROPBOX);
                            si.setAccountId(sourceInfo.getId());
                            si.setPath(e.getPathLower());
                            si.setStatus(AudioStatus.ONLINE);
                            DbEntryService.saveAudio(si);
                            count++;
                        }

                    }
                    if (count % 5 == 0) {
                        notifyAccountSetActivity();
                    }
                }

                if (first.equals(folder.getPath())) {
                    DbEntryService.updateAccountScanStatus(sourceInfo.getId(), ScanStatus
                            .FINISHED);
                    DbEntryService.updateAccountScannedSongs(sourceInfo.getId(), count);
                    Log.e(TAG, "SCAN FINISHED");
                    AnswersImpl.scanAccountFinish(sourceInfo.getId(), sourceInfo.getType());
                    if (AccountSetActivity.getRefreshHandler() != null) {
                        Message m = new Message();
                        Bundle b = new Bundle();
                        b.putSerializable(AccountSetActivity.ACC_ID, sourceInfo.getId());
                        m.setData(b);
                        AccountSetActivity.getRefreshHandler().sendMessage(m);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage() + "");
            }
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
