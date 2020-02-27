package com.wsf.domain.impl;

import com.wsf.domain.Item;

/**
 * 封装byte类型数据的Item
 */
public class ByteItem implements Item {
    private String url;
    private byte[] bytes;

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getUrl() {
        return url;
    }
}
