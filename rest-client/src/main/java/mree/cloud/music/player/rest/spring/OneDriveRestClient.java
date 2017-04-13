package mree.cloud.music.player.rest.spring;


import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import mree.cloud.music.player.common.model.onedrive.Item;
import mree.cloud.music.player.common.model.onedrive.Thumbnail;
import mree.cloud.music.player.common.ws.onedrive.OneDriveParamKeys;
import mree.cloud.music.player.common.ws.onedrive.OneDriveServiceUri;
import mree.cloud.music.player.rest.utils.GsonConverter;


/**
 * Created by mree on 13.11.2015.
 */
public class OneDriveRestClient extends BaseRestClient {


    private static String SERVICE_URI = "https://api.onedrive.com/v1.0";
    private static String PREFIX = "Authorization";

    public OneDriveRestClient(String token) {
        super(SERVICE_URI, PREFIX, "bearer " + token, GsonConverter.getGsonConverterInstance());
    }

    @Override
    public void setxToken(String xToken) {
        super.setxToken("bearer " + xToken);
    }

    public Item getItemInfoByPath(String path) {
        Map<String, String> map = new HashMap<>();
        map.put(OneDriveParamKeys.PATH, path);
        return get(OneDriveServiceUri.GET_ITEM_METADATA_PATH, Item.class, map);
    }

    public Item getItemInfoById(String id) {
        Map<String, String> map = new HashMap<>();
        map.put(OneDriveParamKeys.ID, id);
        return get(OneDriveServiceUri.GET_ITEM_METADATA_ID, Item.class, map);
    }

    public ResponseEntity<Void> downloadItemWithId(String id) {
        Map<String, String> map = new HashMap<>();
        map.put(OneDriveParamKeys.ITEM_ID, id);
        return getForEntity(OneDriveServiceUri.DOWNLOAD_ITEM_WITH_ID, null, map);
    }

    public ResponseEntity<Void> downloadItemWithPath(String path) {
        String allPath = OneDriveServiceUri.DOWNLOAD_ITEM_WITH_PATH + ":" + path
                + ":" + OneDriveServiceUri.CONTENT;
        return getForEntity(allPath, null);
    }


    public Thumbnail getThumbnailById(String id) {
        Map<String, String> map = new HashMap<>();
        map.put(OneDriveParamKeys.ID, id);
        map.put(OneDriveParamKeys.THUMB_ID, "0");
        map.put(OneDriveParamKeys.SIZE, "mediumSquare");
        return get(OneDriveServiceUri.GET_ITEM_THUMB_ID, Thumbnail.class, map);
    }
}
