package mree.cloud.music.player.app.utils;

import java.util.HashMap;
import java.util.Map;

import mree.cloud.music.player.rest.ok.BaseOkHttp;
import mree.cloud.music.player.rest.ok.BoxOkHttp;
import mree.cloud.music.player.rest.ok.DropboxOkHttp;
import mree.cloud.music.player.rest.ok.GoogleOkHttp;
import mree.cloud.music.player.rest.ok.OnedriveOkHttp;
import mree.cloud.music.player.rest.ok.SpotifyOkHttp;
import mree.cloud.music.player.rest.ok.YandexOkHttp;
import mree.cloud.music.player.rest.spring.BaseRestClient;
import mree.cloud.music.player.rest.spring.BoxRestClient;
import mree.cloud.music.player.rest.spring.DropboxRestClient;
import mree.cloud.music.player.rest.spring.GoogleRestClient;
import mree.cloud.music.player.rest.spring.OneDriveRestClient;
import mree.cloud.music.player.rest.spring.SpotifyRestClient;
import mree.cloud.music.player.rest.spring.YandexRestClient;

/**
 * Created by eercan on 03.03.2017.
 */

public class RestHelper {
    private static Map<String, BaseRestClient> restClientMap = new HashMap<>();
    private static Map<String, BaseOkHttp> okClientMap = new HashMap<>();


    public static OneDriveRestClient getOneDriveRestClient(String accId, String token) {
        if (restClientMap.containsKey(accId)) {
            return (OneDriveRestClient) restClientMap.get(accId);
        } else {
            OneDriveRestClient restClient = new OneDriveRestClient(token);
            restClientMap.put(accId, restClient);
            return restClient;
        }
    }

    public static OnedriveOkHttp getOnedriveOkHttp(String accId, String token) {
        if (okClientMap.containsKey(accId)) {
            return (OnedriveOkHttp) okClientMap.get(accId);
        } else {
            OnedriveOkHttp restClient = new OnedriveOkHttp(token);
            okClientMap.put(accId, restClient);
            return restClient;
        }
    }

    public static GoogleRestClient getGoogleRestClient(String accId, String token) {
        if (restClientMap.containsKey(accId)) {
            return (GoogleRestClient) restClientMap.get(accId);
        } else {
            GoogleRestClient restClient = new GoogleRestClient(token);
            restClientMap.put(accId, restClient);
            return restClient;
        }
    }

    public static GoogleOkHttp getGoogleOkHttp(String accId, String token) {
        if (okClientMap.containsKey(accId)) {
            return (GoogleOkHttp) okClientMap.get(accId);
        } else {
            GoogleOkHttp restClient = new GoogleOkHttp(token);
            okClientMap.put(accId, restClient);
            return restClient;
        }
    }

    public static YandexRestClient getYandexRestClient(String accId, String token) {
        if (restClientMap.containsKey(accId)) {
            return (YandexRestClient) restClientMap.get(accId);
        } else {
            YandexRestClient restClient = new YandexRestClient(token);
            restClientMap.put(accId, restClient);
            return restClient;
        }
    }

    public static YandexOkHttp getYandexOkHttp(String accId, String token) {
        if (okClientMap.containsKey(accId)) {
            return (YandexOkHttp) okClientMap.get(accId);
        } else {
            YandexOkHttp restClient = new YandexOkHttp(token);
            okClientMap.put(accId, restClient);
            return restClient;
        }
    }

    public static SpotifyRestClient getSpotifyRestClient(String accId, String token) {
        if (restClientMap.containsKey(accId)) {
            return (SpotifyRestClient) restClientMap.get(accId);
        } else {
            SpotifyRestClient restClient = new SpotifyRestClient(token);
            restClientMap.put(accId, restClient);
            return restClient;
        }
    }

    public static SpotifyOkHttp getSpotifyOkHttp(String accId, String token) {
        if (okClientMap.containsKey(accId)) {
            return (SpotifyOkHttp) okClientMap.get(accId);
        } else {
            SpotifyOkHttp restClient = new SpotifyOkHttp(token);
            okClientMap.put(accId, restClient);
            return restClient;
        }
    }

    public static DropboxOkHttp getDropboxOkHttp(String accId, String token) {
        if (okClientMap.containsKey(accId)) {
            return (DropboxOkHttp) okClientMap.get(accId);
        } else {
            DropboxOkHttp okHttp = new DropboxOkHttp(token);
            okClientMap.put(accId, okHttp);
            return okHttp;
        }
    }

    public static DropboxRestClient getDropboxRestClient(String accId, String token) {
        if (okClientMap.containsKey(accId)) {
            return (DropboxRestClient) restClientMap.get(accId);
        } else {
            DropboxRestClient okHttp = new DropboxRestClient(token);
            restClientMap.put(accId, okHttp);
            return okHttp;
        }
    }

    public static BoxRestClient getBoxRestClient(String accId, String token) {
        if (restClientMap.containsKey(accId)) {
            return (BoxRestClient) restClientMap.get(accId);
        } else {
            BoxRestClient restClient = new BoxRestClient(token);
            restClientMap.put(accId, restClient);
            return restClient;
        }
    }

    public static BoxOkHttp getBoxOkHttpClient(String accId, String token) {
        if (okClientMap.containsKey(accId)) {
            return (BoxOkHttp) okClientMap.get(accId);
        } else {
            BoxOkHttp okHttp = new BoxOkHttp(token);
            okClientMap.put(accId, okHttp);
            return okHttp;
        }
    }


}
