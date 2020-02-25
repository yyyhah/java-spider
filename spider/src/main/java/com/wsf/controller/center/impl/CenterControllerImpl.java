package com.wsf.controller.center.impl;

import com.wsf.config.Configure;
import com.wsf.controller.center.ICenterController;
import com.wsf.controller.request.IRequestController;
import com.wsf.controller.request.impl.RequestControllerImpl;
import com.wsf.factory.io.IOFactory;
import com.wsf.io.IReadFromPool;
import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CenterControllerImpl implements ICenterController {
    //与资源池的接口
    private IReadFromPool urlReader = null;
    private IRequestController requestController = null;
    private Logger logger = Logger.getLogger(getClass());

    //监视所有进程执行情况
    public CenterControllerImpl() {
        init();
    }


    /**
     * 将request执行的公共代码抽取
     *
     * @param inBuffer
     */
    private void executeRequest(ConcurrentLinkedQueue<String> inBuffer) {
        if (inBuffer != null && inBuffer.size() > 0) {
            Integer executeNum = requestController.execute(inBuffer);
            if (executeNum <= 0) {
                logger.warn("管理器繁忙,请稍后再试");
                while (!requestController.isIdle()) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            logger.warn("url池中没数据了");
        }
    }


    //让请求器控制器读取一次资源池，然后执行请求访问
    @Override
    public void startOneRequest() {
        ConcurrentLinkedQueue<String> inBuffer = (ConcurrentLinkedQueue) urlReader.read();
        executeRequest(inBuffer);
    }

    @Override
    public void init() {
        //初始化读取器
        urlReader = IOFactory.getReqReadConnect(Configure.getReqBuffer(),diskSave);
        //创建请求器调度器
        requestController = new RequestControllerImpl();
        //初始化请求器调度器
        requestController.init();
    }

    @Override
    public void destroy() {
        //只有当各个管理器的运行队列为空时才能关闭程序,设置休息时间4秒，4秒后强制关闭
        while (!requestController.isEmpty()) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        requestController.destroy();
        urlReader.close();
    }
}
