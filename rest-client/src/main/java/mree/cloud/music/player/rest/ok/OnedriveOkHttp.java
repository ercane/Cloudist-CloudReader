package mree.cloud.music.player.rest.ok;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import mree.cloud.music.player.common.model.onedrive.Item;
import mree.cloud.music.player.common.model.onedrive.Thumbnail;
import mree.cloud.music.player.common.ws.onedrive.OneDriveServiceUri;
import mree.cloud.music.player.rest.utils.UnauthorizedException;

/**
 * Created by eercan on 06.03.2017.
 */

public class OnedriveOkHttp extends BaseOkHttp {
    private static final String SERVICE_URI = "https://api.onedrive.com/v1.0";
    //private static final Gson GSON = GsonConverter.getGsonInstance();
    private static final Gson GSON = new Gson();

    public OnedriveOkHttp(String access_token) {
        super(access_token);
    }

    public Item getItemInfoByPath(String path) throws Exception {
        Request request = new Request.Builder()
                .url(SERVICE_URI + OneDriveServiceUri.DRIVE + OneDriveServiceUri.ROOT + ":/" +
                        path + ":" + OneDriveServiceUri.EXPAND_CHILDREN)
                .get()
                .addHeader("Authorization", "bearer " + access_token)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() == 401) {
            throw new UnauthorizedException("Unauthorized: 401");
        } else {
            String json = response.body().string();
            Item item = GSON.fromJson(json, Item.class);
            return item;
        }
    }

    public Item getItemInfoById(String id) throws Exception {
        Request request = new Request.Builder()
                .url(SERVICE_URI + OneDriveServiceUri.DRIVE + OneDriveServiceUri.ITEMS + "/" + id
                        + OneDriveServiceUri.EXPAND_CHILDREN)
                .get()
                .addHeader("Authorization", "bearer " + access_token)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() == 401) {
            throw new UnauthorizedException("Unauthorized: 401");
        } else {
            String json = response.body().string();
            Item item = GSON.fromJson(json, Item.class);
            return item;
        }
    }


    public Thumbnail getThumbnailById(String id) throws Exception {
        Request request = new Request.Builder()
                .url(SERVICE_URI + OneDriveServiceUri.DRIVE + OneDriveServiceUri.ITEMS + "/" + id
                        + OneDriveServiceUri.THUMBNAILS + "/0/mediumSquare")
                .get()
                .addHeader("Authorization", "bearer " + access_token)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() == 401) {
            throw new UnauthorizedException("Unauthorized: 401");
        } else {
            String json = response.body().string();
            return GSON.fromJson(json, Thumbnail.class);
        }
    }
}
