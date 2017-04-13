package mree.cloud.music.player.app.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;

import mree.cloud.music.player.app.act.fragment.AudioFragment;

/**
 * Created by mree on 15.03.2017.
 */

public class HardButtonReceiver extends BroadcastReceiver {
    private static final String TAG = HardButtonReceiver.class.getSimpleName();
    private static int CLICK_COUNT = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "Button press received");
        abortBroadcast();
        KeyEvent key = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (key.getAction() == KeyEvent.ACTION_UP) {
            int keycode = key.getKeyCode();
            if (keycode == KeyEvent.KEYCODE_MEDIA_NEXT) {
                if (AudioFragment.getMusicService() != null) {
                    AudioFragment.getMusicService().playNext(false);
                }
                Log.d(TAG, "Next Pressed");
            } else if (keycode == KeyEvent.KEYCODE_MEDIA_PREVIOUS) {
                if (AudioFragment.getMusicService() != null) {
                    AudioFragment.getMusicService().playPrev();
                }
                Log.d(TAG, "Previous pressed");
            } else if (keycode == KeyEvent.KEYCODE_HEADSETHOOK) {
                Log.d(TAG, "Head Set Hook pressed");
                CLICK_COUNT++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        if (CLICK_COUNT == 1) {
                            if (AudioFragment.getMusicService() != null) {
                                if (AudioFragment.getMusicService().isPng()) {
                                    AudioFragment.getMusicService().pausePlayer();
                                } else {
                                    AudioFragment.getMusicService().go();
                                }
                            }
                        }
                        if (CLICK_COUNT == 2) {
                            if (AudioFragment.getMusicService() != null) {
                                AudioFragment.getMusicService().playNext(false);
                            }
                        }
                        CLICK_COUNT = 0;
                    }
                };
                if (CLICK_COUNT == 1) {
                    handler.postDelayed(r, 500);
                }

            }
        }
    }
}
