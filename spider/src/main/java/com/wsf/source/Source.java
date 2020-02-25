package com.wsf.source;

import com.wsf.config.Configure;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 资源池
 */
@SuppressWarnings("all")
public class Source {
    //url池
    private static ConcurrentLinkedQueue<ConcurrentLinkedQueue> urlBuffer;
    //html池
    private static ConcurrentLinkedQueue<ConcurrentHashMap> htmlBuffer;
    //item池
    private static ConcurrentLinkedQueue<ConcurrentHashMap> itemBuffer;

    private static String sourceFile = null;

    private static Integer sourceSize = null;

    private static Double minSize = null;

    private static Double maxSize = null;

    //标识当前磁盘中是否有数据的，如果没有的的话就不会从磁盘读取
    private static Boolean hasUrlResource = null;

    private static Boolean hasHtmlResource = null;

    private static Boolean hasItemResource = null;

    private static Logger logger = Logger.getLogger(Source.class);

    //初始化资源池
    static {
        urlBuffer = new ConcurrentLinkedQueue<>();
        htmlBuffer = new ConcurrentLinkedQueue<>();
        itemBuffer = new ConcurrentLinkedQueue<>();
        sourceFile = Configure.getSourceFile();
        sourceSize = Configure.getSourceSize();
        maxSize = Configure.getMaxSize();
        minSize = Configure.getMinSize();
        File rootFile = new File(sourceFile);
        try {
            //判断资源池文件是否存在
            if (!rootFile.exists()) {
                //如果不存在则创建
                rootFile.mkdirs();
            }
            //判断url文件池文件是否存在
            readFile("url");
            readFile("html");
            readFile("item");
        } catch (Exception e) {
            throw new ExceptionInInitializerError("资源池文件读取出错");
        }
    }


