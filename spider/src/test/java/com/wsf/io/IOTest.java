package com.wsf.io;

import com.wsf.config.Configure;
import com.wsf.domain.impl.BaseItem;
import com.wsf.factory.io.IOFactory;
import com.wsf.io.impl.*;
import com.wsf.source.Source;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class IOTest {
    private IWriteToPool write;
    private IReadFromPool read;
    @Before
    public void init() throws ClassNotFoundException {
        Class.forName("com.wsf.config.Configure");

    }
    @Test
    public void testIOForRequest(){
        write = new WriteToUrl();
        read = new ReadFromUrl(Configure.getReqBuffer());
        for (int i = 0; i < 50; i++) {
            ConcurrentLinkedQueue<String> list = new ConcurrentLinkedQueue<>();
            list.add("翁寿发"+i);
            write.write(list);
        }


        for (int i = 0; i < 50; i++) {
            System.out.println(i+":"+read.read());
        }

        for (int i = 50; i < 100; i++) {
            ConcurrentLinkedQueue<String> list = new ConcurrentLinkedQueue<>();
            list.add("翁寿发"+i);
            write.write(list);
        }


        for (int i = 0; i < 50; i++) {
            System.out.println(i+":"+read.read());
        }
    }


    @Test
    public void testIOForRequestBatch(){
        write = new WriteToUrl();
        read = new ReadFromUrl(Configure.getReqBuffer());
        for (int i = 0; i < 30; i++) {
            ConcurrentLinkedQueue<String> list = new ConcurrentLinkedQueue<>();
            list.add("翁寿发"+i);
            write.write(list);
        }

        LinkedList<ConcurrentLinkedQueue> concurrentLinkedQueues = read.readBatch();
        for (ConcurrentLinkedQueue concurrentLinkedQueue : concurrentLinkedQueues) {
            System.out.println(concurrentLinkedQueue);
        }
        System.out.println("-------------------------------------------------");
        LinkedList<ConcurrentLinkedQueue> concurrentLinkedQueues2 = read.readBatch();
        for (ConcurrentLinkedQueue concurrentLinkedQueue2 : concurrentLinkedQueues2) {
            System.out.println(concurrentLinkedQueue2);
        }
    }
    @Test
    public void testIOForParse(){
        write = new WriteToHtml();
        read = new ReadFromHtml(Configure.getReqBuffer());
        for (int i = 0; i < 50; i++) {
            ConcurrentHashMap<String,byte[]> map = new ConcurrentHashMap<>();
            map.put("翁寿发"+i,("你好"+i).getBytes());
            write.write(map);
        }


        for (int i = 0; i < 50; i++) {
            System.out.println(i+":"+read.read());
        }
        for (int i = 50; i < 100; i++) {
            ConcurrentHashMap<String,byte[]> map = new ConcurrentHashMap<>();
            map.put("翁寿发"+i,("你好"+i).getBytes());
            write.write(map);
        }

        for (int i = 0; i < 50; i++) {
            System.out.println(i+":"+read.read());
        }
    }

    @Test
    public void testIOForParseBatch(){
        write = new WriteToHtml();
        read = new ReadFromHtml(Configure.getReqBuffer());
        for (int i = 0; i < 30; i++) {
            ConcurrentHashMap<String,byte[]> map = new ConcurrentHashMap<>();
            map.put("翁寿发"+i,("你好"+i).getBytes());
            write.write(map);
        }

        LinkedList<ConcurrentHashMap> concurrentLinkedQueues = (LinkedList<ConcurrentHashMap>) read.readBatch();
        for (ConcurrentHashMap concurrentLinkedQueue : concurrentLinkedQueues) {
            System.out.println(concurrentLinkedQueue);
        }
        System.out.println("-------------------------------------------------");
        LinkedList<ConcurrentHashMap> concurrentLinkedQueues2 = (LinkedList<ConcurrentHashMap>)read.readBatch();
        for (ConcurrentHashMap concurrentLinkedQueue2 : concurrentLinkedQueues2) {
            System.out.println(concurrentLinkedQueue2);
        }
    }



    @Test
    public void testIOForSave() throws ClassNotFoundException {
        write = new WriteToItem();
        read = new ReadFromItem(Configure.getReqBuffer());
        for (int i = 0; i < 50; i++) {
            ConcurrentHashMap<String, BaseItem> map = new ConcurrentHashMap<>();
            map.put("翁寿发"+i,new BaseItem());
            write.write(map);
        }


        for (int i = 0; i < 50; i++) {
            System.out.println(i+":"+read.read());
        }
        for (int i = 50; i < 100; i++) {
            ConcurrentHashMap<String, BaseItem> map = new ConcurrentHashMap<>();
            map.put("翁寿发"+i,new BaseItem());
            write.write(map);
        }

        for (int i = 0; i < 50; i++) {
            System.out.println(i+":"+read.read());
        }
    }
    @Test
    public void testIOForSaveBatch(){
        write = new WriteToItem();
        read = new ReadFromItem(Configure.getReqBuffer());
        for (int i = 0; i < 30; i++) {
            ConcurrentHashMap<String, BaseItem> map = new ConcurrentHashMap<>();
            map.put("翁寿发"+i,new BaseItem());
            write.write(map);
        }
        LinkedList<ConcurrentHashMap> concurrentLinkedQueues = read.readBatch();
        for (ConcurrentHashMap concurrentLinkedQueue : concurrentLinkedQueues) {
            System.out.println(concurrentLinkedQueue);
        }
        System.out.println("-------------------------------------------------");
        LinkedList<ConcurrentHashMap> concurrentLinkedQueues2 = read.readBatch();
        for (ConcurrentHashMap concurrentLinkedQueue2 : concurrentLinkedQueues2) {
            System.out.println(concurrentLinkedQueue2);
        }
    }
    @Test
    public void testIOFactory(){
        IReadFromPool readProxy = IOFactory.getReqReadConnect(40,true);
        IWriteToPool writeProxy = IOFactory.getSaveWriteConnect(true);
        ConcurrentLinkedQueue<String> list  = null;
        for (int i = 0;i<2000;i++) {
            list = new ConcurrentLinkedQueue<>();
            list.add("翁寿发"+i);
            writeProxy.write(list);
        }
        writeProxy.close();
        Source.close();
        int i = 0;
        while(readProxy.hasNext()) {
            System.out.println(i+":"+readProxy.read());
            i++;
        }
    }



    @Test
    public void test() throws IllegalAccessException, InvocationTargetException {
        ConcurrentHashMap<String,String> map = new ConcurrentHashMap<>();
        map.put("aaaa","dsdsd");
        map.put("dsadasd","dasdasd");
        ConcurrentLinkedQueue linkedQueue = new ConcurrentLinkedQueue();
        linkedQueue.add(map);
        linkedQueue.add(map);
        System.out.println(linkedQueue);
    }

}
