package mree.cloud.music.player.common.model.google;

import java.util.List;

import mree.cloud.music.player.common.MarkedInfo;

/**
 * Created by eercan on 18.11.2016.
 */

public class FileList extends MarkedInfo {
    private String kind;
    private List<File> files;
    private String nextPageToken;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }
}
