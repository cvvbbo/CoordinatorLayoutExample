package github.hellocsl.ucmainpager.behavior;

import android.content.Context;
import android.support.annotation.Nullable;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;



/**
 * @author meitu.xujun  on 2017/5/2 14:43
 * @version 0.1
 */

public class NestedLinearLayout extends LinearLayout implements NestedScrollingChild {

    private static final String TAG = "NestedLinearLayout";

    private final int[] offset = new int[2];
    private final int[] consumed = new int[2];

    private NestedScrollingChildHelper mScrollingChildHelper;
    private int lastY;

    public NestedLinearLayout(Context context) {
        this(context, null);
    }

    public NestedLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    private void initData() {
        if (mScrollingChildHelper == null) {
            mScrollingChildHelper = new NestedScrollingChildHelper(this);
            mScrollingChildHelper.setNestedScrollingEnabled(true);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent: =" );
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onTouchEvent: ACTION_DOWN=" );
                lastY = (int)event.getRawY();
                // 当开始滑动的时候，告诉父view
                startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL
                        | ViewCompat.SCROLL_AXIS_VERTICAL);
                return true;

            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onTouchEvent: ACTION_MOVE=" );
                int y = (int)(event.getRawY());
                int dy = y - lastY;
                lastY = y;
                Log.i(TAG, "onTouchEvent: lastY=" + lastY);
                Log.i(TAG, "onTouchEvent: dy=" + dy);
                if (startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL) // 如果找到了支持嵌套滚动的父类
                        && dispatchNestedPreScroll(0, Math.abs(dy), consumed, offset)) {// 父类进行了一部分滚动
                }

        }
        return super.onTouchEvent(event);
    }

    private NestedScrollingChildHelper getScrollingChildHelper() {
        return mScrollingChildHelper;
    }

    // 接口实现--------------------------------------------------

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        getScrollingChildHelper().setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return getScrollingChildHelper().isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return getScrollingChildHelper().startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        getScrollingChildHelper().stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return getScrollingChildHelper().hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed,
                                        int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return getScrollingChildHelper().dispatchNestedScroll(dxConsumed,
                dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed,
                                           int[] offsetInWindow) {
        return getScrollingChildHelper().dispatchNestedPreScroll(dx, dy,
                consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY,
                                       boolean consumed) {
        return getScrollingChildHelper().dispatchNestedFling(velocityX,
                velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return getScrollingChildHelper().dispatchNestedPreFling(velocityX,
                velocityY);
    }
}
