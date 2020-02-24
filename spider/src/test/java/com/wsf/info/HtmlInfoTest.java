package com.wsf.info;

import com.wsf.domain.HtmlInfo;
import org.junit.Test;

public class HtmlInfoTest {
    @Test
    public void testGet(){
        HtmlInfo htmlInfo = GetHtmlInfo.get("https://www.baidu.com/");
        System.out.println(htmlInfo);
    }

    @Test
    public void testGetEncodeFromHTML(){
        String htmlInfo = GetHtmlInfo.getEncodeFromHTML("<meta charset=\"utf-8\">");
        System.out.println(htmlInfo);
    }
}
