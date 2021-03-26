package com.yhp.wanandroid.mvp.presenter;

import com.yhp.wanandroid.bean.HomeArticlesData;
import com.yhp.wanandroid.bean.NetworkResponse;
import com.yhp.wanandroid.bean.StarListData;
import com.yhp.wanandroid.mvp.contract.MyStarContract;
import com.yhp.wanandroid.network.MyStarService;
import com.yhp.wanandroid.network.RetrofitHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MyStarPresenter implements MyStarContract.Presenter {

    private static final String TAG = MyStarPresenter.class.getSimpleName();
    private MyStarContract.View mView;
    private MyStarService mService = RetrofitHelper.createMyStarService();

    public MyStarPresenter(MyStarContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getStarArticles(int page) {
        mService.getStarArticles(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<NetworkResponse<StarListData>>() {
                    @Override
                    public void accept(NetworkResponse<StarListData> response) throws Exception {
                        if (response == null) {
                            mView.onArticlesError(new Throwable("获取收藏文章数据失败"));
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
    public void cancelStarArticle(int id, int originId) {
        mService.cancelStar(id, originId)
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
}
