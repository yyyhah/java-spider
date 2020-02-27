package com.wsf.parse;

import com.wsf.controller.center.impl.CenterControllerImpl;
import com.wsf.domain.Template;
import com.wsf.factory.io.IOFactory;
import com.wsf.io.IReadFromPool;
import com.wsf.io.IWriteToPool;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ParseControllerTest {
    @Before
    public void init() throws ClassNotFoundException {
        Class.forName("com.wsf.config.Configure");
    }

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
        template.setElementCss(temp);
        template.setItem("com.wsf.domain.impl.BaseItem");
        lists.add(template);


        Template template1 = new Template();
        template1.setCharset("utf-8");
        template1.setUrlReg("http://www.xbiquge.la/\\d+/\\d+/");
        template1.setItem("com.wsf.domain.impl.IntItem");
        HashMap<String, String> temp2 = new HashMap<>();
        temp2.put("title","#info > h1");
        temp2.put("chapters","#list > dl > dd > a");
        template1.setElementCss(temp2);
        lists.add(template1);
        return lists;
    }

    @Test
    public void testStartOnParse(){
        IWriteToPool writeToUrl = IOFactory.getSaveWriteConnect(true);
        IReadFromPool readFromItem = IOFactory.getSaveReadConnect(40, true);
        IReadFromPool readFromUrl = IOFactory.getReqReadConnect(40, true);
        IWriteToPool writeToHtml = IOFactory.getReqWriteConnect(true);
        for (int i = 0; i < 10; i++) {
            ConcurrentLinkedQueue<String> urls = new ConcurrentLinkedQueue<>();
            urls.add("http://www.xbiquge.la/");
            urls.add("http://www.xbiquge.la/10/10489/");
            writeToUrl.write(urls);
        }
        writeToUrl.flush();


        List<Template> templates = getTemplate();

        CenterControllerImpl centerController = new CenterControllerImpl(templates);
        while(readFromUrl.hasNext()) {
            centerController.startOneRequest();
        }
        centerController.destroy();
    }

    @Test
    public void test(){
        System.out.println("https://www.bilibili.com/anime/?spm_id_from=333.6.b_7375626e6176.1".matches("https://www.bilibili.com/anime/\\?spm_id_from=333\\.6\\.b_7375626e6176\\.1"));
    }


    @Test
    public void execute(){
        IReadFromPool readFromHtml = IOFactory.getParseReadConnect(40, true);

        CenterControllerImpl centerController = new CenterControllerImpl(getTemplate());
        while(readFromHtml.hasNext()){
            centerController.startOneParse();
        }
        centerController.destroy();
    }
    @Test
    public void read(){
        IReadFromPool readFromItem = IOFactory.getSaveReadConnect(40, true);
        while(readFromItem.hasNext()){
            LinkedList linkedList = readFromItem.readBatch();
            System.out.println(linkedList);
        }
    }
}
