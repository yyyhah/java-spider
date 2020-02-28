package com.wsf.domain.impl;

import com.wsf.domain.Item;

/**
 * 空Item
 */
public class EmptyItem implements Item {
    private String url;
    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
