package com.wsf.domain;


import java.util.ArrayList;

public class MyAtom implements Atom{
    private String author;
    private String title;
    private String chapterHref;
    private ArrayList<String> books;
    public MyAtom() {
    }

    public ArrayList<String> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<String> books) {
        this.books = books;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChapterHref() {
        return chapterHref;
    }

    public void setChapterHref(String chapterHref) {
        this.chapterHref = chapterHref;
    }

    @Override
    public String toString() {
        return "MyAtom{" +
                "author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", chapterHref='" + chapterHref + '\'' +
                ", books=" + books +
                '}';
    }
}
