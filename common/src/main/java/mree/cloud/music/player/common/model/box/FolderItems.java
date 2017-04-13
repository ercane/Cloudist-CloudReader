package mree.cloud.music.player.common.model.box;

import java.io.Serializable;
import java.util.List;

/**
 * Created by eercan on 20.02.2017.
 */

public class FolderItems implements Serializable {
    private String limit;

    private List<Order> order;

    private List<Entry> entries;

    private String offset;

    private String total_count;

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> order) {
        this.order = order;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getTotal_count() {
        return total_count;
    }

    public void setTotal_count(String total_count) {
        this.total_count = total_count;
    }
}
