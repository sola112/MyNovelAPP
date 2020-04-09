package com.example.jinwz.mytitle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;

public class TextAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;//创建上下文
    private List<BookText> mDataList;//创建数据源
    private LayoutInflater mLayoutInflater;//

    public TextAdapter(Context mContext, List<BookText> mDataList) {
        this.mContext = mContext;//获取上下文环境
        this.mDataList = mDataList;//获取数据源
        this.mLayoutInflater = LayoutInflater.from(mContext);//
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = mLayoutInflater.from(viewGroup.getContext()).inflate(R.layout.text_card, viewGroup, false);//从上下文中获取
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {//创建拥有所有所需视图的对象
        TextView book_text;

        public ViewHolder(View itemView) {
            super(itemView);
            book_text = (TextView) itemView.findViewById(R.id.book_text);//获取所需小说书名视图对象
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {//进行数据适配
        BookText entity = mDataList.get(position);//获取对应位置（顺延）
        if (null == entity) return;//若为空，则不分配视图空间
        final ViewHolder viewHolder = (ViewHolder) holder;//获取viewholder对象
        viewHolder.book_text.setText(entity.getText());//书名数据适配
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }//继承所必须类
}