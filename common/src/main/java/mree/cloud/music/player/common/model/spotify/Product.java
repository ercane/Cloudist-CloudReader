package mree.cloud.music.player.common.model.spotify;

import java.io.Serializable;

public enum Product implements Serializable {
    PREMIUM("premium"),
    FREE("free"),
    OPEN("open"),
    DAYPASS("daypass");

    public final String type;

    Product(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
