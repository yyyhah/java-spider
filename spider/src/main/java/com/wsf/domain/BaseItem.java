package com.wsf.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 该类保存页面中提取信息的规则,基本通用模板，提取元素之间不会有关联。如果需要元素之间有关联需要自己定义Item
 */
public class BaseItem implements Serializable {
    //从哪个网址获取的资源
    private String url;
    //获取的html元素 和 其信息文本
    private HashMap<String, ArrayList<String>> items;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public HashMap<String, ArrayList<String>> getItems() {
        return items;
    }

    public void setItems(HashMap<String, ArrayList<String>> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "BaseItem{" +
                "url='" + url + '\'' +
                ", items=" + items +
                '}';
    }
}
