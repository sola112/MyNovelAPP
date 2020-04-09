package com.example.jinwz.mytitle;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class BookCatalogActivity extends Activity {
    private TextView textView;
    private List<Bookchapter> chapterlist;//创建小说章节数据源
    private RecyclerView recyclerView;//创建recyclerview布局
    private ChapterAdapter chapterAdapter;//创建小说数据适配器
    private String href;//标明数据来源网址

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);//创建存储
        setContentView(R.layout.catalog_activity);//设置小说目录视图为主活动页面

        //获取小说href
        Intent intent = getIntent();
        href = intent.getStringExtra("href");
        Log.d("Debug", href);
        chapterlist = new ArrayList<Bookchapter>();//初始化小说信息数据源
        chapterAdapter = new ChapterAdapter(this,chapterlist);//初始化小说数据适配器
        recyclerView = (RecyclerView)findViewById(R.id.chapters_list);//获取页面内recyclerview布局
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//设置布局管理器
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画效果
        recyclerView.setAdapter(chapterAdapter);//设置布局内数据适配器

        chapterAdapter.setOnItemClickListener(new ChapterAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int position) {
                Intent intent = new Intent(BookCatalogActivity.this,ChapterTextActivity.class);
                String chapterhref = chapterlist.get(position).getChapterhref();
                String chaptername = chapterlist.get(position).getChaptername();
                intent.putExtra("chapterhref",chapterhref);
                intent.putExtra("chaptername",chaptername);
                startActivity(intent);
            }
        });

        loadWebData(href);//加载网络数据
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
                List<Bookchapter> clist = new ArrayList<>();//创建小说信息资源缓存区
                Bookchapter chapter  = new Bookchapter();//初始化小说资源对象
                String chaptername="";
                String chapterhref="";

                Log.d("Debug007", "准备解析章节");
                if (s!=null&&!s.isEmpty()) {
                    Log.d("Debug008", "开始解析章节1");
                    Document subdocument = Jsoup.parse(s);//解析字符串为html文档
                    Log.d("Debug009", "开始解析章节2");
                    Elements context = subdocument.getElementsByClass("catalog-content-wrap");//获取所有小说章节目录
                    Log.d("Debug009", "开始解析章节3:"+context.toString());
                    Elements chapters = context.select("a");
                    Log.d("Debug009", "开始解析章节4:"+chapters.toString());
                    for (int i=0;i<chapters.size()/6;i++){
                        chapter = new Bookchapter();//初始化小说资源对象
                        Element chapter2 = chapters.get(i);//获取单个小说信息
                        if (chapter2.hasAttr("title")&&chapter2.hasAttr("href")) {
                            chaptername = chapter2.text();
                            chapterhref = "https:"+chapter2.attr("href");
                            chapter.setChapterhref(chapterhref);//获取小说章节目录url并存储
                            chapter.setChaptername(chaptername);//获取章节名并存储
                            clist.add(chapter);//将此章节资源对象存储，并进入下一个小说资源对象存储的活动
                        }
                    }
                    chapterlist.addAll(clist);//将所有章节资源对象进行存储
                    chapterAdapter.notifyDataSetChanged();//提醒界面更新数据
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
