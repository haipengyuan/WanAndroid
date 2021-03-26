package com.yhp.wanandroid.mvp.contract;

import com.yhp.wanandroid.base.BasePresenter;
import com.yhp.wanandroid.base.BaseView;
import com.yhp.wanandroid.bean.HomeArticlesData;
import com.yhp.wanandroid.bean.HomeBannerData;
import com.yhp.wanandroid.bean.NetworkResponse;

import java.util.List;


public interface HomepageContract {

    interface View extends BaseView<Presenter> {

        /**
         * 获取首页文章成功
         * @param response 响应数据
         */
        void onArticlesSuccess(NetworkResponse<HomeArticlesData> response);

        /**
         * 获取首页文章失败
         * @param e 异常
         */
        void onArticlesError(Throwable e);

        /**
         * 获取首页Banner图成功
         * @param response 响应数据
         */
        void onBannerSuccess(NetworkResponse<List<HomeBannerData>> response);

        /**
         * 获取首页Banner图失败
         * @param e 异常
         */
        void onBannerError(Throwable e);

        void onCancelStarSuccess(NetworkResponse<String> response);

        void onCancelStarError(Throwable e);

        void onAddStarSuccess(NetworkResponse<String> response);

        void onAddStarError(Throwable e);
    }

    interface Presenter extends BasePresenter {

        /**
         * 获取首页文章数据
         * @param page 页码
         */
        void getHomeArticles(int page);

        /**
         * 获取首页Banner图
         */
        void getBanner();

        /**
         * 取消收藏
         * @param id
         */
        void cancelStarArticle(int id);

        /**
         * 收藏文章
         * @param id
         */
        void addStarArticle(int id);
    }

}