    private static void readFile(String fileName) {
        File f = new File(sourceFile, fileName);
        if (!f.exists()) {
            f.mkdir();
            hasItemResource = false;
            hasUrlResource = false;
            hasHtmlResource = false;
        } else {
            //筛选出资源文件
            File[] files = f.listFiles(file -> file.getName().endsWith("." + fileName + "s"));
            ObjectInputStream ois = null;
            try {
                for (File file : files) {
                    ois = new ObjectInputStream(new FileInputStream(file));
                    switch (fileName) {
                        case "url":
                            if (urlBuffer.size() > sourceSize) {
                                hasUrlResource = true;
                                return;
                            }
                            //将资源文件中的资源保存进来
                            urlBuffer.addAll((ConcurrentLinkedQueue<ConcurrentLinkedQueue>) ois.readObject());
                            break;
                        case "html":
                            if (htmlBuffer.size() > sourceSize) {
                                hasHtmlResource = true;
                                return;
                            }
                            htmlBuffer.addAll((ConcurrentLinkedQueue<ConcurrentHashMap>) ois.readObject());
                            break;
                        case "item":
                            if (itemBuffer.size() > sourceSize) {
                                hasItemResource = true;
                                return;
                            }
                            itemBuffer.addAll((ConcurrentLinkedQueue<ConcurrentHashMap>) ois.readObject());

                            break;
                        default:
                            return;
                    }
                    ois.close();
                    file.delete();
                }
            } catch (Exception e) {
                logger.error("从磁盘中读取" + fileName + "出错");
            }finally {
                if(ois!=null){
                    try {
                        ois.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            switch (fileName) {
                case "url":
                    hasUrlResource = false;
                    break;
                case "html":
                    hasHtmlResource = false;
                    break;
                case "item":
                    hasItemResource = false;
                    break;
            }
        }
    }
    public static void writeToFile(String fileName){
        writeToFile(fileName,null);
    }

    public static void writeToFile(String fileName,Integer min) {
        File f = new File(sourceFile, fileName);
        ObjectOutputStream oos = null;
        ConcurrentLinkedQueue temp = null;
        int lowest = (int)(sourceSize * minSize);
        if(min!=null && min >=0){
            lowest = min;
        }
        try {
            switch (fileName) {
                case "url":
                    File urlFile = new File(f, "" + System.currentTimeMillis() + UUID.randomUUID() + "." + fileName + "s");
                    temp = new ConcurrentLinkedQueue();
                    urlFile.createNewFile();
                    oos = new ObjectOutputStream(new FileOutputStream(urlFile));
                    while (urlBuffer.size() > lowest) {
                        temp.add(urlBuffer.poll());
                    }
                    oos.writeObject(temp);
                    hasUrlResource = true;
                    break;
                case "html":
                    File htmlFile = new File(f, "" + System.currentTimeMillis() + UUID.randomUUID() + "." + fileName + "s");
                    temp = new ConcurrentLinkedQueue();
                    htmlFile.createNewFile();
                    oos = new ObjectOutputStream(new FileOutputStream(htmlFile));
                    while (htmlBuffer.size() > lowest) {
                        temp.add(htmlBuffer.poll());
                    }
                    oos.writeObject(temp);
                    hasHtmlResource = true;
                    break;
                case "item":
                    File itemFile = new File(f, "" + System.currentTimeMillis() + UUID.randomUUID() + "." + fileName + "s");
                    temp = new ConcurrentLinkedQueue();
                    itemFile.createNewFile();
                    oos = new ObjectOutputStream(new FileOutputStream(itemFile));
                    while (itemBuffer.size() >  lowest) {
                        temp.add(itemBuffer.poll());
                    }
                    oos.writeObject(temp);
                    hasItemResource = true;
                    break;
                default:
                    return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("将" + fileName + "写入磁盘文件出错！");
        }finally {
            if(oos!=null){
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static ConcurrentLinkedQueue<ConcurrentLinkedQueue> getUrlBuffer() {
        return urlBuffer;
    }

    public static void setUrlBuffer(ConcurrentLinkedQueue<ConcurrentLinkedQueue> urlBuffer) {
        Source.urlBuffer = urlBuffer;
    }

    public static ConcurrentLinkedQueue<ConcurrentHashMap> getHtmlBuffer() {
        return htmlBuffer;
    }

    public static void setHtmlBuffer(ConcurrentLinkedQueue<ConcurrentHashMap> htmlBuffer) {
        Source.htmlBuffer = htmlBuffer;
    }

    public static ConcurrentLinkedQueue<ConcurrentHashMap> getItemBuffer() {
        return itemBuffer;
    }

    public static void setItemBuffer(ConcurrentLinkedQueue<ConcurrentHashMap> itemBuffer) {
        Source.itemBuffer = itemBuffer;
    }

    //将数据从磁盘读取到内存
    public static void loadHtml() {
        if (hasHtmlResource) {
            readFile("html");
        }
    }

    public static void loadItem() {
        if (hasItemResource) {
            readFile("item");
        }
    }

    public static void loadUrl() {
        if (hasUrlResource) {
            readFile("url");
        }
    }

    //将数据从内存写到磁盘
    public static void unloadHtml() {
        writeToFile("html");
    }

    public static void unloadItem() {
        writeToFile("item");
    }

    public static void unloadUrl() {
        writeToFile("url");
    }

    public static String getSourceFile() {
        return sourceFile;
    }

    public static void setSourceFile(String sourceFile) {
        Source.sourceFile = sourceFile;
    }

    public static Integer getSourceSize() {
        return sourceSize;
    }

    public static void setSourceSize(Integer sourceSize) {
        Source.sourceSize = sourceSize;
    }

    public static Double getMinSize() {
        return minSize;
    }

    public static void setMinSize(Double minSize) {
        Source.minSize = minSize;
    }

    public static Double getMaxSize() {
        return maxSize;
    }

    public static void setMaxSize(Double maxSize) {
        Source.maxSize = maxSize;
    }


    public static Boolean getHasUrlResource() {
        return hasUrlResource;
    }

    public static void setHasUrlResource(Boolean hasUrlResource) {
        Source.hasUrlResource = hasUrlResource;
    }

    public static Boolean getHasHtmlResource() {
        return hasHtmlResource;
    }

    public static void setHasHtmlResource(Boolean hasHtmlResource) {
        Source.hasHtmlResource = hasHtmlResource;
    }

    public static Boolean getHasItemResource() {
        return hasItemResource;
    }

    public static void setHasItemResource(Boolean hasItemResource) {
        Source.hasItemResource = hasItemResource;
    }
    public static void close(){
        writeToFile("html",0);
        writeToFile("url",0);
        writeToFile("item",0);
        htmlBuffer = null;
        urlBuffer = null;
        itemBuffer = null;
    }
}
