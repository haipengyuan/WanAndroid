package com.yhp.wanandroid.mvp.presenter;

import com.yhp.wanandroid.bean.HomeArticlesData;
import com.yhp.wanandroid.bean.HomeBannerData;
import com.yhp.wanandroid.bean.NetworkResponse;
import com.yhp.wanandroid.mvp.contract.HomepageContract;
import com.yhp.wanandroid.network.HomepageService;
import com.yhp.wanandroid.network.RetrofitHelper;

import java.util.List;

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
                .subscribe(new Consumer<NetworkResponse<HomeArticlesData>>() {
                    @Override
                    public void accept(NetworkResponse<HomeArticlesData> response) throws Exception {
                        if (response == null) {
                            mView.onArticlesError(new Throwable("首页文章数据请求失败"));
                        }
                        mView.onArticlesSuccess(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.onArticlesError(throwable);
                    }
                });
    }

    @Override
    public void getBanner() {
        mService.getBanner()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<NetworkResponse<List<HomeBannerData>>>() {
                    @Override
                    public void accept(NetworkResponse<List<HomeBannerData>> response) throws Exception {
                        if (response == null) {
                            mView.onBannerError(new Throwable("首页Banner请求失败"));
                        }
                        mView.onBannerSuccess(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.onBannerError(throwable);
                    }
                });
    }

    @Override
    public void cancelStarArticle(int id) {
        mService.cancelStar(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<NetworkResponse<String>>() {
                    @Override
                    public void accept(NetworkResponse<String> response) throws Exception {
                        if (response == null) {
                            mView.onCancelStarError(new Throwable("取消收藏失败"));
                        }
                        mView.onCancelStarSuccess(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.onCancelStarError(throwable);
                    }
                });
    }

    @Override
    public void addStarArticle(int id) {
        mService.addStar(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<NetworkResponse<String>>() {
                    @Override
                    public void accept(NetworkResponse<String> response) throws Exception {
                        if (response == null) {
                            mView.onAddStarError(new Throwable("添加收藏失败"));
                        }
                        mView.onAddStarSuccess(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.onAddStarError(throwable);
                    }
                });
    }

}
