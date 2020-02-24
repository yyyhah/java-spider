package com.wsf.controller;

import com.wsf.config.Configure;
import com.wsf.controller.request.impl.CenterControllerImpl;
import com.wsf.io.IReadFromPool;
import com.wsf.io.impl.ReadFromPoolImpl;
import com.wsf.io.impl.WriteToPoolImpl;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CenterControllerTest {
    @Test
    public void testStartOneRequest() throws ClassNotFoundException {
        //加载匹配
        Class.forName("com.wsf.config.Configure");
        WriteToPoolImpl write = new WriteToPoolImpl();
        IReadFromPool read = new ReadFromPoolImpl(Configure.getReqBuffer());

        for (int i = 0; i < 30; i++) {
            ConcurrentLinkedQueue<String> inBuffer = new ConcurrentLinkedQueue<>();
            inBuffer.add("https://vip.iqiyi.com/waimeizhy_pc.html?fv=0519153914b4dcb3b8757c8b4981be6b&bd_vid=11764348259367254056");
            inBuffer.add("https://www.baidu.com");
            inBuffer.add("https://www.bilibili.com/");
            inBuffer.add("https://www.douyu.com/");
            write.writeToReq(inBuffer);
        }
        write.flushReq();//记住要刷新

        CenterControllerImpl center = new CenterControllerImpl();
        for (int i = 0; i < 30; i++) {
            center.startOneRequest();
        }
        center.destroy();

        while(read.hasNextParse()) {
            int i = 0;
            LinkedList<ConcurrentHashMap> mapList = read.readForParseBatch();
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

    @Test
    public void testStartBatchRequest() throws ClassNotFoundException, InterruptedException {

    }
}
