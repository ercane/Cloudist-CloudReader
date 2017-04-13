package mree.cloud.music.player.app.shared;

import android.content.SharedPreferences;

/**
 * Created by mree on 06.12.2015.
 */
public class SharedPreferencesService {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public SharedPreferencesService(SharedPreferences preferences) {
        this.preferences = preferences;
        this.editor = preferences.edit();
    }

    public String getDeviceToken() {
        return preferences.getString(SharedPreferencesKeys.DEVICE_TOKEN, "");
    }

    public void setDeviceToken(String s) {
        editor.putString(SharedPreferencesKeys.DEVICE_TOKEN, s);
        editor.commit();
    }

    public String getServerAddress() {
        return preferences.getString(SharedPreferencesKeys.SERVER_ADDRESS, "");
    }

    public void setServerAddress(String s) {
        editor.putString(SharedPreferencesKeys.SERVER_ADDRESS, s);
        editor.commit();
    }

    public String getUsername() {
        return preferences.getString(SharedPreferencesKeys.USERNAME, "");
    }

    public void setUsername(String s) {
        editor.putString(SharedPreferencesKeys.USERNAME, s);
        editor.commit();
    }

    public String getLocalId() {
        return preferences.getString(SharedPreferencesKeys.LOCAL_ID, "0");
    }

    public void setLocalId(String s) {
        editor.putString(SharedPreferencesKeys.LOCAL_ID, s);
        editor.commit();
    }

    public String getAdDate() {
        return preferences.getString(SharedPreferencesKeys.AD_DATE, "0");
    }

    public void setAdDate(String s) {
        editor.putString(SharedPreferencesKeys.AD_DATE, s);
        editor.commit();
    }

    public Long getFirstOpen() {
        return preferences.getLong(SharedPreferencesKeys.FIRST_OPEN_DATE, 0l);
    }

    public void setFirstOpen(Long s) {
        editor.putLong(SharedPreferencesKeys.FIRST_OPEN_DATE, s);
        editor.commit();
    }

    public Boolean isFirst() {
        return preferences.getBoolean(SharedPreferencesKeys.FIRST, true);
    }

    public void setFirst(Boolean state) {
        editor.putBoolean(SharedPreferencesKeys.FIRST, state);
        editor.commit();
    }

    public Boolean isUpdateApplied() {
        return preferences.getBoolean(SharedPreferencesKeys.IS_UPDATE_APPLIED, false);
    }

    public void setUpdateApplied(Boolean state) {
        editor.putBoolean(SharedPreferencesKeys.IS_UPDATE_APPLIED, state);
        editor.commit();
    }

    public Boolean getAdState() {
        return preferences.getBoolean(SharedPreferencesKeys.AD_STATE, false);
    }

    public void setAdState(Boolean state) {
        editor.putBoolean(SharedPreferencesKeys.AD_STATE, state);
        editor.commit();
    }

    public Boolean isLocalInclude() {
        return preferences.getBoolean(SharedPreferencesKeys.IS_LOCAL_INCLUDE, true);
    }

    public void setLocalInclude(Boolean state) {
        editor.putBoolean(SharedPreferencesKeys.IS_LOCAL_INCLUDE, state);
        editor.commit();
    }

    public Boolean isShowOnlySuitable() {
        return preferences.getBoolean(SharedPreferencesKeys.IS_ONLY_SHOW_SUITABLE, true);
    }

    public void setIsOnlyShowSuitable(Boolean state) {
        editor.putBoolean(SharedPreferencesKeys.IS_ONLY_SHOW_SUITABLE, state);
        editor.commit();
    }

    public Boolean isMobileDataAllowed() {
        return preferences.getBoolean(SharedPreferencesKeys.IS_MOBILE_DATA_ALLOWED, true);
    }

    public void setIsMobileDataAllowed(Boolean state) {
        editor.putBoolean(SharedPreferencesKeys.IS_MOBILE_DATA_ALLOWED, state);
        editor.commit();
    }

    public String getOnedriveAuth() {
        return preferences.getString(SharedPreferencesKeys.ONEDRIVE_AUTH, null);
    }

    public void setOnedriveAuth(String s) {
        editor.putString(SharedPreferencesKeys.ONEDRIVE_AUTH, s);
        editor.commit();
    }

    public String getGoogleDriveAuth() {
        return preferences.getString(SharedPreferencesKeys.GOOGLEDRIVE_AUTH, null);
    }

    public void setGoogleDriveAuth(String s) {
        editor.putString(SharedPreferencesKeys.GOOGLEDRIVE_AUTH, s);
        editor.commit();
    }

    public String getSpotifyAuth() {
        return preferences.getString(SharedPreferencesKeys.SPOTIFY_AUTH, null);
    }

    public void setSpotifyAuth(String s) {
        editor.putString(SharedPreferencesKeys.SPOTIFY_AUTH, s);
        editor.commit();
    }

    public String getDropboxAuth() {
        return preferences.getString(SharedPreferencesKeys.DROPBOX_AUTH, null);
    }

    public void setDropboxAuth(String s) {
        editor.putString(SharedPreferencesKeys.DROPBOX_AUTH, s);
        editor.commit();
    }

    public String getYandexAuth() {
        return preferences.getString(SharedPreferencesKeys.YANDEX_AUTH, null);
    }

    public void setYandexAuth(String s) {
        editor.putString(SharedPreferencesKeys.YANDEX_AUTH, s);
        editor.commit();
    }

    public String getBoxAuth() {
        return preferences.getString(SharedPreferencesKeys.BOX_AUTH, null);
    }

    public void setBoxAuth(String s) {
        editor.putString(SharedPreferencesKeys.BOX_AUTH, s);
        editor.commit();
    }
}
