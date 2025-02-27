package github.hellocsl.ucmainpager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import androidx.core.app.Fragment;
import androidx.core.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import github.hellocsl.ucmainpager.adapter.TestFragmentAdapter;
import github.hellocsl.ucmainpager.behavior.uc.UcNewsHeaderPagerBehavior;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, UcNewsHeaderPagerBehavior.OnPagerStateListener {

    private static final String TAG = "MainActivity";
    private ViewPager mNewsPager;
    private TabLayout mTableLayout;
    private List<TestFragment> mFragments;
    private UcNewsHeaderPagerBehavior mPagerBehavior;


    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uc_main_pager);
        initView();
    }

    protected void initView() {
        findViewById(R.id.iv_github).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMyGitHub();
            }


        });
        mPagerBehavior = (UcNewsHeaderPagerBehavior) ((CoordinatorLayout.LayoutParams) findViewById(R.id.id_uc_news_header_pager).getLayoutParams()).getBehavior();
        mPagerBehavior.setPagerStateListener(this);
        mNewsPager = (ViewPager) findViewById(R.id.id_uc_news_content);
        mTableLayout = (TabLayout) findViewById(R.id.id_uc_news_tab);
        mFragments = new ArrayList<TestFragment>();
        for (int i = 0; i < 4; i++) {
            mFragments.add(TestFragment.newInstance(String.valueOf(i), false));
            mTableLayout.addTab(mTableLayout.newTab().setText("Tab" + i));
        }
        mTableLayout.setTabMode(TabLayout.MODE_FIXED);
        mTableLayout.setOnTabSelectedListener(this);
        mNewsPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTableLayout));
        mNewsPager.setAdapter(new TestFragmentAdapter(mFragments, getSupportFragmentManager()));
        findViewById(R.id.news_tv_header_pager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击我了", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onClick: v=" +v.toString());
            }
        });
        setViewPagerScrollEnable(mNewsPager,false);
    }

    private void openMyGitHub() {
        Uri uri = Uri.parse("https://github.com/BCsl");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mNewsPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPagerClosed() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onPagerClosed: ");
        }
        Snackbar.make(mNewsPager, "pager closed", Snackbar.LENGTH_SHORT).show();
        setFragmentRefreshEnabled(true);
        setViewPagerScrollEnable(mNewsPager,true);
    }



    @Override
    public void onPagerOpened() {
        Snackbar.make(mNewsPager, "pager opened", Snackbar.LENGTH_SHORT).show();
        setFragmentRefreshEnabled(false);
        setViewPagerScrollEnable(mNewsPager,false);
    }

    @Override
    public void onBackPressed() {
        if (mPagerBehavior != null && mPagerBehavior.isClosed()) {
            mPagerBehavior.openPager();
        } else {
            super.onBackPressed();
        }
    }

    public void setViewPagerScrollEnable(ViewPager viewPager,boolean enable){
        if(false==(viewPager instanceof FixedViewPager)){
            return;
        }
        FixedViewPager fixViewPager= (FixedViewPager) viewPager;
        if(enable){
            fixViewPager.setScrollable(true);
        }else{
            fixViewPager.setScrollable(false);
        }
    }

    private void setFragmentRefreshEnabled(boolean enabled) {
        for(Fragment fragment:mFragments){
            ((TestFragment)fragment).setRefreshEnable(enabled);
        }
    }
}
