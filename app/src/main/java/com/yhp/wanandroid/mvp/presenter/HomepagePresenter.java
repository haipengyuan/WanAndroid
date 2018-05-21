package com.yhp.wanandroid.mvp.presenter;

import android.util.Log;

import com.yhp.wanandroid.bean.HomeArticlesResponse;
import com.yhp.wanandroid.mvp.contract.HomepageContract;
import com.yhp.wanandroid.network.HomepageService;
import com.yhp.wanandroid.network.RetrofitHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomepagePresenter implements HomepageContract.Presenter {

    private static final String TAG = HomepagePresenter.class.getSimpleName();
    private HomepageService mService = RetrofitHelper.createHomepageService();
    private HomepageContract.View mView;

    public HomepagePresenter(HomepageContract.View view) {
        mView = view;
//        mView.setPresenter(this);
    }


    @Override
    public void getHomeArticles(int page) {
        mService.getHomepageArticles(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<HomeArticlesResponse>() {
                    @Override
                    public void accept(HomeArticlesResponse homeArticlesResponse) throws Exception {
                        if (homeArticlesResponse == null) {
                            mView.onError(new Throwable("请求失败"));
                        }
                        mView.onSuccess(homeArticlesResponse);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                });
    }
}
