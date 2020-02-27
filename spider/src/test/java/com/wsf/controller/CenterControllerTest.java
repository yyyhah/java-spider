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
        template.setItem("com.wsf.domain.impl.BaseItem");
        lists.add(template);
        Template template1 = new Template();
        template1.setCharset("utf-8");
        template.setUrlReg("http://www.xbiquge\\.la/\\d+/\\d+/");
        template.setItem("com.wsf.domain.impl.IntItem");
        HashMap<String, String> temp2 = new HashMap<>();
        temp2.put("title","#info > h1");
        temp2.put("chapters","#list > dl > dd > a");
        lists.add(template1);
        return lists;
    }
    @Test
    public void testStartOneRequest() throws ClassNotFoundException {
        //加载匹配
        Class.forName("com.wsf.config.Configure");
        WriteToUrl write = new WriteToUrl();
        ReadFromHtml read = new ReadFromHtml(Configure.getReqBuffer());

        for (int i = 0; i < 30; i++) {
            ConcurrentLinkedQueue<String> inBuffer = new ConcurrentLinkedQueue<>();
            inBuffer.add("http://www.xbiquge.la/10/10489/");
            inBuffer.add("http://www.xbiquge.la/");
            write.write(inBuffer);
        }
        write.flush();//记住要刷新

        CenterControllerImpl center = new CenterControllerImpl(getTemplate());
        for (int i = 0; i < 30; i++) {
            center.startOneRequest();
        }
        center.destroy();

        while(read.hasNext()) {
            int i = 0;
            LinkedList<ConcurrentHashMap> mapList = read.readBatch();
            for (ConcurrentHashMap map : mapList) {
                Set<Map.Entry> set = map.entrySet();
                System.out.print(i+":{");
                for (Map.Entry<String,byte[]> entry : set) {
                    System.out.print(entry.getKey()+"="+entry.getValue().length+", ");
                }
                System.out.println("}");
            }
            System.out.println("-----------------------------------------------------");
        }
    }

}
