package com.snail.util;

import android.widget.ImageView;

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

public class FileUtils {

    private static final MessageDigest DIGEST;

    public static final String CHARSET_UTF8 = "UTF-8";

    static {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            digest = null;
        }
        DIGEST = digest;
    }

    public static String getKey(final String uri) {
        final byte[] input;
        try {
            input = uri.getBytes(CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            return null;
        }

        final byte[] digested;
        synchronized (DIGEST) {
            DIGEST.reset();
            digested = DIGEST.digest(input);
        }

        final String hashed = new BigInteger(1, digested).toString(16);
        final int padding = 40 - hashed.length();
        if (padding == 0)
            return hashed;

        final char[] zeros = new char[padding];
        Arrays.fill(zeros, '0');
        return new StringBuilder(40).append(zeros).append(hashed).toString();
    }


    public static InputStream getInputStreamFromURL(String urlStr) {
        InputStream inputStream = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            inputStream = urlConn.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputStream;
    }
}
