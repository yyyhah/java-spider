package com.wsf.domain;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.List;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Item extends Serializable {
    /**
     * 通过反射设置属性值
     * @param property 属性 比如 atoms[1].number
     * @param value 需要设置成的属性值 比如10
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    Logger logger = Logger.getLogger(Item.class);
    /**
     * 设置url
     */
    void setUrl(String url);

    String getUrl();

    default void setProperty(String property,Object value){
        try {
            String[] split = property.split("\\.");
            Object temp = this;
            for (int i = 0; i < split.length - 1; i++) {
                if (temp == null) {
                    logger.error("没有找到该属性值");
                    return;
                }
                temp = get(temp, split[i]);
            }
            set(temp, split[split.length - 1], value);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("无法找到该属性");
        }
    }

    /**
     * 获取属性值
     * @param object
     * @param property
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    default Object get(Object object, String property) throws InvocationTargetException, IllegalAccessException {
        //如果是map类型，返回键值
        if(object instanceof Map){
            return ((Map) object).get(property);
        }
        String reg = "(.*)\\[(\\d+)\\]";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(property);
        String element = null;
        Integer index = null;
        if(matcher.find()){
            element = matcher.group(1);
            index = Integer.parseInt(matcher.group(2));
        }
        Method[] methods = object.getClass().getMethods();
        for (Method method : methods) {
            if(element == null && index != null) {
                if (("get" + property).equalsIgnoreCase(method.getName())) {
                    return method.invoke(object);
                }
            }else{
                if (("get" + element).equalsIgnoreCase(method.getName())) {
                    Object invoke = method.invoke(object);
                    if(invoke instanceof List){
                        return ((List) invoke).get(index);
                    }else if(invoke instanceof Object[]){
                        return ((Object[])invoke)[index];
                    }
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * 设置属性值
     * @param object
     * @param property
     * @param value
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    default void set(Object object,String property,Object value) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = object.getClass().getMethods();
        for (Method method : methods) {
            if(("set"+property).equalsIgnoreCase(method.getName())){
                method.invoke(object,value);
            }
        }
    }

    /**
     * 获取路径元素的字节码
     * @param property
     * @return
     */
    default Object findType(String property){
        String[] split = property.split(".");
        Object object = this;
        for (int i = 0; i < split.length; i++) {
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                //找到该属性
                if(field.getName().equalsIgnoreCase(split[i])){
                    //获取该元素的类型，如果该元素为List类型，且不为最后一个，返回其泛型
                    object = field.getType();
                    if(object instanceof List && i != split.length-1){
                        object = field.getGenericType();
                    }
                }

            }
        }
        return object;
    }
}
