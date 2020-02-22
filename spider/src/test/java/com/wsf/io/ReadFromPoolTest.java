package com.wsf.io;

import com.wsf.config.Configure;
import com.wsf.io.impl.ReadFromReqPoolImpl;
import com.wsf.io.impl.WriteToReqPoolImpl;
import com.wsf.source.Source;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class ReadFromPoolTest {
    @Test
    public void testReadForRequest() throws ClassNotFoundException {
        Class.forName("com.wsf.config.Configure");
        Source source = Configure.getSource();
        IWriteToPool write = new WriteToReqPoolImpl(source);
        LinkedList<String> list = new LinkedList<>();
        list.add("翁寿发");
        Source.getReqInBuffer().addAll(list);
        IReadFromPool read = new ReadFromReqPoolImpl(source,Configure.getReqBuffer());
        LinkedList<String> strings = read.readForRequest();
        System.out.println(strings);
        LinkedList<String> strings2 = read.readForRequest();
        System.out.println(strings);
    }
    @Test
    public void readFrom() throws ClassNotFoundException {
        Class.forName("com.wsf.config.Configure");
        Source source = Configure.getSource();
        IWriteToPool write = new WriteToReqPoolImpl(source);
        LinkedHashMap<String, byte[]> map = new LinkedHashMap<>();
        map.put("翁寿发","wengshoufa".getBytes());
        write.writeFromRequest(map);
        IReadFromPool read = new ReadFromReqPoolImpl(source,Configure.getReqBuffer());
        LinkedHashMap<String, byte[]> reqOutBuffer = Source.getReqOutBuffer();
        System.out.println(new String(reqOutBuffer.get("翁寿发")));
        LinkedHashMap map2 = Source.getReqOutBuffer();
        System.out.println(map2);
    }
    @Test
    public void test(){
        Map<String,String> map1 = new HashMap<String,String>();
        Map<String,String> map2 = new HashMap<String,String>();
        map1.put("aaaaa","ssssss");
        map1.put("rrrrr","wwwwww");
        map2.put("ddddd","gggggg");
        map1.putAll(map2);
        System.out.println(map1);
        map2 = null;
        System.out.println(map1);

    }
}
