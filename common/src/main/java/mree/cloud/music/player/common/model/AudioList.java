package mree.cloud.music.player.common.model;

import java.util.List;

import mree.cloud.music.player.common.MarkedInfo;

/**
 * Created by mree on 28.02.2016.
 */
public class AudioList extends MarkedInfo {

    private List<SongInfo> list;

    public AudioList() {
    }

    public List<SongInfo> getList() {
        return list;
    }

    public void setList(List<SongInfo> list) {
        this.list = list;
    }
}
