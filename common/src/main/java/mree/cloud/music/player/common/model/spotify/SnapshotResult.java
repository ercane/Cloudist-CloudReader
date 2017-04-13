/**
 * Copyright (C) 2014 Spotify AB
 */
package mree.cloud.music.player.common.model.spotify;

import java.io.Serializable;

public class SnapshotResult implements Serializable {

    public String snapshotId;

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

}
