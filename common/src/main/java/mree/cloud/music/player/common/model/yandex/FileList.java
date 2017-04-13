package mree.cloud.music.player.common.model.yandex;

import java.util.List;

/**
 * Created by eercan on 25.01.2017.
 */

public class FileList {
    private int limit;

    private List<Item> items;

    private int offset;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
