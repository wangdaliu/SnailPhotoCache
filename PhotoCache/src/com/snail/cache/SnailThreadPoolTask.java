package com.snail.cache;

import android.os.Process;
import com.snail.util.FileUtils;
import com.snail.util.SnailCache;

import java.io.IOException;
import java.io.InputStream;

public class SnailThreadPoolTask extends ThreadPoolTask {

    private static final String TAG = "MyThreadPoolTask";

    private SnailCache mDiskLruCache;

    private CacheCallBack callback;

    private SnailCache.CacheType mCacheType;

    public SnailThreadPoolTask(String url, SnailCache mDiskLruCache, SnailCache.CacheType cacheType, CacheCallBack callback) {
        super(url);
        this.mDiskLruCache = mDiskLruCache;
        this.callback = callback;
        this.mCacheType = cacheType;
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
        Object ob;

        synchronized (mDiskLruCache) {
            ob = mDiskLruCache.get(url, mCacheType);
        }

        if (ob == null) {
            switch (mCacheType) {
                case INPUTSTREAM:
                    ob = FileUtils.getInputStreamFromURL(url);

                    try {
                        mDiskLruCache.put(url, ob);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case BITMAP:
                    // TODO

                    break;
                case STRING:

                    // TODO

                    break;

                default:
                    break;
            }
        }

        if (callback != null) {
            callback.onFinish(url, ob);
        }
    }

}
