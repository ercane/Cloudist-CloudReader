package mree.cloud.music.player.common.ref;

/**
 * Created by eercan on 12.10.2016.
 */
public enum FragmentType {

    AUDIO(0, "Audio"),
    ALBUM(1, "Album"),
    ARTIST(2, "Artist"),
    ACCOUNT(3, "Account"),
    PLAYLIST(4, "Playlist"),
    NONE(-1, "NONE");

    private Integer code;
    private String desc;

    FragmentType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static FragmentType get(Integer code) {

        if (code == null) {
            return null;
        }

        for (FragmentType ot : values()) {
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
