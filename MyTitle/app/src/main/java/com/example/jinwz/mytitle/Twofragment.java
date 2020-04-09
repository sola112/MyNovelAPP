package com.example.jinwz.mytitle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class Twofragment extends Fragment {
    private List<BookInfo> booklist;//创建小说信息数据源
    private List<Bookchapter> chapterlist;//创建小说章节数据源
    private RecyclerView recyclerView;//创建recyclerview布局
    private BookAdapter bookadapter;//创建小说数据适配器
    private int page = 1;
    private String url = "https://www.qidian.com/all?orderId=&style=1&pageSize=20&siteid=1&pubflag=0&hiddenField=0&page=";//标明数据来源网址
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.twofragment,null);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        booklist = new ArrayList<BookInfo>();//初始化小说信息数据源
        bookadapter = new BookAdapter(getActivity(),booklist);//初始化小说数据适配器
        recyclerView = (RecyclerView)view.findViewById(R.id.books_list);//获取页面内recyclerview布局
        Log.d("Debug001", "lala");
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//设置布局管理器
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画效果
        recyclerView.setAdapter(bookadapter);//设置布局内数据适配器

        //设置各个item单击事件
        bookadapter.setOnItemClickListener(new BookAdapter.OnItemClickListener(){
            @Override
            public void OnItemClick(View v , int position){
                /*
                Toast toast = Toast.makeText(getContext(),booklist.get(position).getBookname(),Toast.LENGTH_LONG);
                toast.show();
                */
                Intent intent = new Intent(getActivity(),BookCatalogActivity.class);
                String href = booklist.get(position).getBookhref();
                intent.putExtra("href",href);
                startActivity(intent);

            }
        });

        //设置加载更多监听
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                bookadapter.setLoadState(bookadapter.LOADING);
                Log.d("Debugggg", url + String.valueOf(page));
                if (booklist.size() < 52){
                    //获取网络数据
                    page = page+1;
                    Log.d("Debugggg", url + String.valueOf(page));
                    loadWebData(url + String.valueOf(page));
                }else{
                    bookadapter.setLoadState(bookadapter.LOADING_END);
                }
            }
        });

        loadWebData(url+"1");//加载网络数据
        //loadWebData("https://www.qidian.com/all?orderId=&style=1&pageSize=20&siteid=1&pubflag=0&hiddenField=0&page=2");
    }


    private void loadWebData(final String url){//后台抓取网络资源，前台处理机制
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... params){
                String s = "";
                Log.d("Debug001", "准备爬取");
                try{
                    Log.d("Debug002", "开始爬取");
                    Document document = Jsoup.connect(url).timeout(4000).userAgent("Mozilla").get();//获取url内资源
                    Log.d("Debug003", "开始爬取2:"+document);
                    Elements context = document.getAllElements();//获取所有元素（方便传参解析）
                    Log.d("Debug004", "开始爬取3");
                    s = context.toString();//转换为字符串
                    Log.d("Debug005", "开始爬取4："+s);
                }catch (Exception e){
                    Log.d("Debug006", "爬取失败");

                }
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                List<BookInfo> blist = new ArrayList<>();//创建小说信息资源缓存区
                List<BookInfo> clist = new ArrayList<>();//创建小说章节资源缓存区
                BookInfo bookinfo = new BookInfo();//初始化小说资源对象
                Log.d("Debug007", "准备解析");
                if (s!=null&&!s.isEmpty()&&s!="") {
                    Log.d("Debug008", "开始解析1");
                    Document subdocument = Jsoup.parse(s);//解析字符串为html文档
                    Log.d("Debug009", "开始解析2");
                    Elements context = subdocument.getElementsByClass("book-mid-info");//获取所有小说目录
                    Elements context_img = subdocument.getElementsByClass("book-img-box");//获取所有小说图片目录
                    for (int i=0;i<context.size()&&i<20;i++){
                        bookinfo = new BookInfo();//初始化小说资源对象
                        Element context2 = context.get(i);//获取单个小说信息
                        bookinfo.setBookhref("http:"+context2.select("a").get(0).attr("href"));//获取小说章节目录url并存储
                        bookinfo.setBookauthor(context2.select("a").get(1).text()//获取小说作者并存储
                        );
                        bookinfo.setBookname(context2.select("a").get(0).text());//获取小说书名并存储
                        bookinfo.setBookintro(context2.getElementsByClass("intro").get(0).text());//获取小说介绍并存储
                        bookinfo.setBookpic("http:"+context_img.get(i).select("img").get(0).attr("src"));//获取小说图片url并存储
                        blist.add(bookinfo);//将此小说资源对象存储，并进入下一个小说资源对象存储的活动
                    }
                    booklist.addAll(blist);//将所有小说资源对象进行存储
                    bookadapter.notifyDataSetChanged();//提醒界面更新数据
                }else {
                    Log.d("Debug001", "解析失败");
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
                    //textView.setText("加载失败，请稍后重试。。。");
                }
            }
        }.execute();//执行
    }
}
