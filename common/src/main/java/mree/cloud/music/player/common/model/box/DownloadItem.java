package mree.cloud.music.player.common.model.box;

import java.io.InputStream;

/**
 * Created by eercan on 08.03.2017.
 */

public class DownloadItem {
    private InputStream inputStream;
    private long contentLength;

    public DownloadItem() {
    }

    public DownloadItem(InputStream inputStream, long contentLength) {
        this.inputStream = inputStream;
        this.contentLength = contentLength;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }
}
