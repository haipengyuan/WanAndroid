package com.yhp.wanandroid.mvp.contract;

import com.yhp.wanandroid.base.BasePresenter;
import com.yhp.wanandroid.base.BaseView;
import com.yhp.wanandroid.bean.NetworkResponse;

public interface MainContract {

    interface View extends BaseView<Presenter> {
        void onLogoutSuccess(NetworkResponse<String> response);
        // void onLogoutError(Throwable e);
    }

    interface Presenter extends BasePresenter {
        void logout();
    }
}
