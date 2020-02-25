package com.wsf.parse;

import com.wsf.controller.center.impl.CenterControllerImpl;
import com.wsf.domain.BaseItem;
import com.wsf.domain.Template;
import com.wsf.factory.io.IOFactory;
import com.wsf.io.IReadFromPool;
import com.wsf.io.IWriteToPool;
import com.wsf.parse.manager.impl.ParseManager;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
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
    public void testStartOnParse() throws UnsupportedEncodingException {
        IReadFromPool readProxy = IOFactory.getParseReadConnect(40,false);
        IReadFromPool readProxy2 = IOFactory.getSaveReadConnect(40, false);
        IWriteToPool writeProxy = IOFactory.getSaveWriteConnect(false);

        ConcurrentLinkedQueue<String> list  = new ConcurrentLinkedQueue<>();
        list.add("http://www.xbiquge.la/");
        writeProxy.write(list);
        writeProxy.flush();//记住要刷新

        CenterControllerImpl center = new CenterControllerImpl();
        center.startOneRequest();
        center.destroy();


        ConcurrentHashMap<String,byte[]> map = (ConcurrentHashMap<String,byte[]>)readProxy.read();
        System.out.println("map:"+map);

        Template template = new Template();
        template.setEncode("utf-8");
        template.setUrlReg("http://www.xbiquge.\\w\\w/");
        HashMap<String, String> temp = new HashMap<>();
        temp.put("title","#newscontent > div.l > ul > li > span.s2 > a");
        temp.put("part","#newscontent > div.l > ul > li > span.s3 > a;href");
        temp.put("author","#newscontent > div.l > ul > li > span.s4");
        template.setElementCss(temp);
        ArrayList<Template> lists = new ArrayList<>();
        lists.add(template);
        ParseManager manager = new ParseManager(lists);
        ConcurrentHashMap<String, BaseItem> result = manager.startOneParse(map);
        System.out.println(result);
    }
}
