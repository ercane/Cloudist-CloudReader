package mree.cloud.music.player.rest.ok;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.net.URLEncoder;

import mree.cloud.music.player.common.model.google.File;
import mree.cloud.music.player.common.model.google.FileList;
import mree.cloud.music.player.common.model.google.Permission;
import mree.cloud.music.player.common.ws.google.GoogleServiceUri;
import mree.cloud.music.player.rest.utils.UnauthorizedException;

/**
 * Created by eercan on 06.03.2017.
 */

public class GoogleOkHttp extends BaseOkHttp {
    private static final String SERVICE_URI = "https://www.googleapis.com/drive";
    private static final Gson GSON = new Gson();

    public GoogleOkHttp(String access_token) {
        super(access_token);
    }

    public FileList getRootFileList() throws Exception {
        Request request = new Request.Builder()
                .url(SERVICE_URI + GoogleServiceUri.ROOT_FILELIST)
                .get()
                .addHeader("Authorization", "Bearer " + access_token)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() == 401) {
            throw new UnauthorizedException("Unauthirized: 401");
        } else {
            String json = response.body().string();
            FileList fileList = GSON.fromJson(json, FileList.class);
            return fileList;
        }
    }

    public FileList getRootFileList(String pageToken) throws Exception {
        Request request = new Request.Builder()
                .url(SERVICE_URI + GoogleServiceUri.ROOT_FILELIST + "&pageToken=" + URLEncoder
                        .encode(pageToken, "UTF-8"))
                .get()
                .addHeader("Authorization", "Bearer " + access_token)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() == 401) {
            throw new UnauthorizedException("Unauthirized: 401");
        } else {
            String json = response.body().string();
            FileList fileList = GSON.fromJson(json, FileList.class);
            return fileList;
        }
    }

    public FileList getFileList(String id) throws Exception {
        Request request = new Request.Builder()
                .url(SERVICE_URI + "/v3/files?'" + id + "' in parents")
                .get()
                .addHeader("Authorization", "Bearer " + access_token)
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

    public FileList getFileList(String id, String pageToken) throws Exception {
        Request request = new Request.Builder()
                .url(SERVICE_URI + "/v3/files?'" + id + "' in parents&pageToken=" + URLEncoder
                        .encode(pageToken, "UTF-8"))
                .get()
                .addHeader("Authorization", "Bearer " + access_token)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 401) {
            throw new UnauthorizedException("Unauthirized: 401");
        } else {
            String json = response.body().string();
            FileList fileList = GSON.fromJson(json, FileList.class);
            return fileList;
        }
    }

    public File getFile(String id) throws Exception {
        Request request = new Request.Builder()
                .url(SERVICE_URI + "/v3/files/" + id + "?fields=id,mimeType,name," +
                        "originalFilename,size,thumbnailLink,webContentLink,webViewLink")
                .get()
                .addHeader("Authorization", "Bearer " + access_token)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 401) {
            throw new UnauthorizedException("Unauthirized: 401");
        } else {
            String json = response.body().string();
            File fileList = GSON.fromJson(json, File.class);
            return fileList;
        }
    }

    public File getFileUrl(String id) throws Exception {
        Request request = new Request.Builder()
                .url(SERVICE_URI + "/v3/files/" + id + "?fields=webContentLink")
                .get()
                .addHeader("Authorization", "Bearer " + access_token)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 401) {
            throw new UnauthorizedException("Unauthirized: 401");
        } else {
            String json = response.body().string();
            File fileList = GSON.fromJson(json, File.class);
            return fileList;
        }
    }

    public void setPermission(String id, Permission p) throws Exception {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String json = new Gson().toJson(p);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(SERVICE_URI + "/v3/files/" + id + "/permissions")
                .post(body)
                .addHeader("Authorization", "Bearer " + access_token)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 401) {
            throw new UnauthorizedException("Unauthirized: 401");
        }
    }
}
