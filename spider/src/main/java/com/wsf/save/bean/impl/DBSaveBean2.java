package com.wsf.save.bean.impl;

import com.wsf.domain.Item;
import com.wsf.domain.Template;
import com.wsf.domain.impl.IntItem;
import com.wsf.save.bean.ISaveBean;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DBSaveBean2 implements ISaveBean {
    private Template template;
    public DBSaveBean2(Template template) {
        this.template = template;
    }

    @Override
    public ConcurrentLinkedQueue<String> start(Item item) throws IOException {
        if(item instanceof IntItem){
            item = (IntItem)item;
            File file = new File("D:\\webSpider\\source\\novel\\"+ UUID.randomUUID()+".txt");
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fos = new FileWriter(file);
            fos.write(((IntItem) item).getTitle()+"\n");
            fos.write(((IntItem) item).getChapters().toString());
            fos.close();
        }
        return null;
    }
}
