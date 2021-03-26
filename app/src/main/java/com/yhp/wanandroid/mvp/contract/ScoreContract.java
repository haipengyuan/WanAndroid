package com.yhp.wanandroid.mvp.contract;

import com.yhp.wanandroid.base.BasePresenter;
import com.yhp.wanandroid.base.BaseView;
import com.yhp.wanandroid.bean.NetworkResponse;
import com.yhp.wanandroid.bean.ScoreData;

public interface ScoreContract {

    interface View extends BaseView<Presenter> {
        void onScoreSuccess(NetworkResponse<ScoreData> response);

        void onScoreError(Throwable e);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取我的积分信息
         */
        void getScore();
    }

}
