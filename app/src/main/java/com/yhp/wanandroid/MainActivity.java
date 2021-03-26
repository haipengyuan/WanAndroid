package com.yhp.wanandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.yhp.wanandroid.base.BaseActivity;
import com.yhp.wanandroid.bean.NetworkResponse;
import com.yhp.wanandroid.constant.Constant;
import com.yhp.wanandroid.event.LoginEvent;
import com.yhp.wanandroid.event.RefreshHomeEvent;
import com.yhp.wanandroid.mvp.contract.MainContract;
import com.yhp.wanandroid.mvp.presenter.MainPresenter;
import com.yhp.wanandroid.ui.activity.AboutUsAcitivity;
import com.yhp.wanandroid.ui.activity.LoginActivity;
import com.yhp.wanandroid.ui.activity.MyStarActivity;
import com.yhp.wanandroid.ui.activity.ScoreActivity;
import com.yhp.wanandroid.ui.fragment.CategoryFragment;
import com.yhp.wanandroid.ui.fragment.HomeFragment;
import com.yhp.wanandroid.widget.CustomToast;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements MainContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.fragment_container)
    FrameLayout mContatiner;
    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;
    @BindView(R.id.drawer_navigation_view)
    NavigationView mNavigationView;

    private MainPresenter mPresenter = new MainPresenter(this);
    private MaterialDialog mDialog;

    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_CATEGORY = 1;
    private HomeFragment mHomeFragment;
    private CategoryFragment mCategoryFragment;

    private TextView userNameTextView;

    // 记录点击返回按钮的时间，实现双击返回按钮退出
    private long firstClickTime;

    private static final String TAG = MainActivity.class.getSimpleName();

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = this.getSharedPreferences(Constant.USER_PREF, this.MODE_PRIVATE);
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
//        switch (item.getItemId()) {
//            case R.id.menu_toolbar_hot:
//                Toast.makeText(this, "hot", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.menu_toolbar_search:
//                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
//                break;
//        }
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

        isLogin = pref.getBoolean(Constant.IS_LOGIN, false);

        // 根据用户是否登录设置退出登录按钮是否显示
        mNavigationView.getMenu().findItem(R.id.nav_logout).setVisible(isLogin);

        // 点击“去登录”进行登录
        View mHeaderView = mNavigationView.inflateHeaderView(R.layout.nav_header);
        userNameTextView = mHeaderView.findViewById(R.id.nav_header_username);

        if (isLogin) {
            userNameTextView.setText(pref.getString(Constant.USERNAME_KEY, getString(R.string.app_name)));
        }

        userNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLogin) {
                    Log.e(TAG, "去登录");
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_my_score:
                        if (isLogin) {
                            Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                            startActivity(intent);
                        } else {
                            new CustomToast(MainActivity.this, getString(R.string.login_tint)).show();
                            goToLogin();
                        }
                        break;
                    case R.id.nav_my_star:
                        if (isLogin) {
                            Intent intent = new Intent(MainActivity.this, MyStarActivity.class);
                            startActivity(intent);
                        } else {
                            new CustomToast(MainActivity.this, getString(R.string.login_tint)).show();
                            goToLogin();
                        }
                        break;
                    case R.id.nav_about_us:
                        Intent intent = new Intent(MainActivity.this, AboutUsAcitivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_logout:
                        // 退出登录
                        logout();
                        break;
                }
                return false;
            }
        });

        // 底部导航栏点击事件
        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_item_home:
                        showFragment(FRAGMENT_HOME);
                        mToolbar.setTitle(getResources().getString(R.string.app_name));
                        break;
                    case R.id.menu_item_category:
                        showFragment(FRAGMENT_CATEGORY);
                        mToolbar.setTitle(getResources().getString(R.string.nav_category));
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 去登录页面
     */
    private void goToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void logout() {
        Log.e(TAG, "退出登录");
        mDialog = new MaterialDialog.Builder(this)
                .title(R.string.confirm_logout)
        //      .content(R.string.content)
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mPresenter.logout();
                    }
                })
                .show();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event) {
        if (event.isLogin()) {
            mHomeFragment.lazyLoad();
            userNameTextView.setText(event.getUser());
            mNavigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
        } else {
            mHomeFragment.lazyLoad();
            userNameTextView.setText(getString(R.string.go_to_login));
            mNavigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(RefreshHomeEvent event) {
        if (event.isRefresh()) {
            mHomeFragment.lazyLoad();
        }
    }

    @Override
    public void onLogoutSuccess(NetworkResponse<String> response) {
        new CustomToast(this, getString(R.string.logout_success));
        isLogin = false;
        //mDialog.dismiss();

        editor = pref.edit();
        editor.clear();
        editor.apply();

        EventBus.getDefault().post(new LoginEvent(isLogin, getString(R.string.go_to_login)));
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {}
}
