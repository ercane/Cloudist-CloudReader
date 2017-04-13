package mree.cloud.music.player.rest.ok;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import mree.cloud.music.player.common.ws.spotify.SpotifyServiceUri;
import mree.cloud.music.player.rest.utils.UnauthorizedException;

/**
 * Created by eercan on 06.03.2017.
 */

public class SpotifyOkHttp extends BaseOkHttp {

    private static final String SERVICE_URI = "https://api.spotify.com";
    private static final Gson GSON = new Gson();

    public SpotifyOkHttp(String access_token) {
        super(access_token);
    }

    public String getCurrentUser() throws Exception {
        Request request = new Request.Builder()
                .url(SERVICE_URI + SpotifyServiceUri.USER_SELF)
                .get()
                .addHeader("Authorization", "Bearer " + access_token)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() == 401) {
            throw new UnauthorizedException("Unauthorized: 401");
        } else {
            String json = response.body().string();
            return json;
        }
    }

    public String getCurrentUserPlaylists(int offset, int limit) throws Exception {
        Request request = new Request.Builder()
                .url(SERVICE_URI + SpotifyServiceUri.USER_SELF + SpotifyServiceUri.PLAYLISTS +
                        "?limit=" + limit + "&offset=" + offset)
                .get()
                .addHeader("Authorization", "Bearer " + access_token)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() == 401) {
            throw new UnauthorizedException("Unauthorized: 401");
        } else {
            String json = response.body().string();
            return json;
        }
    }


    public String getPlaylistTrack(String userId, String playlistId, Integer offset, Integer
            limit) throws Exception {
        Request request = new Request.Builder()
                .url(SERVICE_URI + "/v1/users/" + userId + SpotifyServiceUri.PLAYLISTS + "/" +
                        playlistId + SpotifyServiceUri.TRACKS + "?limit=" + limit + "&offset=" +
                        offset)
                .get()
                .addHeader("Authorization", "Bearer " + access_token)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() == 401) {
            throw new UnauthorizedException("Unauthorized: 401");
        } else {
            String json = response.body().string();
            return json;
        }
    }

    public String getTrack(String id) throws Exception {
        Request request = new Request.Builder()
                .url(SERVICE_URI + "/v1/tracks/" + id)
                .get()
                .addHeader("Authorization", "Bearer " + access_token)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() == 401) {
            throw new UnauthorizedException("Unauthorized: 401");
        } else {
            String json = response.body().string();
            return json;
        }
    }
}
