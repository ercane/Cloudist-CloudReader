/*

 *
 * Project: mcys-server-common
 * Date Created: 28 Nis 2015
 * Created By: ykurt
 */
package mree.cloud.music.player.common;

import java.util.Date;

/**
 * @author ykurt
 *
 */
@SuppressWarnings("serial")
public abstract class MarkedInfo extends BaseInfo {

    public Long createdBy;
    public Long updatedBy;
    public Date createdDate;
    public Date updatedDate;

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

}
