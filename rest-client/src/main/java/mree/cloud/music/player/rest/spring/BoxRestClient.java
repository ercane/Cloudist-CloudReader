package mree.cloud.music.player.rest.spring;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import java.util.HashMap;
import java.util.Map;

import mree.cloud.music.player.common.model.box.File;
import mree.cloud.music.player.common.model.box.FolderItems;
import mree.cloud.music.player.common.model.box.SearchResult;
import mree.cloud.music.player.common.model.box.UserExtended;
import mree.cloud.music.player.common.ws.box.BoxParamKeys;
import mree.cloud.music.player.common.ws.box.BoxServiceUri;

/**
 * Created by eercan on 20.02.2017.
 */

public class BoxRestClient extends BaseRestClient {
    public static String PREFIX = "Authorization";
    private static String SERVICE_URI = "https://api.box.com/2.0";

    public BoxRestClient(String token) {
        super(SERVICE_URI, PREFIX, "Bearer " + token, new GsonHttpMessageConverter());
        addConverter(new StringHttpMessageConverter());
    }

    @Override
    public void setxToken(String xToken) {
        super.setxToken("Bearer " + xToken);
    }

    public SearchResult getAudioFiles(String ext, int limit, int offset) {
        Map params = new HashMap();
        params.put(BoxParamKeys.QUERY_PARAM, "a");
        params.put(BoxParamKeys.FILE_EXT_PARAM, ext);
        params.put(BoxParamKeys.LIMIT, limit);
        params.put(BoxParamKeys.OFFSET, offset);
        return get(BoxServiceUri.SEARCH, SearchResult.class, params);
    }

    public FolderItems getFolderItems(String folderId, int limit, int offset) {
        Map params = new HashMap();
        params.put(BoxParamKeys.FOLDER_PARAM, folderId);
        params.put(BoxParamKeys.LIMIT, limit);
        params.put(BoxParamKeys.OFFSET, offset);
        return get(BoxServiceUri.GET_FOLDER_ITEM, FolderItems.class, params);
    }

    public File getFileInfo(String fileId) {
        Map params = new HashMap();
        params.put(BoxParamKeys.FILE_ID_PARAM, fileId);
        return get(BoxServiceUri.GET_FILE_INFO, File.class, params);
    }

    public ResponseEntity<byte[]> downloadFile(String fileId) {
        Map params = new HashMap();
        params.put(BoxParamKeys.FILE_ID_PARAM, fileId);
        return getForEntity(BoxServiceUri.DOWNLOAD, byte[].class, params);
    }

    public UserExtended getUserInfo(String userId) {
        Map params = new HashMap();
        params.put(BoxParamKeys.USER_PARAM, userId);
        return get(BoxServiceUri.USERS + BoxServiceUri.USER_ID_PARAM, UserExtended.class, params);
    }

}
