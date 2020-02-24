package com.wsf.factory;

import com.wsf.request.RequestBean;

import java.util.Map;

/**
 * 获取RequestBean对象的工厂类，多例
 */
public class RequestFactory {
    private Map<String, String> requestHeader;
    private Integer connTimeout = null;
    private Integer readTimeout = null;

    public RequestFactory(Integer connTimeout,Integer readTimeout,Map<String, String> requestHeader) {
        this.requestHeader = requestHeader;
        //如果超时不为0 设置超时
        if(connTimeout!=null){
            setConnTimeout(connTimeout);
        }
        if(readTimeout!=null) {
            setReadTimeout(readTimeout);
        }
    }
    public Map<String, String> getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(Map<String, String> requestHeader) {
        this.requestHeader = requestHeader;
    }

    /**
     * 获取连接超时时间
     * @return
     */
    public Integer getConnTimeout() {
        return connTimeout;
    }

    /**
     * 设置连接超时时间
     * @param connTimeout
     */
    public void setConnTimeout(Integer connTimeout) {
        this.connTimeout = connTimeout;
        RequestBean.setConnTimeOut(this.connTimeout);
    }

    /**
     * 获取读取超时时间
     * @return
     */
    public Integer getReadTimeout() {
        return readTimeout;
    }
    /**
     * 设置读取超时时间
     * @return
     */
    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
        RequestBean.setReadTimeout(this.readTimeout);
    }

    /**
     * 获取一个RequestBean对象
     * @param url 访问的网址
     * @return RequestBean 返回请求对象
     */
    public  RequestBean getRequestBean(String url){
        //这里直接new创建对象好了，反射的话虽然耦合低，但是会慢挺多的。
        return new RequestBean(url,requestHeader);
    }
}
