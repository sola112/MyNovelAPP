package com.example.jinwz.mytitle;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;

public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;//创建上下文
    private List<BookInfo> mDataList;//创建数据源
    private LayoutInflater mLayoutInflater;//
    private OnItemClickListener onItemClickListener;

    //上拉加载
    private final int TYPE_ITEM = 1;
    private final int TYPE_FOOTER = 2;
    private int loadState = 2;
    public final int LOADING = 1;
    public final int LOADING_COMPLETE = 2;
    public final int LOADING_END = 3;

    public void LoadMoreAdapter(List<BookInfo> dataList){
        this.mDataList = dataList;
    }

    public BookAdapter(Context mContext,List<BookInfo> mDataList){
        this.mContext=mContext;//获取上下文环境
        this.mDataList=mDataList;//获取数据源
        this.mLayoutInflater=LayoutInflater.from(mContext);//
    }
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup,int i){
        //View v =mLayoutInflater.from(viewGroup.getContext()).inflate(R.layout.books_card,viewGroup,false);//从上下文中获取
        if(i == TYPE_ITEM){
            View v =mLayoutInflater.from(viewGroup.getContext()).inflate(R.layout.books_card,viewGroup,false);//从上下文中获取
            return new ViewHolder(v);
        }else if (i == TYPE_FOOTER){
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.footview,viewGroup,false);
            return new FootViewHolder(v);
        }
        return null;
        //return new ViewHolder(v);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{//创建拥有所有所需视图的对象
        TextView books_name,books_author,books_intro;//创建文本视图
        ImageView books_img;//创建图像视图

        public ViewHolder(View itemView){
            super(itemView);
            books_name = (TextView)itemView.findViewById(R.id.books_name);//获取所需小说书名视图对象
            books_author = (TextView)itemView.findViewById(R.id.books_author);//获取所需小说作者视图对象
            books_intro = (TextView)itemView.findViewById(R.id.books_intro);//获取所需小说介绍视图对象
            books_img = (ImageView)itemView.findViewById(R.id.books_img);//获取所需小说图片视图对象
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {//进行数据适配
        /*
        BookInfo entity = mDataList.get(position);//获取对应位置（顺延）
        if (null==entity) return;//若为空，则不分配视图空间
        final ViewHolder viewHolder = (ViewHolder) holder;//获取viewholder对象
        viewHolder.books_name.setText(entity.getBookname());//书名数据适配
        String intro = entity.getBookintro();//小说介绍超字数处理
        if (intro.length()>60){
            intro = intro.substring(0,60)+"......";
        }
        viewHolder.books_intro.setText(intro);//小说介绍数据适配
        viewHolder.books_author.setText("  作者："+entity.getBookauthor());//小说作者数据适配
        Picasso.with(mContext).load(entity.getBookpic()).into(viewHolder.books_img);//小说图像数据加载（网络url）+适配
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClick(viewHolder.itemView,position);
            }
        });
        */
        if (holder instanceof ViewHolder) {
            BookInfo entity = mDataList.get(position);//获取对应位置（顺延）
            if (null==entity) return;//若为空，则不分配视图空间
            final ViewHolder viewHolder = (ViewHolder) holder;//获取viewholder对象
            viewHolder.books_name.setText(entity.getBookname());//书名数据适配
            String intro = entity.getBookintro();//小说介绍超字数处理
            if (intro.length()>60){
                intro = intro.substring(0,60)+"......";
            }
            viewHolder.books_intro.setText(intro);//小说介绍数据适配
            viewHolder.books_author.setText("  作者："+entity.getBookauthor());//小说作者数据适配
            Picasso.with(mContext).load(entity.getBookpic()).into(viewHolder.books_img);//小说图像数据加载（网络url）+适配
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.OnItemClick(viewHolder.itemView,position);
                }
            });

        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (loadState) {
                case LOADING: // 正在加载
                    //footViewHolder.pbLoading.setVisibility(View.VISIBLE);
                    //footViewHolder.tvLoading.setVisibility(View.VISIBLE);
                    //footViewHolder.llEnd.setVisibility(View.GONE);
                    footViewHolder.tvLoading.setText("正在加载");
                    break;

                case LOADING_COMPLETE: // 加载完成
                    //footViewHolder.pbLoading.setVisibility(View.INVISIBLE);
                    //footViewHolder.tvLoading.setVisibility(View.INVISIBLE);
                    //footViewHolder.llEnd.setVisibility(View.GONE);
                    footViewHolder.tvLoading.setText("加载完成");
                    break;

                case LOADING_END: // 加载到底
                    //footViewHolder.pbLoading.setVisibility(View.GONE);
                    //footViewHolder.tvLoading.setVisibility(View.GONE);
                    //footViewHolder.llEnd.setVisibility(View.VISIBLE);
                    footViewHolder.tvLoading.setText("加载到底");
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size()+1;
    }//继承所必须类

    @Override
    public int getItemViewType(int position){
        if (position +1 ==getItemCount()){
            return TYPE_FOOTER;
        }else {
            return TYPE_ITEM;
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(View v,int position);
    }

    private class FootViewHolder extends RecyclerView.ViewHolder {

        ProgressBar pbLoading;
        TextView tvLoading;
        LinearLayout llEnd;

        FootViewHolder(View itemView) {
            super(itemView);
            //pbLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
            tvLoading = (TextView) itemView.findViewById(R.id.tv_loading);
            //llEnd = (LinearLayout) itemView.findViewById(R.id.ll_end);
        }
    }
    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager = ((GridLayoutManager)manager);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_FOOTER ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }
}
