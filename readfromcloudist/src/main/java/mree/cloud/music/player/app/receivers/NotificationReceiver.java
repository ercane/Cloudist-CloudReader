package mree.cloud.music.player.app.receivers;

import android.app.ActivityManager;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import mree.cloud.music.player.app.act.MainActivity;
import mree.cloud.music.player.app.act.fragment.AudioFragment;
import mree.cloud.music.player.app.services.MusicService;

/**
 * Created by mree on 01.03.2016.
 */
public class NotificationReceiver extends BroadcastReceiver {
    public static final String ACTION_NEXT = "action_next";
    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_PREV = "action_prev";
    public static final String ACTION_OPEN_ACTIVITY = "action_activity";
    public static final String ACTION_CLOSE = "action_close";
    private MusicService musicSrv;


    @Override
    public void onReceive(Context context, Intent intent) {
        this.musicSrv = AudioFragment.getMusicService();
        String action = intent.getAction();
        int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        boolean isPlay = false;
        if (musicSrv != null && MusicService.isReady) {
            switch (action) {
                case ACTION_NEXT:
                    musicSrv.playNext(true);
                    break;
                case ACTION_OPEN_ACTIVITY:
                   /* Intent i = new Intent(context, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(i);*/
                    final ActivityManager activityManager = (ActivityManager) context
                            .getSystemService(Context.ACTIVITY_SERVICE);
                    final List<ActivityManager.RunningTaskInfo> recentTasks = activityManager
                            .getRunningTasks(Integer.MAX_VALUE);

                    for (int i = 0; i < recentTasks.size(); i++) {
                        Log.d("Executed app", "Application executed : "
                                + recentTasks.get(i).baseActivity.toShortString()
                                + "\t\t ID: " + recentTasks.get(i).id + "");
                        // bring to front
                        if (recentTasks.get(i).baseActivity.toShortString().contains(MainActivity
                                .class.getSimpleName())) {
                            activityManager.moveTaskToFront(recentTasks.get(i).id,
                                    ActivityManager.MOVE_TASK_WITH_HOME);
                        }
                    }
                    break;
                case ACTION_PLAY:
                    musicSrv.go();
                    isPlay = true;
                    break;
                case ACTION_PAUSE:
                    musicSrv.pausePlayer();
                    isPlay = false;
                    break;
                case ACTION_PREV:
                    musicSrv.playPrev();
                    break;
                case ACTION_CLOSE:
                    musicSrv.stopSelf();
                    int pid = android.os.Process.myPid();
                    android.os.Process.killProcess(pid);
                    System.exit(0);
                    break;
            }

            if (widgetId != -1) {
                MusicService.getWidgetProvider().refresh(isPlay, appWidgetIds);
            }
        }
    }
}
