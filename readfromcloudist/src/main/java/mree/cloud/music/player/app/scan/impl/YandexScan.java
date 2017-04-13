package mree.cloud.music.player.app.scan.impl;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import mree.cloud.music.player.app.act.AccountSetActivity;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.report.AnswersImpl;
import mree.cloud.music.player.app.scan.IScan;
import mree.cloud.music.player.app.utils.Constants;
import mree.cloud.music.player.app.utils.RestHelper;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.model.SourceInfo;
import mree.cloud.music.player.common.model.yandex.FileList;
import mree.cloud.music.player.common.model.yandex.Item;
import mree.cloud.music.player.common.ref.ScanStatus;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.audio.AudioStatus;
import mree.cloud.music.player.rest.ok.YandexOkHttp;

/**
 * Created by eercan on 25.01.2017.
 */

public class YandexScan implements IScan{
    private static final String TAG = YandexScan.class.getSimpleName();
    //private static YandexRestClient restClient;
    private static YandexOkHttp okHttp;
    private static int limit = 100;
    private Context context;
    private SourceInfo sourceInfo;
    private int count;
    private Thread scanThread;
    private List<Item> list;

    public YandexScan(Context context, SourceInfo sourceInfo){
        this.context = context;
        this.sourceInfo = sourceInfo;
    }

    @Override
    public void start(){
        String accessToken = sourceInfo.getAccessToken();
        okHttp = RestHelper.getYandexOkHttp(sourceInfo.getId(), accessToken);
        scanThread = new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    list = new ArrayList<>();
                    int offset = 0;
                    int size = limit;
                    while (size >= limit) {
                        FileList files = okHttp.getAudioFiles(limit, offset);
                        list.addAll(files.getItems());
                        offset += limit;
                        size = files.getItems().size();
                    }
                    startScan();
                    Log.e(TAG, list.size() + "");
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });
        scanThread.start();
        AnswersImpl.scanAccountStart(sourceInfo.getId(), sourceInfo.getType());
        DbEntryService.updateAccountScanStatus(sourceInfo.getId(), ScanStatus.STARTED);
    }

    private synchronized void startScan(){

        try {
            int folderSongCount = 0;
            for (Item i : list) {
                SongInfo si = new SongInfo();
                si.setAccountId(sourceInfo.getId());
                si.setPath(i.getPath());
                si.setSourceType(SourceType.YANDEX_DISK);
                si.setAlbum(Constants.DEFAULT_ALBUM);
                si.setArtist(Constants.DEFAULT_ARTIST);
                si.setTitle(i.getName());
                si.setFileName(i.getName());
                si.setId(i.getResource_id());
                si.setStatus(AudioStatus.ONLINE);
                DbEntryService.saveAudio(si);
                folderSongCount++;

                if (folderSongCount % 5 == 0) {
                    notifyAccountSetActivity();
                }
            }

            DbEntryService.updateAccountScanStatus(sourceInfo.getId(), ScanStatus
                    .FINISHED);
            DbEntryService.updateAccountScannedSongs(sourceInfo.getId(), folderSongCount);
            Log.e(TAG, "SCAN FINISHED: " + folderSongCount);
            if (AccountSetActivity.getRefreshHandler() != null) {
                Message m = new Message();
                Bundle b = new Bundle();
                b.putSerializable(AccountSetActivity.ACC_ID, sourceInfo.getId());
                m.setData(b);
                AccountSetActivity.getRefreshHandler().sendMessage(m);
            }
            AnswersImpl.scanAccountFinish(sourceInfo.getId(), sourceInfo.getType());

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

    @Override
    public void notifyAccountSetActivity(){
        AccountSetActivity.getRefreshHandler().sendEmptyMessage(25);
    }

    @Override
    public boolean isRunning(){
        return false;
    }

    @Override
    public String getAccountId(){
        return null;
    }
}
