package com.wsf.controller.parse.impl;

import com.wsf.controller.parse.IParseController;
import com.wsf.domain.Item;
import com.wsf.domain.Template;
import com.wsf.factory.io.IOFactory;
import com.wsf.io.IWriteToPool;
import com.wsf.parse.manager.impl.ParseManager;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 该类为parse的控制类
 */
public class ParseController implements IParseController {
    private IWriteToPool toResource;
    private List<Template> templates;
    private ParseManager manager;

    public ParseController(List<Template> templates) {
        init(templates);
        init();
    }

    @Override
    public void init(List<Template> templates) {
        //初始化写读取器
        toResource = IOFactory.getParseWriteConnect(diskSave);
        //初始化映射模板
        this.templates = templates;
        //初始化管理器
        this.manager = new ParseManager(this.templates);
    }

    public Integer execute(ConcurrentHashMap<String,byte[]> inBuffer) {
        ConcurrentHashMap<String, Item> map = manager.startOneParse(inBuffer);
        toResource.write(map);
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
