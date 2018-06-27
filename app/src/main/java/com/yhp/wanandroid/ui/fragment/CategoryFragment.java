package com.yhp.wanandroid.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yhp.wanandroid.R;
import com.yhp.wanandroid.bean.CategoryData;
import com.yhp.wanandroid.bean.NetworkResponse;
import com.yhp.wanandroid.mvp.contract.CategoryContract;
import com.yhp.wanandroid.mvp.presenter.CategoryPresenter;
import com.yhp.wanandroid.ui.adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CategoryFragment extends Fragment implements CategoryContract.View {

    @BindView(R.id.category_recycler_view)
    RecyclerView mCategoryView;
    @BindView(R.id.network_fail_layout)
    RelativeLayout mNetworkFailView;
    @BindView(R.id.loading_layout)
    RelativeLayout mLoadingView;

    private static final int STATE_SUCCESS = 593;
    private static final int STATE_FAIL = 759;
    private static final int STATE_LOADING = 719;

    private Activity mActivity;

    private CategoryPresenter mPresenter = new CategoryPresenter(this);
    private CategoryAdapter mCategoryAdapter;
    private LinearLayoutManager mLayoutManager;

    private List<String> mFirstTitleList = new ArrayList<>();
    private Map<String, List<String>> mSecondaryTitleMap = new HashMap<>();
    private Map<String, ArrayList<Integer>> mSecondaryIDMap = new HashMap<>();

    private int mState;

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        mState = STATE_LOADING;
    }

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);

        initView();
        refreshView();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadCategory();
    }

    @Override
    public void onCategorySuccess(NetworkResponse<List<CategoryData>> response) {
        if (response.data != null && response.data.size() != 0) {
            for (CategoryData category : response.data) {
                mFirstTitleList.add(category.name);
                if (category.children != null && category.children.size() != 0) {
                    List<String> titleList = new ArrayList<>();
                    ArrayList<Integer> idList = new ArrayList<>();
                    for (CategoryData child : category.children) {
                        titleList.add(child.name);
                        idList.add(child.id);
                    }
                    mSecondaryTitleMap.put(category.name, titleList);
                    mSecondaryIDMap.put(category.name, idList);
                }
            }
            mCategoryAdapter.setSecondaryTitleMap(mSecondaryTitleMap);
            mCategoryAdapter.setSecondaryIDMap(mSecondaryIDMap);
            mCategoryAdapter.setFirstTitleItems(mFirstTitleList);
        }

        mState = STATE_SUCCESS;
        refreshView();
    }

    @Override
    public void onCategoryError(Throwable e) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        mState = STATE_FAIL;
        refreshView();
    }

    @Override
    public void setPresenter(CategoryContract.Presenter presenter) {}

    private void loadCategory() {
        mPresenter.getCategory();
    }

    private void initView() {
        mLayoutManager = new LinearLayoutManager(mActivity);
        mCategoryView.setLayoutManager(mLayoutManager);

        mCategoryAdapter = new CategoryAdapter(mActivity);
        mCategoryView.setAdapter(mCategoryAdapter);
    }

    /**
     * 网络连接失败时显示错误提示页面
     */
    private void showErrorView() {
        mNetworkFailView.setVisibility(View.VISIBLE);
        mCategoryView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
    }

    /**
     * 网络正常时显示页面内容
     */
    private void showContentView() {
        mCategoryView.setVisibility(View.VISIBLE);
        mNetworkFailView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
    }

    private void showLoadingView() {
        mCategoryView.setVisibility(View.GONE);
        mNetworkFailView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
    }

    private void refreshView() {
        if (mCategoryView != null) {
            mCategoryView.setVisibility(mState == STATE_SUCCESS ? View.VISIBLE : View.GONE);
        }

        if (mNetworkFailView != null) {
            mNetworkFailView.setVisibility(mState == STATE_FAIL ? View.VISIBLE : View.GONE);
        }

        if (mLoadingView != null) {
            mLoadingView.setVisibility(mState == STATE_LOADING ? View.VISIBLE : View.GONE);
        }
    }
}
