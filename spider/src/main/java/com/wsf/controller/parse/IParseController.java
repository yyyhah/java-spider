package com.wsf.controller.parse;

import com.wsf.controller.IController;
import com.wsf.domain.Item;
import com.wsf.domain.Template;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface IParseController extends IController<ConcurrentHashMap<String,byte[]>,ConcurrentHashMap<String, Item>> {
    @Override
    default Boolean isIdle() {
        return null;
    }

    @Override
    default Boolean isEmpty() {
        return null;
    }

    @Override
    default void init() {

    }

    /**
     * 初始化控制器，主要用于读取模板信息
     * @param templates
     */
    void init(List<Template> templates);

}
