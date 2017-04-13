package mree.cloud.music.player.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import mree.cloud.music.player.common.ref.ConnectionType;

/**
 * Created by eercan on 27.02.2017.
 */

public class ConnectivityHelper {
    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnectedOrConnecting());
    }

    public static ConnectionType chkStatus(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager
                .TYPE_MOBILE);
        if (wifi.isConnectedOrConnecting()) {
            return ConnectionType.WIFI;
        } else if (mobile.isConnectedOrConnecting()) {
            return ConnectionType.MOBILE_DATA;
        } else {
            return ConnectionType.NONE;
        }
    }
}
