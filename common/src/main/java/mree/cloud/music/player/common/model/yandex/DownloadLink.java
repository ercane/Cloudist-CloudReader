package mree.cloud.music.player.common.model.yandex;

/**
 * Created by eercan on 25.01.2017.
 */

public class DownloadLink {
    private String href;
    private String method;
    private String templated;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTemplated() {
        return templated;
    }

    public void setTemplated(String templated) {
        this.templated = templated;
    }
}
