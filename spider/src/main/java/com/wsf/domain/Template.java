package com.wsf.domain;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 该类为模板 ，urlReg表示该模板使用的网址 elementcss表示需要提取的元素css映射路径,charset表示网址的编码方式
 */
public class Template implements Serializable {
    //该item对应的网址正则表达式
    private String urlReg;
    //该map对应 元素和页面中的css路径
    private HashMap<String,String> elementPath;
    //网站编码
    private String charset;
    //对应的Item的全类名
    private String item;
    //对应的解析器全类名
    private String parseBean;
    //对应的存储器的全类名
    private String saveBean;

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getUrlReg() {
        return urlReg;
    }

    public void setUrlReg(String urlReg) {
        this.urlReg = urlReg;
    }

    public HashMap<String, String> getElementPath() {
        return elementPath;
    }

    public void setElementPath(HashMap<String, String> elementPath) {
        this.elementPath = elementPath;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getParseBean() {
        return parseBean;
    }

    public void setParseBean(String parseBean) {
        this.parseBean = parseBean;
    }

    public String getSaveBean() {
        return saveBean;
    }

    public void setSaveBean(String saveBean) {
        this.saveBean = saveBean;
    }
}
