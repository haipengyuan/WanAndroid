package com.yhp.wanandroid;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.yhp.wanandroid.base.BaseActivity;
import com.yhp.wanandroid.ui.fragment.CategoryFragment;
import com.yhp.wanandroid.ui.fragment.HomeFragment;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.fragment_container)
    FrameLayout mContatiner;
    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;

    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_CATEGORY = 1;
    private HomeFragment mHomeFragment;
    private CategoryFragment mCategoryFragment;

    // 记录点击返回按钮的时间，实现双击返回按钮退出
    private long firstClickTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        if (savedInstanceState != null) {

        } else {
            // 默认显示HomeFragment
            showFragment(FRAGMENT_HOME);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toolbar_hot:
                Toast.makeText(this, "hot", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_toolbar_search:
                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - firstClickTime) < 2000) {
            super.onBackPressed();
            System.exit(0);
        } else {
            Toast.makeText(this, R.string.double_click_exit, Toast.LENGTH_SHORT).show();
            firstClickTime = currentTime;
        }
    }

    private void initView() {
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 点击ToolBar左侧返回按钮打开关闭DrawerLayout
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        // 底部导航栏点击事件
        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_item_home:
                        showFragment(FRAGMENT_HOME);
                        break;
                    case R.id.menu_item_category:
                        showFragment(FRAGMENT_CATEGORY);
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 显示Fragment页面
     * @param index Fragment对应的下标
     */
    private void showFragment(int index) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideFragment(ft);

        switch (index) {
            case FRAGMENT_HOME:
                // 如果Fragment为空，新建实例  如果不为空，将其从栈中显示
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.newInstance();
                    ft.add(R.id.fragment_container, mHomeFragment, HomeFragment.class.getName());
                } else {
                    ft.show(mHomeFragment);
                }
                break;
            case FRAGMENT_CATEGORY:
                if (mCategoryFragment == null) {
                    mCategoryFragment = CategoryFragment.newInstance();
                    ft.add(R.id.fragment_container, mCategoryFragment, CategoryFragment.class.getName());
                } else {
                    ft.show(mCategoryFragment);
                }
                break;
        }

        ft.commit();
    }

    /**
     * 隐藏所有Fragment
     * @param ft
     */
    private void hideFragment(FragmentTransaction ft) {

        if (mHomeFragment != null) {
            ft.hide(mHomeFragment);
        }

        if (mCategoryFragment != null) {
            ft.hide(mCategoryFragment);
        }
    }
}
