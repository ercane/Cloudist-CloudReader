package mree.cloud.music.player.app.player;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

import mree.cloud.music.player.app.act.fragment.AudioFragment;
import mree.cloud.music.player.app.cach.ProxyServer;
import mree.cloud.music.player.app.report.AnswersImpl;
import mree.cloud.music.player.app.report.Firebase;
import mree.cloud.music.player.app.services.CmpDeviceService;
import mree.cloud.music.player.app.services.MusicService;
import mree.cloud.music.player.app.tasks.ImageLoaderTask;
import mree.cloud.music.player.app.utils.ImageUtils;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.audio.PlaybackState;

/**
 * Created by eercan on 14.10.2016.
 */
public class PlaySong implements Runnable{
    private static final String TAG = PlaySong.class.getSimpleName();
    private static SpotifyMediaPlayer sPlayer;
    private static Context context;
    private static ProxyServer selfProxy;
    private NotificationBuilder not;
    private MediaPlayer player;
    private MusicService service;
    private SongInfo si;

    public PlaySong(Context context, MusicService service, SongInfo si){
        PlaySong.context = context;
        this.service = service;
        this.si = si;
        init();
    }

    public static SpotifyMediaPlayer getsPlayer(){
        return sPlayer;
    }

/*    public static HttpProxyCacheServer getProxy() {
        if (proxy == null) {
            File cache = context.getCacheDir();
            File audioCache = new File(cache, "/audiocache/");
            boolean mkdirs = audioCache.mkdirs();
            proxy = new HttpProxyCacheServer.Builder(context)
                    .maxCacheFilesCount(20)
                    //.cacheDirectory(audioCache)
                    .build();
        }
        return proxy;
    }*/

    public static ProxyServer getSelfProxy(){
        if (selfProxy == null) {
            selfProxy = new ProxyServer(context);
            selfProxy.startServer();
        }
        return selfProxy;
    }


    private void init(){
        setNotification();
        setToolbar();
        setWidget();
        MusicService.playbackState = PlaybackState.PREPARING;
    }

    private void setWidget(){
        try {
            MusicService.getWidgetProvider().setContext(context);
            MusicService.getWidgetProvider().setArtist(si.getArtist());
            MusicService.getWidgetProvider().setTitle(si.getTitle());
            ImageUtils.getImageLoaderTask().loadImage(si.getThumbnail(), new
                    ImageLoaderTask.ImageLoadedListener(){

                        @Override
                        public void imageLoaded(Bitmap image){
                            MusicService.getWidgetProvider().setImage(image);
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    private void setToolbar(){
        if (AudioFragment.getToolbar() != null) {
            //AudioFragment.getToolbar().setCover(si.getSourceType(), si.getThumbnail());
            AudioFragment.getToolbar().refreshListener(si);
        }
    }

    private void setNotification(){

        not = new NotificationBuilder(context, si.getTitle(), si.getArtist(), null);
        //not.prepareNotification(false);
    }

    @Override
    public void run(){
        Boolean prepared = false;
        String downloadUrl = null;
        try {
            downloadUrl = CmpDeviceService.getPlayUrl(si);

            if (si.getSourceType() == SourceType.SPOTIFY) {
                sPlayer = service.getSpotifyPlayer(si.getAccountId());
                sPlayer.playSong(downloadUrl);
            } else {
                //downloadUrl = getProxy().getProxyUrl(downloadUrl, si.getId());

                player = service.getPlayer();
                player.setDataSource(downloadUrl);
                player.prepare();
                player.setOnErrorListener(new MediaPlayer.OnErrorListener(){
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra){
                        Log.e(TAG, "MediaPlayer Error" + what + " " + extra);
                        return false;
                    }
                });
            }
            prepared = true;
            AnswersImpl.audioPlayRequest(si.getTitle(), si.getSourceType());
            Firebase.playSongLog(si.getTitle(), si.getSourceType());
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage() + "");
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage() + "");
        } catch (IllegalStateException e) {
            Log.e(TAG, e.getMessage() + "");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage() + "");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
        Log.e("Prepared", "//" + prepared + " URL: " + downloadUrl);
        if (!prepared) {
            MusicService.playbackState = PlaybackState.INITIAL;
        }
    }

    public NotificationBuilder getNot(){
        return not;
    }
/*
    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
        Log.e(TAG, "Cache file:" + cacheFile.getName() + "\nUrl: " + url + "\n%" +
                percentsAvailable);
    }*/
}
