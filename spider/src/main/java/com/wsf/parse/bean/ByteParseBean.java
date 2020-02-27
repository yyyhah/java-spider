package com.wsf.parse.bean;

import com.wsf.domain.Item;
import com.wsf.domain.Template;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * 解析image，video等字节流的
 */
public class ByteParseBean implements IParseBean{
    private Template template;
    private static Logger logger = Logger.getLogger(HtmlParseBean.class);


    public ByteParseBean(Template template) {
        this.template = template;
    }

    public Object createProperty(byte[] bytes, Class<?> property) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Object object = property.getConstructor().newInstance();
        Field[] fields = property.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if(field.getName().equals("url")){
                continue;
            }
            field.set(object,bytes);
        }
        return object;
    }
    @Override
    public Item start(byte[] bytes) throws Exception {
        return (Item)createProperty(bytes,Class.forName(template.getItem()));
    }
}
