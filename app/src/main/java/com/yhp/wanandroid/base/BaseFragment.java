package com.yhp.wanandroid.base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yhp.wanandroid.constant.Constant;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    private Activity mActivity;
    protected SharedPreferences pref;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(setLayoutResourceID(), container, false);
        ButterKnife.bind(this, view);

        pref = getMActivity().getSharedPreferences(Constant.USER_PREF, getMActivity().MODE_PRIVATE);
        initView();
        initData();

        return view;
    }

    /**
     * 设置布局文件资源ID
     * @return 布局文件资源ID
     */
    protected abstract int setLayoutResourceID();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    public Activity getMActivity() {
        return mActivity;
    }
}
