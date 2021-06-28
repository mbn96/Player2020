package mbn.libs.fragmanager;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.ViewCompat;

import static mbn.libs.fragmanager.CustomAppBar.MODE_DEFAULT;
import static mbn.libs.fragmanager.CustomAppBar.MODE_NONE;


@SuppressWarnings("NullableProblems")
public abstract class BaseFrameLayoutForFrags extends FrameLayout implements CustomAppBar.ToolbarListener, BaseActivity.TouchDispatch, NestedScrollingParent2 {

    private static final String TAG = "BaseLayout";

    private final CustomAppBar.ToolbarHolder toolbarHolder;
    private float density = getResources().getDisplayMetrics().density;
    private int fingerSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    private static final int UP_FLAG = 1;
    private static final int DOWN_FLAG = UP_FLAG << 1;

    private int currentAvailableFlag;
    private final PointF currentStartPoint = new PointF();
    private float changeFactor;
    private int currentScrollSize;
    private int currentFixedSize;

    private boolean touchModeActive, nonTouchModeActive;

    private ValueAnimator springAnimator;

    public BaseFrameLayoutForFrags(@NonNull Context context, CustomAppBar.ToolbarHolder toolbarHolder) {
        super(context);
        this.toolbarHolder = toolbarHolder;
        toolbarHolder.setListener(this);
    }

    boolean canIntercept() {
        return !(toolbarHolder.getMode() == MODE_DEFAULT && getScrollFraction() < 1);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (Math.abs(right - left) > 0) layoutWithSize();
    }

    abstract void layoutWithSize();

//    public void scrollTest() {
//        ValueAnimator animator = ValueAnimator.ofFloat(1, 0);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                toolbarHolder.setFractionOfScroll((Float) animation.getAnimatedValue());
//            }
//        });
//        animator.setDuration(1000).setRepeatMode(ValueAnimator.REVERSE);
//        animator.setRepeatCount(ValueAnimator.INFINITE);
//        animator.start();
//    }


