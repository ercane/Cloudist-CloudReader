package mree.cloud.music.player.app.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.act.fragment.AudioFragment;
import mree.cloud.music.player.app.bill.google.IabHelper;
import mree.cloud.music.player.app.bill.google.IabResult;
import mree.cloud.music.player.app.cach.ProxyServer;
import mree.cloud.music.player.app.database.Database;
import mree.cloud.music.player.app.database.DbConstants;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.database.DbSpecialOperations;
import mree.cloud.music.player.app.database.DbTableService;
import mree.cloud.music.player.app.scan.IScan;
import mree.cloud.music.player.app.scan.impl.BoxScan;
import mree.cloud.music.player.app.scan.impl.DropboxScan;
import mree.cloud.music.player.app.scan.impl.GoogleScan;
import mree.cloud.music.player.app.scan.impl.LocalScan;
import mree.cloud.music.player.app.scan.impl.OnedriveScan;
import mree.cloud.music.player.app.scan.impl.SpotifyScan;
import mree.cloud.music.player.app.scan.impl.YandexScan;
import mree.cloud.music.player.app.shared.SharedPreferencesKeys;
import mree.cloud.music.player.app.shared.SharedPreferencesService;
import mree.cloud.music.player.app.tasks.DownloadAudioTask;
import mree.cloud.music.player.app.utils.Constants;
import mree.cloud.music.player.app.utils.FileUtils;
import mree.cloud.music.player.app.utils.RestHelper;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.model.SourceInfo;
import mree.cloud.music.player.common.model.box.SharedLink;
import mree.cloud.music.player.common.model.dropbox.DownloadInfo;
import mree.cloud.music.player.common.model.onedrive.Item;
import mree.cloud.music.player.common.model.spotify.Track;
import mree.cloud.music.player.common.ref.ScanStatus;
import mree.cloud.music.player.common.ref.audio.AudioStatus;
import mree.cloud.music.player.rest.ok.BoxOkHttp;
import mree.cloud.music.player.rest.ok.DropboxOkHttp;
import mree.cloud.music.player.rest.ok.GoogleOkHttp;
import mree.cloud.music.player.rest.ok.OnedriveOkHttp;
import mree.cloud.music.player.rest.ok.YandexOkHttp;

import static mree.cloud.music.player.app.cach.ProxyServer.CACHE_ROOT;
import static mree.cloud.music.player.app.cach.ProxyServer.EXTENSION;

/**
 * Created by mree on 24.01.2016.
 */
public class CmpDeviceService extends Service{
    private static final String TAG = CmpDeviceService.class.getSimpleName();
    public static boolean running;
    private static ThreadPoolExecutor downloadExecutor;
    private static Database db;
    private static SharedPreferencesService preferencesService;
    private static Context context;
    private static InterstitialAd interstitialAd;
    private static RewardedVideoAd rewardAd;
    private static ProxyServer selfProxy;

    public static SharedPreferencesService getPreferencesService(){
        return preferencesService;
    }

    public static void setPreferencesService(SharedPreferencesService preferencesService){
        if (CmpDeviceService.preferencesService == null) {
            CmpDeviceService.preferencesService = preferencesService;
        }
    }


