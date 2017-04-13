package mree.cloud.music.player.app.scan.impl;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mree.cloud.music.player.app.act.AccountSetActivity;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.report.AnswersImpl;
import mree.cloud.music.player.app.scan.IScan;
import mree.cloud.music.player.app.services.CmpDeviceService;
import mree.cloud.music.player.app.tasks.RefreshTokenTask;
import mree.cloud.music.player.app.utils.Constants;
import mree.cloud.music.player.app.utils.RestHelper;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.model.SourceInfo;
import mree.cloud.music.player.common.model.google.File;
import mree.cloud.music.player.common.model.google.FileList;
import mree.cloud.music.player.common.model.google.Permission;
import mree.cloud.music.player.common.ref.ScanStatus;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.audio.AudioStatus;
import mree.cloud.music.player.rest.ok.GoogleOkHttp;
import mree.cloud.music.player.rest.utils.UnauthorizedException;

/**
 * Created by eercan on 17.11.2016.
 */

public class GoogleScan implements IScan{
    private static final String TAG = GoogleScan.class.getSimpleName();
    private static final String FOLDER_MIME = "application/vnd.google-apps.folder";
    //private static GoogleRestClient restClient;
    private static GoogleOkHttp okHttp;
    private Context context;
    private SourceInfo sourceInfo;
    private int count;
    private Thread scanThread;

    public GoogleScan(Context context, SourceInfo sourceInfo){
        this.context = context;
        this.sourceInfo = sourceInfo;
    }

    public static String getUrl(String accId, String id, GoogleOkHttp grc){
        try {
            File f = grc.getFileUrl(id);
            return f.getWebContentLink();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            try {
                UnauthorizedException ue = (UnauthorizedException) e;
                RefreshTokenTask task = new RefreshTokenTask(DbEntryService.getAccountInfo
                        (accId), grc, null);
                task.run();
                while (!task.isFinished()) {

                }
                File f = grc.getFileUrl(id);
                return f.getWebContentLink();
            } catch (Exception e1) {
                Log.e(TAG, e1.getMessage());
            }
        }
        return null;
    }

    @Override
    public void start(){
        String accessToken = sourceInfo.getAccessToken();
        //restClient = RestHelper.getGoogleRestClient(sourceInfo.getId(), accessToken);
        okHttp = RestHelper.getGoogleOkHttp(sourceInfo.getId(), accessToken);

        scanThread = new Thread(new Runnable(){
            @Override
            public void run(){
                List<File> items = getRootFileList();
                if (items != null) {
                    startScan(items);
                }
            }
        });
        scanThread.start();
        DbEntryService.updateAccountScanStatus(sourceInfo.getId(), ScanStatus.STARTED);
        AnswersImpl.scanAccountStart(sourceInfo.getId(), sourceInfo.getType());
    }


    private synchronized void startScan(List<File> items){

        try {
            int folderSongCount = 0;
            List<String> folders = CmpDeviceService.getVisitedFolders(sourceInfo.getId());

            for (File file : items) {
                count++;
                SongInfo si = new SongInfo();
                si.setSourceType(SourceType.GOOGLE_DRIVE);
                si.setAccountId(sourceInfo.getId());
                si.setTitle(file.getName());
                si.setDownloadUrl(file.getWebContentLink());
                si.setThumbnail(file.getThumbnailLink());
                si.setAlbum(Constants.DEFAULT_ALBUM);
                si.setArtist(Constants.DEFAULT_ARTIST);
                si.setId(file.getId());
                si.setCreatedDate(new Date());
                si.setStatus(AudioStatus.ONLINE);
                DbEntryService.saveAudio(si);
                folderSongCount++;
                Permission p = new Permission();
                p.setRole("reader");
                p.setType("anyone");
                setPermisson(si.getId(), p);

                if (count % 5 == 0) {
                    notifyAccountSetActivity();
                }
            }

            //if (count == 0) {
            DbEntryService.updateAccountScanStatus(sourceInfo.getId(), ScanStatus
                    .FINISHED);
            DbEntryService.updateAccountScannedSongs(sourceInfo.getId(), folderSongCount);
            Log.e(TAG, "SCAN FINISHED");
            AnswersImpl.scanAccountFinish(sourceInfo.getId(), sourceInfo.getType());
            if (AccountSetActivity.getRefreshHandler() != null) {
                Message m = new Message();
                Bundle b = new Bundle();
                b.putSerializable(AccountSetActivity.ACC_ID, sourceInfo.getId());
                m.setData(b);
                AccountSetActivity.getRefreshHandler().sendMessage(m);
            }
            //finished

        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            DbEntryService.updateAccountScanStatus(sourceInfo.getId(), ScanStatus.FAILED);
        }
    }

