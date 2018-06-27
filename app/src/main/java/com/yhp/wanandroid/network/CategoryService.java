package com.yhp.wanandroid.network;

import com.yhp.wanandroid.bean.CategoryData;
import com.yhp.wanandroid.bean.NetworkResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;


public interface CategoryService {

    @GET("tree/json")
    Observable<NetworkResponse<List<CategoryData>>> getCategory();
}
