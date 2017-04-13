package mree.cloud.music.player.app.scan.impl;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mree.cloud.music.player.app.act.AccountSetActivity;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.report.AnswersImpl;
import mree.cloud.music.player.app.scan.IScan;
import mree.cloud.music.player.app.tasks.RefreshTokenTask;
import mree.cloud.music.player.app.utils.Constants;
import mree.cloud.music.player.app.utils.RestHelper;
import mree.cloud.music.player.common.model.PlaylistInfo;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.model.SourceInfo;
import mree.cloud.music.player.common.model.spotify.Image;
import mree.cloud.music.player.common.model.spotify.Page;
import mree.cloud.music.player.common.model.spotify.PlaylistTrack;
import mree.cloud.music.player.common.model.spotify.SimplePlaylist;
import mree.cloud.music.player.common.model.spotify.Track;
import mree.cloud.music.player.common.ref.ScanStatus;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.audio.AudioStatus;
import mree.cloud.music.player.common.ref.audio.PlaylistType;
import mree.cloud.music.player.rest.ok.SpotifyOkHttp;
import mree.cloud.music.player.rest.utils.UnauthorizedException;

/**
 * Created by eercan on 09.11.2016.
 */

public class SpotifyScan implements IScan{
    private static final String TAG = SpotifyScan.class.getSimpleName();
    //private SpotifyRestClient restClient;
    private SpotifyOkHttp okHttp;
    private Context context;
    private SourceInfo sourceInfo;
    private int count;
    private Thread scanThread;

    public SpotifyScan(Context context, SourceInfo sourceInfo){
        this.context = context;
        this.sourceInfo = sourceInfo;
        if (sourceInfo.getUserId() == null) {
            // getUserId(sourceInfo.getId(), sourceInfo.getAccessToken());
        }
    }

    public static Track getTrack(String accId, String id){
        Track t = null;
        SpotifyOkHttp src = RestHelper.getSpotifyOkHttp(accId, DbEntryService
                .getAccountAccessToken(accId));
        try {
            String s = src.getTrack(id);
            s = s.replace("\n", "");
            // s = s.replace(" ", "");
            Type type = new TypeToken<Track>(){
            }.getType();
            t = new Gson().fromJson(s, type);
            return t;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            try {
                UnauthorizedException ue = (UnauthorizedException) e;
                RefreshTokenTask task = new RefreshTokenTask(DbEntryService.getAccountInfo(accId)
                        , src, null);
                task.run();
                while (!task.isFinished()) {

                }
                String s = src.getTrack(id);
                s = s.replace("\n", "");
                // s = s.replace(" ", "");
                Type type = new TypeToken<Track>(){
                }.getType();
                t = new Gson().fromJson(s, type);
                //PlaySong.getsPlayer().init(false);
                return t;
            } catch (Exception e1) {
                Log.e(TAG, e.getMessage() + "");
            }
        }
        return t;
    }

