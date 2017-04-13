package mree.cloud.music.player.app.utils;

import android.widget.ImageView;
import android.widget.TextView;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.common.ref.SourceType;

/**
 * Created by mree on 16.10.2016.
 */
public class IconHelper {
    public static final int dropbox = 0xeaaf;
    public static final int spotify = 0xf1dd;
    public static final int google_drive = 0xf1c1;
    public static final int onedrive = 0xeab0;
    public static final int local = 0xe95c;
    public static final int none = 0xea09;


    public static void setSourceIcon(TextView tvSource, SourceType type) {
/*        switch (type) {
            case LOCAL:
                tvSource.setTypeface(Typeface.createFromAsset(CmpDeviceService.getContext()
                        .getAssets(), "fontawesome-webfont.ttf"));
                tvSource.setText(String.valueOf((char) FontAwasome.stop.getValue()));
                tvSource.setTextColor(Color.BLACK);
                break;
            case ONEDRIVE:
                tvSource.setTypeface(Typeface.createFromAsset(CmpDeviceService.getContext()
                        .getAssets(), "icomoon.ttf"));
                tvSource.setText(String.valueOf((char) onedrive));
                tvSource.setTextColor(Color.BLUE);
                break;
            case DROPBOX:
                tvSource.setTypeface(Typeface.createFromAsset(CmpDeviceService.getContext()
                        .getAssets(), "icomoon.ttf"));
                tvSource.setText(String.valueOf((char) dropbox));
                tvSource.setTextColor(Color.CYAN);
                break;
            case GOOGLE_DRIVE:
                tvSource.setTypeface(Typeface.createFromAsset(CmpDeviceService.getContext()
                        .getAssets(), "foundation.ttf"));
                tvSource.setText(String.valueOf((char) google_drive));
                tvSource.setTextColor(Color.GREEN);
                break;
            case SPOTIFY:
                tvSource.setTypeface(Typeface.createFromAsset(CmpDeviceService.getContext()
                        .getAssets(), "foundation.ttf"));
                tvSource.setText(String.valueOf((char) spotify));
                tvSource.setTextColor(Color.GREEN);
                break;
            case NONE:
                tvSource.setTypeface(Typeface.createFromAsset(CmpDeviceService.getContext()
                        .getAssets(), "icomoon.ttf"));
                tvSource.setText(String.valueOf((char) none));
                tvSource.setTextColor(Color.RED);
                break;
        }    */

        switch (type) {
            case LOCAL:
                tvSource.setBackgroundResource(R.mipmap.ic_launcher);
                break;
            case ONEDRIVE:
                tvSource.setBackgroundResource(R.drawable.ic_onedrive);
                break;
            case DROPBOX:
                tvSource.setBackgroundResource(R.drawable.ic_dropbox);
                break;
            case GOOGLE_DRIVE:
                tvSource.setBackgroundResource(R.drawable.ic_drive);
                break;
            case YANDEX_DISK:
                tvSource.setBackgroundResource(R.drawable.ic_yandex);
                break;
            case SPOTIFY:
                tvSource.setBackgroundResource(R.drawable.ic_spotify);
                break;
            case BOX:
                tvSource.setBackgroundResource(R.drawable.ic_box);
                break;
        }

    }

    public static void setSourceIcon(ImageView ivSource, SourceType type) {
        switch (type) {
            case LOCAL:
                ivSource.setImageResource(R.mipmap.ic_launcher);
                break;
            case ONEDRIVE:
                ivSource.setImageResource(R.drawable.ic_onedrive);
                break;
            case DROPBOX:
                ivSource.setImageResource(R.drawable.ic_dropbox);
                break;
            case GOOGLE_DRIVE:
                ivSource.setImageResource(R.drawable.ic_drive);
                break;
            case YANDEX_DISK:
                ivSource.setImageResource(R.drawable.ic_yandex);
                break;
            case SPOTIFY:
                ivSource.setImageResource(R.drawable.ic_spotify);
                break;
            case BOX:
                ivSource.setImageResource(R.drawable.ic_box);
                break;
        }

    }
}
