package com.yhp.wanandroid.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.yhp.wanandroid.R;
import com.yhp.wanandroid.base.BaseFragment;
import com.yhp.wanandroid.bean.HomeArticlesData;
import com.yhp.wanandroid.bean.NetworkResponse;
import com.yhp.wanandroid.constant.Constant;
import com.yhp.wanandroid.mvp.contract.CategoryArticlesContract;
import com.yhp.wanandroid.mvp.presenter.CategoryArticlesPresenter;
import com.yhp.wanandroid.ui.adapter.HomeArticlesAdapter;

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

    private int cid;

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
        loadArticles(Constant.PAGE_CODE_START, cid);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_article_list;
    }

    @Override
    protected void initView() {
        mLayoutManager = new LinearLayoutManager(getMActivity());
        mContentView.setLayoutManager(mLayoutManager);
        mArticlesAdapter = new HomeArticlesAdapter(getMActivity());
        mContentView.setAdapter(mArticlesAdapter);

        // 设置下拉刷新动画的颜色
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN,
                Color.CYAN, Color.BLUE, Color.MAGENTA);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mArticlesAdapter.clear();
                loadArticles(Constant.PAGE_CODE_START, cid);
            }
        });

        // RecyclerView滚动事件监听
        mContentView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 当位于RecyclerView顶部的时候隐藏FAB，位于其他位置显示FAB
                mFAB.setVisibility(mLayoutManager.findFirstVisibleItemPosition() > 0
                        ? View.VISIBLE : View.GONE);

                // 滑动到最后一项时加载数据
                if (!mSwipeRefreshLayout.isRefreshing()
                        && mLayoutManager.findLastVisibleItemPosition()
                        == mArticlesAdapter.getItemCount() - 1) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    loadArticles(mArticlesAdapter.getItemCount() / 20, cid);
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

    /**
     * 请求获取文章
     * @param page
     * @param cid
     */
    private void loadArticles(int page, int cid) {
        mPresenter.getHomeArticles(page, cid);
    }

    @Override
    public void onArticlesSuccess(NetworkResponse<HomeArticlesData> response) {
        closeSwipeRefresh();

        if (response.data != null && response.data.datas.size() != 0) {
            mArticlesAdapter.addItems(response.data.datas);
        }

        showContentView();
    }

    @Override
    public void onArticlesError(Throwable e) {
        Log.e(TAG, "onError: " + e.getMessage());
        closeSwipeRefresh();

        showErrorView();
    }

    @Override
    public void setPresenter(CategoryArticlesContract.Presenter presenter) {}
}
