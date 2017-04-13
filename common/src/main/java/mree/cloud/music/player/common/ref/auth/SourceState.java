package mree.cloud.music.player.common.ref.auth;

/**
 * Created by mree on 30.08.2016.
 */
public enum SourceState {

    AUTH(1, "Authorized"),
    NOT_AUTH(2, "Not Authorized"),
    EXPIRED(3, "Expired");

    private Integer code;
    private String desc;

    SourceState(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SourceState get(Integer code) {

        if (code == null) {
            return null;
        }

        for (SourceState ot : values()) {
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
