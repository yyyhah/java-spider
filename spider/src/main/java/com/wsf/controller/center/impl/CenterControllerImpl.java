package com.wsf.controller.center.impl;

import com.wsf.config.Configure;
import com.wsf.controller.center.ICenterController;
import com.wsf.controller.parse.IParseController;
import com.wsf.controller.parse.impl.ParseController;
import com.wsf.controller.request.IRequestController;
import com.wsf.controller.request.impl.RequestController;
import com.wsf.controller.save.ISaveController;
import com.wsf.controller.save.impl.SaveController;
import com.wsf.domain.Item;
import com.wsf.domain.Template;
import com.wsf.factory.io.IOFactory;
import com.wsf.io.IReadFromPool;
import com.wsf.source.Source;
import org.apache.log4j.Logger;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CenterControllerImpl implements ICenterController {
    //与资源池的接口
    private IReadFromPool urlReader = null;
    private IReadFromPool htmlReader = null;
    private IReadFromPool itemReader = null;
    //控制器
    private IRequestController requestController = null;
    private IParseController parseController = null;
    private ISaveController saveController = null;
    //模板
    private ArrayList<Template> templates = null;

    private Logger logger = Logger.getLogger(getClass());

    //监视所有进程执行情况
    public CenterControllerImpl(List<Template> templates) {
        init(templates);
    }


    /**
     * 让请求器控制器读取一次资源池，然后执行请求访问
     */
    @Override
    public void startOneRequest() {
        //如果请求器有空闲的队列才会执行
        if(requestController.isIdle()) {
            ConcurrentLinkedQueue<String> inBuffer = (ConcurrentLinkedQueue) urlReader.read();
            //如果有读取到数据才会执行
            if (inBuffer != null && inBuffer.size() > 0) {
                requestController.execute(inBuffer);
            } else {
                logger.warn("url池中没数据了");
            }
        }else{
            logger.warn("管理器繁忙,请稍后再试");
            while (!requestController.isIdle()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    @Override
    public void startOneParse(){
        ConcurrentHashMap<String,byte[]> inBuffer = (ConcurrentHashMap<String,byte[]>)htmlReader.read();
        if(inBuffer!=null && inBuffer.size()>0){
            parseController.execute(inBuffer);
        }
    }

    @Override
    public void startOneSave(){
        ConcurrentHashMap<String,Item> inBuffer = (ConcurrentHashMap<String, Item>)itemReader.read();
        if(inBuffer!=null && inBuffer.size()>0){
            saveController.execute(inBuffer);
        }
    }

    /**
     * 爬虫启动
     */
    public void start(){
        //当有数据存在,或者request未执行完毕时，一直执行
        while(!requestController.isEmpty() || urlReader.hasNext() || itemReader.hasNext() || htmlReader.hasNext()){
            if(urlReader.hasNext()) {
                startOneRequest();
                //每轮请求完毕休息一秒
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(htmlReader.hasNext()) {
                startOneParse();
            }
            if(itemReader.hasNext()) {
                startOneSave();
            }

        }
    }



    @Override
    public void init(List<Template> templates) {
        //初始化读取器
        urlReader = IOFactory.getReqReadConnect(Configure.getReqBuffer(),diskSave);
        htmlReader = IOFactory.getParseReadConnect(Configure.getReqBuffer(),diskSave);
        itemReader = IOFactory.getSaveReadConnect(Configure.getReqBuffer(),diskSave);
        //创建请求器调度器
        requestController = new RequestController();
        parseController = new ParseController(templates);
        saveController = new SaveController(templates);
    }




    @Override
    public void destroy() {
        //只有当各个管理器的运行队列为空时才能关闭程序,设置休息时间4秒
        while (!requestController.isEmpty()) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        requestController.destroy();
        parseController.destroy();
        saveController.destroy();
        urlReader.close();
        htmlReader.close();
        itemReader.close();
        Source.close();
    }
}
