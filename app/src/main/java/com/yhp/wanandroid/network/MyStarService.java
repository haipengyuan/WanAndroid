package com.yhp.wanandroid.network;

import com.yhp.wanandroid.bean.HomeArticlesData;
import com.yhp.wanandroid.bean.NetworkResponse;
import com.yhp.wanandroid.bean.StarListData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MyStarService {

    /**
     * 收藏文章列表
     * https://www.wanandroid.com/lg/collect/list/0/json
     * GET
     * 参数： 页码：拼接在链接中，从0开始。
     * @param page
     * @return
     */
    @GET("lg/collect/list/{page}/json")
    Observable<NetworkResponse<StarListData>> getStarArticles(@Path("page") int page);

    /**
     * 取消收藏
     * @param id
     * @param originId
     * @return
     */
    @POST("lg/uncollect/{id}/json")
    @FormUrlEncoded
    Observable<NetworkResponse<String>> cancelStar(@Path("id") int id, @Field("originId") int originId);
}
