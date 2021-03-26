package com.yhp.wanandroid.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.yhp.wanandroid.R;
import com.yhp.wanandroid.base.BaseFragment;
import com.yhp.wanandroid.bean.ArticleDatas;
import com.yhp.wanandroid.bean.HomeArticlesData;
import com.yhp.wanandroid.bean.NetworkResponse;
import com.yhp.wanandroid.constant.Constant;
import com.yhp.wanandroid.mvp.contract.CategoryArticlesContract;
import com.yhp.wanandroid.mvp.presenter.CategoryArticlesPresenter;
import com.yhp.wanandroid.ui.activity.ArticleContentActivity;
import com.yhp.wanandroid.ui.activity.LoginActivity;
import com.yhp.wanandroid.ui.adapter.HomeArticlesAdapter;
import com.yhp.wanandroid.ui.listener.FooterRecyclerOnScrollListener;
import com.yhp.wanandroid.widget.CustomToast;

import butterknife.BindView;


public class ArticleListFragment extends BaseFragment implements CategoryArticlesContract.View {

    @BindView(R.id.content_recycler_view)
    RecyclerView mContentView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.network_fail_layout)
    RelativeLayout mNetworkFailView;
    @BindView(R.id.fab)
    FloatingActionButton mFAB;
    @BindView(R.id.retry_btn)
    Button mRetryBtn;
    @BindView(R.id.loading_layout)
    RelativeLayout mLoadingLayout;

    private static final String TAG = ArticleListFragment.class.getSimpleName();

    private LinearLayoutManager mLayoutManager;
    private HomeArticlesAdapter mArticlesAdapter;

    private CategoryArticlesPresenter mPresenter = new CategoryArticlesPresenter(this);

    /**
     * 文章分类id
     */
    private int cid;

    private boolean mFirstStart;

    private boolean isLogin;

    public static ArticleListFragment newInstance(int cid) {
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.ARG_CATEGORY_CID, cid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        cid = getArguments().getInt(Constant.ARG_CATEGORY_CID);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFirstStart = true;
        updateArticles();
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_article_list;
    }

    @Override
    protected void initView() {
        mLayoutManager = new LinearLayoutManager(getMActivity());
        mContentView.setLayoutManager(mLayoutManager);

        // 设置categoryFlag为false，不显示分类信息
        mArticlesAdapter = new HomeArticlesAdapter(getMActivity(), false);

        // 设置尾部加载动画
        View footerView = LayoutInflater.from(getMActivity())
                .inflate(R.layout.refresh_footer, mContentView, false);
        mArticlesAdapter.setFooterView(footerView);

        mContentView.setAdapter(mArticlesAdapter);

        // 设置下拉刷新动画的颜色
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN,
                Color.CYAN, Color.BLUE, Color.MAGENTA);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mArticlesAdapter.clear();
                updateArticles();
            }
        });

        // 为RecyclerView添加滚动监听，实现上拉加载
        mContentView.addOnScrollListener(new FooterRecyclerOnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 当位于RecyclerView顶部的时候隐藏FAB，位于其他位置显示FAB
                mFAB.setVisibility(mLayoutManager.findFirstVisibleItemPosition() > 0
                        ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onLoadMore() {
                // 设置状态为正在加载
                mArticlesAdapter.setLoadState(HomeArticlesAdapter.LOADING);

                // 加载数据
                loadMoreArticles();
            }
        });

        mArticlesAdapter.setOnItemClickListener(new HomeArticlesAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.e(TAG, mArticlesAdapter.getItem(position).link);
                Intent intent = new Intent(getMActivity(), ArticleContentActivity.class);
                intent.putExtra(Constant.CONTENT_URL_KEY, mArticlesAdapter.getItem(position).link);
                startActivity(intent);
            }
        });

        mArticlesAdapter.setOnItemChildClickListener(new HomeArticlesAdapter.OnItemChildClickListener() {
            @Override
            public void onClick(View view, int position) {
                ArticleDatas data = mArticlesAdapter.getItem(position);
                switch (view.getId()) {
                    case R.id.article_stared:
                        isLogin = pref.getBoolean(Constant.IS_LOGIN, false);
                        if (isLogin) {
                            isLogin = pref.getBoolean(Constant.IS_LOGIN, false);
                            boolean collect = data.collect;
                            data.collect = !collect;
                            if (collect) {
                                mPresenter.cancelStarArticle(data.id);
                            } else {
                                mPresenter.addStarArticle(data.id);
                            }
                        } else {
                            new CustomToast(getMActivity(), getString(R.string.login_tint)).show();
                            Intent intent = new Intent(getMActivity(), LoginActivity.class);
                            startActivity(intent);
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 返回顶部
                mContentView.smoothScrollToPosition(0);
            }
        });

        showLoadingView();
    }

    @Override
    protected void initData() {}

    @Override
    public void onArticlesSuccess(NetworkResponse<HomeArticlesData> response) {
        closeSwipeRefresh();

        Log.e(TAG, "size: " + response.data.datas.size());

        if (response.data != null && response.data.datas.size() != 0) {
            mArticlesAdapter.setLoadState(HomeArticlesAdapter.LOADING_COMPLETE);
            mArticlesAdapter.addItems(response.data.datas);
        } else {
            mArticlesAdapter.setLoadState(HomeArticlesAdapter.LOADING_NULL);
        }

        if (mFirstStart) {
            showContentView();
            mFirstStart = false;
        }
    }

    @Override
    public void onArticlesError(Throwable e) {
        Log.e(TAG, "onError: " + e.getMessage());
        closeSwipeRefresh();

        showErrorView();
    }

    @Override
    public void onCancelStarSuccess(NetworkResponse<String> response) {
        if (response.errorCode == 0) {
            new CustomToast(getMActivity(), "取消收藏").show();
            mArticlesAdapter.clear();
            updateArticles();
        }
    }

    @Override
    public void onCancelStarError(Throwable e) {
        Log.e(TAG, "onError: " + e.getMessage());
    }

    @Override
    public void onAddStarSuccess(NetworkResponse<String> response) {
        if (response.errorCode == 0) {
            new CustomToast(getMActivity(), "已收藏").show();
            mArticlesAdapter.clear();
            updateArticles();
        }
    }

    @Override
    public void onAddStarError(Throwable e) {
        Log.e(TAG, "onError: " + e.getMessage());
    }

    /**
     * 更新文章数据
     */
    private void updateArticles() {
        // 获取cid分类下第一页的数据
        mPresenter.getHomeArticles(Constant.PAGE_CODE_START, cid);
    }

    /**
     * 加载更多文章数据
     */
    private void loadMoreArticles() {
        int page = (int) Math.ceil(mArticlesAdapter.getDataCount() / (double) Constant.PAGE_SIZE);
        mPresenter.getHomeArticles(page, cid);
    }

    /**
     * 网络连接失败时显示错误提示页面
     */
    private void showErrorView() {
        mNetworkFailView.setVisibility(View.VISIBLE);
        mContentView.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
    }

    /**
     * 网络正常时显示页面内容
     */
    private void showContentView() {
        mNetworkFailView.setVisibility(View.GONE);
        mContentView.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
    }

    /**
     * 数据加载时显示加载页面
     */
    private void showLoadingView() {
        mNetworkFailView.setVisibility(View.GONE);
        mContentView.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 关闭下拉刷新动画
     */
    private void closeSwipeRefresh() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void setPresenter(CategoryArticlesContract.Presenter presenter) {}
}
