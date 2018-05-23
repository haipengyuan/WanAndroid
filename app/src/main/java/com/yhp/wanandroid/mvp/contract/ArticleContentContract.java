package com.yhp.wanandroid.mvp.contract;

import com.yhp.wanandroid.base.BasePresenter;
import com.yhp.wanandroid.base.BaseView;

public interface ArticleContentContract {

    interface View extends BaseView<Presenter> {}

    interface Presenter extends BasePresenter {}

}