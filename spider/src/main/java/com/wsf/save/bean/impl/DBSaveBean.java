package com.wsf.save.bean.impl;

import com.wsf.domain.Item;
import com.wsf.domain.Template;
import com.wsf.domain.impl.BaseItem;
import com.wsf.domain.impl.BilibiliItem;
import com.wsf.domain.impl.MyAtom;
import com.wsf.domain.impl.MyAtom2;
import com.wsf.save.bean.ISaveBean;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DBSaveBean implements ISaveBean {

    private  Template template;
    public DBSaveBean(Template template) {
        this.template = template;
    }

    @Override
    public ConcurrentLinkedQueue<String> start(Item item){
        if(item instanceof BaseItem){
            ConcurrentLinkedQueue<String> urls = new ConcurrentLinkedQueue<>();
            ArrayList<MyAtom> atoms = ((BaseItem) item).getAtoms();
            for (MyAtom atom : atoms) {
                urls.add(atom.getChapterHref());
            }
            return urls;
        }
        return null;
    }
}
