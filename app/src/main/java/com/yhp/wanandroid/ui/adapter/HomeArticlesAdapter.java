package com.yhp.wanandroid.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.yhp.wanandroid.R;
import com.yhp.wanandroid.bean.ArticleDatas;
import com.yhp.wanandroid.ui.adapter.viewholder.BannerViewHolder;
import com.yhp.wanandroid.ui.adapter.viewholder.HomeArticlesViewHolder;
import com.yhp.wanandroid.util.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;


public class HomeArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_BANNER = 100;
    private static final int LIST_FIRST_POSITION = 0;

    private List<ArticleDatas> mDatasList = new ArrayList<>();
    private List<String> mImages = new ArrayList<>();

    private Context mContext;

    private View mHeaderView;

    private OnItemClickListener mOnItemClickListener;

    private OnItemChildClickListener mOnItemChildClickListener;

    private BannerViewHolder mBannerViewHolder;

    public HomeArticlesAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_BANNER) {
            mBannerViewHolder = new BannerViewHolder(mHeaderView);
            return mBannerViewHolder;
        }

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_home_article, parent, false);
        return new HomeArticlesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        if (getItemViewType(position) == TYPE_BANNER) {
            return;
        }

        final int pos = mHeaderView == null ? viewHolder.getLayoutPosition()
                : viewHolder.getLayoutPosition() - 1;
        if (viewHolder instanceof HomeArticlesViewHolder) {
            HomeArticlesViewHolder holder = (HomeArticlesViewHolder) viewHolder;
            ArticleDatas value = mDatasList.get(pos);
            holder.author.setText(mContext.getResources().getString(R.string.author_prefix) + value.author);
            holder.date.setText(value.niceDate);
            holder.title.setText(value.title);
            holder.category.setText(value.chapterName);

            if (mOnItemClickListener != null) {
                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.onClick(pos);
                    }
                });
            }

            if (mOnItemChildClickListener != null) {
                holder.category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemChildClickListener.onClick(view, pos);
                    }
                });
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == LIST_FIRST_POSITION) {
            return TYPE_BANNER;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mDatasList.size() : mDatasList.size() + 1;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    /**
     * 添加一组数据
     * @param list 要添加的数据集合
     */
    public void addItems(List list) {
        mDatasList.addAll(list);
        notifyItemRangeInserted(mDatasList.size(), list.size());
        notifyDataSetChanged();
    }

    /**
     * 清空数据
     */
    public void clear() {
        mDatasList.clear();
        notifyDataSetChanged();
    }

    public ArticleDatas getItem(int position) {
        return mDatasList.get(position);
    }

/*    public void setBannerImages(List<String> images) {
        mImages = images;
        mBannerViewHolder.banner.update(mImages);
    }*/

    /**
     * Item项点击事件监听器
     */
    public interface OnItemClickListener {
        void onClick(int position);
    }

    /**
     * Item项的子控件点击事件监听器
     */
    public interface OnItemChildClickListener {
        void onClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setOnItemChildClickListener(OnItemChildClickListener listener) {
        mOnItemChildClickListener = listener;
    }
}
