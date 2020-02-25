package com.wsf.parse.manager.impl;

import com.wsf.domain.BaseItem;
import com.wsf.domain.Template;
import com.wsf.parse.bean.ParseBean;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class ParseManager {
    //映射模板，String为网址的正则匹配字符串 Item为页面种需要提取的元素,通过这个映射规则知道要找哪个ParseBean解析
    private static ArrayList<Template> templates = null;
    private static Logger logger = Logger.getLogger(ParseManager.class);
    public ParseManager(ArrayList<Template> templates) {
        this.templates = templates;
    }

    /**
     * 解析出该网址需要创建哪种ParseBean处理器处理
     */
    public Template findBean(String url){
        for (Template template : templates) {
            String urlReg = template.getUrlReg();
            if(urlReg==null){
                logger.warn("存在一个template模板网址正则为空");
            }else {
                if (url.matches(urlReg)){
                    return template;
                }
            }
        }
        return null;
    }

    /**
     * 执行一条解析语句请求
     * @param inBuffer
     */
    public ConcurrentHashMap<String, BaseItem> startOneParse(ConcurrentHashMap<String,byte[]> inBuffer){
        ConcurrentHashMap<String, BaseItem> map = new ConcurrentHashMap<String, BaseItem>();
        Set<Map.Entry<String, byte[]>> entries = inBuffer.entrySet();
        for (Map.Entry<String, byte[]> entry : entries) {
            Template template = findBean(entry.getKey());
            if(template == null){
                logger.warn("没有找到对应模板，请确认正则表达式是否正确！");
                break;
            }else {
                BaseItem item = new ParseBean(template, entry.getValue()).start(entry.getKey());
                map.put(entry.getKey(),item);
            }
        }
        return map;
    }

}