    @Override
    public void start(){
        String accessToken = sourceInfo.getAccessToken();
        okHttp = RestHelper.getSpotifyOkHttp(sourceInfo.getId(), accessToken);
        count = 0;
        scanThread = new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    AnswersImpl.scanAccountStart(sourceInfo.getId(), sourceInfo.getType());
                    List<SimplePlaylist> playlists = getSimplePlaylist();
                    for (SimplePlaylist p : playlists) {
                        List<PlaylistTrack> tracks = getSimplePlaylistTrack(p.getOwner().getId(), p
                                .getId());
                        List<SongInfo> addedSongs = new ArrayList<>();
                        for (PlaylistTrack pt : tracks) {
                            try {
                                Track t = pt.getTrack();
                                if (t != null) {
                                    SongInfo si = new SongInfo();
                                    si.setAlbum(t.getAlbum().getName());
                                    si.setAccountId(sourceInfo.getId());
                                    si.setArtist(t.getArtists().get(0).getName());
                                    si.setTrack((long) t.getTrackNumber());
                                    si.setDuration((long) t.getDuration());
                                    List<Image> images = t.getAlbum().getImages();

                                    if (images != null && images.size() > 0) {
                                        si.setThumbnail(images
                                                .get(1)
                                                .getUrl());
                                    }

                                    si.setSourceType(SourceType.SPOTIFY);
                                    if (t.getName() != null && !"".equals(t.getName())) {
                                        si.setTitle(t.getName());
                                    } else {
                                        si.setTitle(Constants.DEFAULT_TITLE);
                                    }
                                    si.setDisc(t.getDiscNumber());
                                    si.setAccountId(sourceInfo.getId());
                                    si.setId(t.getId());
                                    si.setDownloadUrl(t.getUri());
                                    si.setStatus(AudioStatus.ONLINE);
                                    DbEntryService.saveAudio(si);
                                    count++;
                                    addedSongs.add(si);
                                    Log.e(TAG, "Count: " + count);

                                    if (count % 5 == 0) {
                                        notifyAccountSetActivity();
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage() + "");
                            }
                        }

                        PlaylistInfo pi = new PlaylistInfo();
                        pi.setId(p.getId());
                        pi.setOfflineStatus(0);
                        pi.setCreatedDate(new Date());
                        pi.setType(PlaylistType.SPOTIFY);
                        pi.setName(p.getName());
                        pi.setSongs(addedSongs);
                        DbEntryService.savePlaylist(pi);
                    }
                    int accountScannedSongs = DbEntryService.getAccountSongsCount(sourceInfo
                            .getId());
                    DbEntryService.updateAccountScannedSongs(sourceInfo.getId(),
                            accountScannedSongs);
                    DbEntryService.updateAccountScanStatus(sourceInfo.getId(), ScanStatus
                            .FINISHED);
                    Log.e(TAG, "Scan completed. Total: " + accountScannedSongs);
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
                }
            }
        });
        scanThread.start();
        Answers.getInstance().logCustom(new CustomEvent("Spotify Scan")
                .putCustomAttribute("State", ScanStatus.STARTED.getDesc()));
        DbEntryService.updateAccountScanStatus(sourceInfo.getId(), ScanStatus.STARTED);
    }

    private List<PlaylistTrack> getSimplePlaylistTrack(String userId, String playlistId){
        List<PlaylistTrack> tracks = new ArrayList<PlaylistTrack>();
        try {
            int offset = 0;
            int limit = 50;
            while (true) {
                String s = okHttp.getPlaylistTrack(userId, playlistId, offset, limit);
                s = s.replace("\n", "");
                // s = s.replace(" ", "");
                Type type = new TypeToken<Page<PlaylistTrack>>(){
                }.getType();
                Page<PlaylistTrack> page = new Gson().fromJson(s, type);
                tracks.addAll(page.getItems());
                if (page.getItems().size() != page.getLimit() || page.getItems().size() == 0) {
                    break;
                }
                offset += limit;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            try {
                UnauthorizedException ue = (UnauthorizedException) e;
                RefreshTokenTask task = new RefreshTokenTask(sourceInfo, okHttp, null);
                task.run();
                while (!task.isFinished()) {

                }
                int offset = 0;
                int limit = 50;
                while (true) {
                    String s = okHttp.getPlaylistTrack(userId, playlistId, offset, limit);
                    s = s.replace("\n", "");
                    //   s = s.replace(" ", "");
                    Type type = new TypeToken<Page<PlaylistTrack>>(){
                    }.getType();
                    Page<PlaylistTrack> page = new Gson().fromJson(s, type);
                    tracks.addAll(page.getItems());
                    if (page.getItems().size() != page.getLimit() || page.getItems().size() == 0) {
                        break;
                    }
                    offset += limit;
                }
            } catch (Exception e1) {
                Log.e(TAG, e1.getMessage() + "");
            }
        }

        return tracks;
    }

    private List<SimplePlaylist> getSimplePlaylist(){
        List<SimplePlaylist> playlists = new ArrayList<>();
        try {
            int offset = 0;
            int limit = 50;
            while (true) {
                String s = okHttp.getCurrentUserPlaylists(offset, limit);
                s = s.replace("\n", "");
                // s = s.replace(" ", "");
                Type type = new TypeToken<Page<SimplePlaylist>>(){
                }.getType();
                Page<SimplePlaylist> page = new Gson().fromJson(s, type);
                playlists.addAll(page.getItems());
                if (page.getItems().size() != page.getLimit() || page.getItems().size() == 0) {
                    break;
                }
                offset += limit;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            try {
                UnauthorizedException ue = (UnauthorizedException) e;
                RefreshTokenTask task = new RefreshTokenTask(sourceInfo, okHttp, null);
                task.run();
                while (!task.isFinished()) {

                }
                int offset = 0;
                int limit = 50;
                while (true) {
                    String s = okHttp.getCurrentUserPlaylists(offset, limit);
                    s = s.replace("\n", "");
                    //    s = s.replace(" ", "");
                    Type type = new TypeToken<Page<SimplePlaylist>>(){
                    }.getType();
                    Page<SimplePlaylist> page = new Gson().fromJson(s, type);
                    playlists.addAll(page.getItems());
                    if (page.getItems().size() != page.getLimit() || page.getItems().size()
                            == 0) {
                        break;
                    }
                    offset += limit;
                }
            } catch (Exception e1) {
                Log.e(TAG, e1.getMessage() + "");
            }
        }

        return playlists;

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
