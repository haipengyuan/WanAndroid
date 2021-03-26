package com.yhp.wanandroid.mvp.contract;

import com.yhp.wanandroid.base.BasePresenter;
import com.yhp.wanandroid.base.BaseView;
import com.yhp.wanandroid.bean.LoginData;
import com.yhp.wanandroid.bean.NetworkResponse;

public interface LoginContract {

    interface View extends BaseView<Presenter> {
        /**
         * 登录成功
         * @param response 响应数据
         */
        void onLoginSuccess(NetworkResponse<LoginData> response);

        /**
         * 登录失败
         * @param e 异常
         */
        void onLoginError(Throwable e);
    }

    interface Presenter extends BasePresenter {

        void login(String username, String password);
    }
}
