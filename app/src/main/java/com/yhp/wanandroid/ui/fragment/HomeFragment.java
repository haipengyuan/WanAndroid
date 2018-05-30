package com.yhp.wanandroid.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.yhp.wanandroid.R;
import com.yhp.wanandroid.bean.HomeArticlesData;
import com.yhp.wanandroid.bean.HomeBannerData;
import com.yhp.wanandroid.bean.NetworkResponse;
import com.yhp.wanandroid.constant.Constant;
import com.yhp.wanandroid.mvp.contract.HomepageContract;
import com.yhp.wanandroid.mvp.presenter.HomepagePresenter;
import com.yhp.wanandroid.ui.activity.ArticleContentActivity;
import com.yhp.wanandroid.ui.adapter.HomeArticlesAdapter;
import com.yhp.wanandroid.util.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 主页Fragment
 * 显示轮播图和主页文章列表
 */
public class HomeFragment extends Fragment implements HomepageContract.View {

    @BindView(R.id.content_recycler_view)
    RecyclerView mContentView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.network_fail_layout)
    RelativeLayout mNetworkFailView;

    private static final String TAG = HomeFragment.class.getSimpleName();

    private Activity mActivity;

    private HomepagePresenter mPresenter = new HomepagePresenter(this);
    private HomeArticlesAdapter mArticlesAdapter;
    private LinearLayoutManager mLayoutManager;
    private Banner mBanner;

    private List<String> mBannerImages = new ArrayList<>();

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        initView();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadBanner();
        loadArticles(0);
    }

    @Override
    public void onStart() {
        super.onStart();

        // 开始轮播
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();

        // 结束轮播
        mBanner.stopAutoPlay();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setPresenter(HomepageContract.Presenter presenter) {}

    /**
     * 获取文章数据成功，更新页面
     * @param response 响应数据
     */
    @Override
    public void onArticlesSuccess(NetworkResponse<HomeArticlesData> response) {
        closeSwipeRefresh();

        if (response.data != null && response.data.datas.size() != 0) {
            mArticlesAdapter.addItems(response.data.datas);
        }

        showContentView();
    }

    /**
     * 获取文章数据失败，显示错误信息
     * @param e 异常
     */
    @Override
    public void onArticlesError(Throwable e) {
        Log.e(TAG, "onError: " + e.getMessage());
        closeSwipeRefresh();
        showErrorView();
    }

    /**
     * 获取轮播图片数据成功，设置到Banner中
     * @param response 响应数据
     */
    @Override
    public void onBannerSuccess(NetworkResponse<List<HomeBannerData>> response) {
        if (response.data != null && response.data.size() != 0) {
            mBannerImages.clear();
            for (HomeBannerData data : response.data) {
                mBannerImages.add(data.imagePath);
            }
            mBanner.setImages(mBannerImages);
            mBanner.start();
        }
    }

    @Override
    public void onBannerError(Throwable e) {
        Log.e(TAG, "onError: " + e.getMessage());
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mLayoutManager = new LinearLayoutManager(mActivity);
        mContentView.setLayoutManager(mLayoutManager);

        View header = LayoutInflater.from(mActivity).inflate(R.layout.home_header_banner, mContentView, false);
        mBanner = header.findViewById(R.id.home_banner);
        mBanner.setImageLoader(new GlideImageLoader());
        // 设置轮播间隔时间
        mBanner.setDelayTime(4000);
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Log.e(TAG, "OnBannerClick: " + position);
            }
        });

        mArticlesAdapter = new HomeArticlesAdapter(mActivity);
        mArticlesAdapter.setHeaderView(header);

        mContentView.setAdapter(mArticlesAdapter);

        // 设置下拉刷新动画的颜色
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN,
                Color.CYAN, Color.BLUE, Color.MAGENTA);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mArticlesAdapter.clear();
                loadArticles(0);
            }
        });

        // RecyclerView滚动事件监听
        mContentView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 滑动到最后一项时加载数据
                if (!mSwipeRefreshLayout.isRefreshing()
                        && mLayoutManager.findLastVisibleItemPosition()
                        == mArticlesAdapter.getItemCount() - 1) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    loadArticles(mArticlesAdapter.getItemCount() / 20);
                }
            }
        });

        // RecyclerView的item项的点击事件
        mArticlesAdapter.setOnItemClickListener(new HomeArticlesAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Log.e(TAG, mArticlesAdapter.getItem(position).link);
                Intent intent = new Intent(mActivity, ArticleContentActivity.class);
                intent.putExtra(Constant.CONTENT_URL_KEY, mArticlesAdapter.getItem(position).link);
                startActivity(intent);

            }
        });

        // RecyclerView的item项子控件的点击事件
        mArticlesAdapter.setOnItemChildClickListener(new HomeArticlesAdapter.OnItemChildClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (view.getId() == R.id.article_category) {
                    Log.e(TAG, mArticlesAdapter.getItem(position).chapterName);
                }
            }
        });
    }

    /**
     * 网络连接失败时显示错误提示页面
     */
    private void showErrorView() {
        mNetworkFailView.setVisibility(View.VISIBLE);
        mContentView.setVisibility(View.GONE);
        Log.e(TAG, "showErrorView: ");
    }

    /**
     * 网络正常时显示页面内容
     */
    private void showContentView() {
        mNetworkFailView.setVisibility(View.GONE);
        mContentView.setVisibility(View.VISIBLE);
    }

    /**
     * 关闭下拉刷新动画
     */
    private void closeSwipeRefresh() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * 请求获取文章
     * @param page 页码
     */
    private void loadArticles(int page) {
        mPresenter.getHomeArticles(page);
    }

    /**
     * 请求获取轮播图片
     */
    private void loadBanner() {
        mPresenter.getBanner();
    }

}
