package com.wsf.source;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 资源池
 */
public class Source {
    //url池
    private static ConcurrentLinkedQueue<ConcurrentLinkedQueue> urlBuffer;
    //html池
    private static ConcurrentLinkedQueue<ConcurrentHashMap> htmlBuffer;
    //item池
    private static ConcurrentLinkedQueue<ConcurrentHashMap> itemBuffer;

    //初始化资源池
    static {
        urlBuffer = new ConcurrentLinkedQueue<>();
        htmlBuffer = new ConcurrentLinkedQueue<>();
        itemBuffer = new ConcurrentLinkedQueue<>();
    }

    public static ConcurrentLinkedQueue<ConcurrentLinkedQueue> getUrlBuffer() {
        return urlBuffer;
    }

    public static void setUrlBuffer(ConcurrentLinkedQueue<ConcurrentLinkedQueue> urlBuffer) {
        Source.urlBuffer = urlBuffer;
    }

    public static ConcurrentLinkedQueue<ConcurrentHashMap> getHtmlBuffer() {
        return htmlBuffer;
    }

    public static void setHtmlBuffer(ConcurrentLinkedQueue<ConcurrentHashMap> htmlBuffer) {
        Source.htmlBuffer = htmlBuffer;
    }

    public static ConcurrentLinkedQueue<ConcurrentHashMap> getItemBuffer() {
        return itemBuffer;
    }

    public static void setItemBuffer(ConcurrentLinkedQueue<ConcurrentHashMap> itemBuffer) {
        Source.itemBuffer = itemBuffer;
    }
}
