package mree.cloud.music.player.common.model;

import mree.cloud.music.player.common.MarkedInfo;
import mree.cloud.music.player.common.ref.ScanStatus;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.auth.SourceState;

/**
 * Created by mree on 06.11.2015.
 */
public class SourceInfo extends MarkedInfo {
    private String userId;
    private String name;
    private SourceType type;
    private SourceState state;
    private String accessToken;
    private String refreshToken;
    private Long expiredIn;
    private Long scannedSong;
    private String scannedFolders;
    private ScanStatus scanStatus;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SourceType getType() {
        return type;
    }

    public void setType(SourceType type) {
        this.type = type;
    }

    public SourceState getState() {
        return state;
    }

    public void setState(SourceState state) {
        this.state = state;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpiredIn() {
        return expiredIn;
    }

    public void setExpiredIn(Long expiredIn) {
        this.expiredIn = expiredIn;
    }

    public Long getScannedSong() {
        return scannedSong;
    }

    public void setScannedSong(Long scannedSong) {
        this.scannedSong = scannedSong;
    }

    public String getScannedFolders() {
        return scannedFolders;
    }

    public void setScannedFolders(String scannedFolders) {
        this.scannedFolders = scannedFolders;
    }

    public ScanStatus getScanStatus() {
        return scanStatus;
    }

    public void setScanStatus(ScanStatus scanStatus) {
        this.scanStatus = scanStatus;
    }
}
