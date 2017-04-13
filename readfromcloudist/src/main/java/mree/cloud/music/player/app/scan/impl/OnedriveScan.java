package mree.cloud.music.player.app.scan.impl;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.util.List;

import mree.cloud.music.player.app.act.AccountSetActivity;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.report.AnswersImpl;
import mree.cloud.music.player.app.scan.IScan;
import mree.cloud.music.player.app.services.CmpDeviceService;
import mree.cloud.music.player.app.tasks.RefreshTokenTask;
import mree.cloud.music.player.app.utils.Constants;
import mree.cloud.music.player.app.utils.RandomStringUtils;
import mree.cloud.music.player.app.utils.RestHelper;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.model.SourceInfo;
import mree.cloud.music.player.common.model.onedrive.Item;
import mree.cloud.music.player.common.ref.ScanStatus;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.audio.AudioStatus;
import mree.cloud.music.player.rest.ok.OnedriveOkHttp;
import mree.cloud.music.player.rest.utils.UnauthorizedException;

/**
 * Created by eercan on 02.09.2016.
 */
public class OnedriveScan implements IScan{


    private static final String TAG = OnedriveScan.class.getSimpleName();
    private boolean isRunning;
    //private OneDriveRestClient oneDriveRestClient;
    private OnedriveOkHttp okHttp;
    private String first = "/";
    private Context context;
    private SourceInfo sourceInfo;
    private int count;
    private Thread scanThread;

    public OnedriveScan(Context context, SourceInfo sourceInfo){
        this.context = context;
        this.sourceInfo = sourceInfo;
        String accessToken = sourceInfo.getAccessToken();
        okHttp = RestHelper.getOnedriveOkHttp(sourceInfo.getId(), accessToken);
    }

    public static Item getItemById(String accId, String id, OnedriveOkHttp okHttp){
        Item newItem = null;
        try {
            newItem = okHttp.getItemInfoById(id);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            try {
                RefreshTokenTask task = new RefreshTokenTask(accId, okHttp, null);
                task.run();
                while (!task.isFinished()) {

                }
                newItem = okHttp.getItemInfoById(id);
            } catch (Exception e1) {
                Log.e(TAG, e1.getMessage() + "");
            }
        }
        return newItem;
    }


    @Override
    public void start(){

        scanThread = new Thread(new Runnable(){
            @Override
            public void run(){
                Item newItem = getItemByPath(first);

                if (newItem != null && newItem.Folder != null && newItem.Children != null) {
                    startScan(newItem, first);
                }
            }
        });
        scanThread.start();
        AnswersImpl.scanAccountStart(sourceInfo.getId(), sourceInfo.getType());
        DbEntryService.updateAccountScanStatus(sourceInfo.getId(), ScanStatus.STARTED);
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

    private synchronized void startScan(Item newItem, String path){

        try {
            int folderSongCount = 0;
            List<String> folders = CmpDeviceService.getVisitedFolders(sourceInfo.getId());
            if (!folders.contains(path)) {
                count++;
                for (Item child : newItem.Children) {
                    if (child != null && child.Folder != null) {

                        String s = path + child.Name + "/";
                        if (!folders.contains(s)) {
                            Item grandChild = child;
                            try {
                                grandChild = getItemById(child.Id);
                            } catch (Exception e) {
                                handleException(e);
                            }
                            startScan(grandChild, s);
                        }
                    } else if (child.Audio != null) {

                        try {
                            child = getItemById(child.Id);
                        } catch (Exception e) {
                            handleException(e);
                        }

                        SongInfo si = child.Audio.toInfo();
                        si.setSourceType(SourceType.ONEDRIVE);
                        si.setId(child.Id);
                        si.setDownloadUrl(child.Content_downloadUrl);
                        si.setFileName(child.Name);

                        if (child.Audio.Artist != null && !"".equals(child.Audio.Artist)) {
                            si.setArtist(child.Audio.Artist);
                        } else {
                            si.setArtist(Constants.DEFAULT_ARTIST);
                        }

                        if (child.Audio.Album != null && !"".equals(child.Audio.Album)) {
                            si.setAlbum(child.Audio.Album);
                        } else {
                            si.setAlbum(Constants.DEFAULT_ALBUM);
                        }


                        if (child.Audio.Title == null || "".equals(child.Audio.Title)) {
                            si.setTitle(child.Name);
                        } else {
                            si.setTitle(child.Audio.Title);
                        }


                        if (child.Thumbnails != null && child.Thumbnails.size() > 0) {
                            String filename = RandomStringUtils.randomAlphanumeric(25);
                            String url = child.Thumbnails.get(0).Large.Url;
                            si.setThumbnail(filename);
                           /* AudioFragment.getThumbThreadPoolExecutor().submit(new
                                    DownloadThumbnailTask
                                    (context, filename, url));*/
                        }

                        si.setPath(path + child.Name);
                        si.setAccountId(sourceInfo.getId());
                        si.setStatus(AudioStatus.ONLINE);
                        DbEntryService.saveAudio(si);
                        folderSongCount++;
                    /*    Message m = new Message();
                        Bundle b = new Bundle();
                        b.putSerializable(Constants.AUDIO_OBJECT, si);
                        m.setData(b);
                        SettingsActivity.getScanHandler().sendMessage(m);*/

                    }

                    if (folderSongCount % 5 == 0) {
                        notifyAccountSetActivity();
                    }
                }

                folders.add(path);
                CmpDeviceService.setVisitedFolders(sourceInfo.getId(), folders);
                Log.e(TAG, "Folder scanned: " + path);
                count--;
                if (count == 0) {
                    DbEntryService.updateAccountScanStatus(sourceInfo.getId(), ScanStatus
                            .FINISHED);
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
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            DbEntryService.updateAccountScanStatus(sourceInfo.getId(), ScanStatus.FAILED);
        }
    }

    private void handleException(Exception e){

    }


    private Item getItemByPath(String path){
        Item newItem = null;
        try {
            newItem = okHttp.getItemInfoByPath(path);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            try {
                UnauthorizedException ue = (UnauthorizedException) e;
                RefreshTokenTask task = new RefreshTokenTask(sourceInfo, okHttp, "");
                task.run();
                while (!task.isFinished()) {

                }
                newItem = okHttp.getItemInfoByPath(path);
            } catch (Exception e1) {
                handleException(e);
            }


        }

        return newItem;
    }

    private Item getItemById(String id){
        Item newItem = null;
        try {
            newItem = okHttp.getItemInfoById(id);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            try {
                RefreshTokenTask task = new RefreshTokenTask(sourceInfo, okHttp, null);
                task.run();
                while (!task.isFinished()) {

                }
                newItem = okHttp.getItemInfoById(id);
            } catch (Exception e1) {
                handleException(e);
            }


        }
        return newItem;
    }


    @Override
    public void notifyAccountSetActivity(){
        AccountSetActivity.getRefreshHandler().sendEmptyMessage(25);
    }

    @Override
    public boolean isRunning(){
        return isRunning;
    }

    @Override
    public String getAccountId(){
        return sourceInfo.getId();
    }
}