    @Override
    public void onSizeChanged(int fixedPart, int scrollingPart, float scrollFraction) {
        if (toolbarHolder.getMode() != MODE_NONE) {
            if (fixedPart != currentFixedSize) {
                currentFixedSize = fixedPart;
                setPadding(0, fixedPart, 0, 0);
            }

            if (scrollingPart != currentScrollSize) {
                changeFactor = 1f / scrollingPart;
                currentScrollSize = scrollingPart;
            }

            float scrollValue = scrollingPart * scrollFraction;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.setTranslationY(scrollValue);
            }
        }
    }

    /**
     * The old implementation , is more accurate but more demanding and heavier for slower devices.
     * <p>
     * // @Override
     * public void onSizeChanged(int fixedPart, int scrollingPart, float scrollFraction) {
     * if (toolbarHolder.getMode() != MODE_NONE) {
     * if (fixedPart > 0 || scrollingPart > 0) {
     * setPadding(0, (int) (fixedPart + (scrollingPart * scrollFraction)), 0, 0);
     * }
     * <p>
     * if (scrollingPart > 0) {
     * if (currentScrollSize != scrollingPart) {
     * changeFactor = 1f / scrollingPart;
     * currentScrollSize = scrollingPart;
     * }
     * }
     * }
     * }
     */

    private float getScrollFraction() {
        return toolbarHolder.getFractionOfScroll();
    }

    private float getScrollSize() {
        return toolbarHolder.getScrollingPartSize();
    }

    private void findAvailableFlags() {
        currentAvailableFlag = 0;
        if (getScrollFraction() > 0) {
            currentAvailableFlag |= UP_FLAG;
        }
        if (getScrollFraction() < 1) {
            currentAvailableFlag |= DOWN_FLAG;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /*
        if (toolbarHolder.getMode() != MODE_DEFAULT || getScrollSize() <= 0)
            return false;

        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            currentStartPoint.set(ev.getX(), ev.getY());
            findAvailableFlags();
        }

        if (action == MotionEvent.ACTION_MOVE) {
            float cY = ev.getY();
            float cX = ev.getX();
            float deltaY = cY - currentStartPoint.y;
            float deltaX = cX - currentStartPoint.x;
            float absDeltaY = Math.abs(deltaY);

            if (absDeltaY < Math.abs(deltaX) || absDeltaY < fingerSlop) {
                return false;
            }

            if (currentAvailableFlag == (UP_FLAG | DOWN_FLAG)) return true;
            if (deltaY < 0 && currentAvailableFlag == UP_FLAG) return true;
            return deltaY > 0 && currentAvailableFlag == DOWN_FLAG;
        }

        */

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*
        if (toolbarHolder.getMode() == MODE_DEFAULT || getScrollSize() > 0) {
            if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
                float cY = event.getY();
                float deltaY = cY - currentStartPoint.y;
                float currentChange = deltaY * changeFactor;

                float nextScrollFraction = getScrollFraction() + currentChange;
                nextScrollFraction = nextScrollFraction > 1 ? 1 : nextScrollFraction;
                nextScrollFraction = nextScrollFraction < 0 ? 0 : nextScrollFraction;

                toolbarHolder.setFractionOfScroll(nextScrollFraction);
                currentStartPoint.set(event.getX(), event.getY());
            }
        }
        */
        return super.onTouchEvent(event);
    }

    @Override
    public void onTouchDispatch(MotionEvent ev) {
        if (toolbarHolder.getMode() != MODE_DEFAULT || getScrollSize() <= 0)
            return;

        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            currentStartPoint.set(ev.getX(), ev.getY());
        }

        if (action == MotionEvent.ACTION_MOVE) {
            findAvailableFlags();
            float cY = ev.getY();
            float deltaY = cY - currentStartPoint.y;

            /*
            float cX = ev.getX();
            float deltaX = cX - currentStartPoint.x;
            float absDeltaY = Math.abs(deltaY);

            if (absDeltaY < Math.abs(deltaX) || absDeltaY < fingerSlop) {
                return;
            }
            */

            if ((currentAvailableFlag != (UP_FLAG | DOWN_FLAG)) && ((deltaY < 0 && currentAvailableFlag != UP_FLAG) || (deltaY > 0 && currentAvailableFlag != DOWN_FLAG))) {
                return;
            }

            float currentChange = deltaY * changeFactor;
            float nextScrollFraction = getScrollFraction() + currentChange;
            nextScrollFraction = nextScrollFraction > 1 ? 1 : nextScrollFraction;
            nextScrollFraction = nextScrollFraction < 0 ? 0 : nextScrollFraction;

            toolbarHolder.setFractionOfScroll(nextScrollFraction);
            currentStartPoint.set(ev.getX(), ev.getY());
        }
    }

    private void cancelAnimator() {
        if (springAnimator != null && springAnimator.isStarted()) {
            Log.i(TAG, "cancelAnimator: ");
            springAnimator.cancel();
        }
    }

    private void startSpringAnimator() {
        if (touchModeActive || nonTouchModeActive) return;

        if (springAnimator != null && springAnimator.isStarted()) {
            springAnimator.cancel();
        }

        springAnimator = ValueAnimator.ofFloat(getScrollFraction(), 1).setDuration(200);
        springAnimator.setInterpolator(new OvershootInterpolator());
        springAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                toolbarHolder.setFractionOfScroll((Float) animation.getAnimatedValue());
            }
        });
        springAnimator.start();
        Log.i(TAG, "startSpringAnimator: ");
    }

    private int dispatchSpring(int scrollAmount) {
        float maxFactor = 0.5f;
        float currentFactor = maxFactor - (getScrollFraction() - 1);

        int useAmount = (int) (scrollAmount * (currentFactor / maxFactor));

//        Log.i(TAG, "dispatchSpring: " + useAmount);

        float currentChange = -scrollAmount * changeFactor;
        float nextScrollFraction = getScrollFraction() + currentChange;
//        nextScrollFraction = nextScrollFraction > 1 ? 1 : nextScrollFraction;
//        nextScrollFraction = nextScrollFraction < 0 ? 0 : nextScrollFraction;

//        Log.i(TAG, "dispatchSpring: " + nextScrollFraction);
        toolbarHolder.setFractionOfScroll(nextScrollFraction);

        return useAmount;
    }


    private int dispatchScroll(int scrollAmount) {
        findAvailableFlags();

//        if ((currentAvailableFlag != (UP_FLAG | DOWN_FLAG)) && ((scrollAmount > 0 && currentAvailableFlag != UP_FLAG))) {
//            return 0;
//        }
//        if (scrollAmount < 0 && getScrollFraction() >= 1) {
////            Log.i(TAG, "dispatchScroll: ");
//            return dispatchSpring(scrollAmount);
//        }

        if ((currentAvailableFlag != (UP_FLAG | DOWN_FLAG)) && ((scrollAmount > 0 && currentAvailableFlag != UP_FLAG) || (scrollAmount < 0 && currentAvailableFlag != DOWN_FLAG))) {
            return 0;
        }

        int currentPos = (int) (getScrollSize() * getScrollFraction());
        int out = currentPos - scrollAmount;
        out = out > getScrollSize() ? (int) (out - getScrollSize()) : 0;
        out = Math.min(out, 0);

        float currentChange = -scrollAmount * changeFactor;
        float nextScrollFraction = getScrollFraction() + currentChange;
        nextScrollFraction = nextScrollFraction > 1 ? 1 : nextScrollFraction;
        nextScrollFraction = nextScrollFraction < 0 ? 0 : nextScrollFraction;

        toolbarHolder.setFractionOfScroll(nextScrollFraction);
        return scrollAmount + out;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        boolean out = toolbarHolder.getMode() == MODE_DEFAULT && !(getScrollSize() <= 0);
        switch (type) {
            case ViewCompat.TYPE_TOUCH:
//                Log.i(TAG, "onStartNestedScroll: Touch");
                touchModeActive = true;
                break;
            case ViewCompat.TYPE_NON_TOUCH:
//                Log.i(TAG, "onStartNestedScroll: No Touch");
                nonTouchModeActive = true;
                break;
        }
//        if (out && (touchModeActive || nonTouchModeActive)) {
//            Log.i(TAG, "onStartNestedScroll: cancel");
//            cancelAnimator();
//        }
        return out;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {

    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        switch (type) {
            case ViewCompat.TYPE_TOUCH:
                touchModeActive = false;
//                Log.i(TAG, "onStopNestedScroll: Touch");
                break;
            case ViewCompat.TYPE_NON_TOUCH:
//                Log.i(TAG, "onStopNestedScroll: No Touch");
                nonTouchModeActive = false;
                break;
        }
//        if (!(touchModeActive && nonTouchModeActive) && getScrollFraction() > 1) {
//            post(this::startSpringAnimator);
//        }
//        TODO : check for spring anim.
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        dispatchScroll(dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (dy > 0)
            consumed[1] = dispatchScroll(dy);
    }
}
