package mree.cloud.music.player.common.ref;

/**
 * Created by mree on 31.08.2016.
 */
public enum SettingType {
    ACCOUNT(1, "Account Settings"),
    PLAYER(2, "Player Settings"),
    DATA(3, "Data Settings"),
    DISPLAY(4, "Display Settings");

    private Integer code;
    private String desc;

    SettingType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SettingType get(Integer code) {

        if (code == null) {
            return null;
        }

        for (SettingType ot : values()) {
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
