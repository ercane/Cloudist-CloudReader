package mree.cloud.music.player.app.tasks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

import mree.cloud.music.player.app.act.fragment.AudioFragment;
import mree.cloud.music.player.app.database.DbConstants;
import mree.cloud.music.player.common.model.PlaylistInfo;
import mree.cloud.music.player.common.ref.audio.PlaylistType;

/**
 * Created by eercan on 11.11.2016.
 */

public class FillPlaylistInfo implements Runnable {
    private static final String TAG = FillPlaylistInfo.class.getSimpleName();
    private Handler addHandler;
    private List<HashMap<String, String>> allPlaylists;

    public FillPlaylistInfo(Handler addHandler, List<HashMap<String, String>> allPlaylists) {
        this.addHandler = addHandler;
        this.allPlaylists = allPlaylists;
    }

    @Override
    public void run() {
        try {
            for (HashMap<String, String> p : allPlaylists) {
                try {
                    PlaylistInfo pi = new PlaylistInfo();
                    pi.setId(p.get(DbConstants.PLAYLIST_ID));
                    pi.setName(p.get(DbConstants.PLAYLIST_NAME));
                    pi.setCount(Integer.parseInt(p.get(DbConstants.PLAYLIST_AUDIO_COUNT)));
                    pi.setOfflineStatus(Integer.parseInt(p.get(DbConstants
                            .PLAYLIST_OFFLINE_STATUS)));
                    String type = p.get(DbConstants.PLAYLIST_TYPE);
                    if (type != null) {
                        pi.setType(PlaylistType.get(Integer.parseInt(type)));
                    }

                    Bundle b = new Bundle();
                    b.putSerializable(AudioFragment.AUDIO_INFO, pi);
                    Message m = new Message();
                    m.setData(b);
                    addHandler.sendMessage(m);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage() + "", e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
