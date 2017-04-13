package mree.cloud.music.player.common.model.spotify;

import java.io.Serializable;

public class Followers implements Serializable {

    private String href;
    private int total;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
