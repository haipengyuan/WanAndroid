package com.yhp.wanandroid.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yhp.wanandroid.R;
import com.yhp.wanandroid.mvp.contract.ArticleContentContract;

public class ArticleContentFragment extends Fragment implements ArticleContentContract.View {

    public static ArticleContentFragment newInstance() {
        ArticleContentFragment fragment = new ArticleContentFragment();
        return fragment;
    }

    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article_content, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setPresenter(ArticleContentContract.Presenter presenter) {}
}
