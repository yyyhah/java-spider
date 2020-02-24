package com.wsf.request;

import com.wsf.config.Configure;
import com.wsf.factory.ManagerFactory;
import com.wsf.io.IReadFromPool;
import com.wsf.io.impl.ReadFromPoolImpl;
import com.wsf.manager.impl.RequestManager;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.concurrent.*;

public class RequestManagerTest {
    private LinkedHashMap<String,byte[]> map;
    @Test
    public void testRequestManager() throws InterruptedException, ClassNotFoundException, ExecutionException {
        ExecutorService es = Executors.newFixedThreadPool(3);
        ConcurrentLinkedQueue<String> inBuffer = new ConcurrentLinkedQueue<>();
        inBuffer.add("https://cn/tp_up/vie");
        RequestManager manager = ManagerFactory.getRequestManager(1,null,null,null,5);
        manager.setInBuffer(inBuffer);
        manager.init();
        new Thread(manager).start();
        Thread.sleep(100000);
        IReadFromPool read = new ReadFromPoolImpl(Configure.getReqBuffer());
        ConcurrentHashMap<String, byte[]> map = read.readForParse();
        System.out.println(new String(map.get("https://cn/tp_up/vie")));
    }
}
