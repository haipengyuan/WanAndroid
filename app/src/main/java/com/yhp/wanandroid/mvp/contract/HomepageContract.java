package com.yhp.wanandroid.mvp.contract;

import com.yhp.wanandroid.base.BasePresenter;
import com.yhp.wanandroid.base.BaseView;
import com.yhp.wanandroid.bean.HomeArticlesResponse;


public interface HomepageContract {

    interface View extends BaseView<Presenter> {

        void onSuccess(HomeArticlesResponse response);

        void onError(Throwable e);
    }

    interface Presenter extends BasePresenter {

        void getHomeArticles(int page);

    }

}
