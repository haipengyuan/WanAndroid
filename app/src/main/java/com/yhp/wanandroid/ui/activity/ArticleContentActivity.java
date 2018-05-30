package com.yhp.wanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.just.agentweb.AgentWeb;
import com.yhp.wanandroid.R;
import com.yhp.wanandroid.base.BaseActivity;
import com.yhp.wanandroid.constant.Constant;

import butterknife.BindView;

public class ArticleContentActivity extends BaseActivity {

    private AgentWeb mAgentWeb;
    private String mUrl;

    @BindView(R.id.web_content)
    FrameLayout mFrameLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    protected WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mToolbar != null && !TextUtils.isEmpty(title)) {
                mToolbar.setTitle(title);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        Intent intent = getIntent();
        mUrl = intent.getStringExtra(Constant.CONTENT_URL_KEY);

        initAgentWeb();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_article_content;
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initAgentWeb() {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mFrameLayout, new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .setWebChromeClient(mWebChromeClient)
                .createAgentWeb()
                .ready()
                .go(mUrl);
    }

}
