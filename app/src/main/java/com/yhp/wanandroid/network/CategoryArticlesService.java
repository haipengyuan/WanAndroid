package com.yhp.wanandroid.network;

import com.yhp.wanandroid.bean.HomeArticlesData;
import com.yhp.wanandroid.bean.NetworkResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface CategoryArticlesService {

    /**
     * 知识体系下的文章
     * http://www.wanandroid.com/article/list/0/json?cid=60
     * @param page 页码，从0开始
     * @param cid 二级目录的id
     * @return
     */
    @GET("article/list/{page}/json")
    Observable<NetworkResponse<HomeArticlesData>> getHomepageArticles(@Path("page") int page,
                                                                      @Query("cid") int cid);
}
