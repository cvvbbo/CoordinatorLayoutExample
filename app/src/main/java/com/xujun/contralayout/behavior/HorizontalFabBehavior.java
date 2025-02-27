package com.xujun.contralayout.behavior;

import android.animation.Animator;
import android.content.Context;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

/**
 *  FloatingActionButton behavior 向上向下隐藏的
 * @author xujun  on 2016/12/1.
 * @email gdutxiaoxu@163.com
 */

public class HorizontalFabBehavior extends CoordinatorLayout.Behavior<View> {

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    private float viewX;//控件距离coordinatorLayout底部距离
    private boolean isAnimate;//动画是否在进行

    public HorizontalFabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static  final String TAG="xujun";

    boolean isRight = true;

    //在嵌套滑动开始前回调
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {

        if(child.getVisibility() == View.VISIBLE&&viewX==0){
            //获取控件距离父布局（coordinatorLayout）底部距离

            if(isRight){
                viewX=coordinatorLayout.getWidth()-child.getLeft();
                Log.i(TAG, "onStartNestedScroll: viewX=" +viewX);
            }else{
                viewX=-child.getRight();
            }

        }

        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;//判断是否竖直滚动
    }

    //在嵌套滑动进行时，对象消费滚动距离前回调
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        //dy大于0是向上滚动 小于0是向下滚动

        if (dy >=0&&!isAnimate&&child.getVisibility()==View.VISIBLE) {
            hide(child);
        } else if (dy <0&&!isAnimate&&child.getVisibility()==View.GONE) {
            show(child);
        }
    }

    //隐藏时的动画
    private void hide(final View view) {
        ViewPropertyAnimator animator = view.animate().translationX(viewX).
                setInterpolator(INTERPOLATOR).setDuration(200);

        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isAnimate=true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.GONE);
                isAnimate=false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                show(view);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animator.start();
    }

    //显示时的动画
    private void show(final View view) {
        ViewPropertyAnimator animator = view.animate().translationX(0).setInterpolator(INTERPOLATOR).
                setDuration(200);
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                view.setVisibility(View.VISIBLE);
                isAnimate=true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isAnimate=false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                hide(view);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animator.start();
    }
}