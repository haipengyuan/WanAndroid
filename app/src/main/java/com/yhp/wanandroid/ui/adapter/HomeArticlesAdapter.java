package com.yhp.wanandroid.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yhp.wanandroid.R;
import com.yhp.wanandroid.bean.Datas;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeArticlesAdapter extends RecyclerView.Adapter<HomeArticlesAdapter.ViewHolder> {

    private List<Datas> mDatasList = new ArrayList<>();

    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.article_author)
        TextView author;
        @BindView(R.id.article_date)
        TextView date;
        @BindView(R.id.article_title)
        TextView title;
        @BindView(R.id.article_category)
        TextView category;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public HomeArticlesAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_article, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Datas datas = mDatasList.get(position);
        holder.author.setText(mContext.getResources().getString(R.string.author_prefix) + datas.author);
        holder.date.setText(datas.niceDate);
        holder.title.setText(datas.title);
        holder.category.setText(datas.chapterName);
    }

    @Override
    public int getItemCount() {
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
}