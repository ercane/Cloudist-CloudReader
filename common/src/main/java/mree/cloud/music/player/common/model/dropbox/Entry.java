package mree.cloud.music.player.common.model.dropbox;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mree on 09.01.2017.
 */

public class Entry implements Serializable{
    @SerializedName (".tag")
    private String tag;

    @SerializedName ("name")
    private String name;

    @SerializedName ("id")
    private String id;

    @SerializedName ("path_lower")
    private String pathLower;

    @SerializedName ("path_display")
    private String pathDisplay;

    @SerializedName ("client_modified")
    private String clientModifiedDate;

    @SerializedName ("server_modified")
    private String serverModifiedDate;

    @SerializedName ("rev")
    private String rev;

    @SerializedName ("size")
    private Long size;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPathLower() {
        return pathLower;
    }

    public void setPathLower(String pathLower) {
        this.pathLower = pathLower;
    }

    public String getPathDisplay() {
        return pathDisplay;
    }

    public void setPathDisplay(String pathDisplay) {
        this.pathDisplay = pathDisplay;
    }

    public String getClientModifiedDate() {
        return clientModifiedDate;
    }

    public void setClientModifiedDate(String clientModifiedDate) {
        this.clientModifiedDate = clientModifiedDate;
    }

    public String getServerModifiedDate() {
        return serverModifiedDate;
    }

    public void setServerModifiedDate(String serverModifiedDate) {
        this.serverModifiedDate = serverModifiedDate;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
