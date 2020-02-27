package com.wsf.parse.bean;

import com.wsf.domain.Item;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * ParseBean的接口，其实现类必须实现带Template的构造方法
 */
public interface IParseBean {
    /**
     * 获取泛型的真实对象
     * @param type
     * @return
     */
    default Class<?> getGenericType(Type type){
        if(type instanceof ParameterizedType){
            ParameterizedType parameterizedType = (ParameterizedType)type;
            return (Class<?>)parameterizedType.getActualTypeArguments()[0];
        }
        return null;
    }

    /**
     * 启动ParseBean的方法
     * @param bytes
     * @return
     * @throws Exception
     */
    Item start(byte[] bytes) throws Exception;
}
