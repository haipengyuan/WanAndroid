package com.yhp.wanandroid.mvp.presenter;

import com.yhp.wanandroid.MainActivity;
import com.yhp.wanandroid.bean.NetworkResponse;
import com.yhp.wanandroid.mvp.contract.MainContract;
import com.yhp.wanandroid.network.LoginService;
import com.yhp.wanandroid.network.RetrofitHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private LoginService mService = RetrofitHelper.createLoginService();

    public MainPresenter(MainContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void logout() {
        mService.logout()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<NetworkResponse<String>>() {
                    @Override
                    public void accept(NetworkResponse<String> response) throws Exception {
                        mView.onLogoutSuccess(response);
                    }
                });

    }
}
