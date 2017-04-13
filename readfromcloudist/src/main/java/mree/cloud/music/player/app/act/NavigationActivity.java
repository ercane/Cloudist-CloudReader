package mree.cloud.music.player.app.act;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import io.fabric.sdk.android.Fabric;
import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.services.CmpDeviceService;
import mree.cloud.music.player.app.shared.SharedPreferencesKeys;
import mree.cloud.music.player.app.shared.SharedPreferencesService;
import mree.cloud.music.player.app.utils.AuthHelper;
import mree.cloud.music.player.common.model.auth.AuthInfo;

public class NavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_navigation);

        if (!CmpDeviceService.running) {
            startService(new Intent(this, CmpDeviceService.class));
        }

        getNavigationTask().execute();
    }

    private AsyncTask<Void, Void, Intent> getNavigationTask() {

        return new AsyncTask<Void, Void, Intent>() {
            SharedPreferencesService preferencesService;

            @Override
            protected void onPreExecute() {
                CmpDeviceService.setPreferencesService(new SharedPreferencesService
                        (getSharedPreferences
                                (SharedPreferencesKeys.ROOT, MODE_PRIVATE)));
                if (CmpDeviceService.getPreferencesService().getOnedriveAuth() == null) {
                    AuthInfo ai = new AuthInfo();
                    ai.setAuthGrantType(AuthHelper.ONEDRIVE_GRANT_TYPE_AUTH);
                    ai.setAuthUrl(AuthHelper.ONEDRIVE_AUTH_URL);
                    ai.setClientId(AuthHelper.ONEDRIVE_CLIENT_ID);
                    ai.setClientSecret(AuthHelper.ONEDRIVE_CLIENT_SECRET);
                    ai.setRedirectUri(AuthHelper.ONEDRIVE_REDIRECT_URI);
                    ai.setRefreshGrantType(AuthHelper.ONEDRIVE_GRANT_TYPE_REFRESH);
                    ai.setRefreshUrl(AuthHelper.ONEDRIVE_TOKEN_URL);
                    ai.setRespType(AuthHelper.ONEDRIVE_RESP_TYPE);
                    ai.setScopes(AuthHelper.ONEDRIVE_SCOPES);
                    String json = new Gson().toJson(ai);
                    CmpDeviceService.getPreferencesService().setOnedriveAuth(json);
                }

                if (CmpDeviceService.getPreferencesService().getGoogleDriveAuth() == null) {
                    AuthInfo ai = new AuthInfo();
                    ai.setAuthGrantType(AuthHelper.GOOGLE_GRANT_TYPE_AUTH);
                    ai.setAuthUrl(AuthHelper.GOOGLE_AUTH_URL);
                    ai.setClientId(AuthHelper.GOOGLE_CLIENT_ID);
                    ai.setClientSecret(AuthHelper.GOOGLE_CLIENT_SECRET);
                    ai.setRedirectUri(AuthHelper.GOOGLE_REDIRECT_URI);
                    ai.setRefreshGrantType(AuthHelper.GOOGLE_GRANT_TYPE_REFRESH);
                    ai.setRefreshUrl(AuthHelper.GOOGLE_TOKEN_URL);
                    ai.setRespType(AuthHelper.GOOGLE_RESP_TYPE);
                    ai.setScopes(AuthHelper.GOOGLE_SCOPES);
                    String json = new Gson().toJson(ai);
                    CmpDeviceService.getPreferencesService().setGoogleDriveAuth(json);
                }

                if (CmpDeviceService.getPreferencesService().getSpotifyAuth() == null) {
                    AuthInfo ai2 = new AuthInfo();
                    ai2.setAuthGrantType(AuthHelper.SPOTIFY_GRANT_TYPE_AUTH);
                    ai2.setAuthUrl(AuthHelper.SPOTIFY_AUTH_URL);
                    ai2.setClientId(AuthHelper.SPOTIFY_CLIENT_ID);
                    ai2.setClientSecret(AuthHelper.SPOTIFY_CLIENT_SECRET);
                    ai2.setRedirectUri(AuthHelper.SPOTIFY_REDIRECT_URI);
                    ai2.setRefreshGrantType(AuthHelper.SPOTIFY_GRANT_TYPE_REFRESH);
                    ai2.setRefreshUrl(AuthHelper.SPOTIFY_TOKEN_URL);
                    ai2.setRespType(AuthHelper.SPOTIFY_RESP_TYPE);
                    ai2.setScopes(AuthHelper.SPOTIFY_SCOPES);
                    String json2 = new Gson().toJson(ai2);
                    CmpDeviceService.getPreferencesService().setSpotifyAuth(json2);
                }

                if (CmpDeviceService.getPreferencesService().getDropboxAuth() == null) {
                    AuthInfo ai = new AuthInfo();
                    ai.setAuthGrantType(AuthHelper.DROPBOX_GRANT_TYPE_AUTH);
                    ai.setAuthUrl(AuthHelper.DROPBOX_AUTH_URL);
                    ai.setClientId(AuthHelper.DROPBOX_CLIENT_ID);
                    ai.setClientSecret(AuthHelper.DROPBOX_CLIENT_SECRET);
                    ai.setRedirectUri(AuthHelper.DROPBOX_REDIRECT_URI);
                    ai.setRefreshGrantType(AuthHelper.DROPBOX_GRANT_TYPE_REFRESH);
                    ai.setRefreshUrl(AuthHelper.DROPBOX_TOKEN_URL);
                    ai.setRespType(AuthHelper.DROPBOX_RESP_TYPE);
                    ai.setScopes(AuthHelper.DROPBOX_SCOPES);
                    String json = new Gson().toJson(ai);
                    CmpDeviceService.getPreferencesService().setDropboxAuth(json);
                }

                if (CmpDeviceService.getPreferencesService().getYandexAuth() == null) {
                    AuthInfo ai = new AuthInfo();
                    ai.setAuthGrantType(AuthHelper.YANDEX_GRANT_TYPE_AUTH);
                    ai.setAuthUrl(AuthHelper.YANDEX_AUTH_URL);
                    ai.setClientId(AuthHelper.YANDEX_CLIENT_ID);
                    ai.setClientSecret(AuthHelper.YANDEX_CLIENT_SECRET);
                    ai.setRedirectUri(AuthHelper.YANDEX_REDIRECT_URI);
                    ai.setRefreshUrl(AuthHelper.YANDEX_TOKEN_URL);
                    ai.setRespType(AuthHelper.YANDEX_RESP_TYPE);
                    String json = new Gson().toJson(ai);
                    CmpDeviceService.getPreferencesService().setYandexAuth(json);
                }

                //if (CmpDeviceService.getPreferencesService().getBoxAuth() == null) {
                AuthInfo ai = new AuthInfo();
                ai.setAuthGrantType(AuthHelper.BOX_GRANT_TYPE_AUTH);
                ai.setAuthUrl(AuthHelper.BOX_AUTH_URL);
                ai.setClientId(AuthHelper.BOX_CLIENT_ID);
                ai.setClientSecret(AuthHelper.BOX_CLIENT_SECRET);
                ai.setRedirectUri(AuthHelper.BOX_REDIRECT_URI);
                ai.setRefreshUrl(AuthHelper.BOX_TOKEN_URL);
                ai.setRefreshGrantType(AuthHelper.BOX_GRANT_TYPE_REFRESH);
                ai.setRespType(AuthHelper.BOX_RESP_TYPE);
                String json = new Gson().toJson(ai);
                CmpDeviceService.getPreferencesService().setBoxAuth(json);
                //}
            }

            @Override
            protected Intent doInBackground(Void... params) {
                Class clazz;
                preferencesService = CmpDeviceService.getPreferencesService();
                try {
                /*    if (preferencesService.getDeviceToken() == null || "".equals
                            (preferencesService.getDeviceToken())) {
                        clazz = LoginActivity.class;
                    } else {*/
                    clazz = MainActivity.class;
                    //}

                    return new Intent(NavigationActivity.this, clazz);
                    //return new Intent(NavigationActivity.this, MainActivity.class);
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Intent intent) {
                if (intent != null) {
                    startActivity(intent);
                    finish();
                }
            }
        };
    }
}
