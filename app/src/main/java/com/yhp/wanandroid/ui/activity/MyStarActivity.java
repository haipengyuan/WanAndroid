package com.yhp.wanandroid.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.yhp.wanandroid.R;
import com.yhp.wanandroid.base.BaseActivity;
import com.yhp.wanandroid.bean.HomeArticlesData;
import com.yhp.wanandroid.bean.NetworkResponse;
import com.yhp.wanandroid.bean.StarArticleDatas;
import com.yhp.wanandroid.bean.StarListData;
import com.yhp.wanandroid.constant.Constant;
import com.yhp.wanandroid.event.RefreshHomeEvent;
import com.yhp.wanandroid.mvp.contract.MyStarContract;
import com.yhp.wanandroid.mvp.presenter.MyStarPresenter;
import com.yhp.wanandroid.ui.adapter.HomeArticlesAdapter;
import com.yhp.wanandroid.ui.adapter.StarListAdapter;
import com.yhp.wanandroid.widget.CustomToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class MyStarActivity extends BaseActivity implements MyStarContract.View {

    private static final String TAG = MyStarActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

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

    private LinearLayoutManager mLayoutManager;
    private StarListAdapter mStarAdapter;
    private MyStarPresenter mPresenter = new MyStarPresenter(this);

    private int curPage;
    private int pageCount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        initView();
    }

    private void initData() {
        loadArticles(0);
    }

    private void initView() {
        mToolbar.setTitle(getString(R.string.my_star));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mLayoutManager = new LinearLayoutManager(this);
        mContentView.setLayoutManager(mLayoutManager);

        mStarAdapter = new StarListAdapter(this);
        mContentView.setAdapter(mStarAdapter);

        // 设置下拉刷新动画的颜色
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN,
                Color.CYAN, Color.BLUE, Color.MAGENTA);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mStarAdapter.clear();
                loadArticles(0);
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
                        == mStarAdapter.getItemCount() - 1) {
                    if (curPage < pageCount) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        //loadArticles(mStarAdapter.getItemCount() / 20);
                        loadArticles(curPage);
                    }
                }
            }
        });

        // RecyclerView的item项的点击事件
        mStarAdapter.setOnItemClickListener(new StarListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.e(TAG, mStarAdapter.getItem(position).link);
                Intent intent = new Intent(MyStarActivity.this, ArticleContentActivity.class);
                intent.putExtra(Constant.CONTENT_URL_KEY, mStarAdapter.getItem(position).link);
                startActivity(intent);
                MyStarActivity.this.overridePendingTransition(R.anim.activity_translate_enter,
                        R.anim.activity_translate_exit);
            }
        });

        // RecyclerView的item项子控件的点击事件
        mStarAdapter.setOnItemChildClickListener(new StarListAdapter.OnItemChildClickListener() {
            @Override
            public void onClick(View view, int position) {

                StarArticleDatas data = mStarAdapter.getItem(position);

                //                if (view.getId() == R.id.article_category) {
//                    Log.e(TAG, mArticlesAdapter.getItem(position).chapterName);
//                }
                if (view.getId() == R.id.article_stared) {
                    // 点击按钮取消收藏
                    new CustomToast(MyStarActivity.this, "取消收藏").show();

                    int id = data.id;
                    int originId = data.originId;

                    mPresenter.cancelStarArticle(id, originId);
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

        mRetryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStarAdapter.clear();
                loadArticles(0);
            }
        });

        showLoadingView();
    }

    /**
     * 请求获取文章
     * @param page 页码
     */
    private void loadArticles(int page) {
        mPresenter.getStarArticles(page);
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
     * 网络正常时显示页面内容
     */
    private void showContentView() {
        mNetworkFailView.setVisibility(View.GONE);
        mContentView.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
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
     * 关闭下拉刷新动画
     */
    private void closeSwipeRefresh() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_star;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Object event) {}

    @Override
    public void onArticlesSuccess(NetworkResponse<StarListData> response) {
        closeSwipeRefresh();

        if (response.data != null && response.data.datas.size() != 0) {
            curPage = response.data.curPage;
            pageCount = response.data.pageCount;

            mStarAdapter.addItems(response.data.datas);
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
    public void onCancelStarSuccess(NetworkResponse<String> response) {
        if (response.errorCode == 0) {
            new CustomToast(this, "取消收藏").show();
            mStarAdapter.clear();
            loadArticles(0);

            EventBus.getDefault().post(new RefreshHomeEvent(true));
        }
    }

    @Override
    public void onCancelStarError(Throwable e) {
        Log.e(TAG, "onError: " + e.getMessage());
    }

    @Override
    public void setPresenter(MyStarContract.Presenter presenter) {}
}
