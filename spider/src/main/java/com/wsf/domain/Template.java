package com.wsf.domain;

import java.io.Serializable;
import java.util.HashMap;

public class Template implements Serializable {
    //该item对应的网址正则表达式
    private String urlReg;
    //该map对应 元素和页面种的css路径
    private HashMap<String,String> elementCss;

    //网站编码
    private String encode;

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public String getUrlReg() {
        return urlReg;
    }

    public void setUrlReg(String urlReg) {
        this.urlReg = urlReg;
    }

    public HashMap<String, String> getElementCss() {
        return elementCss;
    }

    public void setElementCss(HashMap<String, String> elementCss) {
        this.elementCss = elementCss;
    }
}
