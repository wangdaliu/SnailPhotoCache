package com.snail.util;

import android.os.Process;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;

public class SnailThreadPoolTask extends ThreadPoolTask {

    private static final String TAG = "MyThreadPoolTask";

    private SnailCache mCache;

    private CacheCallBack callback;

    private SnailCache.CacheType mCacheType;

    public SnailThreadPoolTask(String url, SnailCache cache,
                               SnailCache.CacheType cacheType, CacheCallBack callback) {
        super(url);
        this.mCache = cache;
        this.callback = callback;
        this.mCacheType = cacheType;
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
        Object ob;

        if (TextUtils.isEmpty(url)) {
            return;
        }

        synchronized (mCache) {
            ob = mCache.get(url, mCacheType);
        }


        if (ob == null) {
            switch (mCacheType) {
                case INPUTSTREAM:
                    InputStream is = FileUtils.getInputStreamFromURL(url);
                    try {
                        if (null != is) {
                            mCache.put(url, is, SnailCache.CacheType.INPUTSTREAM);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case BITMAP:
                    InputStream is1 = FileUtils.getInputStreamFromURL(url);
                    try {
                        if (null != is1) {
                            mCache.put(url, is1, SnailCache.CacheType.BITMAP);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case STRING:

                    break;
                default:
                    break;
            }
        }

        if (callback != null) {
            callback.onFinish(url);
        }
    }

}
