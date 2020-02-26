package com.wsf.domain;

import java.util.ArrayList;

/**
 * 该类保存页面中提取信息的规则,基本通用模板，提取元素之间不会有关联。如果需要元素之间有关联需要自己定义Item
 * ，限定集合只能使用ArrayList类型,基本类型只能用String
 */
public class BaseItem implements Item {
    //从哪个网址获取的资源
    private String url;
    //atom表示最小的信息单元,List或数组的内部必须是该接口或其实现类
    private ArrayList<MyAtom> atoms = new ArrayList<>();


    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<MyAtom> getAtoms() {
        return atoms;
    }

    public void setAtoms(ArrayList<MyAtom> atoms) {
        this.atoms = atoms;
    }

    @Override
    public String toString() {
        return "BaseItem{" +
                "url='" + url + '\'' +
                ", atoms=" + atoms +
                '}';
    }
}
