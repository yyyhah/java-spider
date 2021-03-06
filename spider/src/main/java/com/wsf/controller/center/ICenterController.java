package com.wsf.controller.center;

import com.wsf.controller.IController;
import com.wsf.domain.Template;
import com.wsf.io.IWriteToPool;

import java.util.List;


/**
 * 中央调度器，主要进行其他几个调度器执行的管理
 * 1. 分配资源，几个调度器的时间占比
 * 2. 负责其他几个调度器的关闭，初始化。
 */
public interface ICenterController extends IController {

    /**
     * 请求器执行一组数据
     */
    void startOneRequest();

    /**
     * 解析器解析一组数据
     */
    void startOneParse();

    /**
     * 存入一组数据
     */
    void startOneSave();

    @Override
    default void init() {

    }

    void init(List<Template> templates);

    @Override
    default Integer execute(Object o) {
        return null;
    }

    @Override
    default void destroy() {

    }
    @Override
    default IWriteToPool getWriter() {
        return null;
    }

}
