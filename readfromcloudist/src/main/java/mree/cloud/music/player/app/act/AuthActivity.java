package mree.cloud.music.player.app.act;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.report.AnswersImpl;
import mree.cloud.music.player.app.report.Firebase;
import mree.cloud.music.player.app.utils.AuthHelper;
import mree.cloud.music.player.app.utils.Constants;
import mree.cloud.music.player.app.utils.GetAccessToken;
import mree.cloud.music.player.app.utils.RestHelper;
import mree.cloud.music.player.common.model.SourceInfo;
import mree.cloud.music.player.common.model.box.UserExtended;
import mree.cloud.music.player.common.ref.ScanStatus;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.auth.SourceState;

public class AuthActivity extends AppCompatActivity {


    private static final String TAG = AuthActivity.class.getSimpleName();
    private WebView web;
    private SourceInfo sourceInfo;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        sourceInfo = (SourceInfo) getIntent().getSerializableExtra(Constants.ACCOUNT_INFO);
        if (sourceInfo == null)
            finish();
        setTitle(sourceInfo.getName() + " Auth");


        String authUrl = AuthHelper.getUrl(sourceInfo.getType());
        Log.i(TAG, "URL: " + authUrl);

        pDialog = new ProgressDialog(AuthActivity.this);

        web = (WebView) findViewById(R.id.loginView);
        WebChromeClient webChromeClient = new WebChromeClient();
        web.setWebChromeClient(webChromeClient);
        //Make sure No cookies are created
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookie();
        } else {
            removeCookies();
        }

        //web.clearHistory();
        //web.clearCache(true);
        //Make sure no autofill for Forms/ user-name password happens for the app
        //web.clearFormData();
        //web.getSettings().setCacheMode(web.getSettings().LOAD_NO_CACHE);
        // web.getSettings().setAppCacheEnabled(false);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //web.getSettings().setSavePassword(false);
        //web.getSettings().setSaveFormData(false);
        //web.getSettings().setDomStorageEnabled(true);
        //web.addJavascriptInterface(new WebAppInterface(this), "android");
        web.loadUrl(authUrl);
        web.setWebViewClient(new WebViewClient() {

            boolean authStart = false;
            boolean authComplete = false;
            Intent resultIntent = new Intent();
            String authCode;

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request,
                                            WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e(TAG, url);
                super.onPageStarted(view, url, favicon);
                if (!authStart) {
                    pDialog = new ProgressDialog(AuthActivity.this);
                    pDialog.setMessage("Contacting ...");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.show();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (pDialog != null && pDialog.isShowing()) {
                    try {
                        pDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (url.contains("?code=") && authComplete != true) {
                    Uri uri = Uri.parse(url);
                    authCode = uri.getQueryParameter("code");
                    Log.i("", "CODE : " + authCode);
                    authComplete = true;
                    pDialog.setMessage("Contacting ...");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.show();
                    tokenGet(sourceInfo.getType()).execute(authCode);
                    //finish();
                } else if (url.contains("error")) {
                    Log.i("", "ACCESS_DENIED_HERE");
                    authComplete = true;
                    Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT)
                            .show();
                } else if (url.contains(sourceInfo.getType().getDesc().toLowerCase(Locale
                        .ENGLISH))) {
                    authStart = true;
                }
            }
        });


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void removeCookies() {
        //Make sure No cookies are created

        CookieManager.getInstance().removeAllCookies(
                new ValueCallback<Boolean>() {
                    @Override
                    public void onReceiveValue(Boolean value) {
                        Log.e(TAG, "Cookie " + value);
                    }
                }
        );


    }

    private AsyncTask<String, String, JSONObject> tokenGet(final SourceType type) {
        return new AsyncTask<String, String, JSONObject>() {


            public String REDIRECT_URI;
            public String GRANT_TYPE;
            public String CLIENT_ID;
            public String CLIENT_SECRET;
            public String TOKEN_URL;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();


                REDIRECT_URI = AuthHelper.getRedirectUri(type);
                GRANT_TYPE = AuthHelper.getAuthGrantType(type);
                CLIENT_ID = AuthHelper.getClientId(type);
                CLIENT_SECRET = AuthHelper.getClientSecret(type);
                TOKEN_URL = AuthHelper.getTokenUrl(type);

            }

            @Override
            protected JSONObject doInBackground(String... args) {
                GetAccessToken jParser = new GetAccessToken();
                JSONObject json = jParser.getToken(TOKEN_URL, args[0], CLIENT_ID, CLIENT_SECRET,
                        REDIRECT_URI, GRANT_TYPE, null);
                if (type == SourceType.SPOTIFY) {
                    try {
                        String tok = json.getString("access_token");
                        String currentUser = RestHelper.getSpotifyOkHttp(sourceInfo.getId(),
                                tok).getCurrentUser();
                        if (!currentUser.contains("premium")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AuthActivity.this, "Your account is not " +
                                            "premium you cannot play any song", Toast.LENGTH_LONG);
                                }
                            });
                            return null;
                        } else {
                            JSONObject object = new JSONObject(currentUser);
                            sourceInfo.setUserId(AuthHelper.getUserId(SourceType.SPOTIFY, object));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (type == SourceType.BOX) {
                    try {
                        String tok = json.getString("access_token");
                        UserExtended me = RestHelper.getBoxOkHttpClient(sourceInfo.getId(), tok)
                                .getUserInfo("me");
                        sourceInfo.setUserId(me.getId());
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage() + "");
                    }
                }
                return json;
            }

            @Override
            protected void onPostExecute(JSONObject json) {
//                pDialog.dismiss();
                if (json != null) {

                    try {
                        String tok = null;
                        try {
                            tok = json.getString("access_token");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Long expire = null;
                        try {
                            expire = json.getLong("expires_in");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String refresh = null;
                        try {
                            refresh = json.getString("refresh_token");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (type != SourceType.SPOTIFY) {
                            sourceInfo.setUserId(AuthHelper.getUserId(sourceInfo.getType(), json));
                        }
                        sourceInfo.setAccessToken(tok);
                        sourceInfo.setExpiredIn(AuthHelper.getExpiredIn(type, expire));
                        sourceInfo.setRefreshToken(refresh);
                        sourceInfo.setState(SourceState.AUTH);
                        sourceInfo.setScannedSong(0l);
                        sourceInfo.setScanStatus(ScanStatus.INITIAL);
                        DbEntryService.updateAccount(sourceInfo);
                        AnswersImpl.authAccount(sourceInfo.getId(), sourceInfo.getType());
                        Firebase.authAccountLog(sourceInfo.getName(), sourceInfo.getType());
                        Toast.makeText(getApplicationContext(), "Account added click for " +
                                "options...", Toast.LENGTH_LONG)
                                .show();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT)
                            .show();
                }
                pDialog.dismiss();
                finish();
            }
        };
    }
}
