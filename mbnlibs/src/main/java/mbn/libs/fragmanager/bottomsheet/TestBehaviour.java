//package mbn.libs.fragmanager.bottomsheet;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//
//import androidx.annotation.NonNull;
//import androidx.coordinatorlayout.widget.CoordinatorLayout;
//import androidx.core.view.ViewCompat;
//
//public class TestBehaviour extends CoordinatorLayout.Behavior {
//
//    private static final String TAG = "TestBehaviour";
//
//    public TestBehaviour() {
////        NestedScrollingParentHelper
//    }
//
//    public TestBehaviour(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull MotionEvent ev) {
//
//        return super.onInterceptTouchEvent(parent, child, ev);
//    }
//
//    @Override
//    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
//        if (axes == ViewCompat.SCROLL_AXIS_VERTICAL) return true;
//        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
//    }
//
//    @Override
//    public void onNestedScrollAccepted(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
//        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, axes, type);
//    }
//
//    @Override
//    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int type) {
//        super.onStopNestedScroll(coordinatorLayout, child, target, type);
//    }
//
//    @Override
//    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
//        Log.i(TAG, "onNestedScroll: " + dyConsumed + " -- " + dyUnconsumed);
//        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
//    }
//
//    @Override
//    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
//        Log.i(TAG, "onNestedPreScroll: " + dx + " -- " + dy);
//        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
//    }
//}
