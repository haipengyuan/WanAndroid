package com.yhp.wanandroid.network;

import com.yhp.wanandroid.bean.NetworkResponse;
import com.yhp.wanandroid.bean.ScoreData;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ScoreService {

    /**
     * 获取个人积分，需要登录后访问
     * @return
     */
    @GET("lg/coin/userinfo/json")
    Observable<NetworkResponse<ScoreData>> getScore();
}
