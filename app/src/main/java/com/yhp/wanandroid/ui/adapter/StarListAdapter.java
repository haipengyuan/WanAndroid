package com.yhp.wanandroid.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yhp.wanandroid.R;
import com.yhp.wanandroid.bean.StarArticleDatas;
import com.yhp.wanandroid.ui.adapter.viewholder.BannerViewHolder;
import com.yhp.wanandroid.ui.adapter.viewholder.FooterViewHolder;
import com.yhp.wanandroid.ui.adapter.viewholder.HomeArticlesViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的收藏列表 适配器
 */
public class StarListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

    private static final int FIRST_POSITION = 0;

    private static final int TYPE_HEADER = 100;
    private static final int TYPE_FOOTER = 101;
    private static final int TYPE_NORMAL = 102;

    /**
     * 当前加载状态，默认为加载完成
     */
    private int loadState = LOADING_COMPLETE;

    /**
     * 数据列表
     */
    private List<StarArticleDatas> mDatasList = new ArrayList<>();

    private Context mContext;

    /**
     * 头部视图（Banner）
     */
    private View mHeaderView;

    /**
     * 尾部视图（正在加载）
     */
    private View mFooterView;

    private OnItemClickListener mOnItemClickListener;

    private OnItemChildClickListener mOnItemChildClickListener;

    /**
     * 标记分类信息是否显示
     *    true：显示
     *    false：不显示
     */
    private boolean mCategoryFlag = true;

    public StarListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public StarListAdapter(Context mContext, boolean mCategoryFlag) {
        this.mContext = mContext;
        this.mCategoryFlag = mCategoryFlag;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new BannerViewHolder(mHeaderView);
        }

        if (viewType == TYPE_FOOTER) {
            return new FooterViewHolder(mFooterView);
        }

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_home_article, parent, false);
        return new HomeArticlesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            return;
        }

        if (getItemViewType(position) == TYPE_NORMAL) {
            if (viewHolder instanceof HomeArticlesViewHolder) {
                HomeArticlesViewHolder holder = (HomeArticlesViewHolder) viewHolder;

                final int pos = (mHeaderView == null) ? position : position - 1;
                if (mDatasList.size() > 0) {
                    StarArticleDatas value = mDatasList.get(pos);
                    if ("".equals(value.author) || "null".equals(value.author)) {
                        holder.author.setText("");
                    } else {
                        holder.author.setText(mContext.getResources()
                                .getString(R.string.author_prefix) + value.author);
                    }
                    holder.date.setText(value.niceDate);
                    holder.title.setText(value.title);
                    if (mCategoryFlag) {
                        holder.category.setText((value.chapterName).trim());
                    } else {
                        // 不显示分类信息
                        holder.category.setVisibility(View.GONE);
                    }
                    holder.starIV.setImageResource(R.drawable.ic_my_star);
                }

                if (mOnItemClickListener != null) {
                    holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mOnItemClickListener.onClick(view, pos);
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
                    holder.starIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mOnItemChildClickListener.onClick(view, pos);
                        }
                    });
                }
            }
        } else if (getItemViewType(position) == TYPE_FOOTER) {
            if (viewHolder instanceof FooterViewHolder) {
                FooterViewHolder footerHolder = (FooterViewHolder) viewHolder;
                switch (loadState) {
                    case LOADING:
                        footerHolder.progressBar.setVisibility(View.VISIBLE);
                        footerHolder.textView.setVisibility(View.VISIBLE);
                        footerHolder.linearLayout.setVisibility(View.GONE);
                        break;
                    case LOADING_COMPLETE:
                        footerHolder.progressBar.setVisibility(View.GONE);
                        footerHolder.textView.setVisibility(View.GONE);
                        footerHolder.linearLayout.setVisibility(View.GONE);
                        break;
                    case LOADING_NULL:
                        footerHolder.progressBar.setVisibility(View.GONE);
                        footerHolder.textView.setVisibility(View.GONE);
                        footerHolder.linearLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == FIRST_POSITION && mHeaderView != null) {
            return TYPE_HEADER;
        } else if (position == getItemCount() - 1 && mFooterView != null) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        if (mHeaderView == null && mFooterView == null) {
            return mDatasList.size();
        } else if (mHeaderView == null && mFooterView != null) {
            return mDatasList.size() + 1;
        } else if (mHeaderView != null && mFooterView == null) {
            return mDatasList.size() + 1;
        } else {
            return mDatasList.size() + 2;
        }
    }

    /**
     * 设置上拉加载状态
     * @param loadState
     */
    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
    }

    /**
     * 设置头部视图Header
     * @param headerView
     */
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(FIRST_POSITION);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    /**
     * 设置尾部视图Footer
     * @param footerView
     */
    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    public View getFooterView() {
        return mFooterView;
    }

    /**
     * 获取列表中数据的总数量
     * @return 数据总数
     */
    public int getDataCount() {
        return mDatasList.size();
    }

    /**
     * 添加一组数据
     * @param list 要添加的数据集合
     */
    public void addItems(List list) {
        mDatasList.addAll(list);
        notifyItemRangeInserted(mDatasList.size(), list.size());
    }

    /**
     * 清空数据
     */
    public void clear() {
        mDatasList.clear();
        notifyDataSetChanged();
    }

    public StarArticleDatas getItem(int position) {
        return mDatasList.get(position);
    }

    /**
     * Item项点击事件监听器
     */
    public interface OnItemClickListener {
        void onClick(View view, int position);
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
