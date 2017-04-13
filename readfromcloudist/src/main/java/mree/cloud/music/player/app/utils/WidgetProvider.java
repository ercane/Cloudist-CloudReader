package mree.cloud.music.player.app.utils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.RemoteViews;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.receivers.NotificationReceiver;

/**
 * Created by eercan on 09.12.2016.
 */

public class WidgetProvider {
    private static final String TAG = WidgetProvider.class.getSimpleName();
    private Context context;
    private String title;
    private String artist;
    private Bitmap image;
    private int[] allWidgetIds;

    public WidgetProvider() {
    }

    public void refresh(boolean isPlaying, int[] allWidgetIds) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        if (allWidgetIds == null || allWidgetIds.length < 1) {
            allWidgetIds = this.allWidgetIds;
        }

        if (allWidgetIds != null) {
            for (int widgetId : allWidgetIds) {
                RemoteViews expView = getExpView(isPlaying, widgetId, allWidgetIds);
                appWidgetManager.updateAppWidget(widgetId, expView);
            }
        }
    }

    public RemoteViews getExpView(boolean isPlaying, int widgetId, int[] appWidgetIds) {

        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout
                .player_not_exp);

        try {
            if (isPlaying) {
                Intent pause = new Intent(NotificationReceiver.ACTION_PAUSE);
                addFlags(pause, widgetId, appWidgetIds);
                PendingIntent pendPause = PendingIntent.getBroadcast(context, 0, pause,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.playPause, pendPause);
                views.setImageViewResource(R.id.playPause, R.drawable.ic_pause_white);
            } else {
                Intent play = new Intent(NotificationReceiver.ACTION_PLAY);
                addFlags(play, widgetId, appWidgetIds);
                PendingIntent pendPlay = PendingIntent.getBroadcast(context, 0, play,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.playPause, pendPlay);
                views.setImageViewResource(R.id.playPause, R.drawable.ic_play_white);
            }

            Intent prev = new Intent(NotificationReceiver.ACTION_PREV);
            addFlags(prev, widgetId, appWidgetIds);
            PendingIntent pendPrev = PendingIntent.getBroadcast(context, 0, prev,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.prev, pendPrev);

            Intent next = new Intent(NotificationReceiver.ACTION_NEXT);
            addFlags(next, widgetId, appWidgetIds);
            PendingIntent pendNext = PendingIntent.getBroadcast(context, 0, next,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.next, pendNext);

            Intent open = new Intent(NotificationReceiver.ACTION_OPEN_ACTIVITY);
            addFlags(open, widgetId, appWidgetIds);
            PendingIntent pendOpen = PendingIntent.getBroadcast(context, 0, open,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.coverImage, pendOpen);

            Intent close = new Intent(NotificationReceiver.ACTION_CLOSE);
            addFlags(close, widgetId, appWidgetIds);
            PendingIntent pendClose = PendingIntent.getBroadcast(context, 0, close,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.close, pendClose);

            if (title != null) {
                views.setTextViewText(R.id.tvTitle, title);
            }

            if (artist != null) {
                views.setTextViewText(R.id.tvArtist, artist);
            }

            if (image != null) {
                //            Bitmap bitmap = BitmapFactory.decodeByteArray(si.getThumbnail(), 0,
                // si.getThumbnail()
                //                    .length);
               /* File filesDir = context.getFilesDir();
                String path = filesDir.getAbsolutePath() + "/" + imagePath;
                Bitmap bitmap = BitmapFactory.decodeFile(path);*/
                try {
                    views.setImageViewBitmap(R.id.coverImage, image);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    views.setImageViewResource(R.id.coverImage, R.drawable.default_cover);
                }
            } else {
                views.setImageViewResource(R.id.coverImage, R.drawable.default_cover);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }

        return views;
    }

    private void addFlags(Intent intent, int widgetId, int[] appWidgetIds) {
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int[] getAllWidgetIds() {
        return allWidgetIds;
    }

    public void setAllWidgetIds(int[] allWidgetIds) {
        this.allWidgetIds = allWidgetIds;
    }
}
