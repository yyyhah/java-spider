package com.wsf.controller.save.impl;

import com.wsf.controller.save.ISaveController;
import com.wsf.domain.Item;
import com.wsf.domain.Template;
import com.wsf.factory.io.IOFactory;
import com.wsf.io.IWriteToPool;
import com.wsf.save.manager.impl.SaveManager;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SaveController implements ISaveController {
    private IWriteToPool toResource;
    private List<Template> templates;
    private SaveManager manager;

    public SaveController(List<Template> templates) {
        this.templates = templates;
        init();
    }

    @Override
    public void init() {
        //初始化写读取器
        toResource = IOFactory.getSaveWriteConnect(diskSave);
        //初始化映射模板
        this.templates = templates;
        //初始化管理器
        this.manager = new SaveManager(this.templates);
    }

    @Override
    public Integer execute(ConcurrentHashMap<String ,Item> inBuffer) {
        ConcurrentLinkedQueue<String> urls = null;
        try {
            urls = manager.startOneSave(inBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果有返回，那就把返回值加入到url池子中。
        if(urls!=null && urls.size()>0) {
            toResource.write(urls);
        }
        return null;
    }

    @Override
    public void destroy() {
        toResource.close();
    }

    @Override
    public IWriteToPool getWriter() {
        return toResource;
    }
}
