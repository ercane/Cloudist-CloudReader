package mree.cloud.music.player.common.ws.yandex;

/**
 * Created by eercan on 10.11.2016.
 */

public class YandexServiceUri {
    public static final String RESOURCES = "/resources";
    public static final String FILES = "/files";
    public static final String DOWNLOAD = "/download";
    public static final String LIMIT = "limit";
    public static final String LIMIT_PARAM = LIMIT + "={limit}";
    public static final String MEDIA_TYPE = "media_type";
    public static final String MEDIA_TYPE_PARAM = MEDIA_TYPE + "={media_type}";
    public static final String OFFSET = "offset";
    public static final String OFFSET_PARAM = OFFSET + "={offset}";
    public static final String PATH = "path";
    public static final String PATH_PARAM = PATH + "={path}";

    public static final String DOWNLOAD_FILE = RESOURCES + DOWNLOAD + "?" + PATH_PARAM;
    public static final String LIST_AUDIO_FILES = RESOURCES + FILES + "?" + LIMIT_PARAM + "&" +
            MEDIA_TYPE_PARAM + "&" + OFFSET_PARAM;


}
