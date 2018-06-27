package com.yhp.wanandroid.network;

import android.util.Log;

import com.yhp.wanandroid.constant.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private static final String TAG = RetrofitHelper.class.getSimpleName();

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constant.REQUEST_BASE_URL)
            .client(new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            Log.i(TAG, message);
                        }
                    }).setLevel(HttpLoggingInterceptor.Level.BODY))
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS)
                    .build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    public static HomepageService createHomepageService() {
        return retrofit.create(HomepageService.class);
    }

    public static CategoryService createCategoryService() {
        return retrofit.create(CategoryService.class);
    }

    public static CategoryArticlesService createCategoryArticlesService() {
        return retrofit.create(CategoryArticlesService.class);
    }

}