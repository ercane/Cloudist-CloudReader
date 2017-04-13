package mree.cloud.music.player.common.model.dropbox;

import java.io.Serializable;

/**
 * Created by eercan on 10.01.2017.
 */

public class Metadata implements Serializable {
    private String id;

    private String path_lower;

    private String rev;

    private String name;

    private String path_display;

    private String client_modified;

    private String size;

    private String server_modified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath_lower() {
        return path_lower;
    }

    public void setPath_lower(String path_lower) {
        this.path_lower = path_lower;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath_display() {
        return path_display;
    }

    public void setPath_display(String path_display) {
        this.path_display = path_display;
    }

    public String getClient_modified() {
        return client_modified;
    }

    public void setClient_modified(String client_modified) {
        this.client_modified = client_modified;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getServer_modified() {
        return server_modified;
    }

    public void setServer_modified(String server_modified) {
        this.server_modified = server_modified;
    }

    @Override
    public String toString() {
        return "[id = " + id + ", path_lower = " + path_lower + ", rev = " + rev + ", name = " +
                name + ", path_display = " + path_display + ", client_modified = " +
                client_modified + ", size = " + size + ", server_modified = " + server_modified +
                "]";
    }
}
