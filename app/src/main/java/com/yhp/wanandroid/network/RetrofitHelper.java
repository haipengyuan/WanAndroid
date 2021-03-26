package com.yhp.wanandroid.network;

import android.util.Log;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.yhp.wanandroid.app.MyApplication;
import com.yhp.wanandroid.constant.Constant;
import com.yhp.wanandroid.network.cookie.CookieJarImpl;
import com.yhp.wanandroid.network.cookie.PersistentCookieStore;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private static final String TAG = RetrofitHelper.class.getSimpleName();
//    private static ClearableCookieJar cookieJar =
//            new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(MyApplication.getContext()));

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constant.REQUEST_BASE_URL)
            .client(new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            Log.e(TAG, message);
                        }
                    }).setLevel(HttpLoggingInterceptor.Level.BODY))
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS)
                   // .cookieJar(new CookieJarImpl(new PersistentCookieStore(MyApplication.getContext())))
                    .cookieJar(new CookieJarImpl(new PersistentCookieStore(MyApplication.getContext())))
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

    public static LoginService createLoginService() {
        return retrofit.create(LoginService.class);
    }

    public static ScoreService createScoreService() {
        return retrofit.create(ScoreService.class);
    }

    public static MyStarService createMyStarService() {
        return retrofit.create(MyStarService.class);
    }
}