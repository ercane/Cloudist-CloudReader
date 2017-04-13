package mree.cloud.music.player.app.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import mree.cloud.music.player.app.act.fragment.AudioFragment;

/**
 * Created by mree on 15.03.2017.
 */

public class NetworkChangeReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(final Context context, final Intent intent){
        sendEmptyMessage().execute();
    }

    AsyncTask<Void, Void, Void> sendEmptyMessage(){
        return new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params){
                try {
                    Thread.sleep(3500);
                    if (AudioFragment.getNetworkHandler() != null) {
                        AudioFragment.getNetworkHandler().sendEmptyMessage(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }
}
