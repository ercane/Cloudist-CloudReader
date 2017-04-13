package mree.cloud.music.player.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import android.util.Log;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mree.cloud.music.player.app.utils.StringUtils;
import mree.cloud.music.player.common.model.DownloadAudioInfo;
import mree.cloud.music.player.common.model.PlaylistInfo;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.model.SourceInfo;
import mree.cloud.music.player.common.ref.ScanStatus;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.audio.AudioStatus;
import mree.cloud.music.player.common.ref.audio.PlaylistType;
import mree.cloud.music.player.common.ref.auth.SourceState;

/**
 * Created by eercan on 18.09.2015.
 */
public class DbEntryService {

    public static String TAG = "DbEntryService";


    public static SQLiteDatabase getDB() {
        SQLiteDatabase database = Database.getDatabase();
        if (database == null || !database.isOpen()) {
            database = Database.getSqoh().getWritableDatabase();
        }
        return database;
    }

    /*Save operations
        *******************************
        */
    public static Long saveAccount(SourceInfo accInfo) {

        try {
            ContentValues values = new ContentValues();
            //values.put(DbConstants.ACCOUNT_ID, accInfo.getId());
            values.put(DbConstants.ACCOUNT_USER_ID, accInfo.getUserId());
            values.put(DbConstants.ACCOUNT_NAME, accInfo.getName());
            values.put(DbConstants.ACCOUNT_STATE, accInfo.getState().getCode());
            values.put(DbConstants.ACCOUNT_TYPE, accInfo.getType().getCode());
            values.put(DbConstants.ACCOUNT_ACCESS_TOKEN, accInfo.getAccessToken());
            values.put(DbConstants.ACCOUNT_EXPIRED_IN, accInfo.getExpiredIn());
            values.put(DbConstants.ACCOUNT_SCANNED_SONG, accInfo.getScannedSong());
            values.put(DbConstants.ACCOUNT_SCAN_STATUS, ScanStatus.INITIAL.getCode());
            values.put(DbConstants.ACCOUNT_SCANNED_FOLDERS, accInfo.getScannedFolders());
            values.put(DbConstants.ACCOUNT_REFRESH_TOKEN, accInfo.getRefreshToken());
            long id = getDB().insert(DbConstants.ACCOUNT_TABLE_NAME, null, values);
            getDB().close(); //Database Bağlantısını kapattık*/
            Log.e(TAG, "Account saved to database. Id: " + accInfo.getId());
            return id;
        } catch (Exception e) {
            Log.e(TAG, "Account cannot be saved to database. Exception: " + e.getMessage());
            return null;
        }
    }

    public static void saveAudio(SongInfo songInfo) {

        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.AUDIO_ID, songInfo.getId());
            values.put(DbConstants.AUDIO_ACCOUNT_ID, songInfo.getAccountId());
            values.put(DbConstants.AUDIO_ALBUM, songInfo.getAlbum());
            values.put(DbConstants.AUDIO_ALBUM_ARTIST, songInfo.getAlbumArtist());
            values.put(DbConstants.AUDIO_ARTIST, songInfo.getArtist());
            values.put(DbConstants.AUDIO_BITRATE, songInfo.getBitrate());
            values.put(DbConstants.AUDIO_COMPOSERS, songInfo.getComposers());
            values.put(DbConstants.AUDIO_COPYRIGHT, songInfo.getCopyright());
            values.put(DbConstants.AUDIO_DISC, songInfo.getDisc());
            values.put(DbConstants.AUDIO_DISC_COUNT, songInfo.getDiscCount());
            values.put(DbConstants.AUDIO_DOWNLOAD_URL, songInfo.getDownloadUrl());
            values.put(DbConstants.AUDIO_DURATION, songInfo.getDuration());
            values.put(DbConstants.AUDIO_GENRE, songInfo.getGenre());
            values.put(DbConstants.AUDIO_HAS_DRM, songInfo.getHasDrm());
            values.put(DbConstants.AUDIO_IS_VARIABLE_BITRATE, songInfo.getIsVariableBitrate());
            values.put(DbConstants.AUDIO_PATH, songInfo.getPath());
            values.put(DbConstants.AUDIO_SHARE_URL, songInfo.getShareUrl());
            values.put(DbConstants.AUDIO_SOURCE_TYPE, songInfo.getSourceType().getCode());
            values.put(DbConstants.AUDIO_STATUS, songInfo.getStatus().getCode());
            values.put(DbConstants.AUDIO_TITLE, songInfo.getTitle());
            values.put(DbConstants.AUDIO_THUMBNAIL, songInfo.getThumbnail());
            values.put(DbConstants.AUDIO_TRACK, songInfo.getTrack());
            values.put(DbConstants.AUDIO_TRACK_COUNT, songInfo.getTrackCount());
            values.put(DbConstants.AUDIO_YEAR, songInfo.getYear());


