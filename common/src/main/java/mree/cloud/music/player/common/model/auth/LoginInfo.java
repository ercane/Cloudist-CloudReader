/*

 *
 * Project: mcys-server-common
 * Date Created: 6 May 2015
 * Created By: ykurt
 */
package mree.cloud.music.player.common.model.auth;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * @author ykurt
 */
@SuppressWarnings("serial")
public class LoginInfo implements Serializable {

    public Long id;
    public String fullName;
    public String userName;
    public String lastLoginIP;
    public Date lastLoginTime;
    public List<String> authorities;
    public Integer erroneousLoginAttempCount;
    public Integer accountStatus;
    public String additionalInfo;
    public String authToken;
    public Boolean systemUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getLastLoginIP() {
        return lastLoginIP;
    }

    public void setLastLoginIP(String lastLoginIP) {
        this.lastLoginIP = lastLoginIP;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public int getErroneousLoginAttempCount() {
        return erroneousLoginAttempCount;
    }

    public void setErroneousLoginAttempCount(Integer erroneousLoginAttempCount) {
        this.erroneousLoginAttempCount = erroneousLoginAttempCount;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Boolean isSystemUser() {
        return systemUser;
    }

    public void setSystemUser(Boolean systemUser) {
        this.systemUser = systemUser;
    }

}
