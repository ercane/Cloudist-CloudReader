package mree.cloud.music.player.common.model;

import mree.cloud.music.player.common.MarkedInfo;
import mree.cloud.music.player.common.ref.ScanStatus;

/**
 * Created by eercan on 16.02.2017.
 */

public class DownloadAudioInfo extends MarkedInfo {
    private String downloadAudioId;
    private ScanStatus status;

    public String getDownloadAudioId() {
        return downloadAudioId;
    }

    public void setDownloadAudioId(String downloadAudioId) {
        this.downloadAudioId = downloadAudioId;
    }

    public ScanStatus getStatus() {
        return status;
    }

    public void setStatus(ScanStatus status) {
        this.status = status;
    }
}
