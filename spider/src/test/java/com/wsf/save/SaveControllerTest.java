package com.wsf.save;

import com.wsf.controller.save.impl.SaveController;
import com.wsf.domain.Item;
import com.wsf.domain.Template;
import com.wsf.factory.io.IOFactory;
import com.wsf.io.IReadFromPool;
import com.wsf.source.Source;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SaveControllerTest {
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
        template.setParseBean("com.wsf.parse.bean.impl.HtmlParseBean");
        template.setSaveBean("com.wsf.save.bean.impl.DBSaveBean2");
        template1.setElementPath(temp2);
        lists.add(template1);
        return lists;
    }
    @Test
    public void testSaveController(){
        IReadFromPool readFromItem = IOFactory.getSaveReadConnect(40, true);
        SaveController controller = new SaveController(getTemplate());
        while (readFromItem.hasNext()) {
            controller.execute((ConcurrentHashMap<String, Item>) readFromItem.read());
        }
        controller.destroy();
        Source.close();
    }
    @Test
    public void read(){
        IReadFromPool readFromUrl = IOFactory.getReqReadConnect(40, true);
        while(readFromUrl.hasNext()){
            System.out.println(readFromUrl.read());
        }
    }
}
