package mree.cloud.music.player.app.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.media.RemoteControlClient;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Build;
import android.util.Log;

import mree.cloud.music.player.app.act.fragment.AudioFragment;
import mree.cloud.music.player.common.model.SongInfo;

/**
 * Created by eercan on 17.03.2017.
 */

public class BluetoothHelper {
    private static String TAG = BluetoothHelper.class.getSimpleName();
    private static AudioManager mAudioManager;
    private static RemoteControlClient mRemoteControlClient;
    private static MediaSession mMediaSession;

    public static void onTrackChanged(final Context context, final SongInfo si) {
        Thread meta = new Thread(new Runnable() {
            public void run() {
                if (mAudioManager == null) {
                    mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                }

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    if (mRemoteControlClient == null) {
                        Log.d("init()", "API " + Build.VERSION.SDK_INT + " lower then " + Build
                                .VERSION_CODES.LOLLIPOP);
                        Log.d("init()", "Using RemoteControlClient API.");

                        mRemoteControlClient = new RemoteControlClient(PendingIntent.getBroadcast
                                (context, 0, new Intent(Intent.ACTION_MEDIA_BUTTON), 0));
                        mAudioManager.registerRemoteControlClient(mRemoteControlClient);
                    }
                } else {
                    if (mMediaSession == null) {
                        Log.d("init()", "API " + Build.VERSION.SDK_INT + " greater or equals " +
                                Build
                                        .VERSION_CODES.LOLLIPOP);
                        Log.d("init()", "Using MediaSession API.");

                        mMediaSession = new MediaSession(context, TAG);
                        mMediaSession.setFlags(MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);
                        mMediaSession.setActive(true);

                    }
                }

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

                    RemoteControlClient.MetadataEditor ed = mRemoteControlClient.editMetadata(true);
                    ed.putString(MediaMetadataRetriever.METADATA_KEY_TITLE, si.getTitle());
                    ed.putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, si.getArtist());
                    ed.putString(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST, si.getArtist());
                    ed.putString(MediaMetadataRetriever.METADATA_KEY_ALBUM, si.getAlbum());
                    ed.putLong(MediaMetadataRetriever.METADATA_KEY_DURATION, AudioFragment
                            .getMusicService().getDur());
                    ed.apply();

                } else {

                    MediaMetadata metadata = new MediaMetadata.Builder()
                            .putString(MediaMetadata.METADATA_KEY_TITLE, si.getTitle())
                            .putString(MediaMetadata.METADATA_KEY_ARTIST, si.getArtist())
                            .putString(MediaMetadata.METADATA_KEY_ALBUM_ARTIST, si.getArtist())
                            .putString(MediaMetadata.METADATA_KEY_ALBUM, si.getAlbum())
                            .putLong(MediaMetadata.METADATA_KEY_DURATION, AudioFragment
                                    .getMusicService()
                                    .getDur())
                            .build();

                    mMediaSession.setMetadata(metadata);

                    PlaybackState state = new PlaybackState.Builder()
                            .setActions(PlaybackState.ACTION_PLAY
                                    | PlaybackState.ACTION_SKIP_TO_NEXT
                                    | PlaybackState.ACTION_PAUSE
                                    | PlaybackState.ACTION_SKIP_TO_PREVIOUS
                                    | PlaybackState.ACTION_STOP
                                    | PlaybackState.ACTION_PLAY_PAUSE)
                            .build();

                    mMediaSession.setPlaybackState(state);
                }
            }
        });
        meta.start();

        try {
            meta.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void sendTrackInfoToBluetoothDevice(Context context) {

    }
}
