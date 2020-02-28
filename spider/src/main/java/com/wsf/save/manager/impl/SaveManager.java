package com.wsf.save.manager.impl;

import com.wsf.domain.Item;
import com.wsf.domain.Template;
import com.wsf.domain.impl.EmptyItem;
import com.wsf.parse.manager.impl.ParseManager;
import com.wsf.save.bean.ISaveBean;
import com.wsf.save.manager.ISaveManager;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@SuppressWarnings("all")
public class SaveManager implements ISaveManager {
    //映射模板
    private static List<Template> templates = null;
    private static Logger logger = Logger.getLogger(ParseManager.class);
    public SaveManager(List<Template> templates) {
        this.templates = templates;
    }

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
    public ConcurrentLinkedQueue<String> startOneSave(ConcurrentHashMap<String, Item> inBuffer) throws Exception{
        ConcurrentLinkedQueue<String> urls = new ConcurrentLinkedQueue<>();
        for (Map.Entry<String, Item> entry : inBuffer.entrySet()) {
            //如果值为EmptyItem表示请求失败的，将重新请求
            if(entry.getValue() instanceof EmptyItem){
                urls.add(entry.getKey());
                continue;
            }
            Template template = findBean(entry.getKey());
            if(template == null){
                logger.warn("没有找到对应模板，请确认 "+entry.getKey()+" 的正则表达式是否正确！注意转义字符");
                continue;
            }else {
                ISaveBean saveBean = (ISaveBean) Class.forName(template.getSaveBean()).getConstructor(Template.class).newInstance(template);
                ConcurrentLinkedQueue<String> ret = saveBean.start(entry.getValue());
                if(ret!=null) {
                    urls.addAll(ret);
                }
            }
        }
        return urls;
    }
}
