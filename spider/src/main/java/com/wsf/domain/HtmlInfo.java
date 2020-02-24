package com.wsf.domain;

public class HtmlInfo {
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
