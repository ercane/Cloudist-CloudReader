package mree.cloud.music.player.common.model.dropbox;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mree on 09.01.2017.
 */

public class ListFolder implements Serializable{
    private List<Entry> entries;
    private String cursor;
    private boolean has_more;
    private String path;
    public List<Entry> getEntries(){
        return entries;
    }

    public void setEntries(List<Entry> entries){
        this.entries = entries;
    }

    public String getCursor(){
        return cursor;
    }

    public void setCursor(String cursor){
        this.cursor = cursor;
    }

    public boolean isHas_more(){
        return has_more;
    }

    public void setHas_more(boolean has_more){
        this.has_more = has_more;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
