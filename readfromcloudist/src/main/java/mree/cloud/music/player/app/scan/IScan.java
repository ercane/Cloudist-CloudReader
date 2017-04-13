package mree.cloud.music.player.app.scan;

/**
 * Created by eercan on 02.09.2016.
 */
public interface IScan{

    void start();

    void stop() throws NullPointerException;

    void resume() throws InterruptedException;

    boolean isRunning();

    String getAccountId();

    void notifyAccountSetActivity();

}
