package com.yhp.wanandroid.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.yhp.wanandroid.R;
import com.yhp.wanandroid.base.BaseActivity;
import com.yhp.wanandroid.bean.NetworkResponse;
import com.yhp.wanandroid.bean.ScoreData;
import com.yhp.wanandroid.mvp.contract.ScoreContract;
import com.yhp.wanandroid.mvp.presenter.ScorePresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * 我的积分页面，用户登录后才能进入
 */
public class ScoreActivity extends BaseActivity implements ScoreContract.View {

    @BindView(R.id.tv_score)
    TextView mScoreTV;
    @BindView(R.id.tv_rank)
    TextView mRankTV;
    @BindView(R.id.tv_level)
    TextView mLevelTV;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private ScorePresenter mPresenter = new ScorePresenter(this);
    private static final String TAG = ScoreActivity.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        initView();
    }

    private void initView() {
        mToolbar.setTitle(getString(R.string.my_score));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initData() {
        mPresenter.getScore();
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
        return R.layout.activity_score;
    }

    @Override
    public void onScoreSuccess(NetworkResponse<ScoreData> response) {
        if (response.data != null) {
            mScoreTV.setText(String.valueOf(response.data.coinCount));
            mRankTV.setText(getString(R.string.rank) + response.data.rank);
            mLevelTV.setText(getString(R.string.level) + response.data.level);
        }
    }

    @Override
    public void onScoreError(Throwable e) {
        Log.e(TAG, "onError: " + e.getMessage());
    }

    @Override
    public void setPresenter(ScoreContract.Presenter presenter) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Object event) {}
}
