package mree.cloud.music.player.common.model;

import java.util.List;

import mree.cloud.music.player.common.MarkedInfo;
import mree.cloud.music.player.common.ref.audio.PlaylistType;

/**
 * Created by mree on 06.11.2015.
 */
public class PlaylistInfo extends MarkedInfo {
    private String name;
    private Integer count;
    private PlaylistType type;
    private int offlineStatus;
    private List<SongInfo> songs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public PlaylistType getType() {
        return type;
    }

    public void setType(PlaylistType type) {
        this.type = type;
    }

    public int getOfflineStatus() {
        return offlineStatus;
    }

    public void setOfflineStatus(int offlineStatus) {
        this.offlineStatus = offlineStatus;
    }

    public List<SongInfo> getSongs() {
        return songs;
    }

    public void setSongs(List<SongInfo> songs) {
        this.songs = songs;
    }
}