            getDB().insert(DbConstants.AUDIO_TABLE_NAME, null, values);
            getDB().close(); //Database Bağlantısını kapattık*/
            Log.e(TAG, "Audio saved to database. Id: " + songInfo.getId());
        } catch (Exception e) {
            Log.e(TAG, "Audio cannot be saved to database. Exception: " + e.getMessage());
        }
    }

    public static long saveDownloadAudios(DownloadAudioInfo dai) {

        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.DOWNLOAD_AUDIOS_AUDIO_ID, dai.getDownloadAudioId());
            values.put(DbConstants.DOWNLOAD_AUDIOS_STATE, dai.getStatus().getCode());
            values.put(DbConstants.DOWNLOAD_AUDIOS_CREATED_DATE, dai.getCreatedDate().getTime());

            long id = getDB().insert(DbConstants.DOWNLOAD_AUDIOS_TABLE_NAME, null, values);
            getDB().close(); //Database Bağlantısını kapattık*/
            Log.e(TAG, "Download queued. Id: " + dai.getId());
            return id;
        } catch (Exception e) {
            Log.e(TAG, "Download queue cannot be saved to database. Exception: " + e.getMessage());
            return 0l;
        }
    }


    public static Long savePlaylist(PlaylistInfo pi) {

        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.PLAYLIST_NAME, pi.getName());
            values.put(DbConstants.PLAYLIST_ID, pi.getId());
            values.put(DbConstants.PLAYLIST_OFFLINE_STATUS, 0);
            if (pi.getType() != null)
                values.put(DbConstants.PLAYLIST_TYPE, pi.getType().getCode());
            values.put(DbConstants.PLAYLIST_CREATION_DATE, pi.getCreatedDate().getTime());
            values.put(DbConstants.PLAYLIST_UPDATED_DATE, pi.getCreatedDate().getTime());
            values.put(DbConstants.PLAYLIST_AUDIO_COUNT, pi.getSongs().size());
            long id = getDB().insert(DbConstants.PLAYLIST_TABLE_NAME, null, values);

            for (SongInfo si : pi.getSongs()) {
                ContentValues values2 = new ContentValues();
                values2.put(DbConstants.PA_PLAYLIST_ID, pi.getId());
                values2.put(DbConstants.PA_AUDIO_ID, si.getId());
                values2.put(DbConstants.PA_CREATED_DATE, System.currentTimeMillis());
                getDB().insert(DbConstants.PLAYLIST_AUDIO_TABLE_NAME, null, values2);
            }

            getDB().close(); //Database Bağlantısını kapattık*/
            Log.e(TAG, "Playlist saved to database. Id: " + pi.getName());
            Answers.getInstance().logCustom(new CustomEvent("Playlist Added")
                    .putCustomAttribute("Playlist Name", pi.getName())
                    .putCustomAttribute("Playlist Type", pi.getType() + ""));
            return id;
        } catch (Exception e) {
            Log.e(TAG, "Playlist cannot be saved to database. Exception: " + e.getMessage());
            return null;
        }
    }

    public static void savePlaylistAudios(long playlistId, List<SongInfo> list) {

        try {
            for (SongInfo si : list) {
                ContentValues values = new ContentValues();
                values.put(DbConstants.PA_PLAYLIST_ID, playlistId);
                values.put(DbConstants.PA_AUDIO_ID, si.getId());
                long id = getDB().insert(DbConstants.PLAYLIST_AUDIO_TABLE_NAME, null, values);
            }
            getDB().close(); //Database Bağlantısını kapattık*/
            Log.e(TAG, "Playlist audio saved to database. Id: " + playlistId);
        } catch (Exception e) {
            Log.e(TAG, "Playlist audio cannot be saved to database. Exception: " + e.getMessage());
        }
    }

    public static Long saveAudioToPlaylist(String playlistId, String songId) {
        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.PA_PLAYLIST_ID, playlistId);
            values.put(DbConstants.PA_AUDIO_ID, songId);
            values.put(DbConstants.PA_CREATED_DATE, System.currentTimeMillis());
            long id = getDB().insert(DbConstants.PLAYLIST_AUDIO_TABLE_NAME, null, values);
            getDB().close(); //Database Bağlantısını kapattık*/
            Log.e(TAG, "Playlist audio saved to database. Id: " + playlistId);
            return id;
        } catch (Exception e) {
            Log.e(TAG, "Playlist audio cannot be saved to database. Exception: " + e.getMessage());
            return null;
        }
    }

    /*
    Get operations
    **************************************************************
     */

    public static ArrayList<HashMap<String, String>> getAllAccounts() {

        try {
            //Bu methodda ise tablodaki tüm değerleri alıyoruz
            //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri
            // listeleyeceğiz
            //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap
            // arrayı demek.
            //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

            String selectQuery = "SELECT * FROM " + DbConstants.ACCOUNT_TABLE_NAME + " order by " +
                    DbConstants.ACCOUNT_ID + " asc";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String,
                    String>>();
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }

                    messageList.add(map);
                } while (cursor.moveToNext());
            }
            getDB().close();
            // return kitap liste
            return messageList;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            return new ArrayList<HashMap<String, String>>();
        }
    }

    public static ArrayList<HashMap<String, String>> getAllDownloadAudios() {

        try {
            //Bu methodda ise tablodaki tüm değerleri alıyoruz
            //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri
            // listeleyeceğiz
            //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap
            // arrayı demek.
            //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

            String selectQuery = "SELECT * FROM " + DbConstants.DOWNLOAD_AUDIOS_TABLE_NAME;
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String,
                    String>>();
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }

                    messageList.add(map);
                } while (cursor.moveToNext());
            }
            getDB().close();
            // return kitap liste
            return messageList;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            return new ArrayList<HashMap<String, String>>();
        }
    }

    public static ArrayList<HashMap<String, String>> getAllDownloadAudiosByState(int status) {

        try {
            //Bu methodda ise tablodaki tüm değerleri alıyoruz
            //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri
            // listeleyeceğiz
            //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap
            // arrayı demek.
            //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

            String selectQuery = "SELECT * FROM " + DbConstants.DOWNLOAD_AUDIOS_TABLE_NAME + " " +
                    "WHERE " + DbConstants.DOWNLOAD_AUDIOS_STATE + "=" + status;
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String,
                    String>>();
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }

                    messageList.add(map);
                } while (cursor.moveToNext());
            }
            getDB().close();
            // return kitap liste
            return messageList;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            return new ArrayList<HashMap<String, String>>();
        }
    }


    public static ArrayList<HashMap<String, String>> getAllAccountsByState(SourceState state) {

        try {
            //Bu methodda ise tablodaki tüm değerleri alıyoruz
            //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri
            // listeleyeceğiz
            //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap
            // arrayı demek.
            //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

            String selectQuery = "SELECT * FROM " + DbConstants.ACCOUNT_TABLE_NAME +
                    " where " + DbConstants.ACCOUNT_STATE + " = " + state.getCode();
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String,
                    String>>();
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }

                    messageList.add(map);
                } while (cursor.moveToNext());
            }
            getDB().close();
            // return kitap liste
            return messageList;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            return new ArrayList<HashMap<String, String>>();
        }
    }

    public static ArrayList<HashMap<String, String>> getAllAccountsByType(SourceType type) {

        try {
            //Bu methodda ise tablodaki tüm değerleri alıyoruz
            //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri
            // listeleyeceğiz
            //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap
            // arrayı demek.
            //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

            String selectQuery = "SELECT * FROM " + DbConstants.ACCOUNT_TABLE_NAME +
                    " where " + DbConstants.ACCOUNT_TYPE + " = " + type.getCode();
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String,
                    String>>();
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }

                    messageList.add(map);
                } while (cursor.moveToNext());
            }
            getDB().close();
            // return kitap liste
            return messageList;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            return new ArrayList<HashMap<String, String>>();
        }
    }

    public static String getAccountScannedFolders(String accountInfoId) {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            String selectQuery = "SELECT " + DbConstants.ACCOUNT_SCANNED_FOLDERS + " FROM " +
                    DbConstants.ACCOUNT_TABLE_NAME +
                    " where " + DbConstants.ACCOUNT_ID + " = '" + accountInfoId + "'";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                } while (cursor.moveToNext());
            }
            getDB().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return kitap liste
        return map.get(DbConstants.ACCOUNT_SCANNED_FOLDERS);
    }

    public static int getAccountScannedSongs(String id) {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            String selectQuery = "SELECT " + DbConstants.ACCOUNT_SCANNED_SONG + " FROM " +
                    DbConstants.ACCOUNT_TABLE_NAME +
                    " where " + DbConstants.ACCOUNT_ID + " = '" + id + "'";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                } while (cursor.moveToNext());
            }
            getDB().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return kitap liste
        String scannedNumber = map.get(DbConstants.ACCOUNT_SCANNED_SONG);
        if (scannedNumber == null) {
            return 0;
        } else {
            return Integer.parseInt(scannedNumber);
        }

    }

    public static int getAccountSongsCount(String id) {
        HashMap<String, String> map = new HashMap<String, String>();
        int count = 0;
        try {
            String selectQuery = "SELECT COUNT(*) FROM " +
                    DbConstants.AUDIO_TABLE_NAME +
                    " where " + DbConstants.AUDIO_ACCOUNT_ID + " = '" + id + "'";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        count = cursor.getInt(i);
                    }
                } while (cursor.moveToNext());
            }
            getDB().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public static ScanStatus getAccountScanStatus(String id) {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            String selectQuery = "SELECT " + DbConstants.ACCOUNT_SCAN_STATUS + " FROM " +
                    DbConstants.ACCOUNT_TABLE_NAME +
                    " where " + DbConstants.ACCOUNT_ID + " = '" + id + "'";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                } while (cursor.moveToNext());
            }
            getDB().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return kitap liste
        String scannedNumber = map.get(DbConstants.ACCOUNT_SCAN_STATUS);
        if (scannedNumber == null) {
            return ScanStatus.FAILED;
        } else {
            return ScanStatus.get(Integer.parseInt(scannedNumber));
        }

    }

    public static SourceInfo getAccountInfo(String id) {
        HashMap<String, String> map = new HashMap<String, String>();
        SourceInfo accInfo = new SourceInfo();
        accInfo.setId(id);
        try {
            String selectQuery = "SELECT * FROM " +
                    DbConstants.ACCOUNT_TABLE_NAME +
                    " where " + DbConstants.ACCOUNT_ID + " = '" + id + "'";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        String value = cursor.getString(i);
                        if (value != null) {
                            switch (cursor.getColumnName(i)) {
                                case DbConstants.ACCOUNT_ACCESS_TOKEN:
                                    accInfo.setAccessToken(value);
                                    break;
                                case DbConstants.ACCOUNT_TYPE:
                                    accInfo.setType(SourceType.get(Integer.parseInt(value)));
                                    break;
                                case DbConstants.ACCOUNT_SCAN_STATUS:
                                    accInfo.setScanStatus(ScanStatus.get(Integer.parseInt(value)));
                                    break;
                                case DbConstants.ACCOUNT_EXPIRED_IN:
                                    accInfo.setExpiredIn(Long.parseLong(value));
                                    break;
                                case DbConstants.ACCOUNT_STATE:
                                    accInfo.setState(SourceState.get(Integer.parseInt(value)));
                                    break;
                                case DbConstants.ACCOUNT_NAME:
                                    accInfo.setName(value);
                                    break;
                                case DbConstants.ACCOUNT_REFRESH_TOKEN:
                                    accInfo.setRefreshToken(value);
                                    break;
                                case DbConstants.ACCOUNT_SCANNED_FOLDERS:
                                    accInfo.setScannedFolders(value);
                                    break;
                                case DbConstants.ACCOUNT_SCANNED_SONG:
                                    accInfo.setScannedSong(Long.parseLong(value));
                                    break;
                                case DbConstants.ACCOUNT_USER_ID:
                                    accInfo.setUserId(value);
                                    break;
                            }
                        }
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                } while (cursor.moveToNext());
            }
            getDB().close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accInfo;
    }

    public static SourceType getAccountType(String id) {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            String selectQuery = "SELECT " + DbConstants.ACCOUNT_TYPE + " FROM " +
                    DbConstants.ACCOUNT_TABLE_NAME +
                    " where " + DbConstants.ACCOUNT_ID + " = '" + id + "'";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                } while (cursor.moveToNext());
            }
            getDB().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return kitap liste
        String typeCode = map.get(DbConstants.ACCOUNT_TYPE);
        if (typeCode == null) {
            return null;
        } else {
            return SourceType.get(Integer.parseInt(typeCode));
        }

    }

    public static String getAccountAccessToken(String accId) {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            String selectQuery = "SELECT " + DbConstants.ACCOUNT_ACCESS_TOKEN + " FROM " +
                    DbConstants.ACCOUNT_TABLE_NAME +
                    " where " + DbConstants.ACCOUNT_ID + " = '" + accId + "'";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                } while (cursor.moveToNext());
            }
            getDB().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return kitap liste
        return map.get(DbConstants.ACCOUNT_ACCESS_TOKEN);
    }

    public static Long getAccountExpiredIn(String id) {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            String selectQuery = "SELECT " + DbConstants.ACCOUNT_EXPIRED_IN + " FROM " +
                    DbConstants.ACCOUNT_TABLE_NAME +
                    " where " + DbConstants.ACCOUNT_ID + " = '" + id + "'";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                } while (cursor.moveToNext());
            }
            getDB().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return kitap liste
        String exp = map.get(DbConstants.ACCOUNT_EXPIRED_IN);
        if (exp == null) {
            return 0l;
        } else {
            return Long.parseLong(exp);
        }

    }

    public static ArrayList<HashMap<String, String>> getAllAudios() {

        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap
        // arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).
        ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String, String>>();


        try {
            String selectQuery = "SELECT * FROM " + DbConstants.AUDIO_TABLE_NAME + " order by " +
                    DbConstants.AUDIO_TITLE + " asc";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        map.put(cursor.getColumnName(i), cursor.getString(i));

                    }

                    messageList.add(map);
                } while (cursor.moveToNext());
            }
            getDB().close();
            // return kitap liste
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageList;
    }

    public static ArrayList<HashMap<String, String>> getAllPlaylists() {

        ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String, String>>();
        try {
            String selectQuery = "SELECT * FROM " + DbConstants.PLAYLIST_TABLE_NAME + " order by " +
                    DbConstants.PLAYLIST_CREATION_DATE + " asc";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                    messageList.add(map);
                } while (cursor.moveToNext());
            }
            getDB().close();
            // return kitap liste
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageList;

    }

    public static ArrayList<HashMap<String, String>> getAllPlaylistsByType(int typeCode) {

        ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String, String>>();
        try {
            String selectQuery = "SELECT * FROM " + DbConstants.PLAYLIST_TABLE_NAME +
                    " WHERE " + DbConstants.PLAYLIST_TYPE + " = " + typeCode +
                    " order by " + DbConstants.PLAYLIST_CREATION_DATE + " asc";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                    messageList.add(map);
                } while (cursor.moveToNext());
            }
            getDB().close();
            // return kitap liste
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageList;

    }

    public static ArrayList<HashMap<String, String>> getAllAudiosByArtist() {

        try {
            //Bu methodda ise tablodaki tüm değerleri alıyoruz
            //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri
            // listeleyeceğiz
            //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap
            // arrayı demek.
            //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

            String selectQuery = "SELECT * FROM " + DbConstants.AUDIO_TABLE_NAME + " GROUP BY " +
                    DbConstants.AUDIO_ARTIST +
                    " order by " + DbConstants.AUDIO_ARTIST + " asc";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String,
                    String>>();
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        map.put(cursor.getColumnName(i), cursor.getString(i));

                    }

                    messageList.add(map);
                } while (cursor.moveToNext());
            }
            getDB().close();
            // return kitap liste
            return messageList;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            return new ArrayList<HashMap<String, String>>();
        }
    }

    public static ArrayList<HashMap<String, String>> getAllAudiosByAlbum() {

        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap
        // arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).
        ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String, String>>();

        try {
            String selectQuery = "SELECT DISTINCT " + DbConstants.AUDIO_ALBUM + ","
                    + DbConstants.AUDIO_ALBUM_ARTIST + ","
                    + DbConstants.AUDIO_ARTIST + ","
                    + DbConstants.AUDIO_SOURCE_TYPE + ","
                    + DbConstants.AUDIO_THUMBNAIL
                    + " FROM " + DbConstants.AUDIO_TABLE_NAME
                    + " GROUP BY " + DbConstants.AUDIO_ALBUM
                    + " order by " + DbConstants.AUDIO_ALBUM + " asc";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        map.put(cursor.getColumnName(i), cursor.getString(i));

                    }

                    messageList.add(map);
                } while (cursor.moveToNext());
            }
            getDB().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return kitap liste
        return messageList;
    }

    public static ArrayList<HashMap<String, String>> getAllAudiosByAccount() {

        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap
        // arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).
        ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String, String>>();

        try {
            String selectQuery = "SELECT DISTINCT " + DbConstants.AUDIO_ALBUM + ","
                    + DbConstants.AUDIO_ALBUM_ARTIST + ","
                    + DbConstants.AUDIO_ARTIST + ","
                    + DbConstants.AUDIO_THUMBNAIL
                    + " FROM " + DbConstants.AUDIO_TABLE_NAME
                    + " GROUP BY " + DbConstants.AUDIO_ACCOUNT_ID;
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        if (DbConstants.AUDIO_THUMBNAIL.equals(cursor.getColumnName(i)) && cursor
                                .getBlob(i) != null) {
                            map.put(cursor.getColumnName(i), Base64.encodeToString(cursor.getBlob
                                            (i),
                                    Base64.DEFAULT));
                        } else {
                            map.put(cursor.getColumnName(i), cursor.getString(i));
                        }
                    }

                    messageList.add(map);
                } while (cursor.moveToNext());
            }
            getDB().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return kitap liste
        return messageList;
    }

    public static ArrayList<HashMap<String, String>> getAudiosOfAlbum(String album, String artist) {

        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap
        // arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

        ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String, String>>();

        try {
            String selectQuery = "SELECT * FROM " + DbConstants.AUDIO_TABLE_NAME +
                    " WHERE " + DbConstants.AUDIO_ALBUM + " = ?  ";

            Cursor cursor = getDB().rawQuery(selectQuery, new String[]{album});
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }

                    messageList.add(map);
                } while (cursor.moveToNext());
            }
            getDB().close();
            // return kitap liste
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageList;

    }

    public static ArrayList<HashMap<String, String>> getAudiosOfArtist(String artist) {

        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap
        // arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

        ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String, String>>();
        try {
            String selectQuery = "SELECT * FROM " + DbConstants.AUDIO_TABLE_NAME +
                    " WHERE " + DbConstants.AUDIO_ALBUM_ARTIST + " = ? OR " +
                    DbConstants.AUDIO_ARTIST + " = ?";

            Cursor cursor = getDB().rawQuery(selectQuery, new String[]{artist, artist});
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }

                    messageList.add(map);
                } while (cursor.moveToNext());
            }
            getDB().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return kitap liste
        return messageList;

    }

    public static ArrayList<HashMap<String, String>> getAudiosOfAccount(String accId) {

        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap
        // arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

        ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String, String>>();
        try {
            String selectQuery = "SELECT * FROM " + DbConstants.AUDIO_TABLE_NAME +
                    " WHERE " + DbConstants.AUDIO_ACCOUNT_ID + " = ? " +
                    " order by " + DbConstants.AUDIO_TITLE + " asc";

            Cursor cursor = getDB().rawQuery(selectQuery, new String[]{accId});
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }

                    messageList.add(map);
                } while (cursor.moveToNext());
            }
            getDB().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return kitap liste
        return messageList;

    }

    public static ArrayList<HashMap<String, String>> getAudiosOfPlaylist(String accId) {

        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap
        // arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

        ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String, String>>();
        try {
            String selectQuery = "SELECT * FROM " + DbConstants.PLAYLIST_AUDIO_TABLE_NAME +
                    " WHERE " + DbConstants.PA_PLAYLIST_ID + " = ? ";

            Cursor cursor = getDB().rawQuery(selectQuery, new String[]{accId});
            // looping through all rows and adding to list
            List<String> songIdList = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        if (DbConstants.PA_AUDIO_ID.equals(cursor.getColumnName(i)))
                            songIdList.add(cursor.getString(i));
                    }
                } while (cursor.moveToNext());
            }

            selectQuery = "SELECT * FROM " + DbConstants.AUDIO_TABLE_NAME +
                    " where " + DbConstants.AUDIO_ID + " in (" + StringUtils.getListEncoded
                    (songIdList) + ") " +
                    " order by " +
                    DbConstants.AUDIO_TITLE + " asc";
            cursor = getDB().rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }

                    messageList.add(map);
                } while (cursor.moveToNext());
            }
            getDB().close();


            getDB().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return kitap liste
        return messageList;

    }

    public static List<String> getThumbnailsByAcc(String id) {
        try {
            List<String> thumbs = new ArrayList<>();
            String selectQuery = "SELECT " + DbConstants.AUDIO_THUMBNAIL + " FROM " + DbConstants
                    .AUDIO_TABLE_NAME + " WHERE (" +
                    DbConstants.AUDIO_ACCOUNT_ID + " = '" + id + "' )";
            Cursor cursor = getDB().rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        thumbs.add(cursor.getString(i));
                    }
                } while (cursor.moveToNext());
            }
            getDB().close();

            return thumbs;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            return new ArrayList<String>();
        }
    }

    public static int getPlaylistCount(String id) {
        int count = 0;
        try {
            String selectQuery = "SELECT COUNT(*) FROM " +
                    DbConstants.PLAYLIST_AUDIO_TABLE_NAME +
                    " where " + DbConstants.PA_PLAYLIST_ID + " = '" + id + "'";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        count = cursor.getInt(i);
                    }
                } while (cursor.moveToNext());
            }
            getDB().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }


    public static HashMap<String, String> getAudioById(String id) {
        try {
            String selectQuery = "SELECT * FROM " + DbConstants.AUDIO_TABLE_NAME + " WHERE (" +
                    DbConstants.AUDIO_ID + " = '" + id + "' )";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            HashMap<String, String> chat = new HashMap<String, String>();

            if (cursor.moveToFirst()) {
                do {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        chat.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                } while (cursor.moveToNext());
            }
            getDB().close();

            return chat;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            return new HashMap<String, String>();
        }
    }

    public static int getAudioRowCount() {
        try {
            String countQuery = "SELECT  * FROM " + DbConstants.AUDIO_TABLE_NAME;
            Cursor cursor = getDB().rawQuery(countQuery, null);
            int rowCount = cursor.getCount();
            getDB().close();
            cursor.close();
            // return row count
            return rowCount;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            return 0;
        }
    }

    /*Remove operations
    *******************************************************************
     */

    public static void removeAccountById(String accId) {
        try {
            getDB().delete(DbConstants.ACCOUNT_TABLE_NAME, DbConstants.ACCOUNT_ID + " = '" +
                    accId + "' ", null);
            getDB().close();
            Log.e(TAG, "Account deleted. ID: " + accId);
        } catch (Exception e) {
            Log.e(TAG, "Account cannot be deleted. ID: " + accId);
        }
    }

    public static void removeAccountByName(String name) {
        try {
            getDB().delete(DbConstants.ACCOUNT_TABLE_NAME, DbConstants.ACCOUNT_NAME + " = '" +
                    name + "' ", null);
            getDB().close();
            Log.e(TAG, "Account deleted. ID: " + name);
        } catch (Exception e) {
            Log.e(TAG, "Account cannot be deleted. ID: " + name);
        }
    }

    public static void removeAudio(String id) {
        try {
            getDB().delete(DbConstants.AUDIO_TABLE_NAME, DbConstants.AUDIO_ID + " = '" + id + "' " +
                    "", null);
            getDB().close();
            Log.e(TAG, "Audio deleted. ID: " + id);
        } catch (Exception e) {
            Log.e(TAG, "Audio cannot be deleted. ID: " + id);
        }
    }

    public static void removeAudioByAccount(String id) {
        try {
            int count = getDB().delete(DbConstants.AUDIO_TABLE_NAME, DbConstants.AUDIO_ACCOUNT_ID
                    + "" + " = '" + id + "' ", null);
            getDB().close();
            Log.e(TAG, count + " Audio deleted. ID: " + id);
        } catch (Exception e) {
            Log.e(TAG, "Audio cannot be deleted. ID: " + id);
        }
    }

    public static void removeFromPlaylistsByAccount(String accId) {
        List<String> idList = new ArrayList<>();
        try {
            String selectQuery = "SELECT " + DbConstants.AUDIO_ID + " FROM " + DbConstants
                    .AUDIO_TABLE_NAME +
                    " WHERE " + DbConstants.AUDIO_ACCOUNT_ID + " = ? " +
                    " order by " + DbConstants.AUDIO_TITLE + " asc";

            Cursor cursor = getDB().rawQuery(selectQuery, new String[]{accId});
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        if (DbConstants.AUDIO_ID.equals(cursor.getColumnName(i))) {
                            idList.add(cursor.getString(i));
                        }
                    }
                } while (cursor.moveToNext());
            }
            int count = 0;
            for (String audioId : idList) {
                count += getDB().delete(DbConstants.PLAYLIST_AUDIO_TABLE_NAME, DbConstants
                        .PA_AUDIO_ID
                        + " = '" + audioId + "'", null);
            }
            Log.e(TAG, "Playlist updated. Count: " + count);
        } catch (Exception e) {
            Log.e(TAG, "Playlist cannot be updated. Exception: " + e.getMessage());
        }
    }


    public static void removeAudioFromPlaylist(String playlistId, String audioId) {
        try {
            long count = getDB().delete(DbConstants.PLAYLIST_AUDIO_TABLE_NAME, DbConstants
                    .PA_AUDIO_ID + " = '" + audioId + "' AND " + DbConstants.PA_PLAYLIST_ID + " =" +
                    " '" + playlistId + "'", null);
            Log.e(TAG, "Playlist updated. Count: " + count);
        } catch (Exception e) {
            Log.e(TAG, "Playlist cannot be updated. Exception: " + e.getMessage());
        }
    }

    public static void removePlaylistByPlaylist(String id) {
        List<String> idList = new ArrayList<>();
        try {
            int count = 0;
            count += getDB().delete(DbConstants.PLAYLIST_AUDIO_TABLE_NAME, DbConstants
                    .PA_PLAYLIST_ID
                    + " = '" + id + "'", null);
            getDB().delete(DbConstants.PLAYLIST_TABLE_NAME, DbConstants.PLAYLIST_ID + "= '" + id
                            + "'",
                    null);
            Log.e(TAG, "Playlist removed. Count: " + count);
        } catch (Exception e) {
            Log.e(TAG, "Playlist cannot be removed. Exception: " + e.getMessage());
        }
    }

    public static void removePlaylistByType(PlaylistType type) {
        List<String> idList = new ArrayList<>();
        try {
            String selectQuery = "SELECT " + DbConstants.PLAYLIST_ID + " FROM " + DbConstants
                    .PLAYLIST_TABLE_NAME +
                    " WHERE " + DbConstants.PLAYLIST_TYPE + " = ? ";

            Cursor cursor = getDB().rawQuery(selectQuery, new String[]{type.getCode().toString()});
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        if (DbConstants.PLAYLIST_ID.equals(cursor.getColumnName(i))) {
                            idList.add(cursor.getString(i));
                        }
                    }
                } while (cursor.moveToNext());
            }

            for (String id : idList) {
                removePlaylistByPlaylist(id);
            }


        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }
    /*
    Update Operations
    *********************************************************************
     */
