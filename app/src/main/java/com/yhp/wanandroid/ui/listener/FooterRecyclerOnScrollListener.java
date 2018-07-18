package com.yhp.wanandroid.ui.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * FooterView滚动监听，实现上拉加载功能
 */
public abstract class FooterRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    // 是否向上滑动
    private boolean isSlidingUpward = false;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

        // 当不滑动时
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            // 获取最后一个完全显示的itemPosition
            int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();

            int itemCount = manager.getItemCount();

            // 回弹效果  如果最后一个完全显示的是倒数第二个，则FooterView显示不完全，进行回弹
            if (lastItemPosition == manager.getItemCount() - 2) {
                // 获取第一个完全显示的itemPosition
                int fcp = manager.findFirstCompletelyVisibleItemPosition();

                View child = manager.findViewByPosition(lastItemPosition);

                // 计算出FooterView移动的距离
                int deltaY = recyclerView.getBottom() - recyclerView.getPaddingBottom()
                        - child.getBottom();

                // fcp为0时说明列表已位于顶部, 无法向下滚动
                if (deltaY > 0 && fcp != 0) {
                    // 将RecyclerView向下滚动deltaY距离，不显示FooterView
                    recyclerView.smoothScrollBy(0, -deltaY);
                }
            } else if (lastItemPosition == (itemCount - 1) && isSlidingUpward) {
                // 判断是否滑动到了最后一个item，并且是向上滑动

                // 加载更多
                onLoadMore();
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        // 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
        isSlidingUpward = dy > 0;
    }

    /**
     * 加载数据
     */
    public abstract void onLoadMore();
}
