package mree.cloud.music.player.common.model.spotify;

import java.io.Serializable;

public class NewReleases implements Serializable {
    private Page<SimpleAlbum> albums;

    public Page<SimpleAlbum> getAlbums() {
        return albums;
    }

    public void setAlbums(Page<SimpleAlbum> albums) {
        this.albums = albums;
    }
}
