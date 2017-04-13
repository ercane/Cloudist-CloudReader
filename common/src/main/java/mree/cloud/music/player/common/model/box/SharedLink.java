package mree.cloud.music.player.common.model.box;


/**
 * Created by eercan on 20.02.2017.
 */

public class SharedLink {
    private String is_password_enabled;

    private String preview_count;

    private String unshared_at;

    private String vanity_url;

    private String download_url;

    private Permissions permissions;

    private String download_count;

    private String access;

    private String url;

    public String getIs_password_enabled() {
        return is_password_enabled;
    }

    public void setIs_password_enabled(String is_password_enabled) {
        this.is_password_enabled = is_password_enabled;
    }

    public String getPreview_count() {
        return preview_count;
    }

    public void setPreview_count(String preview_count) {
        this.preview_count = preview_count;
    }

    public String getUnshared_at() {
        return unshared_at;
    }

    public void setUnshared_at(String unshared_at) {
        this.unshared_at = unshared_at;
    }

    public String getVanity_url() {
        return vanity_url;
    }

    public void setVanity_url(String vanity_url) {
        this.vanity_url = vanity_url;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    public String getDownload_count() {
        return download_count;
    }

    public void setDownload_count(String download_count) {
        this.download_count = download_count;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
