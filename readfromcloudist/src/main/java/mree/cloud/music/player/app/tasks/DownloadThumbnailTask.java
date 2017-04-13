package mree.cloud.music.player.app.tasks;

import android.content.Context;
import android.util.Log;

import mree.cloud.music.player.app.utils.ImageUtils;

/**
 * Created by eercan on 25.11.2016.
 */

public class DownloadThumbnailTask implements Runnable {
    private static final String TAG = DownloadThumbnailTask.class.getSimpleName();
    private Context context;
    private String filename;
    private String url;

    public DownloadThumbnailTask(Context context, String filename, String url) {
        this.context = context;
        this.filename = filename;
        this.url = url;
    }

    @Override
    public void run() {
        ImageUtils.downloadThumbnail(context, filename, url);
        Log.e(TAG, filename + " downloaded");
    }
}
