package com.yhp.wanandroid.network;

import com.yhp.wanandroid.bean.HomeArticlesResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HomepageService {

    /**
     * 首页文章列表
     * http://www.wanandroid.com/article/list/0/json
     * 参数：页码，拼接在连接中，从0开始
     * @param page 页码
     * @return
     */
    @GET("article/list/{page}/json")
    Observable<HomeArticlesResponse> getHomepageArticles(@Path("page") int page);



}
