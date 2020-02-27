package com.wsf.parse.bean;


import com.wsf.domain.Item;
import com.wsf.domain.Template;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 解析html字节流
 */
public class HtmlParseBean implements IParseBean{
    private Template template;
    private static Logger logger = Logger.getLogger(HtmlParseBean.class);
    private HashMap<String, String> elementCss;

    public HtmlParseBean(Template template) {
        this.template = template;
        this.elementCss = template.getElementPath();
    }


    /**
     * 利用反射递归生成item里对应的属性
     * @param elements
     * @return
     */
    private Object createProperty(String path,Elements elements,Class<?> property) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Object object = property.getConstructor().newInstance();
        Field[] fields = property.getDeclaredFields();
        for (Field field : fields) {
            //设置私有变量可以访问
            field.setAccessible(true);
            String cssPath = path.length()> 0 ?path + "." + field.getName():field.getName();
            if(path.length() == 0 && field.getName().equals("url")){
                continue;
            }
            //如果是基本类型的,直接赋值
            if(field.getType().getName().equals("java.lang.String")){
                //切分css路径判断其是text还是attr
                String[] split = elementCss.get(cssPath).split(";");
                Elements select = elements.select(split[0]);
                //如果没有该元素，直接跳过
                if(select == null){
                    break;
                }
                if(split.length == 1){
                    field.set(object, select.text());
                }else if(split.length == 2){
                    field.set(object, select.attr(split[1]));
                }else{
                    logger.error("请检查 "+path+"."+field.getName()+"的css路径是否写错");
                }
            }else if(field.getType().getName().equals("java.util.ArrayList")){
                //如果是list类型
                List list = (List)field.getType().getConstructor().newInstance();
                //如果泛型为基本类型
                if(getGenericType(field.getGenericType()).getName().equals("java.lang.String")){
                    //切分css路径判断其是text还是attr
                    String[] split = elementCss.get(cssPath).split(";");
                    Elements select = elements.select(split[0]);
                    if(select == null){
                        break;
                    }
                    if(split.length == 1){
                        for (Element element : select) {
                            list.add(element.text());
                        }
                    }else if(split.length == 2){
                        for (Element element : select) {
                            list.add(element.attr(split[1]));
                        }
                    }else {
                        logger.error("请检查 "+path+"."+field.getName()+"的css路径是否写错");
                    }
                    field.set(object,list);
                }else {
                    Elements es = elements.select(elementCss.get(cssPath));
                    for(Element e:es){
                        list.add(createProperty(cssPath, e.select("*"), getGenericType(field.getGenericType())));
                    }

                    field.set(object,list);
                }
            }else{
                //如果是非基本类型
                field.set(object,createProperty(cssPath, elements.select(elementCss.get(cssPath)), field.getType()));
            }
        }
        return object;
    }


    /**
     * 启动ParseBean的方法，解析byte字节，调用createProperty方法将html文档信息封装到BeanItem中返回
     * @param bytes
     * @return
     * @throws Exception
     */
    @Override
    public Item start(byte[] bytes) throws Exception {
        //将网页源码编码
        String html = new String(bytes, template.getCharset());
        //解析文档树
        Document document = Jsoup.parse(html);
        return (Item)createProperty("", document.select("html"), Class.forName(template.getItem()));
    }


}
