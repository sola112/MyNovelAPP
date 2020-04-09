package com.example.jinwz.mytitle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;

public class ChapterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;//创建上下文
    private List<Bookchapter> mDataList;//创建数据源
    private LayoutInflater mLayoutInflater;//
    private OnItemClickListener onItemClickListener;

    public ChapterAdapter(Context mContext,List<Bookchapter> mDataList){
        this.mContext=mContext;//获取上下文环境
        this.mDataList=mDataList;//获取数据源
        this.mLayoutInflater=LayoutInflater.from(mContext);//
    }
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup,int i){
        View v =mLayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chapters_card,viewGroup,false);//从上下文中获取
        return new ViewHolder(v);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{//创建拥有所有所需视图的对象
        TextView cname;

        public ViewHolder(View itemView){
            super(itemView);
            cname = (TextView)itemView.findViewById(R.id.chapters_name);//获取所需小说书名视图对象
            }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {//进行数据适配
        Bookchapter entity = mDataList.get(position);//获取对应位置（顺延）
        if (null==entity) return;//若为空，则不分配视图空间
        final ViewHolder viewHolder = (ViewHolder) holder;//获取viewholder对象
        if (entity.getChaptername()==null||entity.getChapterhref()==null) return;
        Log.d("Debug111", entity.getChaptername().toString());
        boolean a = entity.getChaptername() instanceof String ;
        Log.d("Debug111", entity.getChapterhref());
        if(a==true){Log.d("Debug111","true");}
        viewHolder.cname.setText(entity.getChaptername());//章节名
        String c_href = entity.getChapterhref();//章节href

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClick(viewHolder.itemView,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }//继承所必须类

    public interface OnItemClickListener{
        void OnItemClick(View v,int position);
    }

}
