package mree.cloud.music.player.common.ref;

/**
 * Created by mree on 06.11.2015.
 */
public enum SourceType {

    LOCAL(0, "Local"),
    ONEDRIVE(1, "Microsoft OneDrive"),
    DROPBOX(2, "Dropbox"),
    GOOGLE_DRIVE(3, "Google Drive"),
    SPOTIFY(4, "Spotify"),
    YANDEX_DISK(5, "YandexDisk"),
    BOX(6, "Box");

    private Integer code;
    private String desc;

    SourceType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SourceType get(Integer code) {

        if (code == null) {
            return null;
        }

        for (SourceType ot : values()) {
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
