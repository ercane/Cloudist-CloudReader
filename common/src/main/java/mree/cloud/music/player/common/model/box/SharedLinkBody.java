package mree.cloud.music.player.common.model.box;

/**
 * Created by eercan on 20.02.2017.
 */

public class SharedLinkBody {
    private SharedLink shared_link;

    public SharedLinkBody(SharedLink shared_link) {
        this.shared_link = shared_link;
    }

    public SharedLink getShared_link() {
        return shared_link;
    }

    public void setShared_link(SharedLink shared_link) {
        this.shared_link = shared_link;
    }
}
