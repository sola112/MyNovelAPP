package com.example.jinwz.mytitle;

public class BookInfo {
    private String bookhref;
    private String bookname;
    private String bookauthor;
    private String bookintro;
    private String bookpic;
    public String getBookhref(){
        return bookhref;
    }
    public String getBookname(){
        return bookname;
    }
    public String getBookauthor(){
        return bookauthor;
    }
    public String getBookintro(){
        return bookintro;
    }
    public String getBookpic(){
        return bookpic;
    }
    public void setBookhref(String bhref){
        this.bookhref = bhref;
    }
    public void setBookname(String bname){
        this.bookname = bname;
    }
    public void setBookauthor(String bauthor){
        this.bookauthor = bauthor;
    }
    public void setBookintro(String bintro){
        this.bookintro = bintro;
    }
    public void setBookpic(String bookpic){
        this.bookpic = bookpic;
    }
}
