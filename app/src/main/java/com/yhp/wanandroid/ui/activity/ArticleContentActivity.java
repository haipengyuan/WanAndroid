package com.yhp.wanandroid.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(mToolbar);

        Intent intent = getIntent();
        mUrl = intent.getStringExtra(Constant.CONTENT_URL_KEY);

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mFrameLayout, new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(mUrl);
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
}
