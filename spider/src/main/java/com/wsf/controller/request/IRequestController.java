package com.wsf.controller.request;

import com.wsf.controller.IController;

import java.util.LinkedHashMap;
import java.util.LinkedList;


public interface IRequestController extends IController<LinkedList<String>, LinkedHashMap> {
    /**
     * 读入一批数据
     * @return
     */
    Integer executeBatch(LinkedList<LinkedList<String>> inBufferList);

}
