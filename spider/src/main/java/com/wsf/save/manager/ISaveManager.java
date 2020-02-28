package com.wsf.save.manager;

import com.wsf.domain.Item;
import com.wsf.domain.Template;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface ISaveManager {
    /**
     * 寻找匹配的template
     * @param url
     * @return
     */
    Template findBean(String url);

    /**
     * 保存一个item
     * @param inBuffer
     * @return
     * @throws Exception
     */
    ConcurrentLinkedQueue<String> startOneSave(ConcurrentHashMap<String, Item> inBuffer) throws Exception;
}
