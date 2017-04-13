package mree.cloud.music.player.app.player;

import android.content.Context;
import android.util.Log;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import mree.cloud.music.player.app.act.PlaybackActivity;
import mree.cloud.music.player.app.act.fragment.AudioFragment;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.services.MusicService;
import mree.cloud.music.player.app.tasks.RefreshTokenTask;
import mree.cloud.music.player.app.utils.AuthHelper;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.audio.PlaybackState;


/**
 * Created by eercan on 11.11.2016.
 */

public class SpotifyMediaPlayer implements
        Player.AudioDeliveredCallback,
        Player.NotificationCallback,
        Player.AudioFlushCallback,
        Player.OperationCallback,
        ConnectionStateCallback {
    private static final String TAG = SpotifyMediaPlayer.class.getSimpleName();
    private Player sPlayer;
    private String uri;
    private String accId;
    private boolean state;
    private Context context;
    private Config playerConfig;
    private boolean isReady;


    public SpotifyMediaPlayer(Context context, String accId) {
        this.context = context;
        this.accId = accId;
        init(false);
    }

    public void init(final boolean startPlay) {
        isReady = false;
        final SpotifyMediaPlayer ssPlayer = this;
        playerConfig = new Config(context, DbEntryService.getAccountAccessToken(accId),
                AuthHelper.getClientId(SourceType
                        .SPOTIFY));
        Spotify.getPlayer(playerConfig, context, new SpotifyPlayer.InitializationObserver() {
            @Override
            public void onInitialized(SpotifyPlayer spotifyPlayer) {
                ssPlayer.setsPlayer(spotifyPlayer);
                ssPlayer.getsPlayer().addConnectionStateCallback(ssPlayer);
                ssPlayer.getsPlayer().addNotificationCallback(ssPlayer);
                if (spotifyPlayer.isLoggedIn()) {
                    onLoggedIn();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e(TAG, "Could not initialize player: " + throwable.getMessage());
            }
        });
    }

    public void playSong(String uri) {
        this.uri = uri;
        if (isReady)
            getsPlayer().playUri(this, uri, 0, 0);
/*        else {
            init(true);
        }*/
    }

    public Player getsPlayer() {
        if (sPlayer == null) {
            init(false);
        }
        return sPlayer;
    }

    public void setsPlayer(Player sPlayer) {
        this.sPlayer = sPlayer;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    @Override
    public int onAudioDelivered(short[] shorts, int i, int i1, int i2) {
        Log.e(TAG, "onAudioDelivered " + shorts);
        return 0;
    }

    @Override
    public void onAudioFlush() {
        Log.e(TAG, "onAudioFlush");

    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.e(TAG, "onPlaybackEvent: " + playerEvent.name());
        if (PlayerEvent.kSpPlaybackNotifyAudioDeliveryDone == playerEvent) {
            SongInfo songInfo = AudioFragment.getMusicService().playNext(false);
            if (songInfo == null) {
                AudioFragment.getToolbar().hide();
            }
        } else if (PlayerEvent.kSpPlaybackNotifyTrackChanged == playerEvent) {
            MusicService.playbackState = PlaybackState.PREPARING;
            int dur = AudioFragment.getMusicService().getDur();
            if (AudioFragment.getToolbar() != null) {
                AudioFragment.getToolbar().setSeekBar(dur);
            }

            if (PlaybackActivity.getReadyHandler() != null) {
                PlaybackActivity.getReadyHandler().sendEmptyMessage(dur);
            }
            boolean b = AudioFragment.getMusicService().isPng();
            AudioFragment.getMusicService().setNotification(b);

            MusicService.getWidgetProvider().refresh(true, null);

            MusicService.isReady = true;
            if (AudioFragment.getToolbar() != null) {
                AudioFragment.getToolbar().show();
                AudioFragment.getToolbar().setEnabled(true);
            }

            MusicService.playbackState = PlaybackState.PREPARED;

        } else if (PlayerEvent.kSpPlaybackNotifyPlay == playerEvent) {
            AudioFragment.getMusicService().setNotification(true);
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.e(TAG, "onPlaybackError: " + error.name());
        if (Error.kSpErrorFailed == error) {
            AudioFragment.getMusicService().playNext(false);
        } else {
        }
    }

    @Override
    public void onSuccess() {
        Log.e(TAG, "onSuccess");
    }

    @Override
    public void onError(Error error) {
        Log.e(TAG, "onError: " + error.name());
        if (Error.kSpErrorFailed == error) {
            AudioFragment.getMusicService().playNext(false);
        } else if (Error.kSpErrorLoginBadCredentials == error) {
            RefreshTokenTask task = new RefreshTokenTask(accId, null);
            AudioFragment.getThreadPool().submit(task);
            while (!task.isFinished()) {
            }
            init(true);
        }
    }

    @Override
    public void onLoggedIn() {
        Log.e(TAG, "onLoggedIn");

        if (!isReady && !AudioFragment.getMusicService().isPng()) {
            isReady = true;
            getsPlayer().playUri(this, uri, 0, 0);
        }
   /*     RefreshTokenTask task = new RefreshTokenTask(DbEntryService.getAccountInfo(accId),
                null);
        //task.run();
        task.getRefreshHandler().sendEmptyMessage(25);
        while (!task.isFinished()) {

        }
        playerConfig = new Config(context, DbEntryService.getAccountAccessToken(accId),
                AuthHelper.getClientId(SourceType.SPOTIFY));
        sPlayer.initialize(playerConfig);
        playSong(uri);*/
    }

    @Override
    public void onLoggedOut() {
        Log.e(TAG, "onLoggedOut");
        isReady = false;
    }

    @Override
    public void onLoginFailed(int i) {
        Log.e(TAG, i + " onLoginFailed");

        if (i == 8) {
            init(true);
        }
    }

    @Override
    public void onTemporaryError() {
        Log.e(TAG, "onTemporaryError");
    }

    @Override
    public void onConnectionMessage(String s) {
        Log.e(TAG, "onConnectionMessage");
    }
}
