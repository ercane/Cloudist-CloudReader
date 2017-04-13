package mree.cloud.music.player.common.model.box;

/**
 * Created by eercan on 20.02.2017.
 */

public class Permissions {
    private boolean can_download;
    private boolean can_preview;

    public Permissions() {
    }

    public Permissions(boolean can_download, boolean can_preview) {
        this.can_download = can_download;
        this.can_preview = can_preview;
    }

    public boolean isCan_download() {
        return can_download;
    }

    public void setCan_download(boolean can_download) {
        this.can_download = can_download;
    }

    public boolean isCan_preview() {
        return can_preview;
    }

    public void setCan_preview(boolean can_preview) {
        this.can_preview = can_preview;
    }
}
