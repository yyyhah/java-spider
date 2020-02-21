package com.wsf.request;

import java.util.Map;

/**
 * 获取RequestBean对象的工厂类
 */
public class RequestFactory {
    private  Map<String,byte[]> reqConBuffer;
    private Map<String, String> requestHeader;
    /**
     * 创建工厂类
     * @param reqConBuffer 请求器和调度器之间的缓存区(单向)。
     * @param requestHeader
     */
    public RequestFactory(Map<String, byte[]> reqConBuffer, Map<String, String> requestHeader) {
        this.reqConBuffer = reqConBuffer;
        this.requestHeader = requestHeader;
    }

    /**
     * 获取一个RequestBean对象
     * @param url 访问的网址
     * @return RequestBean 返回请求对象
     */
    public  RequestBean getRequestBean(String url){
        //这里直接new创建对象好了，反射的话虽然耦合低，但是会慢挺多的。
        return new RequestBean(url,reqConBuffer,requestHeader);
    }
}
