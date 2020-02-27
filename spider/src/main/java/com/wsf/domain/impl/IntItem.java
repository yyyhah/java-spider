package com.wsf.domain.impl;

import com.wsf.domain.Item;

import java.util.ArrayList;

public class IntItem implements Item {
    private String url;
    private String title;
    private ArrayList<String> chapters;

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "IntItem{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", chapters=" + chapters +
                '}';
    }
}
