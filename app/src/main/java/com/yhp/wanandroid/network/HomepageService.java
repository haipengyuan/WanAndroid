package com.yhp.wanandroid.network;

import com.yhp.wanandroid.bean.HomeArticlesData;
import com.yhp.wanandroid.bean.HomeBannerData;
import com.yhp.wanandroid.bean.NetworkResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
    Observable<NetworkResponse<HomeArticlesData>> getHomepageArticles(@Path("page") int page);

    /**
     * 首页banner
     * http://www.wanandroid.com/banner/json
     * @return
     */
    @GET("banner/json")
    Observable<NetworkResponse<List<HomeBannerData>>> getBanner();

    /**
     * 取消收藏
     * @param id
     * @return
     */
    @POST("lg/uncollect_originId/{id}/json")
    Observable<NetworkResponse<String>> cancelStar(@Path("id") int id);

    /**
     * 添加收藏
     * @param id
     * @return
     */
    @POST("lg/collect/{id}/json")
    Observable<NetworkResponse<String>> addStar(@Path("id") int id);
}
