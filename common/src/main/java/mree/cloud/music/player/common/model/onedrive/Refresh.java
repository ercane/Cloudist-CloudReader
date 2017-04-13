package mree.cloud.music.player.common.model.onedrive;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mree on 16.02.2016.
 */
public class Refresh {

    @SerializedName("token_type")
    public String Type;

    @SerializedName("scope")
    public String scope;

    @SerializedName("access_token")
    public String AccessToken;

    @SerializedName("refresh_token")
    public String RefreshToken;

    @SerializedName("expires_in")
    public String ExpireTime;
}
