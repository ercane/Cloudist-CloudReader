package mree.cloud.music.player.common.ws.spotify;

/**
 * Created by eercan on 10.11.2016.
 */

public class SpotifyServiceUri {
    public static final String VERSION = "/v1";
    public static final String OFFSET_PARAM = "{offset}";
    public static final String LIMIT_PARAM = "{limit}";
    public static final String OFFSET_LIMIT_PARAM = "?" + "offset=" + OFFSET_PARAM + "&limit=" +
            LIMIT_PARAM;
    public static final String USER_PARAM = "/{user-id}";
    public static final String USER = VERSION + "/users" + USER_PARAM;
    public static final String USER_SELF = VERSION + "/me";
    public static final String PLAYLISTS = "/playlists";
    public static final String PLAYLIST_PARAM = "/{playlist-id}";
    public static final String USER_PLAYLIST = USER + PLAYLISTS;
    public static final String USER_SELF_PLAYLIST = USER_SELF + PLAYLISTS + OFFSET_LIMIT_PARAM;
    public static final String TRACKS = "/tracks";
    public static final String PLAYLIST_TRACKS = USER_PLAYLIST + PLAYLIST_PARAM + TRACKS +
            OFFSET_LIMIT_PARAM;

    public static final String ID_PARAM = "/{id}";
    public static final String TRACK = VERSION + "/tracks" + ID_PARAM;

}