    public static String getPlayUrl(SongInfo si) throws Exception{
        String downloadUrl = null;
        File OFFLINE_ROOT = FileUtils.OFFLINE_ROOT;
        File offline_file = new File(OFFLINE_ROOT, ProxyServer.ENCODE_ID(si.getId()) + Constants
                .OFF_EXT);
        File cache_file = new File(CACHE_ROOT, ProxyServer.ENCODE_ID(si.getId()) + EXTENSION);
        if (si.getStatus() == AudioStatus.OFFLINE && offline_file.exists()) {
            return FileUtils.getOfflineFile(si.getId()).getAbsolutePath();
        } else if (getSelfProxy().isCached(si.getId()) && cache_file.exists()) {
            return getSelfProxy().getCacheFilePath(si.getId());
        } else {
            switch (si.getSourceType()) {
                case LOCAL:
                    return si.getDownloadUrl();
                case ONEDRIVE:
                    OnedriveOkHttp onedriveOkHttp = RestHelper.getOnedriveOkHttp(si.getAccountId(),
                            DbEntryService.getAccountAccessToken(si.getAccountId()));
                    Item item = OnedriveScan.getItemById(si.getAccountId(), si.getId(),
                            onedriveOkHttp);
                    downloadUrl = getSelfProxy().getDownloadUrl(item
                            .Content_downloadUrl, si);
                    return downloadUrl;

                case DROPBOX:
                    DropboxOkHttp dropbox = RestHelper.getDropboxOkHttp(si.getAccountId(),
                            DbEntryService.getAccountAccessToken(si.getAccountId()));
                    DownloadInfo link = dropbox.getTempLink(si.getPath());
                    return getSelfProxy().getDownloadUrl(link.link, si);

                case GOOGLE_DRIVE:
                    GoogleOkHttp goh = RestHelper.getGoogleOkHttp(si.getAccountId(),
                            DbEntryService.getAccountAccessToken(si.getAccountId()));
                    downloadUrl = getSelfProxy().getDownloadUrl(GoogleScan.getUrl(si
                            .getAccountId(), si.getId(), goh), si);
                    return downloadUrl;

                case YANDEX_DISK:
                    YandexOkHttp yrc = RestHelper.getYandexOkHttp(si.getAccountId(),
                            DbEntryService.getAccountAccessToken(si.getAccountId()));
                    downloadUrl = getSelfProxy().getDownloadUrl(yrc.getDownloadLink(si.getPath())
                            , si);
                    return downloadUrl;

                case SPOTIFY:
                    Track track = SpotifyScan.getTrack(si.getAccountId(), si.getId());
                    if (track != null) {
                        return track.getUri();
                    } else {
                        return null;
                    }
                case BOX:
                    BoxOkHttp boh = RestHelper.getBoxOkHttpClient(si.getAccountId(),
                            DbEntryService.getAccountAccessToken(si.getAccountId()));
                    SharedLink shared_link = boh.getFileWithSharedLink(si.getId()).getShared_link();
                    downloadUrl = getSelfProxy().getDownloadUrl(shared_link.getDownload_url(), si);
                    return downloadUrl;


                default:
                    return null;
            }
        }
    }

    public static String getDownloadUrl(SongInfo si) throws Exception{
        String downloadUrl = null;
        switch (si.getSourceType()) {
            case ONEDRIVE:
                OnedriveOkHttp onedriveOkHttp = RestHelper.getOnedriveOkHttp(si.getAccountId(),
                        DbEntryService.getAccountAccessToken(si.getAccountId()));
                Item item = OnedriveScan.getItemById(si.getAccountId(), si.getId(), onedriveOkHttp);
                return item.Content_downloadUrl;
            case DROPBOX:
                DropboxOkHttp dropbox = RestHelper.getDropboxOkHttp(si.getAccountId(),
                        DbEntryService.getAccountAccessToken(si.getAccountId()));
                DownloadInfo link = dropbox.getTempLink(si.getPath());
                return link.link;
            case GOOGLE_DRIVE:
                GoogleOkHttp goh = RestHelper.getGoogleOkHttp(si.getAccountId(),
                        DbEntryService.getAccountAccessToken(si.getAccountId()));
                return GoogleScan.getUrl(si.getAccountId(), si.getId(), goh);
            case YANDEX_DISK:
                YandexOkHttp yandexOkHttp = RestHelper.getYandexOkHttp(si.getAccountId(),
                        DbEntryService.getAccountAccessToken(si.getAccountId()));
                return yandexOkHttp.getDownloadLink(si.getPath());
            default:
                return null;
        }
    }

