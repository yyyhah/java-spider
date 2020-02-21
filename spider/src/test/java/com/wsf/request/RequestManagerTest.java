package com.wsf.request;

import org.junit.Test;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class RequestManagerTest {
    private LinkedHashMap<String,byte[]> map;
    @Test
    public void testRequestManager() throws InterruptedException {
        LinkedList<String> strings = new LinkedList<>();
        strings.add("https://i.hdu.edu.cn/tp_up/view?m=up#act=portal/viewhome");
        RequestManager manager = new RequestManager(strings);
        manager.start();
        Thread.sleep(4000);
        byte[] bytes = map.get("https://i.hdu.edu.cn/tp_up/view?m=up#act=portal/viewhome");
        System.out.println(new String(bytes));
    }
}
