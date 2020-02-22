package com.wsf.controller;

import com.wsf.config.Configure;
import com.wsf.controller.request.impl.CenterControllerImpl;
import com.wsf.io.IReadFromPool;
import com.wsf.io.IWriteToPool;
import com.wsf.io.impl.ReadFromReqPoolImpl;
import com.wsf.io.impl.WriteToReqPoolImpl;
import com.wsf.source.Source;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class CenterControllerTest {
    @Test
    public void testStartOneRequest() throws ClassNotFoundException, InterruptedException {
        //加载匹配
        Class.forName("com.wsf.config.Configure");
        //向资源池插入一条数据
        LinkedList<String> inBuffer = new LinkedList<>();
        inBuffer.add("https://i.hdu.edu.cn/tp_up/view?m=up#act=portal/viewhome");
        inBuffer.add("https://www.baidu.com");
        inBuffer.add("https://www.docin.com/p-949992891-f6.html");
        inBuffer.add("https://blog.csdn.net/kq1983/article/details/89851138");
        IWriteToPool write = new WriteToReqPoolImpl(Configure.getSource());
        Source.getReqInBuffer().addAll(inBuffer);

        CenterControllerImpl center = new CenterControllerImpl();
        center.startOneRequest();
        Thread.sleep(4000);

        IReadFromPool read = new ReadFromReqPoolImpl(Configure.getSource(),Configure.getReqBuffer());
        LinkedHashMap<String,byte[]> map = Source.getReqOutBuffer();
        System.out.println(map);
    }

    @Test
    public void testStartBatchRequest() throws ClassNotFoundException, InterruptedException {
        //加载匹配
        Class.forName("com.wsf.config.Configure");
        //向资源池插入一条数据
        LinkedList<String> inBuffer = new LinkedList<>();
        inBuffer.add("https://i.hdu.edu.cn/tp_up/view?m=up#act=portal/viewhome");
        inBuffer.add("https://www.baidu.com");
        inBuffer.add("https://www.docin.com/p-949992891-f6.html");
        inBuffer.add("https://blog.csdn.net/kq1983/article/details/89851138");
        IWriteToPool write = new WriteToReqPoolImpl(Configure.getSource());
        Source.getReqInBuffer().addAll(inBuffer);

        CenterControllerImpl center = new CenterControllerImpl();
        center.startOneRequest();
        Thread.sleep(4000);

        IReadFromPool read = new ReadFromReqPoolImpl(Configure.getSource(),Configure.getReqBuffer());
        LinkedHashMap<String,byte[]> map = Source.getReqOutBuffer();
        System.out.println(map);
    }
}