/*    public static void updateValidPolicy(String id) {
        try {
            
            ContentValues data = new ContentValues();
            data.put(DbConstants.POLICY_VALID_FLAG, "false");
            getDB().update(DbConstants.POLICY_TABLE_NAME, data, DbConstants.POLICY_VALID_FLAG + "
             = "
            + "'true'", null);

            data.clear();
            data.put(DbConstants.POLICY_VALID_FLAG, "true");
            getDB().update(DbConstants.POLICY_TABLE_NAME, data, DbConstants.POLICY_ID + " = '" +
            id +
            "' ", null);
            getDB().close();
            Log.e(TAG, "Policy updated. ID: " + id);
        } catch (Exception e) {
            Log.e(TAG, "Policy cannot be updated. ID: " + id);
        }
    }*/


    public static void updateAccount(SourceInfo accInfo) {
        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.ACCOUNT_USER_ID, accInfo.getUserId());
            values.put(DbConstants.ACCOUNT_STATE, accInfo.getState().getCode());
            values.put(DbConstants.ACCOUNT_ACCESS_TOKEN, accInfo.getAccessToken());
            values.put(DbConstants.ACCOUNT_EXPIRED_IN, accInfo.getExpiredIn());
            values.put(DbConstants.ACCOUNT_REFRESH_TOKEN, accInfo.getRefreshToken());
            getDB().update(DbConstants.ACCOUNT_TABLE_NAME, values, DbConstants.ACCOUNT_ID + " = '" +
                    accInfo.getId() + "'", null);
            getDB().close(); //Database Bağlantısını kapattık*/
            Log.e(TAG, "Account updated. Id: " + accInfo.getId());
        } catch (Exception e) {
            Log.e(TAG, "Account cannot be updated. Exception: " + e.getMessage());
        }
    }

    public static void updateAccountAccessToken(String id, String token) {
        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.ACCOUNT_ACCESS_TOKEN, token);
            getDB().update(DbConstants.ACCOUNT_TABLE_NAME, values, DbConstants.ACCOUNT_ID + " = '" +
                    id + "'", null);
            getDB().close(); //Database Bağlantısını kapattık*/
            Log.e(TAG, "Account updated. Id: " + id);
        } catch (Exception e) {
            Log.e(TAG, "Account cannot be updated. Exception: " + e.getMessage());
        }
    }


    public static void updateAccountScannedFolders(String id, String scannedFolders) {
        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.ACCOUNT_SCANNED_FOLDERS, scannedFolders);
            getDB().update(DbConstants.ACCOUNT_TABLE_NAME, values, DbConstants.ACCOUNT_ID + " = '" +
                    id + "'", null);
            getDB().close(); //Database Bağlantısını kapattık*/
            Log.e(TAG, "Account updated. Id: " + id);
        } catch (Exception e) {
            Log.e(TAG, "Account cannot be updated. Exception: " + e.getMessage());
        }
    }

    public static void updateAccountScannedSongs(String id, int scan) {
        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.ACCOUNT_SCANNED_SONG, scan);
            getDB().update(DbConstants.ACCOUNT_TABLE_NAME, values, DbConstants.ACCOUNT_ID + " = '" +
                    id + "'", null);
            getDB().close(); //Database Bağlantısını kapattık*/
            Log.e(TAG, "Account updated. Id: " + id);
        } catch (Exception e) {
            Log.e(TAG, "Account cannot be updated. Exception: " + e.getMessage());
        }
    }

    public static void updateAccountScanStatus(String id, ScanStatus scan) {
        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.ACCOUNT_SCAN_STATUS, scan.getCode());
            getDB().update(DbConstants.ACCOUNT_TABLE_NAME, values, DbConstants.ACCOUNT_ID + " = '" +
                    id + "'", null);
            getDB().close(); //Database Bağlantısını kapattık*/
            Log.e(TAG, "Account updated. Id: " + id);
        } catch (Exception e) {
            Log.e(TAG, "Account cannot be updated. Exception: " + e.getMessage());
        }
    }

    public static void updateAccountUserId(String accId, String userId) {
        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.ACCOUNT_USER_ID, userId);
            getDB().update(DbConstants.ACCOUNT_TABLE_NAME, values, DbConstants.ACCOUNT_ID + " = '" +
                    accId + "'", null);
            getDB().close(); //Database Bağlantısını kapattık*/
            Log.e(TAG, "Account updated. Id: " + accId);
        } catch (Exception e) {
            Log.e(TAG, "Account cannot be updated. Exception: " + e.getMessage());
        }
    }


    public static void resetScanStatus(String id) {
        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.ACCOUNT_SCANNED_SONG, 0);
            values.put(DbConstants.ACCOUNT_SCANNED_FOLDERS, "");
            values.put(DbConstants.ACCOUNT_SCAN_STATUS, ScanStatus.INITIAL.getCode());
            getDB().update(DbConstants.ACCOUNT_TABLE_NAME, values, DbConstants.ACCOUNT_ID + " = '" +
                    id + "'", null);
            getDB().close(); //Database Bağlantısını kapattık*/
            Log.e(TAG, "Account updated. Id: " + id);
        } catch (Exception e) {
            Log.e(TAG, "Account cannot be updated. Exception: " + e.getMessage());
        }
    }


    public static boolean isPlaylistExist(String name) {
        List<String> idList = new ArrayList<>();
        try {

            String selectQuery = "SELECT " + DbConstants.PLAYLIST_NAME + " FROM " + DbConstants
                    .PLAYLIST_TABLE_NAME +
                    " WHERE " + DbConstants.PLAYLIST_NAME + " = ? ";

            Cursor cursor = getDB().rawQuery(selectQuery, new String[]{name});
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        if (DbConstants.PLAYLIST_NAME.equals(cursor.getColumnName(i))) {
                            idList.add(cursor.getString(i));
                        }
                    }
                } while (cursor.moveToNext());
            }

            return idList.size() > 0;
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
            return false;
        }
    }


    public static void clearEmptyPlaylists() {
        List<Long> idList = new ArrayList<>();
        try {

            String selectQuery = "SELECT " + DbConstants.PLAYLIST_ID + " FROM " + DbConstants
                    .PLAYLIST_TABLE_NAME + " WHERE " + DbConstants.PLAYLIST_TYPE + " = " +
                    PlaylistType.SPOTIFY.getCode();

            Cursor cursor = getDB().rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        if (DbConstants.PLAYLIST_ID.equals(cursor.getColumnName(i))) {
                            idList.add(cursor.getLong(i));
                        }
                    }
                } while (cursor.moveToNext());
            }

            for (Long pId : idList) {
                int count = 0;
                selectQuery = "SELECT COUNT(*) FROM " +
                        DbConstants.PLAYLIST_AUDIO_TABLE_NAME +
                        " where " + DbConstants.PA_PLAYLIST_ID + " = '" + pId + "'";
                cursor = getDB().rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        HashMap<String, String> map = new HashMap<String, String>();
                        for (int i = 0; i < cursor.getColumnCount(); i++) {
                            count = cursor.getInt(i);
                        }
                    } while (cursor.moveToNext());
                }

                if (count == 0) {
                    getDB().delete(DbConstants.PLAYLIST_TABLE_NAME, DbConstants.PLAYLIST_ID + "= " +
                            "'" +
                            pId + "'", null);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    public static void updateScanToFailed() {
        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.ACCOUNT_SCAN_STATUS, ScanStatus.FAILED.getCode());
            int i = getDB().update(DbConstants.ACCOUNT_TABLE_NAME, values, DbConstants
                    .ACCOUNT_SCAN_STATUS +
                    "=" + ScanStatus.STARTED.getCode(), null);
            Log.e(TAG, "Update started to failed operation successed. " + i);
        } catch (Exception e) {
            Log.e(TAG, "Update started to failed operation failed");
        }
    }

    public static void updateAudioOfflineStatus(String id, int status) {
        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.AUDIO_STATUS, status);
            getDB().update(DbConstants.AUDIO_TABLE_NAME, values, DbConstants.AUDIO_ID + " =" +
                    " '" + id + "'", null);
            Log.e(TAG, "Audio offline status change: Id: " + id + " status:" + AudioStatus.get
                    (status));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static void updateDownloadAudioOfflineStatus(String id, int status) {
        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.DOWNLOAD_AUDIOS_STATE, status);
            getDB().update(DbConstants.DOWNLOAD_AUDIOS_TABLE_NAME, values, DbConstants
                    .DOWNLOAD_AUDIOS_AUDIO_ID + " = '" + id + "'", null);
            Log.e(TAG, "Download audio offline status change.Id: " + id + " Status: " +
                    ScanStatus.get(status));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static void updatePlaylistOfflineStatus(String id, int status) {
        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.PLAYLIST_OFFLINE_STATUS, status);
            getDB().update(DbConstants.PLAYLIST_TABLE_NAME, values, DbConstants.PLAYLIST_ID + " =" +
                    " '" + id + "'", null);
            Log.e(TAG, "Playlist offline status change: " + status);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static void removeAudioFromDownloadAudios(String audioId) {
        getDB().delete(DbConstants.DOWNLOAD_AUDIOS_TABLE_NAME, DbConstants
                .DOWNLOAD_AUDIOS_AUDIO_ID + "='" + audioId + "'", null);
    }

    public static boolean isPlaylistOffline(String playlistId) {
        try {
            String selectQuery = "SELECT " + DbConstants.PLAYLIST_OFFLINE_STATUS + " FROM " +
                    DbConstants
                            .PLAYLIST_TABLE_NAME + " WHERE " + DbConstants.PLAYLIST_ID + " = " +
                    "'" + playlistId + "'";

            Cursor cursor = getDB().rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            int status = 0;
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        if (DbConstants.PLAYLIST_OFFLINE_STATUS.equals(cursor.getColumnName(i))) {
                            status = cursor.getInt(i);
                        }
                    }
                } while (cursor.moveToNext());
            }
            return status != 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static PlaylistInfo getPlaylistInfo(String playlistId) {
        PlaylistInfo pi = new PlaylistInfo();
        try {
            String selectQuery = "SELECT * FROM " +
                    DbConstants
                            .PLAYLIST_TABLE_NAME + " WHERE " + DbConstants.PLAYLIST_ID + " = " +
                    "'" + playlistId + "'";

            Cursor cursor = getDB().rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            int status = 0;
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        if (DbConstants.PLAYLIST_ID.equals(cursor.getColumnName(i))) {
                            pi.setId(cursor.getString(i));
                        }
                        if (DbConstants.PLAYLIST_TYPE.equals(cursor.getColumnName(i))) {
                            Integer code = cursor.getInt(i);
                            if (code != null)
                                pi.setType(PlaylistType.get(code));
                        }
                        if (DbConstants.PLAYLIST_NAME.equals(cursor.getColumnName(i))) {
                            pi.setName(cursor.getString(i));
                        }
                        if (DbConstants.PLAYLIST_OFFLINE_STATUS.equals(cursor.getColumnName(i))) {
                            pi.setOfflineStatus(cursor.getInt(i));
                        }
                    }
                } while (cursor.moveToNext());
            }

            return pi;
        } catch (Exception e) {
        }
        return pi;
    }

    public static boolean isAudioAddedToPlaylist(String playlistId, String songId) {
        HashMap<String, String> map = new HashMap<String, String>();
        int count = 0;
        try {
            String selectQuery = "SELECT COUNT(*) FROM " +
                    DbConstants.PLAYLIST_AUDIO_TABLE_NAME +
                    " where " + DbConstants.PA_AUDIO_ID + " = '" + songId + "' AND " +
                    DbConstants.PA_PLAYLIST_ID + "= '" + playlistId + "'";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        count = cursor.getInt(i);
                    }
                } while (cursor.moveToNext());
            }
            getDB().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count > 0;
    }


}
