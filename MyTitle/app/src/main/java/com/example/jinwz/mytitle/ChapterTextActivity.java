package com.example.jinwz.mytitle;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ChapterTextActivity extends Activity {
    private List<BookText> textlist;//创建章节内容数据源
    private RecyclerView recyclerView;//创建recyclerview布局
    private TextAdapter textAdapter;//创建小说数据适配器
    private String href;//标明数据来源网址
    private String chaptername;
    private TextView chapter_title;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);//创建存储
        setContentView(R.layout.text_activity);//设置小说目录视图为主活动页面

        //获取小说href
        Intent intent2 = getIntent();
        href = intent2.getStringExtra("chapterhref");
        chaptername = intent2.getStringExtra("chaptername");
        Log.d("Debugchapter", href);

        //chapter_title = (TextView)findViewById(R.id.text_title);
        //chapter_title.setText(chaptername);

        textlist = new ArrayList<BookText>();//初始化小说信息数据源
        textAdapter = new TextAdapter(this,textlist);//初始化小说数据适配器
        recyclerView = (RecyclerView)findViewById(R.id.books_text);//获取页面内recyclerview布局
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//设置布局管理器
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画效果
        recyclerView.setAdapter(textAdapter);//设置布局内数据适配器

        loadWebData(href);//加载网络数据

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                boolean b = recyclerView.canScrollVertically(-1);
                Log.d("Debug000", "----->??? "+b);
            }
        });
    }
    private void loadWebData(final String href){
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... params){
                String s = "";
                Log.d("Debug001", "准备爬取章节");
                try{
                    Log.d("Debug002", "开始爬取章节");
                    Document document = Jsoup.connect(href).timeout(4000).userAgent("Mozilla").get();//获取url内资源
                    Log.d("Debug003", "开始爬取章节2:"+document);
                    Elements context = document.getAllElements();//获取所有元素（方便传参解析）
                    Log.d("Debug004", "开始爬取章节3");
                    s = context.toString();//转换为字符串
                    Log.d("Debug005", "开始爬取章节4："+s);
                }catch (Exception e){
                    Log.d("Debug006", "爬取章节失败");
                }
                return s;
            }
            @Override
            protected void onPostExecute(String s){
                List<BookText> cText = new ArrayList<>();//创建小说信息资源缓存区
                BookText chapter  = new BookText();//初始化小说资源对象
                String chaptertext="";

                Log.d("Debug007", "准备解析章节");
                if (s!=null&&!s.isEmpty()) {
                    Log.d("Debug008", "开始解析章节1");
                    Document subdocument = Jsoup.parse(s);//解析字符串为html文档
                    Log.d("Debug009", "开始解析章节2");
                    Elements context = subdocument.getElementsByClass("read-content j_readContent");//获取所有章节段落
                    Log.d("Debug009", "开始解析章节3:"+context.toString());
                    Elements chapters = context.select("p");
                    Log.d("Debug009", "开始解析章节4:"+chapters.toString());
                    for (int i=0;i<chapters.size()/9;i++){
                        chapter = new BookText();//初始化章节内容资源对象
                        Element chapter2 = chapters.get(i);//获取单个段落信息
                        Log.d("chapter2", chapter2.toString());
                        chaptertext = chaptertext+chapter2.text()+"\n";
                    }
                    Log.d("chaptertext", chaptertext);
                    chapter.setText(chaptertext);
                    cText.add(chapter);
                    textlist.addAll(cText);//将所有章节资源对象进行存储
                    textAdapter.notifyDataSetChanged();//提醒界面更新数据
                }else {
                    Log.d("Debug001", "解析章节失败");
                    /*
                    System.out.print("3333333");
                    bookinfo.setBookid("112");
                    bookinfo.setBookauthor("lala");
                    bookinfo.setBookintro("heihei");
                    bookinfo.setBookname("lala");
                    bookinfo.setBookpic("https://bookcover.yuewen.com/qdbimg/349573/1010734492/150");
                    clist.add(bookinfo);
                    booklist.addAll(clist);
                    bookadapter.notifyDataSetChanged();
                    */
                }
            }
        }.execute();//执行
    }
}
