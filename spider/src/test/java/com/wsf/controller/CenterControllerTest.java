package com.wsf.controller;

import com.wsf.config.Configure;
import com.wsf.controller.center.impl.CenterControllerImpl;
import com.wsf.domain.Template;
import com.wsf.io.impl.ReadFromHtml;
import com.wsf.io.impl.WriteToUrl;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CenterControllerTest {
    private List<Template> getTemplate(){
        ArrayList<Template> lists = new ArrayList<>();
        //创建模板
        Template template = new Template();
        template.setCharset("utf-8");
        template.setUrlReg("http://www.xbiquge.la/");
        HashMap<String, String> temp = new HashMap<>();
        temp.put("atoms","#main > div:nth-child(4) > div.content");
        temp.put("atoms.title","h2");
        temp.put("atoms.chapterHref","div > dl > dt > a;href");
        temp.put("atoms.author","div > dl > dt > a");
        temp.put("atoms.books","ul > li > a");
        template.setElementPath(temp);
        template.setParseBean("com.wsf.parse.bean.impl.HtmlParseBean");
        template.setItem("com.wsf.domain.impl.BaseItem");
        template.setSaveBean("com.wsf.save.bean.impl.DBSaveBean");
        lists.add(template);


        Template template1 = new Template();
        template1.setCharset("utf-8");
        template1.setUrlReg("http://www.xbiquge.la/\\d+/\\d+/");
        template1.setItem("com.wsf.domain.impl.IntItem");
        HashMap<String, String> temp2 = new HashMap<>();
        temp2.put("title","#info > h1");
        temp2.put("chapters","#list > dl > dd > a");
        template1.setParseBean("com.wsf.parse.bean.impl.HtmlParseBean");
        template1.setSaveBean("com.wsf.save.bean.impl.DBSaveBean2");
        template1.setElementPath(temp2);
        lists.add(template1);
        return lists;
    }
    @Test
    public void testStartOneRequest() throws ClassNotFoundException {
        //加载匹配
        Class.forName("com.wsf.config.Configure");
        WriteToUrl write = new WriteToUrl();
        //写入初始网址
        for (int i = 0; i < 1; i++) {
            ConcurrentLinkedQueue<String> inBuffer = new ConcurrentLinkedQueue<>();
            inBuffer.add("http://www.xbiquge.la/");
            write.write(inBuffer);
        }

        CenterControllerImpl center = new CenterControllerImpl(getTemplate());
        center.start();
        //center.destroy();
    }

}
