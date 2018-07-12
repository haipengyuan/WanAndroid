package com.yhp.wanandroid.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yhp.wanandroid.R;
import com.yhp.wanandroid.constant.Constant;
import com.yhp.wanandroid.ui.activity.CategoryDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context mContext;

    /**
     * 一级标题
     */
    private List<String> mFirstTitleList = new ArrayList<>();

    /**
     * 二级标题
     */
    private Map<String, List<String>> mSecondaryTitleMap = new HashMap<>();

    /**
     * 二级标题id
     */
    private Map<String, ArrayList<Integer>> mSecondaryIDMap = new HashMap<>();

    private OnItemClickListener mOnItemClickListener;

    public CategoryAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String firstTitle = mFirstTitleList.get(position);
        holder.firstTitle.setText(firstTitle);

        final List<String> secondaryTitleList = mSecondaryTitleMap.get(firstTitle);
        final ArrayList<Integer> secondaryIDList = mSecondaryIDMap.get(firstTitle);
        String secondaryTitle = titleListToString(secondaryTitleList);
        holder.secondaryTitle.setText(secondaryTitle);

        if (mOnItemClickListener != null) {
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(position);
                }
            });
        }

 /*       if (holder.getAdapterPosition() == mFirstTitleList.size() - 1) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 0);
            holder.relativeLayout.setLayoutParams(layoutParams);
        }*/

/*        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CategoryDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constant.ARG_FIRST_TITLE, firstTitle);
                bundle.putStringArray(Constant.ARG_SECONDARY_TITLE,
                        secondaryTitleList.toArray(new String[secondaryTitleList.size()]));
                bundle.putIntegerArrayList(Constant.CATEGORY_ID_LIST, secondaryIDList);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mFirstTitleList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;
        TextView firstTitle;
        TextView secondaryTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.relative_layout);
            firstTitle = itemView.findViewById(R.id.first_title);
            secondaryTitle = itemView.findViewById(R.id.secondary_title);
        }
    }

    /**
     * 设置一级标题数据
     * @param items
     */
    public void setFirstTitleItems(List<String> items) {
        mFirstTitleList = items;
        notifyDataSetChanged();
    }

    /**
     * 设置二级标题数据
     * @param map
     */
    public void setSecondaryTitleMap(Map<String, List<String>> map) {
        mSecondaryTitleMap = map;
    }

    public void setSecondaryIDMap(Map<String, ArrayList<Integer>> map) {
        mSecondaryIDMap = map;
    }

    /**
     * String集合转为字符串
     * @param list
     * @return
     */
    private String titleListToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i != list.size() - 1) {
                sb.append("    ");
            }
        }
        return sb.toString();
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }
}
