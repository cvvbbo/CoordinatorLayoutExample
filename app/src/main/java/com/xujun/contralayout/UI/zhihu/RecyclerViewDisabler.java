package com.xujun.contralayout.UI.zhihu;

import android.view.MotionEvent;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author xujun  on 2016/12/3.
 * @email gdutxiaoxu@163.com
 */

public class RecyclerViewDisabler implements RecyclerView.OnItemTouchListener {

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return true;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
