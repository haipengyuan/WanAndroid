package com.yhp.wanandroid.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yhp.wanandroid.ui.fragment.ArticleListFragment;

import java.util.ArrayList;
import java.util.List;


public class CategoryDetailPagerAdapter extends FragmentStatePagerAdapter {

    private List<ArticleListFragment> fragments;

    public CategoryDetailPagerAdapter(FragmentManager fm, List<ArticleListFragment> list) {
        super(fm);
        fragments = list;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
