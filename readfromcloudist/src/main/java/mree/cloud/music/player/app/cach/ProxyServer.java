package mree.cloud.music.player.app.cach;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Locale;

import mree.cloud.music.player.app.act.fragment.AudioFragment;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.services.CmpDeviceService;
import mree.cloud.music.player.app.tasks.RefreshTokenTask;
import mree.cloud.music.player.app.utils.ConnectivityHelper;
import mree.cloud.music.player.app.utils.Constants;
import mree.cloud.music.player.app.utils.FileUtils;
import mree.cloud.music.player.app.utils.RestHelper;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.model.box.DownloadItem;
import mree.cloud.music.player.common.ref.ConnectionType;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.audio.AudioStatus;
import mree.cloud.music.player.rest.ok.BoxOkHttp;


/**
 * Created by eercan on 27.12.2016.
 */

public class ProxyServer {

    public static final String PROXY_HOST = "127.0.0.1";
    public static final String EXTENSION = ".down";
    public static final int PROXY_PORT = 2525;
    private static final String TAG = ProxyServer.class.getSimpleName();
    public static File CACHE_ROOT;
    private static boolean isRunning;
    private static Runnable serverThread;
    private static ServerSocket ss;
    private Context context;
    private Socket client;

    public ProxyServer(Context context) {
        this.context = context;
        initServer();
    }

    private static Runnable getServerThread() {
        if (serverThread == null) {
            serverThread = new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Socket accept = ss.accept();
                            ClientThread clientThread = new ClientThread(accept);
                            //AudioFragment.getThumbThreadPoolExecutor().submit(clientThread);
                            clientThread.run();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        }

        return serverThread;
    }

    public static String ENCODE_ID(String id) {
      /*  final Pattern PATTERN = Pattern.compile("[^A-Za-z0-9_\\-]");
        StringBuffer sb = new StringBuffer();

        // Apply the regex.
        Matcher m = PATTERN.matcher(id);

        while (m.find()) {

            // Convert matched character to percent-encoded.
            String replacement = "%" + Integer.toHexString(m.group().charAt(0)).toUpperCase();

            m.appendReplacement(sb, replacement);
        }
        m.appendTail(sb);

        String encoded = sb.toString();

        return encoded;*/
        //return Base64.encodeToString(id.getBytes(), Base64.DEFAULT);
        return id;
    }

    public static String DECODE_ID(String filename) throws UnsupportedEncodingException {
        String name = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
        return URLDecoder.decode(name, "UTF-8");
    }

    public boolean initServer() {
        try {
            ss = new ServerSocket(PROXY_PORT);
            File root = context.getCacheDir();
            CACHE_ROOT = new File(root, "/file_cache/");
            CACHE_ROOT.mkdirs();
            return CACHE_ROOT.exists();
        } catch (Exception e) {
            return false;
        }
    }

    public void startServer() {
        Runnable thread = null;
        if (!isRunning) {
            thread = getServerThread();
        }

        if (thread != null) {
            AudioFragment.getThumbThreadPoolExecutor().submit(thread);
        }
        isRunning = true;
    }

    public String getDownloadUrl(String url, SongInfo songInfo) {
        File offline_file = new File(FileUtils.OFFLINE_ROOT, ENCODE_ID(songInfo.getId()) +
                Constants.OFF_EXT);
        File file = new File(CACHE_ROOT, ENCODE_ID(songInfo.getId()) + EXTENSION);

        switch (songInfo.getStatus()) {
            case CACHED:
                if (file.exists()) {
                    return file.getAbsolutePath();
                } else {
                    startCache(url, songInfo, file);
                    return url;
                }
            case OFFLINE:
                if (offline_file.exists()) {
                    return offline_file.getAbsolutePath();
                } else {
                    startCache(url, songInfo, file);
                    return url;
                }
            case ONLINE:
            default:
                startCache(url, songInfo, file);
                return url;
        }

    }

    private void startCache(final String urlAddress, final SongInfo songInfo, final File f) {
        final Thread download = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, f.getName() + " will be download...");
                    FileOutputStream fos = new FileOutputStream(f);
                    URL url = new URL(urlAddress);
                    if (SourceType.BOX != songInfo.getSourceType()) {
                        InputStream is = url.openStream();
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        int contentLength = connection.getContentLength();

                        byte[] byteChunk; // Or whatever size you
                        if (contentLength > 0) {
                            byteChunk = new byte[contentLength];
                        } else {
                            byteChunk = new byte[10240];
                        }
                        // want
                        // to read
                        // in at a time.
                        int n;

                        while ((n = is.read(byteChunk)) > 0) {
                            fos.write(byteChunk, 0, n);
                        }
                        if (is != null) {
                            is.close();
                            Log.d(TAG, f.getName() + " downloaded successfully...");
                        }
                    } else {
                        BoxOkHttp boxOkHttp = RestHelper.getBoxOkHttpClient(songInfo
                                .getAccountId(), DbEntryService.getAccountAccessToken
                                (songInfo.getAccountId()));
                        try {
                            DownloadItem downloadItem = boxOkHttp.downloadFile(songInfo.getId());
                            InputStream is = downloadItem.getInputStream();
                            byte[] byteChunk = new byte[(int) downloadItem.getContentLength()];
                            int n;
                            while ((n = is.read(byteChunk)) > 0) {
                                fos.write(byteChunk, 0, n);
                            }
                            if (is != null) {
                                is.close();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage() + "");
                            try {
                                RefreshTokenTask task = new RefreshTokenTask(songInfo
                                        .getAccountId(), boxOkHttp, null);
                                task.run();
                                while (!task.isFinished()) {

                                }
                                DownloadItem downloadItem = boxOkHttp.downloadFile(songInfo.getId
                                        ());
                                InputStream is = downloadItem.getInputStream();
                                byte[] byteChunk = new byte[is.available() + 1];
                                int n;
                                while ((n = is.read(byteChunk)) > 0) {
                                    fos.write(byteChunk, 0, n);
                                }
                                if (is != null) {
                                    is.close();
                                }
                            } catch (Exception e1) {
                                Log.e(TAG, e1.getMessage() + "");
                            }
                        }
                    }
                    DbEntryService.updateAudioOfflineStatus(songInfo.getId(), AudioStatus.CACHED
                            .getCode());
                } catch (Exception e) {
                    Log.d(TAG, f.getName() + " cannot be downloaded. Exception: " + e.getMessage());
                }
            }
        });

        if (checkOptions()) {
            if (f.exists()) {
                f.delete();
            }

            AudioFragment.getThumbThreadPoolExecutor().submit(download);
        }
    }

    private boolean checkOptions() {
        if (CmpDeviceService.getPreferencesService().isMobileDataAllowed()) {
            return true;
        } else {
            return ConnectivityHelper.chkStatus(context) == ConnectionType.WIFI;
        }
    }

    private String appendToProxyUrl(String id) {
        return String.format(Locale.US, "http://%s:%d/id=%s=", PROXY_HOST, PROXY_PORT,
                ENCODE_ID(id));
    }

    public boolean isCached(String id) {
        File file = new File(CACHE_ROOT, id + EXTENSION);
        return file.exists();
    }

    public String getCacheFilePath(String id) {
        File file = new File(CACHE_ROOT, id + EXTENSION);
        return file.getAbsolutePath();
    }


}
