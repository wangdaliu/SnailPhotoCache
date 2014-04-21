package com.snail.util;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {

    private static final String TAG = "ThreadPoolManager";

    private int poolSize;
    private static final int MIN_POOL_SIZE = 1;
    private static final int MAX_POOL_SIZE = 10;

    private ExecutorService threadPool;

    private LinkedList<ThreadPoolTask> asyncTasks;

    private int type;
    public static final int TYPE_FIFO = 0;
    public static final int TYPE_LIFO = 1;

    private Thread poolThread;
    private static final int SLEEP_TIME = 200;

    private SnailCache cache;

    private static ThreadPoolManager poolManager;

    private Context mContext;

    public static synchronized ThreadPoolManager getInstance(Context context) {
        if (null == poolManager) {
            poolManager = new ThreadPoolManager(ThreadPoolManager.TYPE_LIFO, 5, context);
        }
        return poolManager;
    }

    public void loadIcon(final ImageView imageView, final String url) {
        addAsyncTask(new SnailThreadPoolTask(url, cache, SnailCache.CacheType.BITMAP, new CacheCallBack() {
            @Override
            public void onFinish(String key) {
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        SimpleDiskCache.BitmapEntry bitmapEntry = (SimpleDiskCache.BitmapEntry) cache.get(url, SnailCache.CacheType.BITMAP);
                        if (null != bitmapEntry) {
                            imageView.setImageBitmap(bitmapEntry.getBitmap());
                        }
                    }
                });
            }
        }));
    }

    private ThreadPoolManager(int type, int poolSize, Context context) {
        this.type = (type == TYPE_FIFO) ? TYPE_FIFO : TYPE_LIFO;

        if (poolSize < MIN_POOL_SIZE) poolSize = MIN_POOL_SIZE;
        if (poolSize > MAX_POOL_SIZE) poolSize = MAX_POOL_SIZE;
        this.poolSize = poolSize;

        threadPool = Executors.newFixedThreadPool(this.poolSize);

        asyncTasks = new LinkedList<ThreadPoolTask>();

        try {
            cache = new SnailCache(context.getCacheDir(), 1, 10 * 1024 * 1024);
            poolManager.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAsyncTask(ThreadPoolTask task) {
        synchronized (asyncTasks) {
            Log.i(TAG, "add task: " + task.getURL());
            asyncTasks.addLast(task);
        }
    }

    private ThreadPoolTask getAsyncTask() {
        synchronized (asyncTasks) {
            if (asyncTasks.size() > 0) {
                ThreadPoolTask task = (this.type == TYPE_FIFO) ?
                        asyncTasks.removeFirst() : asyncTasks.removeLast();
                Log.i(TAG, "remove task: " + task.getURL());
                return task;
            }
        }
        return null;
    }

    public void start() {
        if (poolThread == null) {
            poolThread = new Thread(new PoolRunnable());
            poolThread.start();
        }
    }

    public void stop() {
        poolThread.interrupt();
        poolThread = null;
    }


    private class PoolRunnable implements Runnable {

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    ThreadPoolTask task = getAsyncTask();
                    if (task == null) {
                        try {
                            Thread.sleep(SLEEP_TIME);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        continue;
                    }
                    threadPool.execute(task);
                }
            } finally {
                threadPool.shutdown();
            }
        }

    }
}
