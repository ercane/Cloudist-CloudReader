package mree.cloud.music.player.app.tasks;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.utils.AuthHelper;
import mree.cloud.music.player.app.utils.GetAccessToken;
import mree.cloud.music.player.common.model.SourceInfo;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.auth.SourceState;
import mree.cloud.music.player.rest.ok.BaseOkHttp;
import mree.cloud.music.player.rest.spring.BaseRestClient;

/**
 * Created by mree on 04.09.2016.
 */
public class RefreshTokenTask implements Runnable {

    private static final String TAG = RefreshTokenTask.class.getSimpleName();
    private Handler refreshHandler;
    private SourceInfo sourceInfo;
    private BaseOkHttp okHttp;
    private String accId;
    private BaseRestClient restClient;
    private boolean isFinished;
    private String REDIRECT_URI;
    private String GRANT_TYPE;
    private String CLIENT_ID;
    private String CLIENT_SECRET;
    private String TOKEN_URL;
    private SourceType type;

    public RefreshTokenTask(SourceInfo sourceInfo, BaseRestClient restClient) {
        this.sourceInfo = sourceInfo;
        this.restClient = restClient;
        type = sourceInfo.getType();
        isFinished = false;
        init();
    }

    public RefreshTokenTask(String accId, BaseRestClient restClient) {
        this.accId = accId;
        this.restClient = restClient;
        type = DbEntryService.getAccountType(accId);
        sourceInfo = DbEntryService.getAccountInfo(accId);
        isFinished = false;
        init();
    }

    public RefreshTokenTask(SourceInfo sourceInfo, BaseOkHttp okHttp, String tmp) {
        this.sourceInfo = sourceInfo;
        this.okHttp = okHttp;
        type = sourceInfo.getType();
        isFinished = false;
        init();
    }

    public RefreshTokenTask(String accId, BaseOkHttp okHttp, String tmp) {
        this.accId = accId;
        this.okHttp = okHttp;
        type = DbEntryService.getAccountType(accId);
        sourceInfo = DbEntryService.getAccountInfo(accId);
        isFinished = false;
        init();
    }

    public Handler getRefreshHandler() {
        return refreshHandler;
    }

    protected void init() {
        REDIRECT_URI = AuthHelper.getRedirectUri(type);
        GRANT_TYPE = AuthHelper.getRefreshGrantType(type);
        CLIENT_ID = AuthHelper.getClientId(type);
        CLIENT_SECRET = AuthHelper.getClientSecret(type);
        TOKEN_URL = AuthHelper.getTokenUrl(type);
        refreshHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 25) {
                    RefreshTokenTask.this.run();
                }
            }
        };
    }

    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void run() {
        Log.i(TAG, "Refresh Token Task started for: " + sourceInfo.getName());
        GetAccessToken jParser = new GetAccessToken();
        JSONObject json = jParser.getToken(TOKEN_URL, null, CLIENT_ID,
                CLIENT_SECRET, REDIRECT_URI, GRANT_TYPE, sourceInfo.getRefreshToken());
        if (json != null) {

            try {

                String tok = json.getString("access_token");
                Long expire = json.getLong("expires_in");
                String refresh = null;
                try {
                    refresh = json.getString("refresh_token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("Token Access", tok);
                Log.d("Expire", expire.toString());
                Log.d("Refresh", refresh + "");


                sourceInfo.setAccessToken(tok);
                sourceInfo.setExpiredIn(AuthHelper.getExpiredIn(type, expire));
                if (refresh != null)
                    sourceInfo.setRefreshToken(refresh);
                sourceInfo.setState(SourceState.AUTH);
                DbEntryService.updateAccount(sourceInfo);

                if (restClient != null)
                    restClient.setxToken(tok);

                if (okHttp != null)
                    okHttp.setAccess_token(tok);

                isFinished = true;

            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
            Log.i(TAG, "Refresh Token Task finished for: " + sourceInfo.getName());
        }
    }

}
