package com.wsf.manager;
import java.util.concurrent.ExecutorService;

/**
 * 各个处理模块的管理器接口模板
 */
public interface IHandlerManager<E,T> extends Runnable{

    /**
     * 初始化方法，创建线程池，建造子模块工厂
     */
    void init();

    /**
     * 管理器销毁方法,关闭线程池等资源
     */
    void destroy();

    /**
     * 获取线程池
     * @return
     */
    ExecutorService getExecutorService();

    /**
     * 设置线程池大小
     * @return
     */
    Integer getSize();

    /**
     * 获取线程池大小
     * @param size
     */
    void setSize(Integer size);

    /**
     * 获取请求超时时间
     * @return
     */
    Integer getConnTimeout();

    /**
     * 设置请求超时时间
     * @param connTimeout
     */
    void setConnTimeout(Integer connTimeout);
    /**
     * 获取读取超时时间
     * @return
     */
    Integer getReadTimeout();
    /**
     * 获取读取超时时间
     * @return
     */
    void setReadTimeout(Integer readTimeout);

    E getInBuffer();

    void setInBuffer(E inBuffer);
}
