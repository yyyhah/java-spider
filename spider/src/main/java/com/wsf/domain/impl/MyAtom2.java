package com.wsf.domain.impl;

import com.wsf.domain.Atom;

public class MyAtom2 implements Atom {
    private String typename;
    private String title;
    private String pic;

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    @Override
    public String toString() {
        return "MyAtom2{" +
                "typename='" + typename + '\'' +
                ", title='" + title + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }
}
