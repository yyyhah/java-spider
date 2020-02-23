package com.wsf.controller;

import com.wsf.config.Configure;
import com.wsf.controller.request.impl.CenterControllerImpl;
import com.wsf.io.IReadFromPool;
import com.wsf.io.impl.ReadFromPoolImpl;
import com.wsf.io.impl.WriteToPoolImpl;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CenterControllerTest {
    @Test
    public void testStartOneRequest() throws ClassNotFoundException, InterruptedException {
        //加载匹配
        Class.forName("com.wsf.config.Configure");
        //向资源池插入一条数据
        ConcurrentLinkedQueue<String> inBuffer = new ConcurrentLinkedQueue<>();
        inBuffer.add("https://i.hdu.edu.cn/tp_up/view?m=up#act=portal/viewhome");
        inBuffer.add("https://www.baidu.com");
        inBuffer.add("https://www.docin.com/p-949992891-f6.html");
        inBuffer.add("https://blog.csdn.net/kq1983/article/details/89851138");
        WriteToPoolImpl write = new WriteToPoolImpl();
        write.writeToReq(inBuffer);
        write.flushReq();//记住要刷新

        CenterControllerImpl center = new CenterControllerImpl();
        center.startOneRequest();
        center.destroy();
        Thread.sleep(3000);

        write.flush();
        IReadFromPool read = new ReadFromPoolImpl(Configure.getReqBuffer());
        ConcurrentHashMap<String, byte[]> map = read.readForParse();
        System.out.println(map);
    }

    @Test
    public void testStartBatchRequest() throws ClassNotFoundException, InterruptedException {

    }
}