    public static List<String> getVisitedFolders(String accountInfoId){
        List<String> visitedFolders;
        String json = DbEntryService.getAccountScannedFolders(accountInfoId);
        visitedFolders = new Gson().fromJson(json, List.class);
        if (visitedFolders == null) {
            return new ArrayList<String>();
        } else {
            return visitedFolders;
        }
    }

    public static IScan getScan(SourceInfo acc){
        switch (acc.getType()) {
            case LOCAL:
                return new LocalScan(context, acc);
            case ONEDRIVE:
                return new OnedriveScan(context, acc);
            case DROPBOX:
                return new DropboxScan(context, acc);
            case GOOGLE_DRIVE:
                return new GoogleScan(context, acc);
            case SPOTIFY:
                return new SpotifyScan(context, acc);
            case YANDEX_DISK:
                return new YandexScan(context, acc);
            case BOX:
                return new BoxScan(context, acc);
            default:
                return null;
        }
    }

    public static void setVisitedFolders(String id, List<String> folders){
        String json = new Gson().toJson(folders);
        DbEntryService.updateAccountScannedFolders(id, json);
    }

    public static void increaseScannedSong(String id, int folderSongCount){
        int scan = DbEntryService.getAccountScannedSongs(id);
        scan += folderSongCount;
        DbEntryService.updateAccountScannedSongs(id, scan);
    }

    public static Context getContext(){
        return context;
    }


    public static RewardedVideoAd getRewardAd(){
        if (rewardAd == null) {
            rewardAd = MobileAds.getRewardedVideoAdInstance(context);
        }
        return rewardAd;
    }

    public static ProxyServer getSelfProxy(){
        if (selfProxy == null) {
            selfProxy = new ProxyServer(context);
        }
        return selfProxy;
    }

    public static Database getDb(){
        if (db == null) {
            db = new Database(context);
        }

        return db;
    }

    public static ThreadPoolExecutor getDownloadExecutor(){
        if (downloadExecutor == null) {
            int KEEP_ALIVE_TIME = 1;
            TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
            int corePoolSize = 1;
            int maximumPoolSize = 5;
            LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
            downloadExecutor = new ThreadPoolExecutor(
                    corePoolSize,       // Initial pool size
                    maximumPoolSize,       // Max pool size
                    KEEP_ALIVE_TIME,
                    KEEP_ALIVE_TIME_UNIT,
                    workQueue);
        }
        return downloadExecutor;
    }

    public static File getCacheRoot(){
        return context.getCacheDir();
    }

    public static File getDataRoot(){
        return context.getFilesDir();
    }

    @Override
    public void onCreate(){
        context = getApplicationContext();
        running = true;
        getDb();
        createTables();

        if (preferencesService == null) {
            preferencesService = new SharedPreferencesService(getSharedPreferences
                    (SharedPreferencesKeys.ROOT, MODE_PRIVATE));
        }

        if (interstitialAd == null) {
            interstitialAd = new InterstitialAd(getContext());
            interstitialAd.setAdUnitId(getContext().getString(R.string.interstital_limitless_id));
        }
        clearEmptyPlaylists();
        checkDownloadList();
        checkAdState();
    }

