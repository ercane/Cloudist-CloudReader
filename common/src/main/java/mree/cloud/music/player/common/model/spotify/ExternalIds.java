package mree.cloud.music.player.common.model.spotify;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ExternalIds implements Serializable {

    private final Map<String, String> externalIds = new HashMap<String, String>();

    public Map<String, String> getExternalIds() {
        return externalIds;
    }

}
