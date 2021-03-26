package com.yhp.wanandroid.mvp.presenter;

import android.util.Log;

import com.yhp.wanandroid.bean.HomeArticlesData;
import com.yhp.wanandroid.bean.NetworkResponse;
import com.yhp.wanandroid.mvp.contract.CategoryArticlesContract;
import com.yhp.wanandroid.network.CategoryArticlesService;
import com.yhp.wanandroid.network.RetrofitHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CategoryArticlesPresenter implements CategoryArticlesContract.Presenter {

    private CategoryArticlesService mService = RetrofitHelper.createCategoryArticlesService();
    private CategoryArticlesContract.View mView;

    public CategoryArticlesPresenter(CategoryArticlesContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getHomeArticles(int page, int cid) {
        mService.getHomepageArticles(page, cid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<NetworkResponse<HomeArticlesData>>() {
                    @Override
                    public void accept(NetworkResponse<HomeArticlesData> response) throws Exception {
                        if (response == null) {
                            mView.onArticlesError(new Throwable("获取文章数据失败"));
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
