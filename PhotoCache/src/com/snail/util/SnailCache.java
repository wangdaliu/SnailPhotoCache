package com.snail.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class SnailCache<V> {

    private final LruCache<String, V> mMemCache;
    private final SimpleDiskCache mDiskCache;

    public enum CacheType {
        INPUTSTREAM, BITMAP, STRING,
    }

    /**
     * Constructor for SnailCache. Use this constructor if only the first
     * level memory cache is needed.
     *
     * @param maxSizeMem
     */
    public SnailCache(int maxSizeMem) {
        mDiskCache = null;
        mMemCache = new LruCache<String, V>(maxSizeMem) {

            @Override
            protected void entryRemoved(boolean evicted, String key,
                                        V oldValue, V newValue) {
                wrapEntryRemoved(evicted, key, oldValue, newValue);
            }


            @Override
            protected int sizeOf(String key, V value) {
                return wrapSizeOf(key, value);
            }

        };
    }


    /**
     * Constructor for TwoLevelLruCache. Use this constructor if the second
     * level disk cache is to be enabled.
     *
     * @param directory   a writable directory for the L2 disk cache.
     * @param appVersion
     * @param maxSizeDisk the maximum number of bytes the L2 disk cache should use to
     *                    store.
     * @throws IOException
     */
    public SnailCache(File directory, int appVersion,
                      long maxSizeDisk) throws IOException {
        super();

        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 4;
        // 设置图片缓存大小为程序最大可用内存的1/4
        mMemCache = new LruCache<String, V>(cacheSize) {

            @Override
            protected void entryRemoved(boolean evicted, String key,
                                        V oldValue, V newValue) {
                wrapEntryRemoved(evicted, key, oldValue, newValue);
            }

            @Override
            protected int sizeOf(String key, V value) {
                return wrapSizeOf(key, value);
            }

        };
        mDiskCache = SimpleDiskCache.open(directory, appVersion, maxSizeDisk);
    }


    /**
     * Returns the value for {@code key} if it exists in the cache or can be
     * created by {@code #create(String)}.
     *
     * @param key
     * @return value
     */
    public final V get(String key, CacheType type) {
        V value = mMemCache.get(key);
        if (mDiskCache != null && value == null) {
            try {
                switch (type) {
                    case BITMAP:
                        value = (V) mDiskCache.getBitmap(key);
                        break;

                    case INPUTSTREAM:
                        value = (V) mDiskCache.getInputStream(key);
                        break;

                    case STRING:
                        value = (V) mDiskCache.getString(key);
                        break;

                    default:
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (value != null) {
                // write back to mem cache
                mMemCache.put(key, value);
            }
        }
        return value;
    }

    /**
     * Caches {@code newValue} for {@code key}.
     *
     * @param key
     * @param newValue
     * @return oldValue
     */
    public final void put(String key, V newValue, CacheType type) throws IOException {

        switch (type) {
            case BITMAP:
                mDiskCache.put(key, (InputStream) newValue);
                mMemCache.put(key, (V) mDiskCache.getBitmap(key));
                break;
            case INPUTSTREAM:
                mDiskCache.put(key, (InputStream) newValue);
                mMemCache.put(key, (V) mDiskCache.getInputStream(key));
                break;
            case STRING:
                mDiskCache.put(key, (String) newValue);
                mMemCache.put(key, (V) mDiskCache.getString(key));
                break;
            default:
                break;
        }
    }


    private int wrapSizeOf(String key, V value) {
        return sizeOf(key, value);
    }

    /**
     * Returns the size of the entry for {@code key} and {@code value} in
     * user-defined units. The default implementation returns 1 so that size is
     * the number of entries and max size is the maximum number of entries.
     * <p/>
     * <p/>
     * An entry's size must not change while it is in the cache.
     *
     * @param key
     * @param value
     * @return sizeOfEntry
     */
    protected int sizeOf(String key, V value) {
        return 1;
    }

    private void wrapEntryRemoved(boolean evicted, String key, V oldValue,
                                  V newValue) {
        if (!evicted) {
            removeFromDiskQuietly(key);
        }
    }

    private void removeFromDiskQuietly(String key) {
        if (mDiskCache == null) {
            return;
        }
        try {
            mDiskCache.remove(key);
        } catch (IOException e) {
            System.out.println("Unable to remove entry from disk cache. key: "
                    + key);
        }
    }

}
