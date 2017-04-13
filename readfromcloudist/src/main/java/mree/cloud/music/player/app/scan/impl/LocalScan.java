package mree.cloud.music.player.app.scan.impl;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import mree.cloud.music.player.app.act.AccountSetActivity;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.scan.IScan;
import mree.cloud.music.player.app.utils.Constants;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.model.SourceInfo;
import mree.cloud.music.player.common.ref.ScanStatus;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.audio.AudioStatus;

/**
 * Created by eercan on 22.12.2016.
 */

public class LocalScan implements IScan{
    private boolean isRunning;
    private Thread scanThread;
    private Context context;
    private SourceInfo sourceInfo;
    private ContentResolver contentResolver;
    private int count = 0;

    public LocalScan(Context context, SourceInfo accountInfo){
        this.context = context;
        this.sourceInfo = accountInfo;
    }

    @Override
    public void start(){
        scanThread = new Thread(getRunnable());
        scanThread.start();
        DbEntryService.updateAccountScanStatus(sourceInfo.getId(), ScanStatus.STARTED);
    }

    private Runnable getRunnable(){
        return new Runnable(){
            @Override
            public void run(){
                try {
                    SongInfo si = new SongInfo();
                    contentResolver = context.getContentResolver();
                    Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    Cursor cursor = contentResolver.query(uri, null, null, null, null);
                    if (cursor == null) {
                        // query failed, handle error.
                    } else if (!cursor.moveToFirst()) {
                        // no media on the device
                    } else {
                        int titleColumn = cursor.getColumnIndex(MediaStore.Audio
                                .Media.TITLE);
                        int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
                        int albumIdColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
                        int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
                        int yearColumn = cursor.getColumnIndex(MediaStore.Audio.Media.YEAR);
                        int durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);


                        int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media
                                ._ID);
                        do {
                            Long id = cursor.getLong(idColumn);
                            String title = cursor.getString(titleColumn);
                            String artist = cursor.getString(artistColumn);
                            String album = cursor.getString(albumColumn);
                            String albumId = cursor.getString(albumIdColumn);
                            Long year = cursor.getLong(yearColumn);
                            Long duration = cursor.getLong(durationColumn);
                            si.setAccountId(sourceInfo.getId());
                            si.setId(id.toString());
                            si.setSourceType(SourceType.LOCAL);

                            if (TextUtils.isEmpty(artist) || artist.toLowerCase().contains
                                    ("unkn")) {
                                si.setArtist(Constants.DEFAULT_ARTIST);
                            } else {
                                si.setArtist(artist);
                            }

                            if (TextUtils.isEmpty(album) || album.toLowerCase().contains("unkn")) {
                                si.setAlbum(Constants.DEFAULT_ALBUM);
                            } else {
                                si.setAlbum(album);
                            }

                            si.setYear(year);

                            if (TextUtils.isEmpty(title) || title.toLowerCase().contains("unkn")) {
                                si.setTitle(Constants.DEFAULT_TITLE);
                            } else {
                                si.setTitle(title);
                            }

                            si.setDuration(duration);
                            si.setThumbnail(getThumbnailByAlbumId(albumId));
                            si.setDownloadUrl(cursor.getString(cursor.getColumnIndex(MediaStore
                                    .Audio
                                    .Media.DATA)));
                            si.setStatus(AudioStatus.OFFLINE);
                            DbEntryService.saveAudio(si);
                            count++;
                            if (count % 5 == 0) {
                                notifyAccountSetActivity();
                            }
                        } while (cursor.moveToNext());
                    }

                    if (AccountSetActivity.getRefreshHandler() != null) {
                        Message m = new Message();
                        Bundle b = new Bundle();
                        b.putSerializable(AccountSetActivity.ACC_ID, sourceInfo.getId());
                        m.setData(b);
                        AccountSetActivity.getRefreshHandler().sendMessage(m);
                    }

                    DbEntryService.updateAccountScanStatus(sourceInfo.getId(), ScanStatus.FINISHED);
                } catch (Exception e) {
                    Log.e("LOCALSCAN", e.getMessage() + "");
                }
            }
        };
    }

    private String getThumbnailByAlbumId(String albumId){
        if (!TextUtils.isEmpty(albumId)) {
            try {
                Uri smusicUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
                Cursor music = contentResolver.query(smusicUri, null, "_ID=?"
                        , new String[]{(albumId)}, null, null);


                music.moveToFirst();
                int x = music.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
                String thisArt = music.getString(x);
                return thisArt;
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void notifyAccountSetActivity(){
        AccountSetActivity.getRefreshHandler().sendEmptyMessage(25);
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
        return isRunning;
    }

    @Override
    public String getAccountId(){
        return null;
    }
}
