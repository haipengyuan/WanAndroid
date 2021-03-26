package com.yhp.wanandroid.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.yhp.wanandroid.R;
import com.yhp.wanandroid.base.BaseActivity;
import com.yhp.wanandroid.util.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AboutUsAcitivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    Banner banner;

    private List<Integer> images;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        mToolbar.setTitle(getString(R.string.about_us));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        images = new ArrayList<>();
        images.add(R.drawable.image_ysu_1);
        images.add(R.drawable.image_ysu_2);
        images.add(R.drawable.image_ysu_3);
        images.add(R.drawable.image_ysu_4);
        images.add(R.drawable.image_ysu_5);

        banner = (Banner) findViewById(R.id.banner);
        banner.setImageLoader(new GlideImageLoader());
        banner.setBannerAnimation(Transformer.DepthPage);
        banner.isAutoPlay(true);
        banner.setDelayTime(4000);
        banner.setImages(images);
        banner.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        banner.stopAutoPlay();
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
        return R.layout.activity_about_us;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Object event) {}
}
