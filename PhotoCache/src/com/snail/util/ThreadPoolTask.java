package com.snail.util;

public abstract class ThreadPoolTask implements Runnable {

    protected String url;

    public ThreadPoolTask(String url) {
        this.url = url;
    }

    public abstract void run();

    public String getURL() {
        return this.url;
    }
}
