package mree.cloud.music.player.app.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import mree.cloud.music.player.app.services.CmpDeviceService;
import mree.cloud.music.player.common.model.auth.AuthInfo;
import mree.cloud.music.player.common.ref.SourceType;


/**
 * Created by mree on 30.08.2016.
 */
public class AuthHelper {


    /*ONEDRIVE
    ***********************************************************************
     */
    public static final String ONEDRIVE_USER_ID = "user_id";
    public static final String ONEDRIVE_EXPIRE = "expires_in";
    public static final String ONEDRIVE_ACCESS = "access_token";
    public static final String ONEDRIVE_REFRESH = "refresh_token";

    public static String ONEDRIVE_AUTH_URL = "https://login.live.com/oauth20_authorize.srf";
    public static String ONEDRIVE_CLIENT_ID = "5451e85e-4b8c-48a1-af34-1c3b3a09bf29";
    public static String ONEDRIVE_CLIENT_SECRET = "WpqvJxX9MHT6aqFo8dGcsKW";
    public static String ONEDRIVE_REDIRECT_URI = "http://localhost";
    public static String ONEDRIVE_RESP_TYPE = "code";
    public static String ONEDRIVE_GRANT_TYPE_AUTH = "authorization_code";
    public static String ONEDRIVE_GRANT_TYPE_REFRESH = "refresh_token";
    public static String ONEDRIVE_TOKEN_URL = "https://login.live.com/oauth20_token.srf";
    public static String[] ONEDRIVE_SCOPES = {"onedrive.appfolder",
            "onedrive.readonly",
            "onedrive.readwrite",
            "wl.signin",
            "wl.offline_access"};

/*
    public static String ONEDRIVE_AUTH_URL = "https://login.live.com/oauth20_authorize.srf";
    public static String ONEDRIVE_CLIENT_ID = "0000000048176A5D";
    public static String ONEDRIVE_CLIENT_SECRET = "j8C5xpxwPi7rXuHWawR15NHVR3WDREuU";
    public static String ONEDRIVE_REDIRECT_URI = "http://localhost";
    public static String ONEDRIVE_RESP_TYPE = "code";
    public static String ONEDRIVE_GRANT_TYPE_AUTH = "authorization_code";
    public static String ONEDRIVE_GRANT_TYPE_REFRESH = "refresh_token";
    public static String ONEDRIVE_TOKEN_URL = "https://login.live.com/oauth20_token.srf";
    public static String[] ONEDRIVE_SCOPES = {"onedrive.appfolder",
            "onedrive.readonly",
            "onedrive.readwrite",
            "wl.signin",
            "wl.offline_access"};
*/

    public static String SPOTIFY_AUTH_URL = "https://accounts.spotify.com/authorize";
    public static String SPOTIFY_CLIENT_ID = "d531e82c180746c78cf107fa2fef3320";
    public static String SPOTIFY_CLIENT_SECRET = "0df715f828b54003a5c6eb37c1b7e68e";
    public static String SPOTIFY_REDIRECT_URI = "http://localhost";
    public static String SPOTIFY_RESP_TYPE = "code";
    public static String SPOTIFY_GRANT_TYPE_AUTH = "authorization_code";
    public static String SPOTIFY_GRANT_TYPE_REFRESH = "refresh_token";
    public static String SPOTIFY_TOKEN_URL = "https://accounts.spotify.com/api/token";
    public static String[] SPOTIFY_SCOPES = {
            "playlist-read-private",
            "playlist-read-collaborative",
            "streaming",
            "user-read-email",
            "user-read-private"
    };

    public static String DROPBOX_AUTH_URL = "https://www.dropbox.com/oauth2/authorize";
    public static String DROPBOX_CLIENT_ID = "fb5myjjney3u234";
    public static String DROPBOX_CLIENT_SECRET = "k4fim13bfoxz7fn";
    public static String DROPBOX_REDIRECT_URI = "http://localhost";
    public static String DROPBOX_RESP_TYPE = "code";
    public static String DROPBOX_GRANT_TYPE_AUTH = "authorization_code";
    public static String DROPBOX_GRANT_TYPE_REFRESH = "refresh_token";
    public static String DROPBOX_TOKEN_URL = "https://www.dropbox.com/1/oauth2/token";
    public static String[] DROPBOX_SCOPES = {"playlist-read-public",
            "playlist-read-collaborative",
            "streaming",
            "user-library-read",
            "user-read-public",
            "user-read-birthdate",
            "user-follow-read",
            "user-top-read",
            "user-read-email"};

