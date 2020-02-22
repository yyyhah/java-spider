package com.wsf.controller.request.impl;

import com.wsf.config.Configure;
import com.wsf.controller.IController;
import com.wsf.io.IReadFromPool;
import com.wsf.io.impl.ReadFromReqPoolImpl;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

public class CenterControllerImpl implements IController {
    //与资源池的接口
    IReadFromPool fromPool = null;
    IController requestController = null;

    public CenterControllerImpl() {
        init();
    }

    public void startBatchRequest(){

    }


    //让请求器控制器读取一次资源池，然后执行请求访问
    public void startOneRequest(){
        LinkedList<String> inBuffer = fromPool.readForRequest();
        try {
            Integer executeNum = requestController.execute(inBuffer);
            System.out.println("Center:"+executeNum);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(){
        //初始化读取器
        fromPool = new ReadFromReqPoolImpl(Configure.getSource(),Configure.getReqBuffer());
        //创建请求器调度器
        requestController = new RequestControllerImpl();
        //初始化请求器调度器
        requestController.init();
    }

    @Override
    public void destroy() {

    }

    @Override
    public LinkedHashMap createOutBuffer() {
        return null;
    }


    @Override
    public Integer execute(Object o) throws ExecutionException, InterruptedException {
        return null;
    }
}
