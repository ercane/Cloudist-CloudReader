package mree.cloud.music.player.common.model.spotify;

import java.io.Serializable;

public class FeaturedPlaylists implements Serializable {

    private String message;
    private Page<SimplePlaylist> playlists;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Page<SimplePlaylist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(Page<SimplePlaylist> playlists) {
        this.playlists = playlists;
    }
}
