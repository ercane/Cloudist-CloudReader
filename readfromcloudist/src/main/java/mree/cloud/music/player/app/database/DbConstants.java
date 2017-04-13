package mree.cloud.music.player.app.database;

/**
 * Created by eercan on 18.09.2015.
 */
public class DbConstants {
    /************************************
     * DATABASE CONSTANTS
     ***********************************/
    public final static Integer CMP_DB_VERSION = 1;

    public final static String CMP_DATABASE_NAME = "CMP";

    public final static String AUDIO_TABLE_NAME = "Audio";
    public final static String AUDIO_ID = "Audio";
    public final static String AUDIO_ACCOUNT_ID = "acc_id";
    public final static String AUDIO_ALBUM = "Album";
    public final static String AUDIO_ALBUM_ARTIST = "AlbumArtist";
    public final static String AUDIO_ARTIST = "Artist";
    public final static String AUDIO_BITRATE = "Bitrate";
    public final static String AUDIO_COMPOSERS = "Composers";
    public final static String AUDIO_COPYRIGHT = "Copyright";
    public final static String AUDIO_DISC = "Disc";
    public final static String AUDIO_DISC_COUNT = "DiscCount";
    public final static String AUDIO_DURATION = "Duration";
    public final static String AUDIO_FILENAME = "Filename";
    public final static String AUDIO_GENRE = "Genre";
    public final static String AUDIO_HAS_DRM = "HasDrm";
    public final static String AUDIO_IS_VARIABLE_BITRATE = "isVariableBitrate";
    public final static String AUDIO_PATH = "path";
    public final static String AUDIO_SHARE_URL = "ShareUrl";
    public final static String AUDIO_SOURCE_TYPE = "SourceType";
    public final static String AUDIO_STATUS = "Status";
    public final static String AUDIO_THUMBNAIL = "Thumbnail";
    public final static String AUDIO_TITLE = "Title";
    public final static String AUDIO_TRACK = "Track";
    public final static String AUDIO_TRACK_COUNT = "TrackCount";
    public final static String AUDIO_YEAR = "Year";
    public final static String AUDIO_DOWNLOAD_URL = "DownloadUrl";

    public final static String ACCOUNT_TABLE_NAME = "Accounts";
    public final static String ACCOUNT_ID = "_id";
    public final static String ACCOUNT_USER_ID = "USER_ID";
    public final static String ACCOUNT_NAME = "name";
    public final static String ACCOUNT_TYPE = "Type";
    public final static String ACCOUNT_STATE = "State";
    public final static String ACCOUNT_ACCESS_TOKEN = "Access";
    public final static String ACCOUNT_REFRESH_TOKEN = "Refresh";
    public final static String ACCOUNT_EXPIRED_IN = "Expire";
    public final static String ACCOUNT_SCANNED_SONG = "Scanned";
    public final static String ACCOUNT_SCANNED_FOLDERS = "ScannedFolders";
    public final static String ACCOUNT_SCAN_STATUS = "ScannedStatus";

    public final static String PLAYLIST_TABLE_NAME = "Playlists";
    public final static String PLAYLIST_ID = "_id";
    public final static String PLAYLIST_NAME = "name";
    public final static String PLAYLIST_TYPE = "type";
    public final static String PLAYLIST_OFFLINE_STATUS = "online_status";
    public final static String PLAYLIST_CREATION_DATE = "created_date";
    public final static String PLAYLIST_UPDATED_DATE = "updated_date";
    public final static String PLAYLIST_AUDIO_COUNT = "audio_count";

    public final static String PLAYLIST_AUDIO_TABLE_NAME = "PlaylistAudio";
    public final static String PA_PLAYLIST_ID = "playlist_id";
    public final static String PA_AUDIO_ID = "audio_id";
    public final static String PA_CREATED_DATE = "created_date";

    public final static String DOWNLOAD_AUDIOS_TABLE_NAME = "Download_Audios";
    public final static String DOWNLOAD_AUDIOS_ID = "_id";
    public final static String DOWNLOAD_AUDIOS_AUDIO_ID = "playlist_id";
    public final static String DOWNLOAD_AUDIOS_STATE = "state";
    public final static String DOWNLOAD_AUDIOS_CREATED_DATE = "created_date";
    public final static String DOWNLOAD_AUDIOS_UPDATED_DATE = "updated_date";

}
