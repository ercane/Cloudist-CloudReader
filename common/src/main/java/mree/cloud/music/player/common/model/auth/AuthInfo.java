package mree.cloud.music.player.common.model.auth;

import mree.cloud.music.player.common.MarkedInfo;

/**
 * Created by mree on 09.01.2017.
 */

public class AuthInfo extends MarkedInfo{
    private String authUrl;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String respType;
    private String authGrantType;
    private String refreshGrantType;
    private String refreshUrl;
    private String[] scopes;

    public String getAuthUrl(){
        return authUrl;
    }

    public void setAuthUrl(String authUrl){
        this.authUrl = authUrl;
    }

    public String getClientId(){
        return clientId;
    }

    public void setClientId(String clientId){
        this.clientId = clientId;
    }

    public String getClientSecret(){
        return clientSecret;
    }

    public void setClientSecret(String clientSecret){
        this.clientSecret = clientSecret;
    }

    public String getRedirectUri(){
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri){
        this.redirectUri = redirectUri;
    }

    public String getRespType(){
        return respType;
    }

    public void setRespType(String respType){
        this.respType = respType;
    }

    public String getAuthGrantType(){
        return authGrantType;
    }

    public void setAuthGrantType(String authGrantType){
        this.authGrantType = authGrantType;
    }

    public String getRefreshGrantType(){
        return refreshGrantType;
    }

    public void setRefreshGrantType(String refreshGrantType){
        this.refreshGrantType = refreshGrantType;
    }

    public String getRefreshUrl(){
        return refreshUrl;
    }

    public void setRefreshUrl(String refreshUrl){
        this.refreshUrl = refreshUrl;
    }

    public String[] getScopes(){
        return scopes;
    }

    public void setScopes(String[] scopes){
        this.scopes = scopes;
    }
}
