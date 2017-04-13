/*

 *
 * Project: mcys-server-common
 * Date Created: 14 Nis 2015
 * Created By: ykurt
 */
package mree.cloud.music.player.common.model.auth;

import java.util.Date;

import mree.cloud.music.player.common.MarkedInfo;
import mree.cloud.music.player.common.ref.auth.AccountStatus;

/**
 * @author ykurt
 *
 */
@SuppressWarnings("serial")
public class AccountInfo extends MarkedInfo {

    public String fullName;
    public String password;
    public String emailAddress;
    public AccountStatus status;
    public Date expirationDate;
    private String token;

    public AccountInfo() {
        // Intentionally left blank.
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
