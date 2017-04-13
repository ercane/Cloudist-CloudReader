package mree.cloud.music.player.common.model.box;

/**
 * Created by eercan on 20.02.2017.
 */

public class Version {
    private String id;

    private String sha1;

    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
