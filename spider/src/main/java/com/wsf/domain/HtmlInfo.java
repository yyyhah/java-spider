package com.wsf.domain;

import java.io.Serializable;

public class HtmlInfo implements Serializable {
    private String charset;

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    @Override
    public String toString() {
        return "HtmlInfo{" +
                "charset='" + charset + '\'' +
                '}';
    }
}
