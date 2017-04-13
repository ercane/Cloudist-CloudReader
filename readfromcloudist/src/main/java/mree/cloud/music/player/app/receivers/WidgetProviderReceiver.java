package mree.cloud.music.player.app.receivers;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import mree.cloud.music.player.app.utils.WidgetProvider;

/**
 * Created by eercan on 09.12.2016.
 */

public class WidgetProviderReceiver extends AppWidgetProvider {
    private static final String TAG = WidgetProviderReceiver.class.getSimpleName();
    private static WidgetProvider provider;
    private Context context;

    public static void setProvider(WidgetProvider provider) {
        WidgetProviderReceiver.provider = provider;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        if (provider != null) {
            provider.setAllWidgetIds(appWidgetIds);
        }
        for (int i : appWidgetIds) {
            RemoteViews expView = provider.getExpView(false, i, appWidgetIds);
            appWidgetManager.updateAppWidget(i, expView);
        }
    }
}
