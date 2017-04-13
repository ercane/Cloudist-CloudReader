/*

 *
 * Project: mcys-server-common
 * Date Created: 28 Nis 2015
 * Created By: ykurt
 */
package mree.cloud.music.player.common;

import java.io.Serializable;

/**
 * @author ykurt
 */
@SuppressWarnings("serial")
public abstract class BaseInfo implements Serializable {

    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
