package com.wsf.parse;

import com.wsf.controller.center.impl.CenterControllerImpl;
import com.wsf.domain.Item;
import com.wsf.domain.Template;
import com.wsf.factory.io.IOFactory;
import com.wsf.io.IReadFromPool;
import com.wsf.io.IWriteToPool;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
        template.setElementPath(temp);
        template.setParseBean("com.wsf.parse.bean.impl.HtmlParseBean");
        template.setItem("com.wsf.domain.impl.BaseItem");
        lists.add(template);


        Template template1 = new Template();
        template1.setCharset("utf-8");
        template1.setUrlReg("http://www.xbiquge.la/\\d+/\\d+/");
        template1.setItem("com.wsf.domain.impl.IntItem");
        HashMap<String, String> temp2 = new HashMap<>();
        temp2.put("title","#info > h1");
        temp2.put("chapters","#list > dl > dd > a");
        template.setParseBean("com.wsf.parse.bean.impl.HtmlParseBean");
        template1.setElementPath(temp2);
        lists.add(template1);
        return lists;
    }

    /*
    private List<Template> getTemplate(){
        ArrayList<Template> list = new ArrayList<>();
        Template template = new Template();
        template.setCharset("utf-8");
        template.setUrlReg("http://i\\d.hdslb.com/bfs/archive/.+?\\.jpg");
        template.setItem("com.wsf.domain.impl.ByteItem");
        template.setParseBean("com.wsf.parse.bean.impl.ByteParseBean");
        list.add(template);
        return list;
    }

     */
    @Test
    public void testStartOnParse(){
        IWriteToPool writeToUrl = IOFactory.getSaveWriteConnect(true);
        IReadFromPool readFromItem = IOFactory.getSaveReadConnect(40, true);
        IReadFromPool readFromUrl = IOFactory.getReqReadConnect(40, true);
        IWriteToPool writeToHtml = IOFactory.getReqWriteConnect(true);
        for (int i = 0; i <2; i++) {
            ConcurrentLinkedQueue<String> urls = new ConcurrentLinkedQueue<>();
            urls.add("http://www.xbiquge.la/");
            writeToUrl.write(urls);
        }


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
    public void read() throws IOException {
        IReadFromPool readFromItem = IOFactory.getSaveReadConnect(40, true);
        File file = new File("D:\\webSpider\\source\\images");

        while (readFromItem.hasNext()){
            ConcurrentHashMap<String,Item> read = (ConcurrentHashMap<String, Item>)readFromItem.read();
            System.out.println(read);
        }
    }
}
