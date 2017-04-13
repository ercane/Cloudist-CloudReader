package mree.cloud.music.player.app.utils;

import android.util.Log;

import java.io.File;

import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.common.ref.audio.AudioStatus;

/**
 * Created by eercan on 08.03.2017.
 */

public class OfflineHelper {
    private static final String TAG = OfflineHelper.class.getSimpleName();

    public static boolean changeStatus(String songId, AudioStatus status) {
        try {
            DbEntryService.updateAudioOfflineStatus(songId, status.getCode());
            if (status == AudioStatus.ONLINE) {
                File offlineFile = FileUtils.getOfflineFile(songId);
                offlineFile.delete();
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }
}
