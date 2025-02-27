package com.xujun.contralayout.UI.weibo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.xujun.contralayout.R;
import com.xujun.contralayout.UI.ListFragment;
import com.xujun.contralayout.UI.weibo.behavior.WeiboHeaderPagerBehavior;
import com.xujun.contralayout.base.BaseFragmentAdapter;
import com.xujun.contralayout.base.BaseMVPActivity;
import com.xujun.contralayout.base.mvp.IBasePresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class WeiboSampleActivity extends BaseMVPActivity implements WeiboHeaderPagerBehavior.OnPagerStateListener {

    ViewPager mViewPager;
    List<Fragment> mFragments;
    Toolbar mToolbar;

    String[] mTitles = new String[]{
            "主页", "微博", "相册"
    };
    private View mHeaderView;
    private WeiboHeaderPagerBehavior mHeaderPagerBehavior;
    private View mIvBack;

    @Override
    protected IBasePresenter setPresenter() {
        return null;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_weibo_sample;
    }

    @Override
    protected void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mHeaderView = findViewById(R.id.id_weibo_header);
        mIvBack = findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBack();
            }
        });
        mIvBack.setVisibility(View.INVISIBLE);

        findViewById(R.id.iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WeiboSampleActivity.this," 点击 header", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        setupViewPager();
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
                mHeaderView.getLayoutParams();
        mHeaderPagerBehavior = (WeiboHeaderPagerBehavior) layoutParams.getBehavior();
        mHeaderPagerBehavior.setPagerStateListener(this);
    }

    private void setupViewPager() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        mFragments = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            ListFragment listFragment = ListFragment.newInstance(mTitles[i]);
            mFragments.add(listFragment);
        }
        BaseFragmentAdapter adapter =
                new BaseFragmentAdapter(getSupportFragmentManager(), mFragments, mTitles);


        viewPager.setAdapter(adapter);
    }

    @Override
    public void onPagerClosed() {
        Toast.makeText(this, "关闭了", Toast.LENGTH_SHORT).show();
        mIvBack.setVisibility(View.VISIBLE);
        for(Fragment fragment:mFragments){
            ListFragment listFragment= (ListFragment) fragment;
            listFragment.tooglePager(false);
        }
    }

    @Override
    public void onPagerOpened() {
        Toast.makeText(this, "打开了", Toast.LENGTH_SHORT).show();
        mIvBack.setVisibility(View.INVISIBLE);
        for(Fragment fragment:mFragments){
            ListFragment listFragment= (ListFragment) fragment;
            listFragment.tooglePager(true);
        }
    }

    @Override
    public void onBackPressed() {
        handleBack();
    }

    private void handleBack() {
        if(mHeaderPagerBehavior.isClosed()){
            mHeaderPagerBehavior.openPager();
            return;
        }
        finish();
    }
}
