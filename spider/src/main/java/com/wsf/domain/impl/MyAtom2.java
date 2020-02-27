package com.wsf.domain.impl;

import com.wsf.domain.Atom;

public class MyAtom2 implements Atom {
    private String typename;
    private String title;
    private String pic;

    @Override
    public String toString() {
        return "MyAtom2{" +
                "typename='" + typename + '\'' +
                ", title='" + title + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }
}
