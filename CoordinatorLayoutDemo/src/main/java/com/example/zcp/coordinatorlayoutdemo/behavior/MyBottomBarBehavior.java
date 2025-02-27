package com.example.zcp.coordinatorlayoutdemo.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * Created by 赵晨璞 on 2016/7/4.
 */

public class MyBottomBarBehavior extends CoordinatorLayout.Behavior<View> {


    public MyBottomBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        //这个方法是说明这个子控件是依赖AppBarLayout的
        return dependency instanceof AppBarLayout;
    }


    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {

        float translationY = Math.abs(dependency.getTop());//获取更随布局的顶部位置
        child.setTranslationY(translationY);
        return true;
    }

}
