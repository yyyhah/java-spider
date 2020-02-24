package com.wsf.info;

import com.wsf.config.Configure;
import com.wsf.domain.HtmlInfo;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 该类主要用于html的基本信息获取
 * 1. 网页的编码方式
 * 2. 请求是否有gzip压缩
 */
@SuppressWarnings("all")
public class GetHtmlInfo {
    private static Logger logger = Logger.getLogger(GetHtmlInfo.class);
    //默认使用配置文件中的请求头
    private static Map<String, String> requestHeader = Configure.getRequestHeader();

    public static HtmlInfo get(String url, Integer readTimeout, HashMap<String, String> header) {
        return get(url, null, readTimeout, header);
    }

    public static HtmlInfo get(String url, HashMap<String, String> header) {
        return get(url, null, null, header);
    }

    public static HtmlInfo get(String url) {
        return get(url, null, null, null);
    }

    /**
     * 获取网站的基本信息
     *
     * @param url
     * @param connTimeout
     * @param readTimeout
     * @param header
     * @return
     */
    public static HtmlInfo get(String url, Integer connTimeout, Integer readTimeout, HashMap<String, String> header) {
        HtmlInfo info = new HtmlInfo();
        try {
            URL u = new URL(url);
            URLConnection connection = u.openConnection();
            if (header != null) {
                requestHeader = header;
            }
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
}
