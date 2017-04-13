package mree.cloud.music.player.common.model.google;

import java.io.Serializable;

/**
 * Created by eercan on 18.11.2016.
 */

public class Permission implements Serializable {
    private String role;
    private String type;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
