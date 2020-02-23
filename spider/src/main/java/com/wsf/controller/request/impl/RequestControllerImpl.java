package com.wsf.controller.request.impl;


import com.wsf.config.Configure;
import com.wsf.controller.request.IRequestController;
import com.wsf.factory.ManagerFactory;
import com.wsf.io.IWriteToPool;
import com.wsf.io.impl.WriteToPoolImpl;
import com.wsf.manager.impl.RequestManager;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 连接请求器的控制器实现类,单例,请求器调度器接收中央调度器的资源，访问返回资源写入资源池。
 */
@SuppressWarnings("all")
public class RequestControllerImpl implements IRequestController {
    //向资源池写入接口
    private static IWriteToPool toResource;
    //请求器线程池大小
    private static Integer managerNumber = Configure.getReqControllerThreadNumber()==null?10:Configure.getReqControllerThreadNumber();
    //请求器线程池
    private static ExecutorService managerPool;
    //管理器
    private static LinkedList<RequestManager> managers = new LinkedList<RequestManager>();
    //运行中的管理器
    private static ConcurrentHashMap<Integer,RequestManager> runManager = new ConcurrentHashMap<Integer, RequestManager>();
    //等待队列中管理器
    private static ConcurrentHashMap<Integer,RequestManager> waitManager = new ConcurrentHashMap<Integer, RequestManager>();


    @Override
    public void destroy() {
        for (RequestManager manager : managers) {
            //因为这里5秒之后强制关闭，所以一般会在managePool关闭之前关闭
            manager.destroy();
        }
        managerPool.shutdown();
        try {
            //6秒之后没有关闭，强制关闭
            if(managerPool.awaitTermination(6000, TimeUnit.SECONDS)){
                managerPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            //异常也强制关闭
            managerPool.shutdownNow();
            e.printStackTrace();
        }
        //等待7秒，让线程完全关闭
        try {
            Thread.sleep(70000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ConcurrentHashMap<String,byte[]> createOutBuffer() {
        //每次创造一块写出缓存区，保存请求器得到的信息
        return new ConcurrentHashMap<String, byte[]>();
    }

    /**
     * 将outBuffer存入资源池
     * @param outBuffer
     */
    public static void setResource(ConcurrentHashMap<String,byte[]> outBuffer) {
        toResource.writeToParse(outBuffer);
    }

    @Override
    public void init() {
        // 创建管理器资源池
        managerPool = Executors.newFixedThreadPool(managerNumber);
        //导入资源池
        toResource = new WriteToPoolImpl();
        //创建子管理器,之所以要先创建，主要是为了能重复利用子管理器。
        for (int i = 0; i < managerNumber; i++) {
            RequestManager manager = ManagerFactory.getRequestManager(i,Configure.getConnTimeout(),Configure.getReadTimeout(),Configure.getRequestHeader(), Configure.getHandlerNumber());
            manager.init();
            managers.add(manager);
            //将所有的管理器放入等待队列
            waitManager.put(i,manager);
        }
        System.out.println(managers);
    }

    @Override
    public Integer execute(ConcurrentLinkedQueue<String> inBuffer) throws ExecutionException, InterruptedException {
        if(waitManager.size()==0){
            return -1;
        }else{
            Map.Entry<Integer, RequestManager> next = waitManager.entrySet().iterator().next();
            Integer id = next.getKey();
            RequestManager manager = next.getValue();
            //将该元素从空闲池 移到运行池中
            waitManager.remove(id);
            runManager.put(id, manager);
            //为其加入输入和输出
            manager.setInBuffer(inBuffer);
            manager.setOutBuffer(createOutBuffer());
            managerPool.submit(manager);
            return waitManager.size();
        }
    }


    @Override
    public Integer executeBatch(LinkedList<ConcurrentLinkedQueue<String>> inBufferList){
        //如果没有可用的manager，那就返回-1表示请求器繁忙，让中央调度器自己协调
        if(waitManager.size()==0){
            return -1;
        }else{
            for (ConcurrentLinkedQueue<String> inBuffer : inBufferList) {
                //获取第一个元素
                Map.Entry<Integer, RequestManager> next = waitManager.entrySet().iterator().next();
                Integer id = next.getKey();
                RequestManager manager = next.getValue();
                //将该元素从空闲池 移到运行池中
                waitManager.remove(id);
                runManager.put(id, manager);
                //为其加入输入和输出
                manager.setInBuffer(inBuffer);
                //移除当前
                manager.setOutBuffer(createOutBuffer());
                managerPool.submit(manager);
            }
            return waitManager.size();
        }
    }

    //Manager线程池的回调方法。
    public static void finish(ConcurrentHashMap<String,byte[]> outBuffer,Integer managerId){
        //当一个管理器线程运行结束，将该管理器线程从运行队列移除，加入空闲队列，并将输入输出数据源设为空,由于上下两处都用到了waitManager,这里加个锁
        if (runManager.containsKey(managerId)) {
            RequestManager remove = runManager.remove(managerId);
            //将得到的数据保存到资源池中
            setResource(remove.getOutBuffer());
            remove.setInBuffer(null);
            remove.setOutBuffer(null);
            waitManager.put(managerId, remove);
        }
    }
    public static Integer getManagerNumber() {
        return managerNumber;
    }

    public static void setManagerNumber(Integer managerNumber) {
        RequestControllerImpl.managerNumber = managerNumber;
    }
}
