package mree.cloud.music.player.common.model.box;

import java.util.List;

import mree.cloud.music.player.common.MarkedInfo;

/**
 * Created by eercan on 20.02.2017.
 */

public class SearchResult extends MarkedInfo {
    private int limit;

    private List<File> entries;

    private int offset;

    private int total_count;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<File> getEntries() {
        return entries;
    }

    public void setEntries(List<File> entries) {
        this.entries = entries;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }
}
