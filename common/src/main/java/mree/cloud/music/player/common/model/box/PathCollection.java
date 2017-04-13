package mree.cloud.music.player.common.model.box;

import java.util.List;

/**
 * Created by eercan on 20.02.2017.
 */

public class PathCollection {
    private List<Entry> entries;

    private String total_count;

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public String getTotal_count() {
        return total_count;
    }

    public void setTotal_count(String total_count) {
        this.total_count = total_count;
    }
}
