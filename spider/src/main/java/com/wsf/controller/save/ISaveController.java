package com.wsf.controller.save;

import com.wsf.controller.IController;
import com.wsf.domain.Item;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface ISaveController extends IController<ConcurrentHashMap<String, Item>, ConcurrentLinkedQueue<String>> {
}
