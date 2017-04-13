package mree.cloud.music.player.app.services;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mree.cloud.music.player.app.act.PlaybackActivity;
import mree.cloud.music.player.app.act.fragment.AudioFragment;
import mree.cloud.music.player.app.bill.ad.admob.AdMob;
import mree.cloud.music.player.app.player.NotificationBuilder;
import mree.cloud.music.player.app.player.PlaySong;
import mree.cloud.music.player.app.player.SpotifyMediaPlayer;
import mree.cloud.music.player.app.receivers.AudioFocusReceiver;
import mree.cloud.music.player.app.receivers.WidgetProviderReceiver;
import mree.cloud.music.player.app.tasks.ImageLoaderTask;
import mree.cloud.music.player.app.utils.BluetoothHelper;
import mree.cloud.music.player.app.utils.ImageUtils;
import mree.cloud.music.player.app.utils.WidgetProvider;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.audio.PlaybackState;

/**
 * Created by mree on 27.02.2016.
 */
public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    //notification id
    public static final int NOTIFY_ID = 25;
    private static final String AVRCP_PLAYSTATE_CHANGED = "com.android.music.playstatechanged";
    private static final String AVRCP_META_CHANGED = "com.android.music.metachanged";
    private static final String TAG = MusicService.class.getSimpleName();
    public static boolean running;
    public static boolean isReady;
    public static boolean alreadyStopped;
    public static PlaybackState playbackState = PlaybackState.INITIAL;
    private static PlaySong playTask;
    private static SpotifyMediaPlayer spotifyPlayer;
    private static WidgetProvider widgetProvider;
    private static AudioFocusReceiver audioFocus;
    private final IBinder musicBind = new MusicBinder();
    //media player
    private MediaPlayer player;
    //song list
    private List<SongInfo> playList;
    private List<Integer> playedList;
    //current position
    private int songPosn;
    private SongInfo currentSong;
    //title of current song
    private String songTitle = "";
    private boolean shuffle = false;
    private boolean loop = false;
    private boolean loop_one = false;
    private Random rand;
    private NotificationBuilder not;

    public MusicService() {
    }

    public static WidgetProvider getWidgetProvider() {
        if (widgetProvider == null) {
            widgetProvider = new WidgetProvider();
        }
        return widgetProvider;
    }

    public MediaPlayer getPlayer() {
        if (player == null) {
            player = new MediaPlayer();
        }

        initMusicPlayer();
        return player;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (player != null) {
            try {
                player.stop();
                player.release();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }

        if (spotifyPlayer != null) {
            spotifyPlayer.getsPlayer().destroy();
        }
        stopForeground(true);
        playbackState = PlaybackState.INITIAL;
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (player.getCurrentPosition() > 0) {
            mp.reset();
            playNext(false);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        playbackState = PlaybackState.INITIAL;
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (AudioFragment.getToolbar() != null) {
            AudioFragment.getToolbar().show();
            AudioFragment.getToolbar().setSeekBar(player.getDuration());
            AudioFragment.getToolbar().setEnabled(true);
            isReady = true;
        }

        if (PlaybackActivity.getReadyHandler() != null) {
            PlaybackActivity.getReadyHandler().sendEmptyMessage(player.getDuration());
        }
        playbackState = PlaybackState.PREPARED;
        mp.start();
        //notification
        setNotification(true);
        widgetProvider.refresh(true, null);
    }

    private void bluetoothNotifyChange(String what) {
        try {
            Intent i = new Intent(what);
            i.putExtra("id", Long.valueOf(getPosn()));
            i.putExtra("artist", currentSong.getArtist());
            i.putExtra("album", currentSong.getAlbum());
            i.putExtra("track", currentSong.getTrack());
            i.putExtra("playing", isPng());
            i.putExtra("ListSize", playList.size());
            i.putExtra("duration", getDur());
            i.putExtra("position", getPosn());
            sendBroadcast(i);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    public void onCreate() {
        //create the service
        super.onCreate();
        running = true;
        audioFocus = new AudioFocusReceiver(getApplicationContext());
        //audioFocus.requestFocus();
//initialize position
        songPosn = 0;
//create player
        AdMob.requestNewPeriodic();
        rand = new Random();
        WidgetProviderReceiver.setProvider(getWidgetProvider());
    }


    public void initMusicPlayer() {
        //set player properties
        //stopForeground(true);
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //set listeners
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(List<SongInfo> theSongs) {
        playList = theSongs;
        playedList = new ArrayList<>();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setNotification(final boolean b) {
        if (playTask != null) {
            not = playTask.getNot();
            try {
                if (currentSong.getThumbnail() != null) {
                    ImageUtils.getImageLoaderTask().loadImage(currentSong.getThumbnail(), new
                            ImageLoaderTask.ImageLoadedListener() {

                                @Override
                                public void imageLoaded(Bitmap imageBitmap) {
                                    not.setImage(imageBitmap);
                                    not.prepareNotification(b);
                                    startForeground(NOTIFY_ID, not.getNot());
                                }
                            });
                }

            } catch (Exception e) {
            }


            not.prepareNotification(b);
            startForeground(NOTIFY_ID, not.getNot());
        }
    }

    public SongInfo playSong() {
        playbackState = PlaybackState.STARTED;
        if (player != null) {
            try {
                player.reset();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage() + "");
            }
        }

        if (AudioFragment.getToolbar() != null) {
            isReady = false;
            AudioFragment.getToolbar().setEnabled(isReady);
        }

        if (spotifyPlayer != null && spotifyPlayer.getsPlayer() != null && spotifyPlayer
                .getsPlayer().getPlaybackState().isPlaying) {
            spotifyPlayer.getsPlayer().pause(spotifyPlayer);
        }
        //get song
        SongInfo playSong = new SongInfo();
        if (playList != null && playList.size() > 0) {
            playSong = playList.get(songPosn);
            if (!playedList.contains(songPosn)) {
                playedList.add(songPosn);
            }
            songTitle = playSong.getTitle();
            /*Player asyncPlayer = new Player();
            asyncPlayer.execute(playSong);*/
//            setNotification();
            PlaySong asyncPlayer = new PlaySong(this, this, playSong);
            playTask = asyncPlayer;
            setNotification(false);
            Thread t = new Thread(asyncPlayer);
            t.start();
            currentSong = playList.get(songPosn);

        }

        if (PlaybackActivity.getRefreshHandler() != null) {
            /*Bundle data = new Bundle();
            data.putSerializable(PlaybackToolbar.SI_PARAM, playSong);
            Message msg = new Message();
            msg.setData(data);*/
            PlaybackActivity.getRefreshHandler().sendEmptyMessage(25);
        }


        bluetoothNotifyChange(AVRCP_META_CHANGED);
        BluetoothHelper.onTrackChanged(getApplicationContext(), playSong);
        return playSong;
    }

    public void setSong(int songIndex) {
        songPosn = songIndex;
        currentSong = playList.get(songPosn);
    }

    public Integer getPosn() {
        try {
            if (currentSong != null && currentSong.getSourceType() == SourceType.SPOTIFY &&
                    spotifyPlayer != null) {
                return (int) spotifyPlayer.getsPlayer().getPlaybackState().positionMs;
            } else if (player != null) {
                try {
                    return player.getCurrentPosition();
                } catch (Exception e) {
                    return -1;
                }
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public int getDur() {

        if (currentSong != null && currentSong.getSourceType() == SourceType.SPOTIFY &&
                spotifyPlayer != null) {
            try {
                int duration = (int) spotifyPlayer.getsPlayer().getMetadata().currentTrack
                        .durationMs;
                return duration;
            } catch (Exception e) {
                return 0;
            }
        } else if (player != null) {
            try {
                return player.getDuration();
            } catch (Exception e) {
                return -1;
            }
        } else {
            return 0;
        }

    }

    public boolean isPng() {
        try {
            if (currentSong != null && currentSong.getSourceType() == SourceType.SPOTIFY &&
                    spotifyPlayer != null) {
                return spotifyPlayer.getsPlayer().getPlaybackState().isPlaying;
            } else if (player != null) {
                try {
                    return player.isPlaying();
                } catch (Exception e) {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            return false;
        }
    }

    public void pausePlayer() {
        if (currentSong != null && currentSong.getSourceType() == SourceType.SPOTIFY &&
                spotifyPlayer != null) {
            spotifyPlayer.getsPlayer().pause(spotifyPlayer);
        } else if (player != null) {
            try {
                player.pause();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        if (AudioFragment.getToolbar() != null) {
            AudioFragment.getToolbar().pause();
        }
        setNotification(false);
        playbackState = PlaybackState.PAUSED;
        bluetoothNotifyChange(AVRCP_PLAYSTATE_CHANGED);
        if (PlaybackActivity.getRefreshHandler() != null) {
            PlaybackActivity.getRefreshHandler().sendEmptyMessage(25);
        }
    }

    public void seek(int posn) {
        if (currentSong != null && currentSong.getSourceType() == SourceType.SPOTIFY &&
                spotifyPlayer != null) {
            spotifyPlayer.getsPlayer().seekToPosition(spotifyPlayer, posn);
            bluetoothNotifyChange(AVRCP_PLAYSTATE_CHANGED);
        } else if (player != null) {
            try {
                player.seekTo(posn);
                bluetoothNotifyChange(AVRCP_PLAYSTATE_CHANGED);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public void go() {
        try {
            if (currentSong != null && currentSong.getSourceType() == SourceType.SPOTIFY &&
                    !spotifyPlayer.getsPlayer().getPlaybackState().isPlaying && spotifyPlayer !=
                    null) {
                spotifyPlayer.getsPlayer().resume(spotifyPlayer);
            } else if (player != null && !player.isPlaying()) {
                player.start();
                setNotification(true);
            }
            playbackState = PlaybackState.PLAYING;
            bluetoothNotifyChange(AVRCP_PLAYSTATE_CHANGED);

            if (AudioFragment.getToolbar() != null) {
                AudioFragment.getToolbar().show();
            }

            if (PlaybackActivity.getRefreshHandler() != null) {
            /*Bundle data = new Bundle();
            data.putSerializable(PlaybackToolbar.SI_PARAM, playSong);
            Message msg = new Message();
            msg.setData(data);*/
                PlaybackActivity.getRefreshHandler().sendEmptyMessage(25);
            }
        } catch (IllegalStateException e) {

        }

    }

    public SongInfo playPrev() {
        if (!shuffle) {
            songPosn--;
            if (songPosn < 0) {
                songPosn = playList.size() - 1;
            }
        } else {
            if (playedList != null && playedList.size() > 1) {
                songPosn = playedList.get(playedList.size() - 2);
                playedList.remove(playedList.size() - 1);
            } else {
                return null;
            }
        }
        return playSong();
    }

    //skip to next
    public SongInfo playNext(boolean isUser) {
        if (playList != null) {
            if (loop_one) {
                if (isUser) {
                    loop_one = false;
                    songPosn = generateNextSong();
                } else {

                }
            } else if (loop) {
                songPosn = generateNextSong();
            } else {
                if (isUser) {
                    songPosn = generateNextSong();
                } else {
                    int temp = generateNextSong();
                    if (playedList.size() == 0) {
                        return null;
                    }
                    songPosn = temp;
                }
            }

            /*if (isUser && mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }*/
        }
        return playSong();
    }

    private int generateNextSong() {
        int newSong = 0;
        if (shuffle) {
            if (playedList.size() < playList.size()) {
                newSong = rand.nextInt(playList.size());
                while (playedList.contains(newSong)) {
                    newSong = rand.nextInt(playList.size());
                }
                songPosn = newSong;
            } else {
                playedList = new ArrayList<>();
                newSong = rand.nextInt(playList.size());
                songPosn = newSong;
            }
        } else {
            newSong = songPosn + 1;
            if (newSong >= playList.size()) {
                newSong = 0;
                playedList = new ArrayList<>();
            }
        }

        return newSong;
    }

    @Override
    public void onDestroy() {
        running = false;
        super.onDestroy();
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public void setShuffle(boolean b) {
        shuffle = b;
        playedList = new ArrayList<>();
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public boolean isLoop_one() {
        return loop_one;
    }

    public void setLoop_one(boolean loop_one) {
        this.loop_one = loop_one;
        if (currentSong != null && currentSong.getSourceType() == SourceType.SPOTIFY &&
                spotifyPlayer != null) {
            spotifyPlayer.getsPlayer().setRepeat(spotifyPlayer, loop_one);
        } else {
            //player.setLooping(loop_one);
        }
    }

    public SpotifyMediaPlayer getSpotifyPlayer(String accId) {
        if (spotifyPlayer == null) {
            spotifyPlayer = new SpotifyMediaPlayer(getApplicationContext(), accId);
        } else if (!accId.equals(spotifyPlayer.getAccId())) {
            spotifyPlayer.setAccId(accId);
            spotifyPlayer.init(false);
        }

        return spotifyPlayer;

    }

    public SongInfo getCurrentSong() {
        return currentSong;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}

