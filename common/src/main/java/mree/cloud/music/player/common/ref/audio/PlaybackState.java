package mree.cloud.music.player.common.ref.audio;

/**
 * Created by eercan on 13.12.2016.
 */

public enum PlaybackState {

    INITIAL(-25, "Initial"),
    STARTED(0, "Started"),
    PREPARING(1, "Preparing"),
    PLAYING(2, "Playing"),
    PAUSED(3, "Paused"),
    PREPARED(4, "Prepared");

    private Integer code;
    private String desc;

    PlaybackState(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PlaybackState get(Integer code) {

        if (code == null) {
            return null;
        }

        for (PlaybackState ot : values()) {
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
