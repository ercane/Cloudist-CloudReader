package mree.cloud.music.player.common.ws.google;

/**
 * Created by eercan on 10.11.2016.
 */

public class GoogleServiceUri {
    public static final String VERSION = "/v3";
    public static final String FILES = "/files";
    public static final String CORPUS = "/{corpus}";
    public static final String QUERY = "?{q}";
    public static final String FILE_ID = "/{fileId}";
    public static final String PERMISSIONS = "/permissions";
    public static final String FIELDS = "?fields={fields}";

    public static final String FILE = VERSION + FILES + FILE_ID + FIELDS;
    public static final String FILELIST = VERSION + FILES + QUERY;
    public static final String ROOT_FILELIST = VERSION + FILES + "?q=mimeType contains 'audio'" +
            "&pageSize=100";
    public static final String SET_PERMISSION = VERSION + FILES + FILE_ID + PERMISSIONS;

}
