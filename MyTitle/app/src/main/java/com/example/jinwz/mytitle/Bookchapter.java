package com.example.jinwz.mytitle;

public class Bookchapter {
    private String bookid;
    private String chaptername;
    private String chapterhref;
    public String getBookid(){
        return bookid;
    }
    public String getChaptername(){
        return chaptername;
    }
    public String getChapterhref(){
        return chapterhref;
    }
    public void setBookid(String bookid){
        this.bookid=bookid;
    }
    public void setChaptername(String chaptername){
        this.chaptername = chaptername;
    }
    public void setChapterhref(String chapterhref){
        this.chapterhref = chapterhref;
    }
}
