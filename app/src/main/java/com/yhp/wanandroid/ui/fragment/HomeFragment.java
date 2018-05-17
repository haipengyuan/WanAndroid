package com.yhp.wanandroid.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yhp.wanandroid.R;
import com.yhp.wanandroid.bean.HomeArticlesResponse;
import com.yhp.wanandroid.mvp.contract.HomepageContract;
import com.yhp.wanandroid.mvp.presenter.HomepagePresenter;
import com.yhp.wanandroid.ui.adapter.HomeArticlesAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment implements HomepageContract.View {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private static final String TAG = HomeFragment.class.getSimpleName();
    private Activity mActivity;

    private HomepagePresenter mPresenter = new HomepagePresenter(this);
    private HomeArticlesAdapter mArticlesAdapter;
    private LinearLayoutManager mLayoutManager;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mArticlesAdapter.getItemCount() == 0) {
            loadArticles(0);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setPresenter(HomepageContract.Presenter presenter) {
    }

    public void loadArticles(int page) {
        mPresenter.getHomeArticles(page);
    }

    @Override
    public void onSuccess(HomeArticlesResponse response) {

        // 请求完成，关闭刷新动画
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        if (response.data.datas != null && response.data.datas.size() != 0) {
            mArticlesAdapter.addItems(response.data.datas);
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "onError: " + e.getMessage());
    }

    private void init() {
        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mArticlesAdapter = new HomeArticlesAdapter(mActivity);
        mRecyclerView.setAdapter(mArticlesAdapter);

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
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
    }
}
