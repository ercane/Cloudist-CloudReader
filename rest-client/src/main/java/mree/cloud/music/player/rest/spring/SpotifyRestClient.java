package mree.cloud.music.player.rest.spring;

import org.springframework.http.converter.StringHttpMessageConverter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import mree.cloud.music.player.common.ws.spotify.SpotifyParamKeys;
import mree.cloud.music.player.common.ws.spotify.SpotifyServiceUri;

/**
 * Created by eercan on 10.11.2016.
 */

public class SpotifyRestClient extends BaseRestClient {

    private static String SERVICE_URI = "https://api.spotify.com";
    private static String PREFIX = "Authorization";

    public SpotifyRestClient(String xToken) {
        super(SERVICE_URI, PREFIX, "Bearer " + xToken, new StringHttpMessageConverter());
    }

    @Override
    public void setxToken(String xToken) {
        super.setxToken("Bearer " + xToken);
    }

    public String getCurrentUser() {
        return get(SpotifyServiceUri.USER_SELF, String.class, Collections.EMPTY_MAP);
    }

    public String getCurrentUserPlaylists(int offset, int limit) {
        Map<String, Integer> map = new HashMap<>();
        map.put(SpotifyParamKeys.OFFSET_PARAM, offset);
        map.put(SpotifyParamKeys.LIMIT_PARAM, limit);
        return get(SpotifyServiceUri.USER_SELF_PLAYLIST, String.class, map);
    }


    public String getPlaylistTrack(String userId, String playlistId, Integer offset, Integer
            limit) {
        Map<String, String> map = new HashMap<>();
        map.put(SpotifyParamKeys.USER_PARAM, userId);
        map.put(SpotifyParamKeys.PLAYLIST_PARAM, playlistId);
        map.put(SpotifyParamKeys.LIMIT_PARAM, limit.toString());
        map.put(SpotifyParamKeys.OFFSET_PARAM, offset.toString());
        return get(SpotifyServiceUri.PLAYLIST_TRACKS, String.class, map);
    }

    public String getTrack(String id) {
        Map<String, String> map = new HashMap<>();
        map.put(SpotifyParamKeys.ID_PARAM, id);
        return get(SpotifyServiceUri.TRACK, String.class, map);
    }


}
