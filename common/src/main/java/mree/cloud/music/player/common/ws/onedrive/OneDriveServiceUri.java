package mree.cloud.music.player.common.ws.onedrive;

/**
 * Created by mree on 23.01.2016.
 */
public class OneDriveServiceUri {

    public static final String DRIVE = "/drive";
    public static final String DRIVES = "/drives";
    public static final String DRIVE_ID_PARAM = "/{drive-id}";
    public static final String ITEMS = "/items";
    public static final String ITEM_ID_PARAM = "/{item-id}";
    public static final String ROOT = "/root";
    public static final String CHILDREN = "/children";
    public static final String ID_PARAM = "/{id}";
    public static final String THUMB_ID_PARAM = "/{thumb_id}";
    public static final String SIZE_PARAM = "/{size}";
    public static final String PARENT_ID_PARAM = "/{parent-id}";
    public static final String PATH_PARAM = ":/{path}:";
    public static final String NAME_PARAM = "/{name}";
    public static final String EXPAND_PARAM = "/{expand}";
    public static final String CONTENT = "/content";
    public static final String THUMBNAILS = "/thumbnails";
    public static final String CHANGINGS = "/view.delta";
    public static final String PARENT_PATH_PARAM = ":/{parent-path}";
    public static final String EXPAND_CHILDREN = "?expand=children&expand=thumbnails";
    public static final String EXPAND_THUMS = "?expand=thumbnails";
    public static final String THUMB = "/thumbnails";
    public static final String REFRESH = "" +
            "client_id={client_id}&" +
            "redirect_uri={redirect_uri}&" +
            "client_secret={client_secret}&" +
            "refresh_token={refresh_token}&" +
            "grant_type=refresh_token";

    public static final String GET_ITEM_METADATA_PATH = DRIVE + ROOT + PATH_PARAM + EXPAND_CHILDREN;
    public static final String GET_ITEM_METADATA_ID = DRIVE + ITEMS + ID_PARAM + EXPAND_CHILDREN;
    public static final String GET_ITEM_THUMB_PATH = DRIVE + ROOT + PATH_PARAM + THUMB;
    public static final String GET_ITEM_THUMB_ID = DRIVE + ITEMS + ID_PARAM + THUMBNAILS +
            THUMB_ID_PARAM + SIZE_PARAM;
    public static final String DOWNLOAD_ITEM_WITH_ID = DRIVE + ITEMS + ITEM_ID_PARAM + CONTENT;
    public static final String DOWNLOAD_ITEM_WITH_PATH = DRIVE + ROOT;


    public static final String GET_ALL_CHANGINGS = DRIVE + ROOT + CHANGINGS;
}
