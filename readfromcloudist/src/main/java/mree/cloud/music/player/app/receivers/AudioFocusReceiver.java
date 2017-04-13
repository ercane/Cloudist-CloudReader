package mree.cloud.music.player.app.receivers;

import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;

import mree.cloud.music.player.app.act.fragment.AudioFragment;
import mree.cloud.music.player.app.services.MusicService;

/**
 * Created by eercan on 22.12.2016.
 */

public class AudioFocusReceiver implements AudioManager.OnAudioFocusChangeListener {
    private Context context;
    private AudioManager manager;

    public AudioFocusReceiver(Context context) {
        this.context = context;
        this.manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        manager.registerMediaButtonEventReceiver(new ComponentName(context.getPackageName(),
                HardButtonReceiver.class.getName()));
    }

    public boolean requestFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                manager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN);
    }

    public boolean abandonFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                manager.abandonAudioFocus(this);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        try {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    // resume playback
                    if (AudioFragment.getMusicService() != null) {
                        if (!MusicService.alreadyStopped)
                            AudioFragment.getMusicService().go();
                    }
                    break;

                case AudioManager.AUDIOFOCUS_LOSS:
                    // Lost focus for an unbounded amount of time: stop playback and release media
                    // player
                    if (AudioFragment.getMusicService() != null) {
                        if (AudioFragment.getMusicService().isPng()) {
                            AudioFragment.getMusicService().pausePlayer();
                            MusicService.alreadyStopped = false;
                        } else {
                            MusicService.alreadyStopped = true;
                        }
                    }
                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    // Lost focus for a short time, but we have to stop
                    // playback. We don't release the media player because playback
                    // is likely to resume
                    if (AudioFragment.getMusicService() != null) {
                        if (AudioFragment.getMusicService().isPng()) {
                            AudioFragment.getMusicService().pausePlayer();
                            MusicService.alreadyStopped = false;
                        } else {
                            MusicService.alreadyStopped = true;
                        }
                    }
                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    // Lost focus for a short time, but it's ok to keep playing
                    // at an attenuated level
                    //if (mMediaPlayer.isPlaying()) mMediaPlayer.setVolume(0.1f, 0.1f);
                    if (AudioFragment.getMusicService() != null) {
                        if (AudioFragment.getMusicService().isPng()) {
                            AudioFragment.getMusicService().pausePlayer();
                            MusicService.alreadyStopped = false;
                        } else {
                            MusicService.alreadyStopped = true;
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
