package mree.cloud.music.player.common.ws.dropbox;

/**
 * Created by mree on 09.01.2017.
 */

public class Credentials{
    public String path;
    public boolean recursive;
    public boolean include_media_info;
    public boolean include_deleted;
    public boolean include_has_explicit_shared_members;

    public static Credentials getDefault(){
        Credentials c = new Credentials();
        c.recursive = false;
        c.include_media_info = true;
        c.include_deleted = false;
        c.include_has_explicit_shared_members = true;
        return c;
    }
}
