package mree.cloud.music.player.app.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import mree.cloud.music.player.app.act.fragment.AudioFragment;


/**
 * Created by eercan on 09.11.2016.
 */

public class HeadsetPlugReceiver extends BroadcastReceiver {
    private static final String TAG = HeadsetPlugReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            switch (state) {
                case 0:
                    if (AudioFragment.getMusicService() != null && AudioFragment.getMusicService
                            ().isPng()) {
                        AudioFragment.getMusicService().pausePlayer();
                    }
                    Log.d(TAG, "Headset is unplugged");
                    break;
                case 1:
                    Log.d(TAG, "Headset is plugged");
                    break;
                default:
                    Log.d(TAG, "I have no idea what the headset state is");
            }
        }
    }
}
