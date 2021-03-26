package com.yhp.wanandroid.mvp.presenter;

import com.yhp.wanandroid.bean.LoginData;
import com.yhp.wanandroid.bean.NetworkResponse;
import com.yhp.wanandroid.mvp.contract.LoginContract;
import com.yhp.wanandroid.network.LoginService;
import com.yhp.wanandroid.network.RetrofitHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.Presenter {

    private static final String TAG = LoginPresenter.class.getSimpleName();

    private LoginService mService = RetrofitHelper.createLoginService();
    private LoginContract.View mView;

    public LoginPresenter(LoginContract.View view) {
        mView = view;
    }

    @Override
    public void login(String username, String password) {
        mService.login(username, password)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Consumer<NetworkResponse<LoginData>>() {
                                    @Override
                                    public void accept(NetworkResponse<LoginData> response) throws Exception {
                                        if (response == null) {
                                            mView.onLoginError(new Throwable("登录失败"));
                                        }
                        mView.onLoginSuccess(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.onLoginError(throwable);
                    }
                });
    }
}
