package mree.cloud.music.player.rest.spring;

import org.springframework.http.converter.StringHttpMessageConverter;

import java.util.HashMap;
import java.util.Map;

import mree.cloud.music.player.common.model.google.Permission;
import mree.cloud.music.player.common.ws.google.GoogleParamKeys;
import mree.cloud.music.player.common.ws.google.GoogleServiceUri;


/**
 * Created by eercan on 17.11.2016.
 */

public class GoogleRestClient extends BaseRestClient {

    private static String SERVICE_URI = "https://www.googleapis.com/drive";
    private static String PREFIX = "Authorization";

    public GoogleRestClient(String xToken) {
        super(SERVICE_URI, PREFIX, "Bearer " + xToken, new StringHttpMessageConverter());
    }

    @Override
    public void setxToken(String xToken) {
        super.setxToken("Bearer " + xToken);
    }

    public String getRootFileList() {
        Map<String, String> map = new HashMap<>();
        //map.put(YandexParamKeys.QUERY_PARAM,"'root' in parents");
        return get(GoogleServiceUri.ROOT_FILELIST, String.class, map);
    }

    public String getFileList(String id) {
        Map<String, String> map = new HashMap<>();
        map.put(GoogleParamKeys.QUERY_PARAM, "'" + id + "' in parents");
        return get(GoogleServiceUri.FILELIST, String.class, map);
    }

    public String getFile(String id) {
        Map<String, String> map = new HashMap<>();
        map.put(GoogleParamKeys.FILE_ID_PARAM, id);
        map.put(GoogleParamKeys.FIELDS_PARAM, "id,mimeType,name,originalFilename,size," +
                "thumbnailLink,webContentLink,webViewLink");
        return get(GoogleServiceUri.FILE, String.class, map);
    }

    public String getFileUrl(String id) {
        Map<String, String> map = new HashMap<>();
        map.put(GoogleParamKeys.FILE_ID_PARAM, id);
        map.put(GoogleParamKeys.FIELDS_PARAM, "webContentLink");
        return get(GoogleServiceUri.FILE, String.class, map);
    }

    public void setPermission(String id, Permission p) {
        Map<String, String> map = new HashMap<>();
        map.put(GoogleParamKeys.FILE_ID_PARAM, id);
        post(GoogleServiceUri.SET_PERMISSION, p, Void.class, map);
    }
}
