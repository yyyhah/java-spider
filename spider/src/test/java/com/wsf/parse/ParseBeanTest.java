package com.wsf.parse;

import com.wsf.controller.center.impl.CenterControllerImpl;
import com.wsf.domain.Item;
import com.wsf.domain.Template;
import com.wsf.domain.impl.BaseItem;
import com.wsf.factory.io.IOFactory;
import com.wsf.io.IReadFromPool;
import com.wsf.io.IWriteToPool;
import com.wsf.parse.manager.impl.ParseManager;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ParseBeanTest {
    @Before
    public void init() throws ClassNotFoundException {
        Class.forName("com.wsf.source.Source");
    }
    @Test
    public void testStartOnParse(){
        IReadFromPool readProxy = IOFactory.getParseReadConnect(40,false);
        IReadFromPool readProxy2 = IOFactory.getSaveReadConnect(40, false);
        IWriteToPool writeProxy = IOFactory.getSaveWriteConnect(false);

        ConcurrentLinkedQueue<String> list  = new ConcurrentLinkedQueue<>();
        list.add("http://www.xbiquge.la/");
        writeProxy.write(list);
        writeProxy.flush();//记住要刷新

        //创建模板
        Template template = new Template();
        template.setCharset("utf-8");
        template.setUrlReg("http://www.xbiquge.\\w\\w/");
        HashMap<String, String> temp = new HashMap<>();
        temp.put("atoms","#main > div:nth-child(4) > div.content");
        temp.put("atoms.title","h2");
        temp.put("atoms.chapterHref","div > dl > dt > a;href");
        temp.put("atoms.author","div > dl > dt > a");
        temp.put("atoms.books","ul > li > a");
        template.setElementCss(temp);
        ArrayList<Template> lists = new ArrayList<>();
        lists.add(template);




        CenterControllerImpl center = new CenterControllerImpl(lists);
        center.startOneRequest();
        center.destroy();

        ConcurrentHashMap<String,byte[]> map = (ConcurrentHashMap<String,byte[]>)readProxy.read();
        System.out.println("map:"+map);




        //将模板导入manager
        ParseManager manager = new ParseManager(lists);
        //开启一个ParseBean执行清洗任务
        ConcurrentHashMap<String, Item> result = manager.startOneParse(map);
        System.out.println(result);

    }
}
