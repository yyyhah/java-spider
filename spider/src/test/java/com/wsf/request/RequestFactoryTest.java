package com.wsf.request;

import com.wsf.factory.RequestFactory;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 测试RequestFactory
 */
public class RequestFactoryTest {
    @Test
    public void testFactory() throws InterruptedException, ExecutionException {
        LinkedHashMap<String, byte[]> map = new LinkedHashMap<String, byte[]>();
        HashMap<String, String> header = new HashMap<>();
        header.put("request.cookie","JSESSIONID=uOtr_zLHZLzJfSVCowEu45O8Kkz4IkfiKgv5rHDcbfoGHIyRDa8H!-1737983810; tp_up=_mVr_0AUTif5XhZ8PtUJpvxd-MqP44awiYC_T0oWNrBh6qjY4BD8!-1737983810");
        header.put("request.referer","https://i.hdu.edu.cn/tp_up/view?m=up");
        header.put("request.host","Host: i.hdu.edu.cn");
        header.put("request.user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36");
        RequestFactory factory = new RequestFactory(null,null,header);
        RequestBean req = factory.getRequestBean("https://i.hdu");
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<Object[]> submit = executorService.submit(req);
        Object[] objects = submit.get();
        System.out.println(objects[0]+":"+objects[1]);
    }
}
