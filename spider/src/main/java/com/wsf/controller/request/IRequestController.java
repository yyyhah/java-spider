package com.wsf.controller.request;

import com.wsf.controller.IController;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


public interface IRequestController extends IController<ConcurrentLinkedQueue<String>, ConcurrentHashMap<String, byte[]>> {
//    Integer executeBatch(LinkedList<ConcurrentLinkedQueue> inBufferList);
}
