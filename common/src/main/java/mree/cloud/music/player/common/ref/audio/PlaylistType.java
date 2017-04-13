package mree.cloud.music.player.common.ref.audio;

/**
 * Created by eercan on 11.11.2016.
 */

public enum PlaylistType {
    LOCAL(0, "LOCAL"),
    SPOTIFY(1, "Spotify"),
    NONE(-1, "NONE");

    private Integer code;
    private String desc;

    PlaylistType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PlaylistType get(Integer code) {

        if (code == null) {
            return null;
        }

        for (PlaylistType ot : values()) {
            if (ot.code == code) {
                return ot;
            }
        }
        throw new IllegalArgumentException("No matching type: " + code);
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
