package com.wsf.request;

import com.wsf.factory.ManagerFactory;
import com.wsf.manager.impl.RequestManager;
import org.junit.Test;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestManagerTest {
    private LinkedHashMap<String,byte[]> map;
    @Test
    public void testRequestManager() throws InterruptedException, ClassNotFoundException, ExecutionException {
        ExecutorService es = Executors.newFixedThreadPool(3);
        LinkedHashMap<String, byte[]> outBuffer = new LinkedHashMap<>();
        LinkedList<String> inBuffer = new LinkedList<>();
        inBuffer.add("https://i.hdu.edu.cn/tp_up/view?m=up#act=portal/viewhome");
        RequestManager manager = ManagerFactory.getRequestManager(1,null,null,null,5);
        manager.setInBuffer(inBuffer);
        manager.setOutBuffer(outBuffer);
        manager.init();
        new Thread(manager).start();
        Thread.sleep(3000);
        System.out.println(new String(outBuffer.get("https://i.hdu.edu.cn/tp_up/view?m=up#act=portal/viewhome")));
    }
}
