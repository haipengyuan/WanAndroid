package com.yhp.wanandroid.ui.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yhp.wanandroid.R;

public class FooterViewHolder extends RecyclerView.ViewHolder {

    public ProgressBar progressBar;
    public TextView textView;
    public LinearLayout linearLayout;

    public FooterViewHolder(View itemView) {
        super(itemView);

        progressBar = itemView.findViewById(R.id.pb_loading);
        textView = itemView.findViewById(R.id.tv_loading);
        linearLayout = itemView.findViewById(R.id.ll_loading_end);
    }
}
