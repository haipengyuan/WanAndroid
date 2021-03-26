package com.yhp.wanandroid.mvp.contract;

import com.yhp.wanandroid.base.BasePresenter;
import com.yhp.wanandroid.base.BaseView;
import com.yhp.wanandroid.bean.NetworkResponse;
import com.yhp.wanandroid.bean.StarListData;

public interface MyStarContract {

    interface View extends BaseView<MyStarContract.Presenter> {
        /**
         * 获取我的收藏的文章成功
         * @param response 响应数据
         */
        void onArticlesSuccess(NetworkResponse<StarListData> response);

        /**
         * 获取我的收藏的文章成功失败
         * @param e 异常
         */
        void onArticlesError(Throwable e);

        void onCancelStarSuccess(NetworkResponse<String> response);

        void onCancelStarError(Throwable e);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取收藏的文章数据
         * @param page 页码
         */
        void getStarArticles(int page);

        /**
         * 取消收藏
         * @param id
         * @param originId
         */
        void cancelStarArticle(int id, int originId);
    }
}