    public static String YANDEX_AUTH_URL = "https://oauth.yandex.com/authorize";
    public static String YANDEX_CLIENT_ID = "12c6a19d626a4b43a118f5ba816c42bf";
    public static String YANDEX_CLIENT_SECRET = "06364453595f4ab686d2ba352bb0d3bc";
    public static String YANDEX_REDIRECT_URI = "http://localhost";
    public static String YANDEX_RESP_TYPE = "code";
    public static String YANDEX_GRANT_TYPE_AUTH = "authorization_code";
    public static String YANDEX_TOKEN_URL = "https://oauth.yandex.com/token";

//    public static String SPOTIFY_AUTH_URL = "https://accounts.spotify.com/authorize";
//    public static String SPOTIFY_CLIENT_ID = "5e87b40b381742ceb16b6152cbcb5a7f";
//    public static String SPOTIFY_CLIENT_SECRET = "b26c373fd9fe47bab38c955fd01cc26b";
//    public static String SPOTIFY_REDIRECT_URI = "http://localhost";
//    public static String SPOTIFY_RESP_TYPE = "code";
//    public static String SPOTIFY_GRANT_TYPE_AUTH = "authorization_code";
//    public static String SPOTIFY_GRANT_TYPE_REFRESH = "refresh_token";
//    public static String SPOTIFY_TOKEN_URL = "https://accounts.spotify.com/api/token";
//    public static String[] SPOTIFY_SCOPES = {"playlist-read-public",
//            "playlist-read-collaborative",
//            "streaming",
//            "user-library-read",
//            "user-read-public",
//            "user-read-birthdate",
//            "user-follow-read",
//            "user-top-read",
//            "user-read-email"};

/*    public static String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    public static String GOOGLE_CLIENT_ID = "61549570272-ii38h68sdhlp84gm2f4relbre7gt5ug6.apps" +
            ".googleusercontent.com";
    public static String GOOGLE_CLIENT_SECRET = "o6J7FWvgTRzCKcC70czxdCJW";
    public static String GOOGLE_REDIRECT_URI = "http://localhost";
    public static String GOOGLE_RESP_TYPE = "code";
    public static String GOOGLE_GRANT_TYPE_AUTH = "authorization_code";
    public static String GOOGLE_ACCESS_TYPE = "offline";
    public static String GOOGLE_GRANT_TYPE_REFRESH = "refresh_token";
    public static String GOOGLE_TOKEN_URL = "https://www.googleapis.com/oauth2/v4/token";
    public static String[] GOOGLE_SCOPES = {"https://www.googleapis.com/auth/drive",
            "https://www.googleapis.com/auth/drive.readonly",
            "https://www.googleapis.com/auth/drive.metadata.readonly",
            "https://www.googleapis.com/auth/userinfo.profile"};    */


    public static String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    public static String GOOGLE_CLIENT_ID = "937174397613-ppld5aa9cr29t3299f64oulo05k35oll.apps" +
            ".googleusercontent.com";
    public static String GOOGLE_CLIENT_SECRET = "9JRabBp6HYmahc5XhcfJgnQj";
    public static String GOOGLE_REDIRECT_URI = "http://localhost";
    public static String GOOGLE_RESP_TYPE = "code";
    public static String GOOGLE_GRANT_TYPE_AUTH = "authorization_code";
    public static String GOOGLE_ACCESS_TYPE = "offline";
    public static String GOOGLE_GRANT_TYPE_REFRESH = "refresh_token";
    public static String GOOGLE_TOKEN_URL = "https://www.googleapis.com/oauth2/v4/token";
    public static String[] GOOGLE_SCOPES = {"https://www.googleapis.com/auth/drive",
            "https://www.googleapis.com/auth/drive.readonly",
            "https://www.googleapis.com/auth/drive.metadata.readonly",
            "https://www.googleapis.com/auth/userinfo.profile"};