    private void setPermisson(String id, Permission p){
        try {
            okHttp.setPermission(id, p);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            try {
                UnauthorizedException ue = (UnauthorizedException) e;
                RefreshTokenTask task = new RefreshTokenTask(sourceInfo, okHttp, null);
                task.run();
                while (!task.isFinished()) {

                }
                okHttp.setPermission(id, p);
            } catch (Exception e1) {
                Log.e(TAG, e1.getMessage());
            }

        }
    }

    private File getFileById(String id){
        File file = null;
        try {
            file = okHttp.getFile(id);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            try {
                UnauthorizedException ue = (UnauthorizedException) e;
                RefreshTokenTask task = new RefreshTokenTask(sourceInfo, okHttp, null);
                task.run();
                while (!task.isFinished()) {

                }
                file = okHttp.getFile(id);
            } catch (Exception e1) {
                Log.e(TAG, e1.getMessage());
            }
        }
        return file;
    }

    private List<File> getRootFileList(){
        FileList fileList = null;
        List<File> list = new ArrayList<>();
        try {
            fileList = okHttp.getRootFileList();
            if (fileList != null && fileList.getFiles() != null) {
                list.addAll(fileList.getFiles());
                while (!TextUtils.isEmpty(fileList.getNextPageToken())) {
                    fileList = okHttp.getRootFileList(fileList.getNextPageToken());
                    list.addAll(fileList.getFiles());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            try {
                UnauthorizedException ue = (UnauthorizedException) e;
                RefreshTokenTask task = new RefreshTokenTask(sourceInfo, okHttp, null);
                task.run();
                while (!task.isFinished()) {

                }
                fileList = okHttp.getRootFileList();
                if (fileList != null && fileList.getFiles() != null) {
                    list.addAll(fileList.getFiles());
                    while (!TextUtils.isEmpty(fileList.getNextPageToken())) {
                        fileList = okHttp.getRootFileList(fileList.getNextPageToken());
                        list.addAll(fileList.getFiles());
                    }
                }
            } catch (Exception e1) {
                Log.e(TAG, e1.getMessage());
            }
        }
        return list;
    }

    private List<File> getFileList(String id){
        FileList fileList = null;
        List<File> list = new ArrayList<>();
        try {
            fileList = okHttp.getFileList(id);
            if (fileList != null && fileList.getFiles() != null) {
                list.addAll(fileList.getFiles());
                while (!TextUtils.isEmpty(fileList.getNextPageToken())) {
                    fileList = okHttp.getFileList(id, fileList.getNextPageToken());
                    if (fileList.getFiles() != null) {
                        list.addAll(fileList.getFiles());
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            try {
                UnauthorizedException ue = (UnauthorizedException) e;
                RefreshTokenTask task = new RefreshTokenTask(sourceInfo, okHttp, null);
                task.run();
                while (!task.isFinished()) {

                }
                fileList = okHttp.getFileList(id);
                if (fileList != null && fileList.getFiles() != null) {
                    list.addAll(fileList.getFiles());
                    while (!TextUtils.isEmpty(fileList.getNextPageToken())) {
                        fileList = okHttp.getFileList(id, fileList.getNextPageToken());
                        if (fileList.getFiles() != null) {
                            list.addAll(fileList.getFiles());
                        }
                    }
                }
            } catch (Exception e1) {
                Log.e(TAG, e1.getMessage());
            }
        }

        return list;
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
