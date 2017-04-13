package mree.cloud.music.player.app.database;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by eercan on 18.09.2015.
 */
public class DbTableService {

    public static SQLiteOpenHelper sqoh;
    public static String TAG = "DATABASE";

    private static SQLiteDatabase getDB() {
        SQLiteDatabase database = Database.getDatabase();
        if (database == null || !database.isOpen()) {
            database = Database.getSqoh().getWritableDatabase();
        }
        return database;
    }

    public static void createAudioTable() {
        try {

            String createTable = "CREATE TABLE IF NOT EXISTS " + DbConstants.AUDIO_TABLE_NAME + "("
                    + DbConstants.AUDIO_ID + " TEXT PRIMARY KEY,"
                    + DbConstants.AUDIO_ACCOUNT_ID + " TEXT,"
                    + DbConstants.AUDIO_ALBUM + " TEXT ,"
                    + DbConstants.AUDIO_ALBUM_ARTIST + " TEXT,"
                    + DbConstants.AUDIO_ARTIST + " TEXT,"
                    + DbConstants.AUDIO_BITRATE + " NUMERIC,"
                    + DbConstants.AUDIO_COMPOSERS + " TEXT,"
                    + DbConstants.AUDIO_COPYRIGHT + " TEXT,"
                    + DbConstants.AUDIO_DISC + " NUMERIC,"
                    + DbConstants.AUDIO_DISC_COUNT + " NUMERIC,"
                    + DbConstants.AUDIO_DOWNLOAD_URL + " TEXT,"
                    + DbConstants.AUDIO_DURATION + " NUMERIC,"
                    + DbConstants.AUDIO_FILENAME + " TEXT,"
                    + DbConstants.AUDIO_GENRE + " TEXT,"
                    + DbConstants.AUDIO_HAS_DRM + " TEXT,"
                    + DbConstants.AUDIO_IS_VARIABLE_BITRATE + " TEXT,"
                    + DbConstants.AUDIO_PATH + " TEXT,"
                    + DbConstants.AUDIO_SHARE_URL + " TEXT,"
                    + DbConstants.AUDIO_SOURCE_TYPE + " NUMERIC,"
                    + DbConstants.AUDIO_STATUS + " NUMERIC,"
                    + DbConstants.AUDIO_THUMBNAIL + " TEXT,"
                    + DbConstants.AUDIO_TITLE + " TEXT,"
                    + DbConstants.AUDIO_TRACK + " NUMERIC,"
                    + DbConstants.AUDIO_TRACK_COUNT + " NUMERIC,"
                    + DbConstants.AUDIO_YEAR + " NUMERIC,"
                    + "FOREIGN KEY(" + DbConstants.AUDIO_ACCOUNT_ID + ") REFERENCES " +
                    DbConstants.ACCOUNT_TABLE_NAME + "(" + DbConstants.ACCOUNT_ID + "))";
            getDB().execSQL(createTable);
            getDB().close();
            Log.e(TAG, DbConstants.AUDIO_TABLE_NAME + " table created. ");
        } catch (Exception e) {
            Log.e(TAG, DbConstants.AUDIO_TABLE_NAME + " table cannot be created. Exception: " + e
                    .getMessage());
        }
    }

    public static void createAccountTable() {
        try {

            String createTable = "CREATE TABLE IF NOT EXISTS " + DbConstants.ACCOUNT_TABLE_NAME +
                    "("
                    + DbConstants.ACCOUNT_ID + "  INTEGER PRIMARY KEY,"
                    + DbConstants.ACCOUNT_USER_ID + " TEXT ,"
                    + DbConstants.ACCOUNT_NAME + " TEXT ,"
                    + DbConstants.ACCOUNT_STATE + " NUMERIC,"
                    + DbConstants.ACCOUNT_ACCESS_TOKEN + " TEXT,"
                    + DbConstants.ACCOUNT_REFRESH_TOKEN + " NUMERIC,"
                    + DbConstants.ACCOUNT_EXPIRED_IN + " NUMERIC,"
                    + DbConstants.ACCOUNT_SCANNED_SONG + " NUMERIC,"
                    + DbConstants.ACCOUNT_SCAN_STATUS + " NUMERIC,"
                    + DbConstants.ACCOUNT_SCANNED_FOLDERS + " TEXT,"
                    + DbConstants.ACCOUNT_TYPE + " NUMERIC,"
                    + " CONSTRAINT source UNIQUE ("
                    + DbConstants.ACCOUNT_NAME + "," + DbConstants.ACCOUNT_TYPE + "))";
            getDB().execSQL(createTable);
            getDB().close();
            Log.e(TAG, DbConstants.ACCOUNT_TABLE_NAME + " table created. ");
        } catch (Exception e) {
            Log.e(TAG, DbConstants.ACCOUNT_TABLE_NAME + " table cannot be created. Exception: " + e
                    .getMessage());
        }
    }

