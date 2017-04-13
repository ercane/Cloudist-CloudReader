package mree.cloud.music.player.rest.ok;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import mree.cloud.music.player.common.model.yandex.DownloadLink;
import mree.cloud.music.player.common.model.yandex.FileList;
import mree.cloud.music.player.rest.utils.UnauthorizedException;

/**
 * Created by eercan on 06.03.2017.
 */

public class YandexOkHttp extends BaseOkHttp {
    private static final String SERVICE_URI = "https://cloud-api.yandex.net/v1/disk";
    private static final Gson GSON = new Gson();

    public YandexOkHttp(String access_token) {
        super(access_token);
    }

    public FileList getAudioFiles(int limit, int offset) throws Exception {
        Request request = new Request.Builder()
                .url(SERVICE_URI + "/resources/files?limit=" + limit + "&media_type=audio" +
                        "&offset=" + offset)
                .get()
                .addHeader("Authorization", "OAuth " + access_token)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() == 401) {
            throw new UnauthorizedException("Unauthorized: 401");
        } else {
            String json = response.body().string();
            FileList fileList = GSON.fromJson(json, FileList.class);
            return fileList;
        }

    }

    public String getDownloadLink(String path) throws Exception {
        Request request = new Request.Builder()
                .url(SERVICE_URI + "/resources/download?path=" + path)
                .get()
                .addHeader("Authorization", "OAuth " + access_token)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() == 401) {
            throw new UnauthorizedException("Unauthorized: 401");
        } else {
            String json = response.body().string();
            DownloadLink downloadLink = GSON.fromJson(json, DownloadLink.class);
            return downloadLink.getHref();
        }
    }
}