    public static String BOX_AUTH_URL = "https://account.box.com/api/oauth2/authorize";
    public static String BOX_CLIENT_ID = "vj5btdm94b4cmv5aqcja4l3ufu6p1bb5";
    public static String BOX_CLIENT_SECRET = "Lh6mQPmtpQL6jXbNdx8kqcew1UB1Tgya";
    public static String BOX_REDIRECT_URI = "https://localhost";
    public static String BOX_RESP_TYPE = "code";
    public static String BOX_GRANT_TYPE_AUTH = "authorization_code";
    public static String BOX_ACCESS_TYPE = "offline";
    public static String BOX_GRANT_TYPE_REFRESH = "refresh_token";
    public static String BOX_TOKEN_URL = "https://api.box.com/oauth2/token";
    public static String[] BOX_SCOPES = {"https://www.googleapis.com/auth/drive",
            "https://www.googleapis.com/auth/drive.readonly",
            "https://www.googleapis.com/auth/drive.metadata.readonly",
            "https://www.googleapis.com/auth/userinfo.profile"};
    /*
    ***************************************************************************
     */

/*    public static String getUrl(SourceType sourceType) {
        switch (sourceType) {
            case ONEDRIVE:
                return ONEDRIVE_AUTH_URL + "?redirect_uri=" + ONEDRIVE_REDIRECT_URI +
                        "&response_type=" + ONEDRIVE_RESP_TYPE +
                        "&client_id=" + ONEDRIVE_CLIENT_ID +
                        "&scope=" + getScope(sourceType);
            *//*case DROPBOX:
                break;*//*
            case GOOGLE_DRIVE:
                return GOOGLE_AUTH_URL + "?redirect_uri=" + GOOGLE_REDIRECT_URI +
                        "&response_type=" + GOOGLE_RESP_TYPE +
                        "&client_id=" + GOOGLE_CLIENT_ID +
                        "&access_type=offline" +
                        "&approval_prompt=force" +
                        "&scope=" + getScope(sourceType);
            case SPOTIFY:
                return SPOTIFY_AUTH_URL + "?redirect_uri=" + SPOTIFY_REDIRECT_URI +
                        "&response_type=" + SPOTIFY_RESP_TYPE +
                        "&client_id=" + SPOTIFY_CLIENT_ID +
                        "&scope=" + getScope(sourceType);

        }
        return null;
    }    */

    public static String getUrl(SourceType sourceType) {
        try {
            String json;
            AuthInfo authInfo;
            switch (sourceType) {


                case ONEDRIVE:
                    json = CmpDeviceService.getPreferencesService().getOnedriveAuth();
                    if (json == null) {
                        return null;
                    }
                    authInfo = new Gson().fromJson(json, AuthInfo.class);
                    return authInfo.getAuthUrl() + "?redirect_uri=" + authInfo.getRedirectUri() +
                            "&response_type=" + authInfo.getRespType() +
                            "&client_id=" + authInfo.getClientId() +
                            "&scope=" + getScope(authInfo.getScopes());
                case DROPBOX:
                    json = CmpDeviceService.getPreferencesService().getDropboxAuth();
                    if (json == null) {
                        return null;
                    }
                    authInfo = new Gson().fromJson(json, AuthInfo.class);
                    return authInfo.getAuthUrl() + "?redirect_uri=" + authInfo.getRedirectUri() +
                            "&response_type=" + authInfo.getRespType() +
                            "&client_id=" + authInfo.getClientId();
                case GOOGLE_DRIVE:
                    json = CmpDeviceService.getPreferencesService().getGoogleDriveAuth();
                    if (json == null) {
                        return null;
                    }
                    authInfo = new Gson().fromJson(json, AuthInfo.class);
                    return authInfo.getAuthUrl() + "?redirect_uri=" + authInfo.getRedirectUri() +
                            "&response_type=" + authInfo.getRespType() +
                            "&client_id=" + authInfo.getClientId() +
                            "&access_type=offline" +
                            "&approval_prompt=force" +
                            "&scope=" + getScope(authInfo.getScopes());
                case SPOTIFY:
                    json = CmpDeviceService.getPreferencesService().getSpotifyAuth();
                    if (json == null) {
                        return null;
                    }
                    authInfo = new Gson().fromJson(json, AuthInfo.class);
                    return authInfo.getAuthUrl() + "?redirect_uri=" + authInfo.getRedirectUri() +
                            "&response_type=" + authInfo.getRespType() +
                            "&client_id=" + authInfo.getClientId() +
                            "&scope=" + getScope(authInfo.getScopes());

                case YANDEX_DISK:
                    json = CmpDeviceService.getPreferencesService().getYandexAuth();
                    if (json == null) {
                        return null;
                    }
                    authInfo = new Gson().fromJson(json, AuthInfo.class);
                    return authInfo.getAuthUrl() + "?response_type=" + authInfo.getRespType() +
                            "&client_id=" + authInfo.getClientId();


                case BOX:
                    json = CmpDeviceService.getPreferencesService().getBoxAuth();
                    if (json == null) {
                        return null;
                    }
                    authInfo = new Gson().fromJson(json, AuthInfo.class);
                    return authInfo.getAuthUrl() + "?response_type=" + authInfo.getRespType() +
                            "&client_id=" + authInfo.getClientId() +
                            "&redirect_uri=" + authInfo.getRedirectUri();

            }

        } catch (JsonSyntaxException e) {
            Log.e("AUTH_HELPER", e.getMessage());
        }
        return null;
    }

