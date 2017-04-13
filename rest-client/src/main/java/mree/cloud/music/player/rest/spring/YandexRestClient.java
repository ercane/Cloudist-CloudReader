package mree.cloud.music.player.rest.spring;

import org.springframework.http.converter.json.GsonHttpMessageConverter;

import java.util.HashMap;
import java.util.Map;

import mree.cloud.music.player.common.model.yandex.DownloadLink;
import mree.cloud.music.player.common.model.yandex.FileList;
import mree.cloud.music.player.common.ws.yandex.YandexParamKeys;
import mree.cloud.music.player.common.ws.yandex.YandexServiceUri;

/**
 * Created by eercan on 25.01.2017.
 */

public class YandexRestClient extends BaseRestClient {
    private static String SERVICE_URI = "https://cloud-api.yandex.net/v1/disk";
    private static String PREFIX = "Authorization";

    public YandexRestClient(String xToken) {
        super(SERVICE_URI, PREFIX, "OAuth " + xToken, new GsonHttpMessageConverter());
    }

    @Override
    public void setxToken(String xToken) {
        super.setxToken("OAuth " + xToken);
    }

    public FileList getAudioFiles(int limit, int offset) {
        Map<String, String> params = new HashMap<>();
        params.put(YandexParamKeys.LIMIT_PARAM, limit + "");
        params.put(YandexParamKeys.MEDIA_TYPE_PARAM, "audio");
        params.put(YandexParamKeys.OFFSET_PARAM, offset + "");
        return get(YandexServiceUri.LIST_AUDIO_FILES, FileList.class, params);
    }

    public String getDownloadLink(String path) {
        Map<String, String> params = new HashMap<>();
        params.put(YandexServiceUri.PATH, path);
        DownloadLink downloadLink = get(YandexServiceUri.DOWNLOAD_FILE, DownloadLink.class, params);
        return downloadLink.getHref();
    }
}
