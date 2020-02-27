package com.wsf.info;

import com.wsf.config.Configure;
import com.wsf.domain.HtmlInfo;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.CookiePolicy;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * 该工具类主要用于html的基本信息获取
 * 1. 网页的编码方式
 * 2. 请求是否有gzip压缩
 */
@SuppressWarnings("all")
public class GetHtmlInfo {
    private static Logger logger = Logger.getLogger(GetHtmlInfo.class);
    //默认使用配置文件中的请求头
    private static Map<String, String> requestHeader = Configure.getRequestHeader();
    private static Integer connTimeout = Configure.getConnTimeout();
    private static Integer readTimeout = Configure.getReadTimeout();
    private static byte[] response;
    private static String contentEncode = null;

    /**
     * 获取网站的基本信息
     *
     * @param url
     * @param connTimeout
     * @param readTimeout
     * @param header
     * @return
     */
    public static HtmlInfo get(String url) {
        HtmlInfo info = new HtmlInfo();
        try {
            URL u = new URL(url);
            URLConnection connection = u.openConnection();
            //添加配置请求头
            if (requestHeader != null && requestHeader.size() > 0) {
                for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
                    connection.setRequestProperty(entry.getKey().split("\\.")[1], entry.getValue());
                }
            }
            //设置超时时间
            if (connTimeout != null) {
                connection.setConnectTimeout(connTimeout);
            }
            if (readTimeout != null) {
                connection.setReadTimeout(readTimeout);
            }
            connection.connect();
            String encode = connection.getContentType();
            contentEncode = connection.getContentEncoding();
//            String contentEncoding = connection.getContentEncoding();
//            System.out.println("网站内容编码:"+contentEncoding);

            if (encode != null && encode.contains("charset")) {
                encode = encode.split("=")[1];
            } else {
                encode = null;
            }
            if (encode == null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                String line = null;
                StringBuilder sb = new StringBuilder();
                String ret = null;
                while ((line = br.readLine()) != null) {
                    ret = getEncodeFromHTML(line);
                    if (ret != null) {
                        encode = ret;
                        break;
                    }
                }
            }
            info.setCharset(encode);

            BufferedInputStream bis = null;
            if(connection.getContentEncoding()==null||!connection.getContentEncoding().equals("gzip")) {
                bis = new BufferedInputStream(connection.getInputStream());
            }else{
                bis = new BufferedInputStream(new GZIPInputStream(connection.getInputStream()));
            }
            byte[] bytes = new byte[1024];
            int len = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((len = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
            response = bos.toByteArray();
        } catch (IOException e) {
            logger.error("网站访问失败！");
        }
        return info;
    }

    public static String getEncodeFromHTML(String html) {
        String reg = "meta.*charset=[\"']?(.+?)[\"']";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    //获取网页的压缩编码方式，如果这个值不为nul，表示网页进行了压缩编码处理，需要解码后再编码
    public static String getContentEncode(String url) {
        get(url);
        return contentEncode;
    }

    public static byte[] getResponse(String url){
        get(url);
        return response;
    }

    public static Map<String, String> getRequestHeader() {
        return requestHeader;
    }

    public static void setRequestHeader(Map<String, String> requestHeader) {
        GetHtmlInfo.requestHeader = requestHeader;
    }

    public static Integer getConnTimeout() {
        return connTimeout;
    }

    public static void setConnTimeout(Integer connTimeout) {
        GetHtmlInfo.connTimeout = connTimeout;
    }

    public static Integer getReadTimeout() {
        return readTimeout;
    }

    public static void setReadTimeout(Integer readTimeout) {
        GetHtmlInfo.readTimeout = readTimeout;
    }
}
