package com.yhp.wanandroid.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yhp.wanandroid.base.BaseActivity;
import com.yhp.wanandroid.R;
import com.yhp.wanandroid.bean.LoginData;
import com.yhp.wanandroid.bean.NetworkResponse;
import com.yhp.wanandroid.constant.Constant;
import com.yhp.wanandroid.event.LoginEvent;
import com.yhp.wanandroid.mvp.contract.LoginContract;
import com.yhp.wanandroid.mvp.presenter.LoginPresenter;
import com.yhp.wanandroid.widget.CustomToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 *  登录页面
 */
public class LoginActivity extends BaseActivity implements LoginContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.et_username)
    EditText usernameEditText;      // 用户名输入框

    @BindView(R.id.et_password)
    EditText passwordEditText;      // 密码输入框

    @BindView(R.id.btn_login)
    TextView loginBtn;      // 登录按钮

    @BindView(R.id.tv_sign_up)
    TextView signUpBtn;    // 没有账号？去注册

    private static final String TAG = LoginActivity.class.getSimpleName();
    private LoginPresenter mPresenter = new LoginPresenter(this);

    private String username;
    private String password;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = this.getSharedPreferences(Constant.USER_PREF, this.MODE_PRIVATE);
        initView();
    }

    private void initView() {
        mToolbar.setTitle(getResources().getString(R.string.title_activity_login));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                // 如果输入符合要求，则进行登录
                if (isValid(username, password)) {
                    mPresenter.login(username, password);
                }
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 检测用户输入的用户名、密码的格式是否符合要求
     * @param username
     * @param password
     * @return
     */
    private boolean isValid(String username, String password) {
        boolean valid = true;
        if (username.isEmpty()) {
            usernameEditText.setError(getString(R.string.username_not_empty));
            valid = false;
        }
        if (password.isEmpty()) {
            passwordEditText.setError(getString(R.string.password_not_empty));
            valid = false;
        }
        return valid;
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
        return R.layout.activity_login;
    }

    @Override
    public void onLoginSuccess(NetworkResponse<LoginData> response) {
        // errorCode = 0 则登录成功，非 0 则失败
        if (response.errorCode != 0) {
            // 账号密码不匹配！
            // Toast.makeText(this, response.errorMsg, Toast.LENGTH_SHORT).show();
            new CustomToast(this, response.errorMsg).show();
        } else {
            new CustomToast(this, getString(R.string.login_success)).show();
            isLogin = true;

            editor = pref.edit();
            editor.putBoolean(Constant.IS_LOGIN, isLogin);
            editor.putString(Constant.USERNAME_KEY, username);
            editor.putString(Constant.PASSWORD_KEY, password);
            editor.apply();

            EventBus.getDefault().post(new LoginEvent(isLogin, username));
            finish();
        }
    }

    @Override
    public void onLoginError(Throwable e) {
        Log.e(TAG, "onError: " + e.getMessage());
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {}

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Object event) {}
}

