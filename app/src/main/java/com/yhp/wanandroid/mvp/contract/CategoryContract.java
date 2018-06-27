package com.yhp.wanandroid.mvp.contract;

import com.yhp.wanandroid.base.BasePresenter;
import com.yhp.wanandroid.base.BaseView;
import com.yhp.wanandroid.bean.CategoryData;
import com.yhp.wanandroid.bean.NetworkResponse;

import java.util.List;


public interface CategoryContract {

    interface View extends BaseView<Presenter> {
        void onCategorySuccess(NetworkResponse<List<CategoryData>> response);

        void onCategoryError(Throwable e);
    }

    interface Presenter extends BasePresenter {
        void getCategory();
    }
}
