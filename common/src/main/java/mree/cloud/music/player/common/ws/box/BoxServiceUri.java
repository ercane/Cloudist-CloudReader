package mree.cloud.music.player.common.ws.box;

/**
 * Created by eercan on 10.11.2016.
 */

public class BoxServiceUri {
    public static final String FILES = "/files";
    public static final String FILE_ID_PARAM = "/{file_id}";
    public static final String FOLDERS = "/folders";
    public static final String FOLDER_ID_PARAM = "/{folder_id}";
    public static final String USERS = "/users";
    public static final String USER_ID_PARAM = "/{user_id}";
    public static final String QUERY_PARAM = "/query={query}";
    public static final String FILE_EXT_PARAM = "file_extensions={file_extensions}";
    public static final String LIMIT = "limit";
    public static final String LIMIT_PARAM = LIMIT + "={limit}";
    public static final String OFFSET = "offset";
    public static final String OFFSET_PARAM = OFFSET + "={offset}";
    public static final String THUMBNAIL = "/thumbnail.extension";
    public static final String SEARCH = "/search?" + QUERY_PARAM + "&" + FILE_EXT_PARAM + "&" +
            LIMIT_PARAM + "&" + OFFSET_PARAM;
    public static final String GET_FOLDER_ITEM = FOLDERS + FOLDER_ID_PARAM + "/items?" +
            LIMIT_PARAM + "&" + OFFSET_PARAM;
    public static final String GET_FILE_INFO = FILES + FILE_ID_PARAM;
    public static final String GET_THUMB = FILES + FILE_ID_PARAM + THUMBNAIL;
    public static final String DOWNLOAD = FILES + FILE_ID_PARAM + "/content";
}
