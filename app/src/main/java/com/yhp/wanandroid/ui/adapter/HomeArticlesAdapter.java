package com.yhp.wanandroid.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.yhp.wanandroid.R;
import com.yhp.wanandroid.bean.ArticleDatas;
import com.yhp.wanandroid.ui.adapter.viewholder.BannerViewHolder;
import com.yhp.wanandroid.ui.adapter.viewholder.FooterViewHolder;
import com.yhp.wanandroid.ui.adapter.viewholder.HomeArticlesViewHolder;

import java.util.ArrayList;
import java.util.List;


public class HomeArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int LIST_FIRST_POSITION = 0;
    private static final int TYPE_BANNER = 100;
    private static final int TYPE_FOOTER = 101;

    /**
     * 正在加载
     */
    public static final int LOADING = 200;

    /**
     * 加载完成
     */
    public static final int LOADING_COMPLETE = 201;

    /**
     * 没有更多内容
     */
    public static final int LOADING_NULL = 202;

    private List<ArticleDatas> mDatasList = new ArrayList<>();

    private Context mContext;

    private View mHeaderView;

    private OnItemClickListener mOnItemClickListener;

    private OnItemChildClickListener mOnItemChildClickListener;

    private BannerViewHolder mBannerViewHolder;

    /**
     * 标记分类信息是否显示
     *    true：显示
     *    false：不显示
     */
    private boolean mCategoryFlag = true;

    /**
     * 当前加载状态，默认为加载完成
     */
    private int loadState = LOADING_COMPLETE;

    public HomeArticlesAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * 构造方法
     * @param categoryFlag 设置Category分类信息是否显示 false：不显示
     */
    public HomeArticlesAdapter(Context context, boolean categoryFlag) {
        this.mContext = context;
        this.mCategoryFlag = categoryFlag;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_BANNER) {
            mBannerViewHolder = new BannerViewHolder(mHeaderView);
            return mBannerViewHolder;
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.refresh_footer, parent, false);
            return new FooterViewHolder(view);
        }

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_home_article, parent, false);
        return new HomeArticlesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        if (getItemViewType(position) == TYPE_BANNER && mHeaderView != null) {
            return;
        }

        final int pos = (mHeaderView == null) ? viewHolder.getLayoutPosition()
                : viewHolder.getLayoutPosition() - 1;
        if (viewHolder instanceof HomeArticlesViewHolder) {
            HomeArticlesViewHolder holder = (HomeArticlesViewHolder) viewHolder;
            if (mDatasList.size() > 0) {
                ArticleDatas value = mDatasList.get(pos);
                holder.author.setText(mContext.getResources().getString(R.string.author_prefix) + value.author);
                holder.date.setText(value.niceDate);
                holder.title.setText(value.title);
                if (mCategoryFlag) {
                    holder.category.setText(value.chapterName);
                } else {
                    // 不显示分类信息
                    holder.category.setVisibility(View.GONE);
                }
            }

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
        } else if (viewHolder instanceof FooterViewHolder) {
            FooterViewHolder holder = (FooterViewHolder) viewHolder;

            switch (loadState) {
                case LOADING:
                    holder.progressBar.setVisibility(View.VISIBLE);
                    holder.textView.setVisibility(View.VISIBLE);
                    holder.linearLayout.setVisibility(View.GONE);
                    break;
                case LOADING_COMPLETE:
                    holder.progressBar.setVisibility(View.GONE);
                    holder.textView.setVisibility(View.GONE);
                    holder.linearLayout.setVisibility(View.GONE);
                    break;
                case LOADING_NULL:
                    holder.progressBar.setVisibility(View.GONE);
                    holder.textView.setVisibility(View.GONE);
                    holder.linearLayout.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == LIST_FIRST_POSITION) {
            return TYPE_BANNER;
        } else if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
//        return mHeaderView == null ? mDatasList.size() : mDatasList.size() + 1;
        return mHeaderView == null ? mDatasList.size() + 1 : mDatasList.size() + 2;
    }

    /**
     * 设置上拉加载状态
     * @param loadState
     */
    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
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
 //       notifyDataSetChanged();
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
