package com.yhp.wanandroid.ui.activity;import android.content.Intent;import android.support.design.widget.TabLayout;import android.support.design.widget.FloatingActionButton;import android.support.design.widget.Snackbar;import android.support.v4.app.FragmentStatePagerAdapter;import android.support.v7.app.ActionBar;import android.support.v7.app.AppCompatActivity;import android.support.v7.widget.Toolbar;import android.support.v4.app.Fragment;import android.support.v4.app.FragmentManager;import android.support.v4.app.FragmentPagerAdapter;import android.support.v4.view.ViewPager;import android.os.Bundle;import android.util.Log;import android.view.KeyEvent;import android.view.LayoutInflater;import android.view.Menu;import android.view.MenuItem;import android.view.View;import android.view.ViewGroup;import android.widget.TextView;import com.yhp.wanandroid.R;import com.yhp.wanandroid.base.BaseActivity;import com.yhp.wanandroid.constant.Constant;import com.yhp.wanandroid.ui.adapter.CategoryDetailPagerAdapter;import com.yhp.wanandroid.ui.fragment.ArticleListFragment;import java.util.ArrayList;import java.util.List;import butterknife.BindView;public class CategoryDetailActivity extends BaseActivity {    @BindView(R.id.toolbar)    Toolbar mToolbar;    @BindView(R.id.tabs)    TabLayout mTabLayout;    @BindView(R.id.container)    ViewPager mViewPager;    private static final String TAG = CategoryDetailActivity.class.getSimpleName();    private CategoryDetailPagerAdapter mPagerAdapter;    private String mFirstTitle;    private String[] mSecondaryTitle;    private ArrayList<Integer> mSecondaryIDList;    @Override    public void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        Intent intent = getIntent();        Bundle bundle = intent.getExtras();        mFirstTitle = bundle.getString(Constant.ARG_FIRST_TITLE);        mSecondaryTitle = bundle.getStringArray(Constant.ARG_SECONDARY_TITLE);        mSecondaryIDList = bundle.getIntegerArrayList(Constant.CATEGORY_ID_LIST);        initView();        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {            @Override            public void onTabSelected(TabLayout.Tab tab) {                mViewPager.setCurrentItem(tab.getPosition(), false);            }            @Override            public void onTabUnselected(TabLayout.Tab tab) {            }            @Override            public void onTabReselected(TabLayout.Tab tab) {            }        });    }    private void initView() {        mToolbar.setTitle(mFirstTitle);        setSupportActionBar(mToolbar);        ActionBar actionBar = getSupportActionBar();        if (actionBar != null) {            actionBar.setDisplayHomeAsUpEnabled(true);        }        if (mSecondaryTitle != null && mSecondaryTitle.length != 0) {            initTabLayout(mSecondaryTitle);        }        List<ArticleListFragment> fragments = new ArrayList<>();        for (Integer id : mSecondaryIDList) {            fragments.add(ArticleListFragment.newInstance(id));        }        mPagerAdapter = new CategoryDetailPagerAdapter(getSupportFragmentManager(), fragments);        mViewPager.setAdapter(mPagerAdapter);    }    private void initTabLayout(String[] titles) {        for (String title : titles) {            TabLayout.Tab tab = mTabLayout.newTab().setText(title);            mTabLayout.addTab(tab);        }        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);    }    public int getLayoutId() {        return R.layout.activity_category_detail;    }    @Override    public boolean onKeyDown(int keyCode, KeyEvent event) {        if (keyCode == KeyEvent.KEYCODE_BACK) {            finish();            return true;        }        return super.onKeyDown(keyCode, event);    }}