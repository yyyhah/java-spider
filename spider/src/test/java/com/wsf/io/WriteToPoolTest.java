package com.wsf.io;

import com.wsf.config.Configure;
import com.wsf.io.impl.WriteToReqPoolImpl;
import com.wsf.source.Source;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class WriteToPoolTest {
    @Test
    public void testWriteFromRequest() throws ClassNotFoundException {
        Class.forName("com.wsf.config.Configure");
        Source source = Configure.getSource();
        IWriteToPool write = new WriteToReqPoolImpl(source);
        LinkedHashMap<String, byte[]> map = new LinkedHashMap<>();
        map.put("翁寿发","翁寿发".getBytes());
        write.writeFromRequest(map);
        LinkedHashMap<String, byte[]> reqOutBuffer = Source.getReqOutBuffer();
        System.out.println(new String(reqOutBuffer.get("翁寿发")));
    }

    @Test
    public void testWriteInto() throws ClassNotFoundException {
        Class.forName("com.wsf.config.Configure");
        Source source = Configure.getSource();
        IWriteToPool write = new WriteToReqPoolImpl(source);
        LinkedList<String> list = new LinkedList<>();
        list.add("翁寿发");
        Source.getReqInBuffer().addAll(list);
        LinkedList<String> reqOutBuffer = Source.getReqInBuffer();
        System.out.println(reqOutBuffer);
    }
}
