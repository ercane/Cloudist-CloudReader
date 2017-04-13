package mree.cloud.music.player.common.model;

import mree.cloud.music.player.common.MarkedInfo;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.audio.AudioStatus;

/**
 * Created by mree on 06.11.2015.
 */
public class SongInfo extends MarkedInfo {

    private String accountId;
    private SourceType sourceType;
    private String filename;
    private String thumbnail;
    private String sourceUrl;
    private String shareUrl;
    private String path;
    private String album;
    private String albumArtist;
    private String artist;
    private Long bitrate;
    private String composers;
    private String copyright;
    private Integer disc;
    private Integer discCount;
    private String downloadUrl;
    private Long duration;
    private String genre;
    private Boolean hasDrm;
    private Boolean isVariableBitrate;
    private String title;
    private Long track;
    private Long trackCount;
    private Long year;
    private String fileName;
    private AudioStatus status;


    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Long getBitrate() {
        return bitrate;
    }

    public void setBitrate(Long bitrate) {
        this.bitrate = bitrate;
    }

    public String getComposers() {
        return composers;
    }

    public void setComposers(String composers) {
        this.composers = composers;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public Integer getDisc() {
        return disc;
    }

    public void setDisc(Integer disc) {
        this.disc = disc;
    }

    public Integer getDiscCount() {
        return discCount;
    }

    public void setDiscCount(Integer discCount) {
        this.discCount = discCount;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Boolean getHasDrm() {
        return hasDrm;
    }

    public void setHasDrm(Boolean hasDrm) {
        this.hasDrm = hasDrm;
    }

    public Boolean getIsVariableBitrate() {
        return isVariableBitrate;
    }

    public void setIsVariableBitrate(Boolean isVariableBitrate) {
        this.isVariableBitrate = isVariableBitrate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTrack() {
        return track;
    }

    public void setTrack(Long track) {
        this.track = track;
    }

    public Long getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(Long trackCount) {
        this.trackCount = trackCount;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Boolean getVariableBitrate() {
        return isVariableBitrate;
    }

    public void setVariableBitrate(Boolean variableBitrate) {
        isVariableBitrate = variableBitrate;
    }

    public AudioStatus getStatus() {
        return status;
    }

    public void setStatus(AudioStatus status) {
        this.status = status;
    }
}
