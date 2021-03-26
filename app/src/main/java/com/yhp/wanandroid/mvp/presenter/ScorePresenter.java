package com.yhp.wanandroid.mvp.presenter;

import com.yhp.wanandroid.bean.NetworkResponse;
import com.yhp.wanandroid.bean.ScoreData;
import com.yhp.wanandroid.mvp.contract.ScoreContract;
import com.yhp.wanandroid.network.RetrofitHelper;
import com.yhp.wanandroid.network.ScoreService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ScorePresenter implements ScoreContract.Presenter {
    private static final String TAG = ScorePresenter.class.getSimpleName();
    private ScoreContract.View mView;
    private ScoreService mService = RetrofitHelper.createScoreService();

    public ScorePresenter(ScoreContract.View View) {
        this.mView = View;
    }

    @Override
    public void getScore() {
        mService.getScore()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<NetworkResponse<ScoreData>>() {
                    @Override
                    public void accept(NetworkResponse<ScoreData> response) throws Exception {
                        if (response == null) {
                            mView.onScoreError(new Throwable("获取我的积分失败"));
                        }
                        mView.onScoreSuccess(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.onScoreError(throwable);
                    }
                });
    }
}
