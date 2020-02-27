package com.wsf.info;

import com.wsf.domain.HtmlInfo;
import com.wsf.domain.Item;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.io.IOException;

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
    @Test
    public void testGetResponse() throws ClassNotFoundException, IOException {
        String json = new String(GetHtmlInfo.getResponse("https://api.bilibili.com/x/web-interface/ranking/region?rid=33&day=3&original=0"), "utf-8");
        JSONObject jsonObject = JSONObject.fromObject(json);
        JSONArray data = jsonObject.getJSONArray("data");
        data.size();
        System.out.println();
    }
}
