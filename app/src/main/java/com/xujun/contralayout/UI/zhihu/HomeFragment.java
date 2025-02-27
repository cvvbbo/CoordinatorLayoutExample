package com.xujun.contralayout.UI.zhihu;

import android.os.Bundle;

import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.xujun.contralayout.R;
import com.xujun.contralayout.adapter.ItemAdapter;
import com.xujun.contralayout.base.BaseFragment;
import com.xujun.contralayout.recyclerView.divider.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author xujun  on 2016/12/3.
 * @email gdutxiaoxu@163.com
 */

public class HomeFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private FloatingActionMenu mMenu;
    private FloatingActionButton mMenuItemCollect;
    private FloatingActionButton mMenuItemComment;
    private FloatingActionButton mMenuItemStar;

    private static final String KEY = "key";
    private String title = "测试";
    public static final String TAG = "xujun";

    List<String> mDatas = new ArrayList<>();
    private ItemAdapter mAdapter;
    private RecyclerViewDisabler mRecyclerViewDisabler;

    public static HomeFragment newInstance(String text) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        if (text != null) {
            bundle.putString(KEY, text);
            homeFragment.setArguments(bundle);
        }
        return homeFragment;
    }

    @Override
    protected void initView(View view) {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);
        mMenu = (FloatingActionMenu) mView.findViewById(R.id.menu);
        mMenuItemCollect = (FloatingActionButton) mView.findViewById(R.id.menu_item_collect);
        mMenuItemComment = (FloatingActionButton) mView.findViewById(R.id.menu_item_comment);
        mMenuItemStar = (FloatingActionButton) mView.findViewById(R.id.menu_item_star);
        mMenu.setVisibility(View.GONE);

    }

    @Override
    protected void initListener() {
        super.initListener();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private boolean isHandleScroll(int dy) {
        return Math.abs(dy) > 10;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {
        super.initData();

        mRecyclerViewDisabler = new RecyclerViewDisabler();

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mContext,
                LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(layoutManager);

        for (int i = 0; i < 30; i++) {
            String s = String.format("我是第%d个" + title, i);
            mDatas.add(s);
        }

        mAdapter = new ItemAdapter(mContext, mDatas);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void fetchData() {

    }
}
