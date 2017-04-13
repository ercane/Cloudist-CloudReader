package mree.cloud.music.player.common.ref.audio;

/**
 * Created by eercan on 20.01.2017.
 */

public enum AudioStatus {
    ONLINE(0, "Online"),
    OFFLINE(1, "Offline"),
    CACHED(2, "NONE");

    private Integer code;
    private String desc;

    AudioStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static AudioStatus get(Integer code) {

        if (code == null) {
            return null;
        }

        for (AudioStatus ot : values()) {
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
