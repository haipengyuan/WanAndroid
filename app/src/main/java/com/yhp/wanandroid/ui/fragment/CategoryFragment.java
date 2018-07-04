package com.yhp.wanandroid.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.yhp.wanandroid.base.BaseFragment;
import com.yhp.wanandroid.bean.CategoryData;
import com.yhp.wanandroid.bean.NetworkResponse;
import com.yhp.wanandroid.constant.Constant;
import com.yhp.wanandroid.mvp.contract.CategoryContract;
import com.yhp.wanandroid.mvp.presenter.CategoryPresenter;
import com.yhp.wanandroid.ui.activity.CategoryDetailActivity;
import com.yhp.wanandroid.ui.adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 体系Fragment
 * 显示知识体系分类
 */
public class CategoryFragment extends BaseFragment implements CategoryContract.View {

    @BindView(R.id.category_recycler_view)
    RecyclerView mCategoryView;
    @BindView(R.id.network_fail_layout)
    RelativeLayout mNetworkFailView;
    @BindView(R.id.loading_layout)
    RelativeLayout mLoadingView;

    /**
     * 数据请求成功
     */
    private static final int STATE_SUCCESS = 593;

    /**
     * 数据请求失败
     */
    private static final int STATE_FAIL = 759;

    /**
     * 正在加载
     */
    private static final int STATE_LOADING = 719;

    /**
     * 状态标志
     * STATE_SUCCESS, STATE_FAIL, STATE_LOADING
     */
    private int mState;

    private CategoryPresenter mPresenter = new CategoryPresenter(this);
    private CategoryAdapter mCategoryAdapter;
    private LinearLayoutManager mLayoutManager;

    /**
     * 知识体系一级标题列表
     */
    private List<String> mFirstTitleList = new ArrayList<>();

    /**
     * 知识体系二级标题
     * key为一级标题，value为二级标题列表
     */
    private Map<String, List<String>> mSecondaryTitleMap = new HashMap<>();

    /**
     * 知识体系二级标题的id值
     * key为一级标题，value为二级标题id列表
     */
    private Map<String, ArrayList<Integer>> mSecondaryIDMap = new HashMap<>();

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mState = STATE_LOADING;
    }

/*    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadCategory();
    }*/

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
        mState = STATE_FAIL;
        refreshView();
    }

    @Override
    public void setPresenter(CategoryContract.Presenter presenter) {}

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_category;
    }

    @Override
    protected void initView() {
        mLayoutManager = new LinearLayoutManager(getMActivity());
        mCategoryView.setLayoutManager(mLayoutManager);

        mCategoryAdapter = new CategoryAdapter(getMActivity());
        mCategoryView.setAdapter(mCategoryAdapter);

        mCategoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                String firstTitle = mFirstTitleList.get(position);
                List<String> secondaryTitleList = mSecondaryTitleMap.get(firstTitle);
                ArrayList<Integer> secondaryIDList = mSecondaryIDMap.get(firstTitle);

                Intent intent = new Intent(getMActivity(), CategoryDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constant.ARG_FIRST_TITLE, firstTitle);
                bundle.putStringArray(Constant.ARG_SECONDARY_TITLE,
                        secondaryTitleList.toArray(new String[secondaryTitleList.size()]));
                bundle.putIntegerArrayList(Constant.CATEGORY_ID_LIST, secondaryIDList);
                intent.putExtras(bundle);
                getMActivity().startActivity(intent);
            }
        });

        refreshView();
    }

    @Override
    protected void initData() {
        loadCategory();
    }

    /**
     * 加载知识体系分类数据
     */
    private void loadCategory() {
        mPresenter.getCategory();
    }

    /**
     * 根据当前的状态显示对应的页面
     */
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

    /**
     * 请求数据时显示加载页面
     */
    private void showLoadingView() {
        mCategoryView.setVisibility(View.GONE);
        mNetworkFailView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
    }
}
