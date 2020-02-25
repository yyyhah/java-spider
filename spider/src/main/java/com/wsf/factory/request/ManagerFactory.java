package com.wsf.factory.request;

import com.wsf.request.manager.impl.RequestManager;

import java.util.Map;

/**
 * 该工厂用于获取request管理器对象
 */
public class ManagerFactory {
    /**
     * 获取请求器管理器对象
     *
     * @return
     */
    public static RequestManager getRequestManager(Integer managerId, Integer connTimeout, Integer readTimeout, Map<String, String> header, Integer size,Boolean gzip) {
        return new RequestManager(managerId, header, connTimeout, readTimeout, size,gzip);
    }
}
