package mree.cloud.music.player.app.utils;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import mree.cloud.music.player.app.database.DbConstants;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.services.CmpDeviceService;
import mree.cloud.music.player.common.ref.audio.AudioStatus;

/**
 * Created by eercan on 27.02.2017.
 */

public class FileUtils {
    public static final File OFFLINE_ROOT = new File(CmpDeviceService.getDataRoot(), Constants
            .OFF_ROOT);
    private static final String TAG = FileUtils.class.getSimpleName();

    public static void removeOfflinePlaylistFiles(String playlistId) {
        ArrayList<HashMap<String, String>> audios = DbEntryService.getAudiosOfPlaylist(playlistId);
        for (HashMap<String, String> audio : audios) {
            removeOfflineFile(audio.get(DbConstants.AUDIO_ID));
        }
    }

    public static void removeOfflineFile(String audioId) {
        File file = new File(OFFLINE_ROOT, audioId + Constants.OFF_EXT);
        boolean status = false;
        if (file.exists()) {
            status = file.delete();
        }

        if (status) {
            DbEntryService.updateAudioOfflineStatus(audioId, AudioStatus.ONLINE.getCode());
            Log.e(TAG, audioId + Constants.OFF_EXT + " removed");
        } else {
            Log.e(TAG, audioId + Constants.OFF_EXT + " cannot be removed");
        }

    }

    public static File getOfflineFile(String audioId) {
        return new File(OFFLINE_ROOT, audioId + Constants.OFF_EXT);
    }


}
