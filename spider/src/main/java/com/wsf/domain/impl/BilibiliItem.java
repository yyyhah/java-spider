package com.wsf.domain.impl;

import com.wsf.domain.Item;

import java.util.ArrayList;

public class BilibiliItem implements Item {
    private String url;
    private ArrayList<MyAtom2> atoms;

    public String getUrl() {
        return url;
    }

    public ArrayList<MyAtom2> getAtoms() {
        return atoms;
    }

    public void setAtoms(ArrayList<MyAtom2> atoms) {
        this.atoms = atoms;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "BilibiliItem{" +
                "url='" + url + '\'' +
                ", atoms=" + atoms +
                '}';
    }
}
