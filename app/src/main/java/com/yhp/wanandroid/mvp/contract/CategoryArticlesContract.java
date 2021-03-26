package com.yhp.wanandroid.mvp.contract;

import com.yhp.wanandroid.base.BasePresenter;
import com.yhp.wanandroid.base.BaseView;
import com.yhp.wanandroid.bean.HomeArticlesData;
import com.yhp.wanandroid.bean.NetworkResponse;

public interface CategoryArticlesContract {

    interface View extends BaseView<Presenter> {
        /**
         * 获取分类下文章成功
         * @param response 响应数据
         */
        void onArticlesSuccess(NetworkResponse<HomeArticlesData> response);

        /**
         * 获取分类下文章失败
         * @param e 异常
         */
        void onArticlesError(Throwable e);

        void onCancelStarSuccess(NetworkResponse<String> response);

        void onCancelStarError(Throwable e);

        void onAddStarSuccess(NetworkResponse<String> response);

        void onAddStarError(Throwable e);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取分类下文章数据
         * @param page 页码
         * @param cid 分类id
         */
        void getHomeArticles(int page, int cid);

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
