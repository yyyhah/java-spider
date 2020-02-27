package com.wsf.parse.manager.impl;

import com.wsf.domain.Item;
import com.wsf.domain.Template;
import com.wsf.domain.impl.BaseItem;
import com.wsf.parse.bean.ParseBean;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class ParseManager {
    //映射模板，String为网址的正则匹配字符串 Item为页面种需要提取的元素,通过这个映射规则知道要找哪个ParseBean解析
    private static List<Template> templates = null;
    private static Logger logger = Logger.getLogger(ParseManager.class);
    public ParseManager(List<Template> templates) {
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
    public ConcurrentHashMap<String, Item> startOneParse(ConcurrentHashMap<String,byte[]> inBuffer){
        ConcurrentHashMap<String, Item> map = new ConcurrentHashMap<String, Item>();
        Set<Map.Entry<String, byte[]>> entries = inBuffer.entrySet();
        for (Map.Entry<String, byte[]> entry : entries) {
            Template template = findBean(entry.getKey());
            if(template == null){
                logger.warn("没有找到对应模板，请确认 "+entry.getKey()+" 的正则表达式是否正确！注意转义字符");
                continue;
            }else {
                Item item = null;
                try {
                    item = new ParseBean(template).start(entry.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("html解析封装出错！");
                }
                item.setUrl(entry.getKey());
                map.put(template.getItem(),item);
            }
        }
        return map;
    }

}
