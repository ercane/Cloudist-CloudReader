package mree.cloud.music.player.common.ref;

/**
 * Created by eercan on 09.03.2017.
 */

public enum ConnectionType {
    MOBILE_DATA(0, "Mobile Data"),
    WIFI(1, "WIFI"),
    NONE(-1, "NONE");

    private Integer code;
    private String desc;

    ConnectionType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ConnectionType get(Integer code) {

        if (code == null) {
            return null;
        }

        for (ConnectionType ot : values()) {
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
