package com.wsf.parse.bean;

import com.wsf.domain.Item;
import com.wsf.domain.Template;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.util.List;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * 解析json字节流的
 */
@SuppressWarnings("all")
public class JsonParseBean implements IParseBean{
    private Template template;
    private static Logger logger = Logger.getLogger(HtmlParseBean.class);
    private HashMap<String, String> jsonPath;

    public JsonParseBean(Template template) {
        this.template = template;
        this.jsonPath = template.getElementPath();
    }

    /**
     * 利用反射将json数据包装进Item实现类
     * @param path
     * @param jsonObject
     * @param property
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    public Object createProperty(String path, JSONObject jsonObject, Class<?> property) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Object object= property.getConstructor().newInstance();
        Field[] fields = property.getDeclaredFields();
        for (Field field : fields) {
            //设置私有变量可以访问
            field.setAccessible(true);
            String jsonElementPath = path.length()> 0 ?path + "." + field.getName():field.getName();
            if(path.length() == 0 && field.getName().equals("url")){
                continue;
            }
            //如果是基本类型的,直接赋值
            if(field.getType().getName().equals("java.lang.String")){
                field.set(object, jsonObject.get(jsonPath.get(jsonElementPath)));
            }else if(field.getType().getName().equals("java.util.ArrayList")){
                //如果是list类型
                List list = (List)field.getType().getConstructor().newInstance();
                //如果泛型为基本类型
                if(getGenericType(field.getGenericType()).getName().equals("java.lang.String")){
                    JSONArray jsonArray = jsonObject.getJSONArray(jsonPath.get(jsonElementPath));
                    for (int i = 0; i < jsonArray.size(); i++) {
                        list.add((String)jsonArray.get(i));
                    }
                    field.set(object,list);
                }else {
                    JSONArray jsonArray = jsonObject.getJSONArray(jsonPath.get(jsonElementPath));
                    for (int i = 0; i < jsonArray.size(); i++) {
                        list.add(createProperty(jsonElementPath,(JSONObject)jsonArray.get(i),getGenericType(field.getGenericType())));
                    }
                    field.set(object,list);
                }
            }else{
                //如果是非基本类型
                field.set(object,createProperty(jsonElementPath, (JSONObject) jsonObject.get(jsonPath.get(jsonElementPath)), field.getType()));
            }
        }
        return object;
    }

    @Override
    public Item start(byte[] bytes) throws Exception {
        String json = new String(bytes, template.getCharset());
        JSONObject jsonObject = JSONObject.fromObject(json);
        return (Item) createProperty("",jsonObject,Class.forName(template.getItem()));
    }
}
