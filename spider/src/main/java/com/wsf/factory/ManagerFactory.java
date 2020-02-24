package com.wsf.factory;

import com.wsf.manager.impl.RequestManager;

import java.util.Map;

/**
 * 该工厂用于获取管理器对象
 */
public class ManagerFactory {
    /**
     * 获取请求器管理器对象
     *
     * @return
     */
    public static RequestManager getRequestManager(Integer managerId, Integer connTimeout, Integer readTimeout, Map<String, String> header, Integer size) {
        return new RequestManager(managerId, header, connTimeout, readTimeout, size);
    }
}
