/*

 *
 * Project: mcys-server-common
 * Date Created: 14 Nis 2015
 * Created By: ykurt
 */
package mree.cloud.music.player.common.ref.auth;

/**
 * @author ykurt
 */
public enum AccountStatus {

    ACTIVE(1, "Active"),
    PASSIVE(2, "Passive"),
    LOCKED(3, "Locked"),
    SUSPENDED(4, "Suspended"),
    CANCELLED(5, "Cancelled");

    private Integer code;
    private String desc;

    AccountStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;

    }

    public static AccountStatus get(Integer code) {

        if (code == null) {
            return null;
        }

        for (AccountStatus v : values()) {
            if (v.code == code) {
                return v;
            }
        }
        throw new IllegalArgumentException("No matching type: " + code);
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return desc;
    }
}
