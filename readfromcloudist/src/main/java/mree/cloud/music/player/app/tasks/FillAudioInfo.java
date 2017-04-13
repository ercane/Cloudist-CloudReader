package mree.cloud.music.player.app.tasks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

import mree.cloud.music.player.app.act.fragment.AudioFragment;
import mree.cloud.music.player.app.database.DbConstants;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.audio.AudioStatus;

/**
 * Created by mree on 28.02.2016.
 */
public class FillAudioInfo implements Runnable {

    private static final String TAG = FillAudioInfo.class.getSimpleName();
    private Handler addHandler;
    private Handler finishHandler;
    private List<HashMap<String, String>> allAudios;

    public FillAudioInfo(Handler addHandler, Handler finishHandler, List<HashMap<String, String>>
            allAudios) {
        this.addHandler = addHandler;
        this.finishHandler = finishHandler;
        this.allAudios = allAudios;
    }

    @Override
    public void run() {
        try {
            for (HashMap<String, String> audio : allAudios) {
                try {
                    SongInfo si = new SongInfo();

                    if (audio.get(DbConstants.AUDIO_ID) != null &&
                            !"".equals(audio.get(DbConstants.AUDIO_ID))) {
                        si.setId(audio.get(DbConstants.AUDIO_ID));
                    }

                    if (audio.get(DbConstants.AUDIO_ACCOUNT_ID) != null &&
                            !"".equals(audio.get(DbConstants.AUDIO_ACCOUNT_ID))) {
                        si.setAccountId(audio.get(DbConstants.AUDIO_ACCOUNT_ID));
                    }

                    if (audio.get(DbConstants.AUDIO_PATH) != null &&
                            !"".equals(audio.get(DbConstants.AUDIO_PATH))) {
                        si.setPath(audio.get(DbConstants.AUDIO_PATH));
                    }

                    if (audio.get(DbConstants.AUDIO_TITLE) != null &&
                            !"".equals(audio.get(DbConstants.AUDIO_TITLE))) {
                        si.setTitle(audio.get(DbConstants.AUDIO_TITLE));
                    }

                    if (audio.get(DbConstants.AUDIO_ALBUM) != null &&
                            !"".equals(audio.get(DbConstants.AUDIO_ALBUM))) {
                        si.setAlbum(audio.get(DbConstants.AUDIO_ALBUM));
                    }

                    if (audio.get(DbConstants.AUDIO_ARTIST) != null &&
                            !"".equals(audio.get(DbConstants.AUDIO_ARTIST))) {
                        si.setArtist(audio.get(DbConstants.AUDIO_ARTIST));
                    } else if (audio.get(DbConstants.AUDIO_ALBUM_ARTIST) != null &&
                            !"".equals(audio.get(DbConstants.AUDIO_ALBUM_ARTIST))) {
                        si.setArtist(audio.get(DbConstants.AUDIO_ALBUM_ARTIST));
                    }

                    if (audio.get(DbConstants.AUDIO_SOURCE_TYPE) != null &&
                            !"".equals(audio.get(DbConstants.AUDIO_SOURCE_TYPE))) {
                        Integer code = Integer.parseInt(audio.get(DbConstants.AUDIO_SOURCE_TYPE));
                        si.setSourceType(SourceType.get(code));
                    }

                    if (audio.get(DbConstants.AUDIO_DOWNLOAD_URL) != null &&
                            !"".equals(audio.get(DbConstants.AUDIO_DOWNLOAD_URL))) {
                        si.setDownloadUrl(audio.get(DbConstants.AUDIO_DOWNLOAD_URL));
                    }

                    if (audio.get(DbConstants.AUDIO_THUMBNAIL) != null &&
                            !"".equals(audio.get(DbConstants.AUDIO_THUMBNAIL))) {
                        si.setThumbnail(audio.get(DbConstants.AUDIO_THUMBNAIL));
                    }

                    if (audio.get(DbConstants.AUDIO_STATUS) != null &&
                            !"".equals(audio.get(DbConstants.AUDIO_STATUS))) {
                        si.setStatus(AudioStatus.get(Integer.parseInt(audio.get(DbConstants
                                .AUDIO_STATUS))));
                    } else {
                        si.setStatus(AudioStatus.ONLINE);
                    }

                    Bundle b = new Bundle();
                    b.putSerializable(AudioFragment.AUDIO_INFO, si);
                    Message m = new Message();
                    m.setData(b);
                    addHandler.sendMessage(m);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            Log.e(TAG, "Scan completed");
            finishHandler.sendEmptyMessage(25);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
