package mree.cloud.music.player.app.tasks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.HashMap;
import java.util.List;

import mree.cloud.music.player.app.act.fragment.AudioFragment;
import mree.cloud.music.player.app.database.DbConstants;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.ref.SourceType;

/**
 * Created by mree on 28.02.2016.
 */
public class FillAlbumInfo implements Runnable {

    private Handler addHandler;
    private List<HashMap<String, String>> allAudios;

    public FillAlbumInfo(Handler addHandler, List<HashMap<String, String>> allAudios) {
        this.addHandler = addHandler;
        this.allAudios = allAudios;
    }

    @Override
    public void run() {
        try {
            for (HashMap<String, String> audio : allAudios) {
                SongInfo si = new SongInfo();


                if (audio.get(DbConstants.AUDIO_ALBUM) != null &&
                        !"".equals(audio.get(DbConstants.AUDIO_ALBUM))) {
                    si.setAlbum(audio.get(DbConstants.AUDIO_ALBUM));
                } else {
                    continue;
                }

                if (audio.get(DbConstants.AUDIO_ARTIST) != null &&
                        !"".equals(audio.get(DbConstants.AUDIO_ARTIST))) {
                    si.setArtist(audio.get(DbConstants.AUDIO_ARTIST));
                } else if (audio.get(DbConstants.AUDIO_ALBUM_ARTIST) != null &&
                        !"".equals(audio.get(DbConstants.AUDIO_ALBUM_ARTIST))) {
                    si.setArtist(audio.get(DbConstants.AUDIO_ALBUM_ARTIST));
                }


                if (audio.get(DbConstants.AUDIO_THUMBNAIL) != null &&
                        !"".equals(audio.get(DbConstants.AUDIO_THUMBNAIL))) {
                    si.setThumbnail(audio.get(DbConstants.AUDIO_THUMBNAIL));
                }

                if (audio.get(DbConstants.AUDIO_SOURCE_TYPE) != null &&
                        !"".equals(audio.get(DbConstants.AUDIO_SOURCE_TYPE))) {
                    si.setSourceType(SourceType.get(Integer.parseInt(audio.get(DbConstants
                            .AUDIO_SOURCE_TYPE))));
                }

                Bundle b = new Bundle();
                b.putSerializable(AudioFragment.AUDIO_INFO, si);
                Message m = new Message();
                m.setData(b);
                addHandler.sendMessage(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
