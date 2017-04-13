package mree.cloud.music.player.common.model.dropbox;

import com.google.gson.annotations.SerializedName;

/**
 * Created by eercan on 10.01.2017.
 */

public class DownloadInfo {
    @SerializedName("metadata")
    public Metadata metadata;

    @SerializedName("link")
    public String link;
}
