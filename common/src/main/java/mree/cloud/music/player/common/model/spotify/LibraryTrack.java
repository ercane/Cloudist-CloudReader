/**
 * Copyright (C) 2014 Spotify AB
 */
package mree.cloud.music.player.common.model.spotify;

import java.io.Serializable;
import java.util.Date;

public class LibraryTrack implements Serializable {

    private Date addedAt;
    private Track track;

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }
}