    public static void createPlaylistTable() {
        try {

            String createTable = "CREATE TABLE IF NOT EXISTS " + DbConstants.PLAYLIST_TABLE_NAME
                    + "("
                    + DbConstants.PLAYLIST_ID + "  TEXT PRIMARY KEY,"
                    + DbConstants.PLAYLIST_NAME + " TEXT ,"
                    + DbConstants.PLAYLIST_TYPE + " NUMERIC ,"
                    + DbConstants.PLAYLIST_CREATION_DATE + " NUMERIC ,"
                    + DbConstants.PLAYLIST_OFFLINE_STATUS + " NUMERIC ,"
                    + DbConstants.PLAYLIST_UPDATED_DATE + " NUMERIC,"
                    + DbConstants.PLAYLIST_AUDIO_COUNT + " NUMERIC)";
            getDB().execSQL(createTable);
            getDB().close();
            Log.e(TAG, DbConstants.PLAYLIST_TABLE_NAME + " table created. ");
        } catch (Exception e) {
            Log.e(TAG, DbConstants.PLAYLIST_TABLE_NAME + " table cannot be created. Exception: " + e
                    .getMessage());
        }
    }

    public static void createPlaylistAudioTable() {
        try {

            String createTable = "CREATE TABLE IF NOT EXISTS " + DbConstants
                    .PLAYLIST_AUDIO_TABLE_NAME
                    + "("
                    + DbConstants.PA_PLAYLIST_ID + "  TEXT,"
                    + DbConstants.PA_AUDIO_ID + " TEXT,"
                    + DbConstants.PA_CREATED_DATE + " NUMERIC,"
                    + "FOREIGN KEY(" + DbConstants.PA_PLAYLIST_ID + ") REFERENCES " +
                    DbConstants.PLAYLIST_TABLE_NAME + "(" + DbConstants.PLAYLIST_ID + "),"
                    + "FOREIGN KEY(" + DbConstants.PA_AUDIO_ID + ") REFERENCES " +
                    DbConstants.AUDIO_TABLE_NAME + "(" + DbConstants.AUDIO_ID + "),"
                    + "PRIMARY KEY (" + DbConstants.PA_PLAYLIST_ID + "," + DbConstants
                    .PA_AUDIO_ID + "))";
            getDB().execSQL(createTable);
            getDB().close();
            Log.e(TAG, DbConstants.PLAYLIST_AUDIO_TABLE_NAME + " table created. ");
        } catch (Exception e) {
            Log.e(TAG, DbConstants.PLAYLIST_AUDIO_TABLE_NAME + " table cannot be created. " +
                    "Exception: " + e
                    .getMessage());
        }
    }

    public static void createDownloadAudiosTable() {
        try {

            String createTable = "CREATE TABLE IF NOT EXISTS " + DbConstants
                    .DOWNLOAD_AUDIOS_TABLE_NAME
                    + "("
                    + DbConstants.DOWNLOAD_AUDIOS_ID + " INTEGER PRIMARY KEY " +
                    "AUTOINCREMENT,"
                    + DbConstants.DOWNLOAD_AUDIOS_AUDIO_ID + "  TEXT,"
                    + DbConstants.DOWNLOAD_AUDIOS_STATE + " NUMERIC,"
                    + DbConstants.DOWNLOAD_AUDIOS_CREATED_DATE + " NUMERIC,"
                    + DbConstants.DOWNLOAD_AUDIOS_UPDATED_DATE + " NUMERIC,"
                    + "FOREIGN KEY(" + DbConstants.DOWNLOAD_AUDIOS_AUDIO_ID + ") REFERENCES " +
                    DbConstants.AUDIO_TABLE_NAME + "(" + DbConstants.AUDIO_ID + "))";
            getDB().execSQL(createTable);
            getDB().close();
            Log.e(TAG, DbConstants.DOWNLOAD_AUDIOS_TABLE_NAME + " table created. ");
        } catch (Exception e) {
            Log.e(TAG, DbConstants.DOWNLOAD_AUDIOS_TABLE_NAME + " table cannot be " +
                    "created. Exception: " + e.getMessage());
        }
    }

    public static void dropTable(String tableName) {
        try {

            String DROP_TABLE = "DROP TABLE IF EXISTS " + tableName;
            getDB().execSQL(DROP_TABLE);
            getDB().close();
            Log.e(TAG, "Table '" + tableName + "' dropped");
        } catch (SQLException e) {
            Log.e(TAG, "Table '" + tableName + "'cannot be dropped. Exception: " + e.getMessage());
        }
    }
}
