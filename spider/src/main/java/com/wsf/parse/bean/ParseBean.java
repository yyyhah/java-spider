package com.wsf.parse.bean;


import com.wsf.domain.BaseItem;
import com.wsf.domain.Template;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParseBean {
    private Template template;
    private byte[] htmlBytes;
    private static Logger logger = Logger.getLogger(ParseBean.class);
    public ParseBean(Template template,byte[] htmlBytes) {
        this.template = template;
        this.htmlBytes = htmlBytes;
    }

    private ArrayList<String> parseCssPath(Document document,String url,String key,String cssPath){
        String[] split = cssPath.split(";");
        //如果路径中有;则将:后的内容视为属性
        Elements element = document.select(split[0]);
        if(element!=null) {
            ArrayList<String> select = new ArrayList<String>();
            if(split.length==1){
                element.stream().forEach(element1->select.add(element1.text()));
            }else if(split.length==2){
                element.stream().forEach(element1 -> select.add(element1.attr(split[1])));
            }else{
                logger.warn("css书写路径错误请检查！");
            }
            return select;
        }else{
            logger.warn("在网址: "+url+" 中 "+key+" 元素为空");
            return null;
        }
    }
    /**
     * 解析网页的源码，获取信息封装到item当中
     * @param url 网站得到网址
     * @return
     */
    public BaseItem start(String url){
        String html = null;
        BaseItem item = new BaseItem();
        try {
            html = new String(htmlBytes,template.getEncode());
            Document document = Jsoup.parse(html);
            HashMap<String, String> elementCss = template.getElementCss();
            HashMap<String,ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
            for (Map.Entry<String, String> entry : elementCss.entrySet()) {
                ArrayList<String> list = parseCssPath(document, url, entry.getKey(), entry.getValue());
                map.put(entry.getKey(),list);
            }
            item.setUrl(url);
            item.setItems(map);
            return item;
        } catch (UnsupportedEncodingException e) {
            logger.error("文件编码错误");
        }
        return null;
    }
}
