package mree.cloud.music.player.app.tasks;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.cach.ProxyServer;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.services.CmpDeviceService;
import mree.cloud.music.player.app.utils.ConnectivityHelper;
import mree.cloud.music.player.app.utils.Constants;
import mree.cloud.music.player.app.utils.FileUtils;
import mree.cloud.music.player.app.utils.NotificationHelper;
import mree.cloud.music.player.app.utils.RandomStringUtils;
import mree.cloud.music.player.app.utils.RestHelper;
import mree.cloud.music.player.common.model.DownloadAudioInfo;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.model.box.DownloadItem;
import mree.cloud.music.player.common.ref.ConnectionType;
import mree.cloud.music.player.common.ref.ScanStatus;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.audio.AudioStatus;
import mree.cloud.music.player.rest.ok.BoxOkHttp;
import mree.cloud.music.player.rest.utils.UnauthorizedException;


/**
 * Created by eercan on 20.01.2017.
 */

public class DownloadAudioTask implements Runnable {
    private static final String TAG = DownloadAudioTask.class.getSimpleName();
    private static final int NOT_ID = Integer.parseInt(RandomStringUtils.randomNumeric(4));
    private static File OFFLINE_ROOT = FileUtils.OFFLINE_ROOT;
    private static NotificationManager mNotifyManager;
    public NotificationCompat.Builder mBuilder;
    private File file;
    private Context context;
    private SongInfo songInfo;
    private DownloadAudioInfo dai;

    public DownloadAudioTask(Context context, SongInfo songInfo) {
        this.context = context;
        this.songInfo = songInfo;
        init();

    }

    private void init() {
        OFFLINE_ROOT.mkdirs();
        file = new File(OFFLINE_ROOT, ProxyServer.ENCODE_ID(songInfo.getId()) + Constants.OFF_EXT);
        dai = new DownloadAudioInfo();
        dai.setDownloadAudioId(songInfo.getId());
        dai.setStatus(ScanStatus.STARTED);
        dai.setCreatedDate(new Date());
        long id = DbEntryService.saveDownloadAudios(dai);
        dai.setId(id + "");
        if (file.exists() && songInfo.getStatus() == AudioStatus.ONLINE)
            file.delete();

        if (ConnectivityHelper.isConnected(context)) {
            mNotifyManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(context);
            mBuilder.setContentTitle(songInfo.getTitle() + " downloading...")
                    .setContentText("Download in progress")
                    .setSmallIcon(R.drawable.ic_cloud_download_white_48dp);
        }
    }

    @Override
    public void run() {
        try {
            if (songInfo.getStatus() == AudioStatus.OFFLINE) {
                return;
            } else if (checkOptions()) {
                /*NotificationHelper.showSimpleNotification(context, 1, R.drawable.ic_download_dark,
                        context.getString(R.string.download_start_msg), songInfo.getTitle());*/
                Log.d(TAG, songInfo.getTitle() + " will be download...");
                FileOutputStream fos = new FileOutputStream(file);
                boolean isDownloaded = false;
                if (songInfo.getSourceType() == SourceType.BOX) {
                    BoxOkHttp boxOkHttp = RestHelper.getBoxOkHttpClient(songInfo.getAccountId
                            (), DbEntryService.getAccountAccessToken(songInfo.getAccountId()));
                    try {
                        DownloadItem di = boxOkHttp.downloadFile(songInfo.getId());
                        InputStream is = di.getInputStream();
                        Long lenght = di.getContentLength();
                        BufferedInputStream bis = new BufferedInputStream(is);
                        Log.e(TAG, songInfo.getTitle() + " download started...");
                        byte[] byteChunk = new byte[lenght.intValue()];
                        int n;
                        Long total = 0l;
                        while ((n = bis.read(byteChunk)) > 0) {
                            mBuilder.setProgress(lenght.intValue(), total.intValue(), false);
                            mNotifyManager.notify(NOT_ID, mBuilder.build());
                            fos.write(byteChunk, 0, n);
                        }
                        if (is != null) {
                            is.close();
                        }
                        isDownloaded = true;
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage() + "");
                        try {
                            UnauthorizedException ue = (UnauthorizedException) e;
                            RefreshTokenTask task = new RefreshTokenTask(songInfo
                                    .getAccountId(), boxOkHttp, null);
                            task.run();
                            while (!task.isFinished()) {

                            }
                            DownloadItem di = boxOkHttp.downloadFile(songInfo.getId());
                            InputStream is = di.getInputStream();
                            BufferedInputStream bis = new BufferedInputStream(is);
                            Log.e(TAG, songInfo.getTitle() + " download started...");
                            Long lenght = di.getContentLength();
                            byte[] byteChunk = new byte[lenght.intValue()];
                            int n;
                            Long total = 0l;
                            while ((n = bis.read(byteChunk)) > 0) {
                                mBuilder.setProgress(lenght.intValue(), total.intValue(),
                                        false);
                                mNotifyManager.notify(NOT_ID, mBuilder.build());
                                fos.write(byteChunk, 0, n);
                            }
                            if (is != null) {
                                is.close();
                            }
                            isDownloaded = true;
                        } catch (Exception e1) {
                            Log.e(TAG, e1.getMessage() + "");
                        }
                    }
                } else {
                    URL url = new URL(CmpDeviceService.getDownloadUrl(songInfo));
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    int lenght = connection.getContentLength();
                    InputStream is = new BufferedInputStream(connection.getInputStream());
                    int n;
                    Long total = 0l;
                    int count;
                    byte[] byteChunk;
                    if (lenght > 0) {
                        byteChunk = new byte[lenght];
                    } else {
                        byteChunk = new byte[10240];
                    }
                    while ((count = is.read(byteChunk)) > 0) {
                        fos.write(byteChunk, 0, count);
                        total += count;
                        if (lenght > 0) {
                            mBuilder.setProgress(lenght, total.intValue(), false);
                        } else {
                            mBuilder.setProgress(lenght, total.intValue(), true);
                        }
                        mNotifyManager.notify(NOT_ID, mBuilder.build());
                    }
                    if (is != null) {
                        is.close();
                    }
                    isDownloaded = true;
                }
                if (isDownloaded) {
                    Log.e(TAG, songInfo.getTitle() + " downloaded successfully...");
                    DbEntryService.updateAudioOfflineStatus(songInfo.getId(), AudioStatus
                            .OFFLINE
                            .getCode());
                    DbEntryService.updateDownloadAudioOfflineStatus(dai.getId(), ScanStatus
                            .FINISHED
                            .getCode());
                    NotificationHelper.showSimpleNotification(context, NOT_ID, R.drawable
                                    .ic_cloud_download_white_48dp,
                            context.getString(R.string.download_finish_msg), songInfo
                                    .getTitle());
                } else {
                    /*NotificationHelper.showSimpleNotification(context, 1, R.drawable
                                    .ic_download_dark,
                            context.getString(R.string.download_failed_msg), songInfo.getTitle());*/
                }
            } else {
                Toast.makeText(context, context.getString(R.string.spotify_download_error),
                        Toast
                                .LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, songInfo.getTitle() + " cannot be downloaded. Exception: " + e.getMessage());
        }
    }

    private void moveFromCache() {
    }

    private boolean checkOptions() {
        if (CmpDeviceService.getPreferencesService().isMobileDataAllowed()) {
            return true;
        } else {
            return ConnectivityHelper.chkStatus(context) == ConnectionType.WIFI;
        }
    }
}
