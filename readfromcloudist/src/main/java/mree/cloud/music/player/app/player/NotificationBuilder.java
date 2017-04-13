package mree.cloud.music.player.app.player;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.act.MainActivity;
import mree.cloud.music.player.app.receivers.NotificationReceiver;

/**
 * Created by eercan on 14.10.2016.
 */
public class NotificationBuilder {
    private static final String TAG = NotificationBuilder.class.getSimpleName();
    private Notification not;
    private Context context;
    private String title;
    private String artist;
    private Bitmap image;

    public NotificationBuilder(Context context, String title, String artist, Bitmap image) {
        this.context = context;
        this.title = title;
        this.artist = artist;
        this.image = image;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void prepareNotification(boolean b) {
        Intent notIntent = new Intent(context, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);


        Notification.Builder builder = new Notification.Builder(context);
/*        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notIntent);
        PendingIntent pendInt = stackBuilder.getPendingIntent(
                0, PendingIntent.FLAG_UPDATE_CURRENT);*/
        PendingIntent pendInt = PendingIntent.getActivity(context, 0, notIntent, PendingIntent
                .FLAG_UPDATE_CURRENT);


        final RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout
                .player_notf_layout);
        contentView.setTextViewText(R.id.tvTitle, title);

        /*Action next*/
        Intent next = new Intent(NotificationReceiver.ACTION_NEXT);
        PendingIntent pendNext = PendingIntent.getBroadcast(context, 0, next,
                PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.next, pendNext);

        /*Action pause*/
        if (b) {
            Intent pause = new Intent(NotificationReceiver.ACTION_PAUSE);
            PendingIntent pendPause = PendingIntent.getBroadcast(context, 0, pause,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            contentView.setOnClickPendingIntent(R.id.playPause, pendPause);
            contentView.setImageViewResource(R.id.playPause, R.drawable.ic_pause_white);
        } else {
            /*Action play*/
            Intent play = new Intent(NotificationReceiver.ACTION_PLAY);
            PendingIntent pendPlay = PendingIntent.getBroadcast(context, 0, play,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            contentView.setOnClickPendingIntent(R.id.playPause, pendPlay);
            contentView.setImageViewResource(R.id.playPause, R.drawable.ic_play_white);
        }

        if (image != null) {
            /*File filesDir = context.getFilesDir();
            String path = filesDir.getAbsolutePath() + "/" + imagePath;
            Bitmap bitmap = BitmapFactory.decodeFile(path);*/
            try {

                contentView.setImageViewBitmap(R.id.coverImage, image);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                contentView.setImageViewResource(R.id.coverImage, R.drawable.default_cover);
            }
        } else {
            contentView.setImageViewResource(R.id.coverImage, R.drawable.default_cover);
        }

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(title)
                .setOngoing(true)
                .setContent(contentView);

        not = builder.build();
        not.bigContentView = getExpView(b);
    }

    private RemoteViews getExpView(boolean isPlaying) {
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout
                .player_not_exp);

        if (isPlaying) {
            Intent pause = new Intent(NotificationReceiver.ACTION_PAUSE);
            PendingIntent pendPause = PendingIntent.getBroadcast(context, 0, pause,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.playPause, pendPause);
            views.setImageViewResource(R.id.playPause, R.drawable.ic_pause_white);
        } else {
            Intent play = new Intent(NotificationReceiver.ACTION_PLAY);
            PendingIntent pendPlay = PendingIntent.getBroadcast(context, 0, play,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.playPause, pendPlay);
            views.setImageViewResource(R.id.playPause, R.drawable.ic_play_white);
        }

        Intent prev = new Intent(NotificationReceiver.ACTION_PREV);
        PendingIntent pendPrev = PendingIntent.getBroadcast(context, 0, prev,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.prev, pendPrev);

        Intent next = new Intent(NotificationReceiver.ACTION_NEXT);
        PendingIntent pendNext = PendingIntent.getBroadcast(context, 0, next,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.next, pendNext);

        Intent open = new Intent(NotificationReceiver.ACTION_OPEN_ACTIVITY);
        PendingIntent pendOpen = PendingIntent.getBroadcast(context, 0, open,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.coverImage, pendOpen);

        Intent close = new Intent(NotificationReceiver.ACTION_CLOSE);
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
//            Bitmap bitmap = BitmapFactory.decodeByteArray(si.getThumbnail(), 0, si.getThumbnail()
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

        return views;
    }

    public Notification getNot() {
        return not;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
