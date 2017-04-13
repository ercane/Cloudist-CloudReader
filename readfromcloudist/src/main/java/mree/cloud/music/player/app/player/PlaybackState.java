package mree.cloud.music.player.app.player;

import mree.cloud.music.player.common.model.SongInfo;

/**
 * Created by eercan on 03.11.2016.
 */

public class PlaybackState {
    private SongInfo si;
    private PlaybackStatus status;
    private int duration;
    private int currentDuration;

    public PlaybackState(SongInfo si, PlaybackStatus status, int duration, int currentDuration) {
        this.si = si;
        this.status = status;
        this.duration = duration;
        this.currentDuration = currentDuration;
    }

    public SongInfo getSi() {
        return si;
    }

    public void setSi(SongInfo si) {
        this.si = si;
    }

    public PlaybackStatus getStatus() {
        return status;
    }

    public void setStatus(PlaybackStatus status) {
        this.status = status;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCurrentDuration() {
        return currentDuration;
    }

    public void setCurrentDuration(int currentDuration) {
        this.currentDuration = currentDuration;
    }
}
