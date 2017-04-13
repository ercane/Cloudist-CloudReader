package mree.cloud.music.player.app.utils;

/**
 * Created by mree on 30.08.2016.
 */

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetAccessToken {
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    Map<String, String> mapn;
    DefaultHttpClient httpClient;
    HttpPost httpPost;

    public GetAccessToken() {
    }

    public JSONObject getToken(String address, String token, String client_id, String client_secret,
                               String redirect_uri, String grant_type, String refreshToken) {
        // Making HTTP request
        try {
            // DefaultHttpClient
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(address);

            if (token != null)
                params.add(new BasicNameValuePair("code", token));

            if (client_secret != null) {
                params.add(new BasicNameValuePair("client_secret", client_secret));
            }

            if (refreshToken != null) {
                params.add(new BasicNameValuePair("refresh_token", refreshToken));
            }

            params.add(new BasicNameValuePair("client_id", client_id));

            if (!address.contains("yandex"))
                params.add(new BasicNameValuePair("redirect_uri", redirect_uri));

            params.add(new BasicNameValuePair("grant_type", grant_type));

            if (address.contains("google")) {
                params.add(new BasicNameValuePair("access_type", "offline"));
                //params.add(new BasicNameValuePair("approval_prompt", "force"));
            }

            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                //sb.append(line + "n");
                sb.append(line);
            }
            is.close();

            json = sb.toString();
            Log.e("JSONStr", "Access token taken");
        } catch (Exception e) {
            e.getMessage();
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        // Parse the String to a JSON Object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        // Return JSON String
        return jObj;
    }

}
