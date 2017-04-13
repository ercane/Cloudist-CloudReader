package mree.cloud.music.player.common.model.google;

import com.google.gson.annotations.SerializedName;

/**
 * Created by eercan on 18.11.2016.
 */

public enum GoogleKind {

    FILE("drive#file"),
    FILELIST("drive#fileList");

    @SerializedName("kind")
    private String desc;

    GoogleKind(String desc) {
        this.desc = desc;
    }


}
