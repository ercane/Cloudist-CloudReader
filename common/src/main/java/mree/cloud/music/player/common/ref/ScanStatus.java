package mree.cloud.music.player.common.ref;

/**
 * Created by mree on 03.09.2016.
 */
public enum ScanStatus {
    INITIAL(1, "Initial"),
    STARTED(2, "Started"),
    STOPPED(3, "Stooped"),
    FINISHED(4, "Finished"),
    FAILED(5, "Failed");

    private Integer code;
    private String desc;

    ScanStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ScanStatus get(Integer code) {

        if (code == null) {
            return null;
        }

        for (ScanStatus ot : values()) {
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
