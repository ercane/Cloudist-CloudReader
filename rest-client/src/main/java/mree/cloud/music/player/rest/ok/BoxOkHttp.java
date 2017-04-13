package mree.cloud.music.player.rest.ok;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import mree.cloud.music.player.common.model.box.DownloadItem;
import mree.cloud.music.player.common.model.box.File;
import mree.cloud.music.player.common.model.box.FolderItems;
import mree.cloud.music.player.common.model.box.Permissions;
import mree.cloud.music.player.common.model.box.SharedLink;
import mree.cloud.music.player.common.model.box.SharedLinkBody;
import mree.cloud.music.player.common.model.box.UserExtended;
import mree.cloud.music.player.rest.utils.UnauthorizedException;

/**
 * Created by eercan on 20.02.2017.
 */

public class BoxOkHttp extends BaseOkHttp {
    private static final String SERVICE_URI = "https://api.box.com/2.0";
    private static final Gson GSON = new Gson();

    public BoxOkHttp(String access_token) {
        super(access_token);
    }

    public FolderItems getFolderItems(String folderId, int limit, int offset) throws Exception {
        Request request = new Request.Builder()
                .url(SERVICE_URI + "/folders/" + folderId + "/items?limit=" + limit + "&offset="
                        + offset)
                .get()
                .addHeader("Authorization", "Bearer " + access_token)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 401) {
            throw new UnauthorizedException("Unauthorized: 401");
        } else {
            String json = response.body().string();
            FolderItems folderItems = GSON.fromJson(json, FolderItems.class);
            return folderItems;
        }
    }

    public File getFileInfo(String fileId) throws Exception {
        Request request = new Request.Builder()
                .url(SERVICE_URI + "/files/" + fileId)
                .get()
                .addHeader("Authorization", "Bearer " + access_token)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 401) {
            throw new UnauthorizedException("Unauthorized: 401");
        } else {
            String json = response.body().string();
            File folderItems = GSON.fromJson(json, File.class);
            return folderItems;
        }
    }


    public UserExtended getUserInfo(String userId) throws Exception {
        Request request = new Request.Builder()
                .url(SERVICE_URI + "/users/" + userId)
                .get()
                .addHeader("Authorization", "Bearer " + access_token)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 401) {
            throw new UnauthorizedException("Unauthorized: 401");
        } else {
            String json = response.body().string();
            UserExtended folderItems = GSON.fromJson(json, UserExtended.class);
            return folderItems;
        }
    }

    public File getFileWithSharedLink(String fileId) throws Exception {
        SharedLink sl = new SharedLink();
        sl.setAccess("open");
        Permissions permissions = new Permissions(true, true);
        sl.setPermissions(permissions);
        SharedLinkBody slb = new SharedLinkBody(sl);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject data = new JSONObject();
        try {
            data.put("shared_link", new Gson().toJson(sl));
        } catch (JSONException e) {
        }

        String json = data.toString();
        json = new Gson().toJson(slb);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(SERVICE_URI + "/" + fileId)
                .addHeader("Authorization", "Bearer " + access_token)
                .put(body)
                .build();

        Response response = client.newCall(request).execute();
        String resultJson = response.body().string();
        File file = new Gson().fromJson(resultJson, File.class);
        return file;
    }


    public DownloadItem downloadFile(String fileId) throws Exception {
        Request request = new Request.Builder()
                .url(SERVICE_URI + "/files/" + fileId + "/content")
                .get()
                .addHeader("Authorization", "Bearer " + access_token)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 401) {
            throw new UnauthorizedException("Unauthorized: 401");
        } else if (response.code() == 200 || response.code() == 304) {
            return new DownloadItem(response.body().byteStream(), response.body().contentLength());
        } else {
            throw new Exception(response.body().string());
        }
    }


    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
