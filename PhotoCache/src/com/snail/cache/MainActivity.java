package com.snail.cache;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.LruCache;
import android.widget.ImageView;
import com.snail.util.FileUtils;
import com.snail.util.SimpleDiskCache;
import com.snail.util.SnailCache;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends Activity {

    private static final String URL1 = "http://d.hiphotos.baidu.com/image/w%3D2048/sign=b0f07f7141a98226b8c12c27bebab801/03087bf40ad162d9a9e79f1813dfa9ec8a13cdad.jpg";


    SimpleDiskCache diskCache = null;
    ImageView imageView;
    private ThreadPoolManager poolManager;
    SnailCache cache = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        poolManager = new ThreadPoolManager(ThreadPoolManager.TYPE_LIFO, 5);
        imageView = (ImageView) findViewById(R.id.image);


        try {
            cache = new SnailCache(getCacheDir(), 1, 10 * 1024 * 1024);

            poolManager.start();
            poolManager.addAsyncTask(new SnailThreadPoolTask(URL1, cache, SnailCache.CacheType.INPUTSTREAM, new CacheCallBack() {
                @Override
                public void onFinish(String key, Object ob) {
                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap((Bitmap) cache.get(URL1, SnailCache.CacheType.BITMAP));
                        }
                    });
                }
            }));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
