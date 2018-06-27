package com.yhp.wanandroid.mvp.presenter;

import com.yhp.wanandroid.bean.CategoryData;
import com.yhp.wanandroid.bean.NetworkResponse;
import com.yhp.wanandroid.mvp.contract.CategoryContract;
import com.yhp.wanandroid.network.CategoryService;
import com.yhp.wanandroid.network.RetrofitHelper;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CategoryPresenter implements CategoryContract.Presenter {

    private CategoryService mService = RetrofitHelper.createCategoryService();
    private CategoryContract.View mView;

    public CategoryPresenter(CategoryContract.View view) {
        mView = view;
    }

    @Override
    public void getCategory() {
        mService.getCategory()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<NetworkResponse<List<CategoryData>>>() {
                    @Override
                    public void accept(NetworkResponse<List<CategoryData>> response) throws Exception {
                        if (response == null) {
                            mView.onCategoryError(new Throwable("知识体系数据请求失败"));
                        }
                        mView.onCategorySuccess(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.onCategoryError(throwable);
                    }
                });
    }
}
