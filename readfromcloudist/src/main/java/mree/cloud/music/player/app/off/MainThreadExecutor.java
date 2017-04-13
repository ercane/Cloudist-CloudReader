package mree.cloud.music.player.app.off;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by eercan on 07.03.2017.
 */

public class MainThreadExecutor extends ThreadPoolExecutor {

    private Handler handler;
    private List<Runnable> tasks;

    public MainThreadExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit
            unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        tasks = new ArrayList<>();
    }

    @Override
    public Future<?> submit(Runnable task) {
        tasks.add(task);
        return super.submit(task);
    }


}