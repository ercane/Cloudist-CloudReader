/*

 *
 * Project: mcys-server-common
 * Date Created: 9 Nis 2015
 * Created By: ykurt
 */
package mree.cloud.music.player.common.ref;

import java.util.HashSet;
import java.util.Set;

/**
 * @author ykurt
 */
public enum CommonStatus {

    YES(1, "Yes"),
    NO(2, "No"),
    ENABLED(3, "Enabled"),
    DISABLED(4, "Disabled"),
    EXISTS(5, "Exists"),
    NOTEXISTS(6, "Not Exists"),
    REJECTED(7, "Rejected"),
    AGGREED(8, "Aggreed"),
    POSTPONED(9, "Postponed"),
    ABANDONED(10, "Abandoned"),
    SUSPENDED(11, "Suspended"),
    ACCEPTED(12, "Accepted"),
    CANCELLED(13, "Cancelled"),
    STARTED(14, "Started"),
    FINISHED(15, "Finished"),
    SUCCESS(16, "Success"),
    FAIL(17, "Fail"),
    CLOUDY(18, "Cloudy"),
    CREATED(19, "Created"),
    SUBMIT(20, "Submitted"),
    SENT(21, "Sent"),
    PENDING(22, "Pending"),
    ERROR(23, "Error"),
    ACTIVE(24, "Active"),
    PASSIVE(25, "Passive"),
    PARTIAL(26, "Partial");

    private int code;
    private String desc;

    CommonStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;

    }

    public static CommonStatus get(Integer code) {

        if (code == null) {
            return null;
        }

        for (CommonStatus v : values()) {
            if (v.code == code) {
                return v;
            }
        }
        throw new IllegalArgumentException("No matching type: " + code);
    }

    public static CommonStatus get(String desc) {

        if (desc == null) {
            return null;
        }

        for (CommonStatus v : values()) {
            if (v.desc == desc) {
                return v;
            }
        }
        throw new IllegalArgumentException("No matching desc: " + desc);
    }

    public static String encode(Set<CommonStatus> csSet) {
        StringBuilder enc = new StringBuilder();

        for (CommonStatus cs : csSet) {
            enc.append(cs.getCode()).append("-");
        }

        if (enc.length() > 1) {
            enc.replace(enc.length() - 1, enc.length(), "");
        }

        return enc.toString();
    }

    public static Set<CommonStatus> decode(String encoded) {

        Set<CommonStatus> csList = new HashSet<CommonStatus>();

        if (encoded != null) {

            String[] split = encoded.split("-");

            for (String s : split) {
                try {
                    int c = Integer.parseInt(s);
                    csList.add(get(c));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return csList;
    }

    public static Set<Integer> getCodes(Set<CommonStatus> csSet) {
        Set<Integer> codeSet = new HashSet<Integer>();

        for (CommonStatus cs : csSet) {
            codeSet.add(cs.getCode());
        }

        return codeSet;
    }

    public static Set<Integer> getCodes(String encoded) {
        return getCodes(decode(encoded));
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