    private void checkAdState(){
        getPreferencesService().setAdState(false);
        try {
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference dbReference = db.getReference("IAB");
            dbReference.addValueEventListener(new ValueEventListener(){
                IabHelper iabHelper;
                String ITEM_SKU = "remove_ads";

                @Override
                public void onDataChange(DataSnapshot dataSnapshot){
                    String key = dataSnapshot.getValue(String.class);
                    key.replace(" ", "");
                    iabHelper = new IabHelper(CmpDeviceService.this, key);
                    iabHelper.enableDebugLogging(true, TAG);
                    iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener(){
                        public void onIabSetupFinished(IabResult result){
                            if (!result.isSuccess()) {
                                Log.e(TAG, "In-app Billing setup failed: " + result);
                            } else {
                                Log.e(TAG, "In-app Billing is set up OK");
                                boolean isOwned = iabHelper.checkAlreadyOwned(ITEM_SKU, "inapp", null);
                                Log.e(TAG, "IsOwned: " + isOwned);
                                CmpDeviceService.getPreferencesService().setAdState(isOwned);
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError error){
                    // Failed to read value
                    Log.e(TAG, "Failed to read value.", error.toException());
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void checkDownloadList(){
        ArrayList<HashMap<String, String>> allDownloads = DbEntryService
                .getAllDownloadAudiosByState(ScanStatus.STARTED.getCode());
        for (HashMap<String, String> item : allDownloads) {
            String songId = item.get(DbConstants.DOWNLOAD_AUDIOS_AUDIO_ID);
            HashMap<String, String> audioById = DbEntryService.getAudioById(songId);
            if (!TextUtils.isEmpty(audioById.get(DbConstants.AUDIO_ID))) {
                SongInfo si = AudioFragment.getSongInfo(audioById);
                switch (si.getStatus()) {
                    case ONLINE:
                        DbEntryService.removeAudioFromDownloadAudios(songId);
                        break;
                    case OFFLINE:
                        if (!FileUtils.getOfflineFile(si.getId()).exists()) {
                            DownloadAudioTask task = new DownloadAudioTask(context, si);
                            CmpDeviceService.getDownloadExecutor().submit(task);
                        } else {
                            DbEntryService.updateDownloadAudioOfflineStatus(si.getId(), ScanStatus
                                    .FINISHED.getCode());
                        }
                        break;
                    case CACHED:
                        DbEntryService.removeAudioFromDownloadAudios(songId);
                        break;
                }
            } else {
                DbEntryService.removeAudioFromDownloadAudios(songId);
            }
        }
    }

    private void clearEmptyPlaylists(){
        DbEntryService.clearEmptyPlaylists();
    }

    private void createTables(){
        DbTableService.createAccountTable();
        DbTableService.createAudioTable();
        DbTableService.createPlaylistTable();
        DbTableService.createPlaylistAudioTable();
        DbTableService.createDownloadAudiosTable();
        checkUpdateChanges();
        checkStartedScans();
    }

    private void checkStartedScans(){
        DbEntryService.updateScanToFailed();
    }

    private void checkUpdateChanges(){
        try {
            /*PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int versionCode = info.versionCode;
            if (versionCode > 10 && !CmpDeviceService.getPreferencesService().isUpdateApplied()) {
                boolean b1 = DbSpecialOperations.addColumn(DbConstants.AUDIO_TABLE_NAME,
                        DbConstants.AUDIO_STATUS, "NUMERIC", AudioStatus.ONLINE.getCode()
                                .toString());

                boolean b2 = DbSpecialOperations.addColumn(DbConstants
                                .PLAYLIST_AUDIO_TABLE_NAME, DbConstants.PA_CREATED_DATE, "NUMERIC",
                        System.currentTimeMillis() + "");

                boolean b5 = DbSpecialOperations.addColumn(DbConstants.PLAYLIST_TABLE_NAME,
                        DbConstants.PLAYLIST_OFFLINE_STATUS, "NUMERIC", "0");

                boolean b3 = DbSpecialOperations.recreateTableWithData(DbConstants
                        .PLAYLIST_AUDIO_TABLE_NAME);

                boolean b4 = DbSpecialOperations.recreateTableWithData(DbConstants
                        .PLAYLIST_TABLE_NAME);

                if (b1 && b2 && b3 && b4 && b5) {
                    getPreferencesService().setUpdateApplied(true);
                }
            }*/
            if (!DbSpecialOperations.isFieldExist(DbConstants.PLAYLIST_TABLE_NAME, DbConstants
                    .PLAYLIST_OFFLINE_STATUS)) {
                DbSpecialOperations.addColumn(DbConstants.PLAYLIST_TABLE_NAME, DbConstants
                        .PLAYLIST_OFFLINE_STATUS, "NUMERIC", "0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
