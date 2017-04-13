package mree.cloud.music.player.app.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by eercan on 28.02.2017.
 */

public class NotificationHelper {
    public static void showSimpleNotification(Context context, int notId, int icon, String title,
                                              String msg) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(icon)
                        .setContentTitle(title)
                        .setContentText(msg);
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService
                (NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(notId, mBuilder.build());
    }
}
