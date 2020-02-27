package com.wsf.domain.impl;

import com.wsf.domain.Atom;

public class MyAtom2 implements Atom {
    private String name;
    private String bofang;
    private String pinlun;

    @Override
    public String toString() {
        return "MyAtom2{" +
                "name='" + name + '\'' +
                ", bofang='" + bofang + '\'' +
                ", pinlun='" + pinlun + '\'' +
                '}';
    }
}
