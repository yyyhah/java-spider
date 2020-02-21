package com.wsf.request;

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
/**
 * 测试RequestBean类
 */
public class RequestBeanTest {
    @Test
    /**
     * 测试能否建立连接，以及能否设置请求头
     */
    public void testRequestBean1() throws InterruptedException {
        LinkedHashMap<String, byte[]> map = new LinkedHashMap<String, byte[]>();
        HashMap<String, String> header = new HashMap<>();
        header.put("request.cookie","JSESSIONID=OmBnrUluuac67skRU7KSTnke6Yn-3OszhznqBnGKo7y6D6aoxPV_!-1737983810; tp_up=TBBnrVuQxjPKmdbgpy5NfD7s07usalmprJiDOEovZQ4jPn5iI4FG!-1737983810");
        header.put("request.referer","Referer: https://i.hdu.edu.cn/tp_up/view?m=up");
        header.put("request.host","Host: i.hdu.edu.cn");
        header.put("request.user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36");
        RequestBean req = new RequestBean("https://i.hdu.edu.cn/tp_up/view?m=up#act=portal/viewhome",  map,header);
        new Thread(req).start();
        Thread.sleep(4000);
        byte[] bytes = map.get("https://i.hdu.edu.cn/tp_up/view?m=up#act=portal/viewhome");
        System.out.println(new String(bytes));
    }
}