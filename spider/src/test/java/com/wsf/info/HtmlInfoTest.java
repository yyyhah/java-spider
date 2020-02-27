package com.wsf.info;

import com.wsf.domain.HtmlInfo;
import org.junit.Test;

public class HtmlInfoTest {
    @Test
    public void testGet(){
        HtmlInfo htmlInfo = GetHtmlInfo.get("https://www.bilibili.com/anime/?spm_id_from=333.6.b_7375626e6176.1");
        System.out.println(htmlInfo);
    }

    @Test
    public void testGetEncodeFromHTML(){
        String contentEncode = GetHtmlInfo.getContentEncode("https://www.bilibili.com/anime/?spm_id_from=333.6.b_7375626e6176.1");
        System.out.println(contentEncode);
    }
}
