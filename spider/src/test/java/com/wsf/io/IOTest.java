package com.wsf.io;

import com.wsf.config.Configure;
import com.wsf.domain.Item;
import com.wsf.io.impl.ReadFromPoolImpl;
import com.wsf.io.impl.WriteToPoolImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class IOTest {
    private IWriteToPool write;
    private IReadFromPool read;
    @Before
    public void init() throws ClassNotFoundException {
        Class.forName("com.wsf.config.Configure");
        write = new WriteToPoolImpl();
        read = new ReadFromPoolImpl(Configure.getReqBuffer());
    }
    @Test
    public void testIOForRequest(){
        for (int i = 0; i < 50; i++) {
            ConcurrentLinkedQueue<String> list = new ConcurrentLinkedQueue<>();
            list.add("翁寿发"+i);
            write.writeToReq(list);
        }
        write.flush();


        for (int i = 0; i < 50; i++) {
            System.out.println(i+":"+read.readForReq());
        }

        for (int i = 50; i < 100; i++) {
            ConcurrentLinkedQueue<String> list = new ConcurrentLinkedQueue<>();
            list.add("翁寿发"+i);
            write.writeToReq(list);
        }
        write.flush();


        for (int i = 0; i < 50; i++) {
            System.out.println(i+":"+read.readForReq());
        }
    }


    @Test
    public void testIOForRequestBatch(){
        for (int i = 0; i < 30; i++) {
            ConcurrentLinkedQueue<String> list = new ConcurrentLinkedQueue<>();
            list.add("翁寿发"+i);
            write.writeToReq(list);
        }
        write.flush();

        LinkedList<ConcurrentLinkedQueue> concurrentLinkedQueues = read.readForReqBatch();
        for (ConcurrentLinkedQueue concurrentLinkedQueue : concurrentLinkedQueues) {
            System.out.println(concurrentLinkedQueue);
        }
        System.out.println("-------------------------------------------------");
        LinkedList<ConcurrentLinkedQueue> concurrentLinkedQueues2 = read.readForReqBatch();
        for (ConcurrentLinkedQueue concurrentLinkedQueue2 : concurrentLinkedQueues2) {
            System.out.println(concurrentLinkedQueue2);
        }
    }
    @Test
    public void testIOForParse(){
        for (int i = 0; i < 50; i++) {
            ConcurrentHashMap<String,byte[]> map = new ConcurrentHashMap<>();
            map.put("翁寿发"+i,("你好"+i).getBytes());
            write.writeToParse(map);
        }
        write.flush();


        for (int i = 0; i < 50; i++) {
            System.out.println(i+":"+read.readForParse());
        }
        for (int i = 50; i < 100; i++) {
            ConcurrentHashMap<String,byte[]> map = new ConcurrentHashMap<>();
            map.put("翁寿发"+i,("你好"+i).getBytes());
            write.writeToParse(map);
        }
        write.flush();

        for (int i = 0; i < 50; i++) {
            System.out.println(i+":"+read.readForParse());
        }
    }

    @Test
    public void testIOForParseBatch(){
        for (int i = 0; i < 30; i++) {
            ConcurrentHashMap<String,byte[]> map = new ConcurrentHashMap<>();
            map.put("翁寿发"+i,("你好"+i).getBytes());
            write.writeToParse(map);
        }
        write.flush();

        LinkedList<ConcurrentHashMap> concurrentLinkedQueues = read.readForParseBatch();
        for (ConcurrentHashMap concurrentLinkedQueue : concurrentLinkedQueues) {
            System.out.println(concurrentLinkedQueue);
        }
        System.out.println("-------------------------------------------------");
        LinkedList<ConcurrentHashMap> concurrentLinkedQueues2 = read.readForParseBatch();
        for (ConcurrentHashMap concurrentLinkedQueue2 : concurrentLinkedQueues2) {
            System.out.println(concurrentLinkedQueue2);
        }
    }



    @Test
    public void testIOForSave() throws ClassNotFoundException {
        for (int i = 0; i < 50; i++) {
            ConcurrentHashMap<String,Item> map = new ConcurrentHashMap<>();
            map.put("翁寿发"+i,new Item());
            write.writeToSave(map);
        }


        for (int i = 0; i < 50; i++) {
            System.out.println(i+":"+read.readForSave());
        }
        for (int i = 50; i < 100; i++) {
            ConcurrentHashMap<String,Item> map = new ConcurrentHashMap<>();
            map.put("翁寿发"+i,new Item());
            write.writeToSave(map);
        }
        write.flush();

        for (int i = 0; i < 50; i++) {
            System.out.println(i+":"+read.readForSave());
        }
    }
    @Test
    public void testIOForSaveBatch(){
        for (int i = 0; i < 30; i++) {
            ConcurrentHashMap<String,Item> map = new ConcurrentHashMap<>();
            map.put("翁寿发"+i,new Item());
            write.writeToSave(map);
        }
        write.flush();

        LinkedList<ConcurrentHashMap> concurrentLinkedQueues = read.readForSaveBatch();
        for (ConcurrentHashMap concurrentLinkedQueue : concurrentLinkedQueues) {
            System.out.println(concurrentLinkedQueue);
        }
        System.out.println("-------------------------------------------------");
        LinkedList<ConcurrentHashMap> concurrentLinkedQueues2 = read.readForSaveBatch();
        for (ConcurrentHashMap concurrentLinkedQueue2 : concurrentLinkedQueues2) {
            System.out.println(concurrentLinkedQueue2);
        }
    }
}
