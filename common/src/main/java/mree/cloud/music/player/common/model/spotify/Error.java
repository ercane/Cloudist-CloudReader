package mree.cloud.music.player.common.model.spotify;

import java.io.Serializable;

public class Error implements Serializable {

    private String description;
    private String type;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
