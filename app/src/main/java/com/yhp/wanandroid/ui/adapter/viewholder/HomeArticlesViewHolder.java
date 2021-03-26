package com.yhp.wanandroid.ui.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yhp.wanandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeArticlesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.article_author)
    public TextView author;
    @BindView(R.id.article_date)
    public TextView date;
    @BindView(R.id.article_title)
    public TextView title;
    @BindView(R.id.article_category)
    public TextView category;
    @BindView(R.id.relative_layout)
    public RelativeLayout relativeLayout;
    @BindView(R.id.article_stared)
    public ImageView starIV;

    public HomeArticlesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
