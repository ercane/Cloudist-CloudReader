package mree.cloud.music.player.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.tasks.ImageLoaderTask;
import mree.cloud.music.player.app.utils.lazylist.LazyListImageLoader;
import mree.cloud.music.player.common.ref.SourceType;


/**
 * Created by eercan on 14.11.2016.
 */

public class ImageUtils {

    private static final String TAG = ImageUtils.class.getSimpleName();
    private static LazyListImageLoader lazyListImageLoaderTask;
    private static ImageLoaderTask imageLoader;

    public static LazyListImageLoader getLazyImageLoaderTask(Context context) {
        if (lazyListImageLoaderTask == null)
            lazyListImageLoaderTask = new LazyListImageLoader(context);

        return lazyListImageLoaderTask;
    }

    public static ImageLoaderTask getImageLoaderTask() {
        if (imageLoader == null)
            imageLoader = new ImageLoaderTask();

        return imageLoader;
    }


    public static void downloadThumbnail(Context context, String filename, String urlAddress) {
        //ByteArrayOutputStream baos = new ByteArrayOutputStream();

        InputStream is = null;
        try {
            File filesDir = context.getFilesDir();
            File onedrive = new File(filesDir.getAbsolutePath() + "/onedrive/");
            boolean mkdirs = onedrive.mkdirs();
            File f;
            if (mkdirs) {
                f = new File(onedrive, filename);
            } else {
                f = new File(filesDir, filename);
            }
            FileOutputStream fos = new FileOutputStream(f);
            URL url = new URL(urlAddress);
            is = url.openStream();
            byte[] byteChunk = new byte[is.available() + 1]; // Or whatever size you want to read
            // in at a time.
            int n;

            while ((n = is.read(byteChunk)) > 0) {
                fos.write(byteChunk, 0, n);
            }
            if (is != null) {
                is.close();
            }
            return;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage() + "");
            return;
        }

    }

    public static void setCoverImage(Context context, SourceType type, String thumbnail,
                                     ImageView imageView) {
        switch (type) {
            case LOCAL:
                try {
                    Bitmap bmp = BitmapFactory.decodeFile(thumbnail);
                    imageView.setImageBitmap(bmp);
                } catch (Exception e) {
                    imageView.setImageResource(R.drawable.default_cover);
                }
                break;
            case ONEDRIVE:
                /*File filesDir = context.getFilesDir();
                String path = filesDir.getAbsolutePath() + "/onedrive/" + thumbnail;
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                imageView.setImageBitmap(bitmap);*/
                getLazyImageLoaderTask(context).DisplayImage(thumbnail, imageView);
                break;
            /*case DROPBOX:*/
            case GOOGLE_DRIVE:
            case SPOTIFY:
                getLazyImageLoaderTask(context).DisplayImage(thumbnail, imageView);
                break;
        }

    }
}
