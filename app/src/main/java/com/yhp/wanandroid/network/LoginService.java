package com.yhp.wanandroid.network;

import com.yhp.wanandroid.bean.LoginData;
import com.yhp.wanandroid.bean.NetworkResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoginService {

    /**
     * 用户登录
     * https://www.wanandroid.com/user/login
     * @param username
     * @param password
     * @return
     */
    @POST("user/login")
    @FormUrlEncoded
    Observable<NetworkResponse<LoginData>> login(
            @Field("username") String username, @Field("password") String password);

    /**
     * 退出登录
     * @return
     */
    @GET("user/logout/json")
    Observable<NetworkResponse<String>> logout();
}