    public static String getScope(String[] scopes) {
        String scope = "";
        int i = 0;
        while (scopes != null && i < scopes.length) {
            if (i == scopes.length - 1) {
                scope += scopes[i];
            } else {
                scope += scopes[i] + "+";
            }

            i++;
        }

        return scope;
    }

    public static String getTokenUrl(SourceType type) {
        try {
            String json = null;
            AuthInfo authInfo = null;
            switch (type) {
                case ONEDRIVE:
                    json = CmpDeviceService.getPreferencesService().getOnedriveAuth();
                    break;
                case DROPBOX:
                    json = CmpDeviceService.getPreferencesService().getDropboxAuth();
                    break;
                case GOOGLE_DRIVE:
                    json = CmpDeviceService.getPreferencesService().getGoogleDriveAuth();
                    break;
                case SPOTIFY:
                    json = CmpDeviceService.getPreferencesService().getSpotifyAuth();
                    break;
                case YANDEX_DISK:
                    json = CmpDeviceService.getPreferencesService().getYandexAuth();
                    break;
                case BOX:
                    json = CmpDeviceService.getPreferencesService().getBoxAuth();
                    break;
            }

            if (json == null) {
                return null;
            }

            authInfo = new Gson().fromJson(json, AuthInfo.class);
            return authInfo.getRefreshUrl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getClientId(SourceType type) {
        try {
            String json = null;
            AuthInfo authInfo = null;
            switch (type) {
                case ONEDRIVE:
                    json = CmpDeviceService.getPreferencesService().getOnedriveAuth();
                    break;
                case DROPBOX:
                    json = CmpDeviceService.getPreferencesService().getDropboxAuth();
                    break;
                case GOOGLE_DRIVE:
                    json = CmpDeviceService.getPreferencesService().getGoogleDriveAuth();
                    break;
                case SPOTIFY:
                    json = CmpDeviceService.getPreferencesService().getSpotifyAuth();
                    break;
                case YANDEX_DISK:
                    json = CmpDeviceService.getPreferencesService().getYandexAuth();
                    break;
                case BOX:
                    json = CmpDeviceService.getPreferencesService().getBoxAuth();
                    break;
            }

            if (json == null) {
                return null;
            }

            authInfo = new Gson().fromJson(json, AuthInfo.class);
            return authInfo.getClientId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getClientSecret(SourceType type) {
        try {
            String json = null;
            AuthInfo authInfo = null;
            switch (type) {
                case ONEDRIVE:
                    json = CmpDeviceService.getPreferencesService().getOnedriveAuth();
                    break;
                case DROPBOX:
                    json = CmpDeviceService.getPreferencesService().getDropboxAuth();
                    break;
                case GOOGLE_DRIVE:
                    json = CmpDeviceService.getPreferencesService().getGoogleDriveAuth();
                    break;
                case SPOTIFY:
                    json = CmpDeviceService.getPreferencesService().getSpotifyAuth();
                    break;
                case YANDEX_DISK:
                    json = CmpDeviceService.getPreferencesService().getYandexAuth();
                    break;
                case BOX:
                    json = CmpDeviceService.getPreferencesService().getBoxAuth();
                    break;
            }

            if (json == null) {
                return null;
            }

            authInfo = new Gson().fromJson(json, AuthInfo.class);
            return authInfo.getClientSecret();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getRedirectUri(SourceType type) {
        try {
            String json = null;
            AuthInfo authInfo = null;
            switch (type) {
                case ONEDRIVE:
                    json = CmpDeviceService.getPreferencesService().getOnedriveAuth();
                    break;
                case DROPBOX:
                    json = CmpDeviceService.getPreferencesService().getDropboxAuth();
                    break;
                case GOOGLE_DRIVE:
                    json = CmpDeviceService.getPreferencesService().getGoogleDriveAuth();
                    break;
                case SPOTIFY:
                    json = CmpDeviceService.getPreferencesService().getSpotifyAuth();
                    break;
                case YANDEX_DISK:
                    json = CmpDeviceService.getPreferencesService().getYandexAuth();
                    break;
                case BOX:
                    json = CmpDeviceService.getPreferencesService().getBoxAuth();
                    break;
            }

            if (json == null) {
                return null;
            }

            authInfo = new Gson().fromJson(json, AuthInfo.class);
            return authInfo.getRedirectUri();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAuthGrantType(SourceType type) {
        try {
            String json = null;
            AuthInfo authInfo = null;
            switch (type) {
                case ONEDRIVE:
                    json = CmpDeviceService.getPreferencesService().getOnedriveAuth();
                    break;
                case DROPBOX:
                    json = CmpDeviceService.getPreferencesService().getDropboxAuth();
                    break;
                case GOOGLE_DRIVE:
                    json = CmpDeviceService.getPreferencesService().getGoogleDriveAuth();
                    break;
                case SPOTIFY:
                    json = CmpDeviceService.getPreferencesService().getSpotifyAuth();
                    break;
                case YANDEX_DISK:
                    json = CmpDeviceService.getPreferencesService().getYandexAuth();
                    break;
                case BOX:
                    json = CmpDeviceService.getPreferencesService().getBoxAuth();
                    break;
            }

            if (json == null) {
                return null;
            }

            authInfo = new Gson().fromJson(json, AuthInfo.class);
            return authInfo.getAuthGrantType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getRefreshGrantType(SourceType type) {
        try {
            String json = null;
            AuthInfo authInfo = null;
            switch (type) {
                case ONEDRIVE:
                    json = CmpDeviceService.getPreferencesService().getOnedriveAuth();
                    break;
                case DROPBOX:
                    json = CmpDeviceService.getPreferencesService().getDropboxAuth();
                    break;
                case GOOGLE_DRIVE:
                    json = CmpDeviceService.getPreferencesService().getGoogleDriveAuth();
                    break;
                case SPOTIFY:
                    json = CmpDeviceService.getPreferencesService().getSpotifyAuth();
                    break;
                case YANDEX_DISK:
                    json = CmpDeviceService.getPreferencesService().getYandexAuth();
                    break;
                case BOX:
                    json = CmpDeviceService.getPreferencesService().getBoxAuth();
                    break;
            }

            if (json == null) {
                return null;
            }

            authInfo = new Gson().fromJson(json, AuthInfo.class);
            return authInfo.getRefreshGrantType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long getExpiredIn(SourceType type, Long expire) {
        switch (type) {
            case ONEDRIVE:
            case GOOGLE_DRIVE:
            case SPOTIFY:
            case YANDEX_DISK:
            case BOX:
                return System.currentTimeMillis() + (expire * 1000);
            /*case DROPBOX:
                break;*/
        }
        return null;

    }

    public static int getIcon(SourceType type) {

        switch (type) {
            case ONEDRIVE:
                return Constants.onedrive;
            case DROPBOX:
                return Constants.dropbox;
            case GOOGLE_DRIVE:
                return Constants.google_drive;
            case SPOTIFY:
                return Constants.box;
            default:
                return Constants.none;
        }
    }


    public static String getUserId(SourceType type, JSONObject json) {

        try {
            switch (type) {
                case ONEDRIVE:
                    return json.getString("user_id");
                case DROPBOX:
                    return json.getString("account_id");
                case GOOGLE_DRIVE:
                    return json.getString("id_token");
                case SPOTIFY:
                    return json.getString("uri");
                default:
                    break;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
