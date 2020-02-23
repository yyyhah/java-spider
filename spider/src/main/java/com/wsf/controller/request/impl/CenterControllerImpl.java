package com.wsf.controller.request.impl;

import com.wsf.config.Configure;
import com.wsf.controller.IController;
import com.wsf.controller.request.ICenterController;
import com.wsf.io.IReadFromPool;
import com.wsf.io.IWriteToPool;
import com.wsf.io.impl.ReadFromPoolImpl;
import com.wsf.io.impl.WriteToPoolImpl;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;

public class CenterControllerImpl implements ICenterController {
    //与资源池的接口
    private IReadFromPool fromPool = null;
    private IWriteToPool toResource = null;
    private IController requestController = null;
    //监视所有进程执行情况
    public CenterControllerImpl() {
        init();
    }

    public void startBatchRequest(){

    }


    //让请求器控制器读取一次资源池，然后执行请求访问
    @Override
    public void startOneRequest(){
        ConcurrentLinkedQueue<String> inBuffer = fromPool.readForReq();
        if(inBuffer!=null) {
            try {
                Integer executeNum = requestController.execute(inBuffer);
                System.out.println("Center:" + executeNum);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("url池中没数据了");
        }
    }

    @Override
    public void init(){
        //初始化读取器
        fromPool = new ReadFromPoolImpl(Configure.getReqBuffer());
        toResource = new WriteToPoolImpl();
        //创建请求器调度器
        requestController = new RequestControllerImpl();
        //初始化请求器调度器
        requestController.init();


    }

    @Override
    public void destroy() {
        requestController.destroy();
        while(!RequestControllerImpl.getManagerPool().isTerminated()){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        toResource.close();
        fromPool.close();
    }
}
