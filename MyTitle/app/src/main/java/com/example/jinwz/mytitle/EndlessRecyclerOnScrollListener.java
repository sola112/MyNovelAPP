package com.example.jinwz.mytitle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    private boolean isSlidingUpward = false;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView,int newState){
        super.onScrollStateChanged(recyclerView,newState);
        LinearLayoutManager manager = (LinearLayoutManager)recyclerView.getLayoutManager();
        //当不滑动时
        if (newState == RecyclerView.SCROLL_STATE_IDLE){
            //获取最后一个完全显示的itemPosition
            int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
            int itemCount = manager.getItemCount();

            //判断是否滑动到了最后一个item，并且是向上滑动
            if (lastItemPosition == (itemCount - 1) && isSlidingUpward){
                //加载更多
                onLoadMore();
            }
        }
    }
    @Override
    public void onScrolled(RecyclerView recyclerView,int dx,int dy){
        super.onScrolled(recyclerView,dx,dy);
        isSlidingUpward = dy > 0;
    }

    public abstract void onLoadMore();
}
