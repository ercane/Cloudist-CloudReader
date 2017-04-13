package mree.cloud.music.player.app.utils;

import java.util.List;

/**
 * Created by eercan on 11.11.2016.
 */

public class StringUtils {
    public static String getListEncoded(List<String> list) {
        String value = "";
        int i = 0;
        for (i = 0; i < list.size(); i++) {
            value += "'" + list.get(i) + "'";

            if (i != list.size() - 1) {
                value += ",";
            }
        }
        return value;
    }
}
