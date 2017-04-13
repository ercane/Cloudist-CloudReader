package mree.cloud.music.player.rest.ok;

import com.squareup.okhttp.OkHttpClient;

/**
 * Created by eercan on 03.03.2017.
 */

public class BaseOkHttp {
    protected String access_token;
    protected OkHttpClient client;

    public BaseOkHttp(String access_token) {
        this.access_token = access_token;
        client = new OkHttpClient();
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }
}
