package mbn.libs.fragmanager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import java.util.ArrayList;


public class MbnNavDrawer extends FrameLayout {

    public static final int STATE_OPEN = 0;
    public static final int STATE_CLOSED = 1;

    private FrameLayout mainLayout;
    private DrawerLayout drawerLayout;
    private View touchGetter;

    private int currentState = STATE_CLOSED;

    private ArrayList<MbnNavDrawerChangeListener> changeListeners = new ArrayList<>();
    private VelocityTracker velocityTracker;
    private boolean ignore;
    private boolean working;
    private ViewConfiguration viewConfiguration;
    private float fingerSlop;
    private int moveTouchEventCount;
    private GestureDetectorCompat gestureDetectorCompat;
    private float currentVelocity;
    private Animator animator;
    private float density;
    private float currentFraction = 0;

    public MbnNavDrawer(@NonNull Context context) {
        super(context);
        init();
    }

    public MbnNavDrawer(@NonNull Context context, View main, View drawer) {
        super(context);
        init();
        setMainView(main);
        setDrawerView(drawer);
    }

    public void setMainView(View main) {
        mainLayout.addView(main);
    }

    public void setDrawerView(View drawer) {
        drawerLayout.addView(drawer);
        drawerLayout.setVisibility(INVISIBLE);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        mainLayout = new FrameLayout(getContext());
        drawerLayout = new DrawerLayout(getContext());
        drawerLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.LEFT));
        touchGetter = new View(getContext());
        touchGetter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentState(STATE_CLOSED);
            }
        });
        addView(drawerLayout);
        addView(mainLayout);
        density = getResources().getDisplayMetrics().density;
        mainLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        viewConfiguration = ViewConfiguration.get(getContext());
        fingerSlop = viewConfiguration.getScaledTouchSlop();
        gestureDetectorCompat = new GestureDetectorCompat(getContext(), gestureListener);
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
//        if (this.currentState != currentState) {
        this.currentState = currentState;
        animation(currentState == STATE_OPEN);
//        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_UP || ev.getActionMasked() == MotionEvent.ACTION_CANCEL) {
            try {
                velocityTracker.recycle();
            } catch (Exception ignored) {
            }
            ignore = false;
            working = false;
        }

        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            moveTouchEventCount = 0;
            ignore = false;
            working = false;
            try {
                velocityTracker.recycle();
            } catch (Exception ignored) {
            }
            velocityTracker = VelocityTracker.obtain();
            velocityTracker.addMovement(ev);
        }
//        if (ev.getAction() == MotionEvent.ACTION_MOVE && moveTouchEventCount < 2) {
//            moveTouchEventCount++;
//            return false;
//        }

        return super.onInterceptTouchEvent(ev) || gestureDetectorCompat.onTouchEvent(ev);
    }


    private void findCurrentVelocity() {
        velocityTracker.computeCurrentVelocity(1000);
        switch (currentState) {
            case STATE_CLOSED:
                currentVelocity = velocityTracker.getXVelocity();
                break;
            case STATE_OPEN:
                currentVelocity = -velocityTracker.getXVelocity();
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        velocityTracker.addMovement(event);
        if ((event.getActionMasked() == MotionEvent.ACTION_UP || event.getActionMasked() == MotionEvent.ACTION_CANCEL)) {
            findCurrentVelocity();
//            Log.i("veloc", String.valueOf(currentVelocity));
            switch (currentState) {
                case STATE_CLOSED:
                    if (currentFraction > 0.5f || currentVelocity >= viewConfiguration.getScaledMinimumFlingVelocity())
                        setCurrentState(STATE_OPEN);
                    else setCurrentState(STATE_CLOSED);
                    break;
                case STATE_OPEN:
                    if (currentFraction < 0.5f || currentVelocity >= viewConfiguration.getScaledMinimumFlingVelocity())
                        setCurrentState(STATE_CLOSED);
                    else setCurrentState(STATE_OPEN);
                    break;
            }
//                if (currentFraction > 0.5f || currentVelocity > 30) {
//                    if (currentState == STATE_CLOSED) {
//                        setCurrentState(STATE_OPEN);
//                    }
//                } else {
//                    setCurrentState(STATE_CLOSED);
//                }
            try {
                velocityTracker.recycle();
            } catch (Exception ignored) {
            }
            ignore = false;
            working = false;
            return true;
        }

//        gestureDetectorCompat.onTouchEvent(event);
//        return true;

//        return super.onTouchEvent(event) && gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event) || gestureDetectorCompat.onTouchEvent(event);
//        return gestureDetectorCompat.onTouchEvent(event) || super.onTouchEvent(event);
    }

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (ignore) {
                return false;
            }

            switch (currentState) {
                case STATE_CLOSED:
                    if ((!working && Math.abs(e2.getRawY() - e1.getRawY()) > fingerSlop * 3) || e1.getX() > getWidth() / 10) {
                        ignore = true;
                        return false;
                    }
                    if ((animator == null || !animator.isRunning()) && e2.getRawX() - e1.getRawX() > fingerSlop) {
                        working = true;
                        fractionGetter((e2.getRawX() - e1.getRawX()) / getWidth());
                        return true;
                    }
                    break;

                case STATE_OPEN:
                    if (!working && Math.abs(e2.getRawY() - e1.getRawY()) > fingerSlop * 3) {
                        ignore = true;
                        return false;
                    }
                    if ((animator == null || !animator.isRunning()) && e1.getRawX() - e2.getRawX() > fingerSlop) {
                        working = true;
                        fractionGetter(1 - ((e1.getRawX() - e2.getRawX()) / getWidth()));
                        return true;
                    }
                    break;
            }
            return false;
        }
    };


    private void animation(boolean open) {
        if (animator != null) {
            animator.cancel();
            animator.removeAllListeners();
            try {
                ((ValueAnimator) animator).removeAllUpdateListeners();
            } catch (Exception ignored) {
            }
            animator = null;
        }
        int end = open ? 1 : 0;
//        int isFling = 0;
        animator = ValueAnimator.ofFloat(currentFraction, end);
        ((ValueAnimator) animator).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fractionGetter((Float) animation.getAnimatedValue());
            }
        });
        animator.setDuration((long) (Math.abs(end - currentFraction) * 500f));
        if ((Math.abs(end - currentFraction) == 1))
            animator.setInterpolator(new FastOutSlowInInterpolator());
        else animator.setInterpolator(new DecelerateInterpolator());

        if (open) {
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    outSideTouchView(true);
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        } else {
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    outSideTouchView(false);
                }
            });
        }
        animator.start();
    }

    private void outSideTouchView(boolean add) {
        mainLayout.removeView(touchGetter);
        if (add) mainLayout.addView(touchGetter);
        else {
            mainLayout.removeView(touchGetter);
        }
    }

    private void fractionGetter(float fraction) {
        if (fraction >= 0 && fraction <= 1) {
            if (fraction == 0) {
                drawerLayout.setVisibility(INVISIBLE);
                mainLayout.setElevation(0);
                mainLayout.setBackground(null);
            } else {
                drawerLayout.setVisibility(VISIBLE);
                mainLayout.setElevation(5 * density);
                mainLayout.setBackgroundColor(Color.WHITE);
            }
            currentFraction = fraction;
            mainLayout.setTranslationX((getWidth() * (2 / 3f)) * fraction);
            int width = drawerLayout.getWidth();
            drawerLayout.setTranslationX(((-1 / 3f) * width) + ((1 / 3f) * fraction * width));
//            drawerLayout.setTranslationX((getWidth() / 2) * (-1 + fraction));
            for (MbnNavDrawerChangeListener cl : changeListeners) {
                cl.onSlide(fraction);
            }
        }
    }

    public void addChangeListener(MbnNavDrawerChangeListener changeListener) {
        changeListeners.add(changeListener);
    }

    public void removeChangeListener(MbnNavDrawerChangeListener changeListener) {
        changeListeners.remove(changeListener);
    }

    public interface MbnNavDrawerChangeListener {
        void onSlide(float slide);

//        void onStateChange(int state);
    }

    private class DrawerLayout extends FrameLayout {
        public DrawerLayout(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int sendWidth = MeasureSpec.makeMeasureSpec((int) (MeasureSpec.getSize(widthMeasureSpec) * (2 / 3f)), MeasureSpec.getMode(widthMeasureSpec));
            super.onMeasure(sendWidth, heightMeasureSpec);
//            setMeasuredDimension((int) (MeasureSpec.getSize(widthMeasureSpec) * (2 / 3f)), MeasureSpec.getSize(heightMeasureSpec));
        }
    }


//    public static class MbnUpPageButton extends View implements MbnNavDrawerChangeListener {
//
//        private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        private Path mPath = new Path();
//
//        private int mState = STATE_CLOSED;
//        private float currentFraction = 0;
//        private float density;
//        private MbnNavDrawer mbnNavDrawer;
//
//        public MbnUpPageButton(Context context) {
//            super(context);
//            init();
//        }
//
//        public MbnUpPageButton(Context context, @Nullable AttributeSet attrs) {
//            super(context, attrs);
//            init();
//        }
//
//        public MbnUpPageButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//            super(context, attrs, defStyleAttr);
//            init();
//        }
//
//        public MbnUpPageButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//            super(context, attrs, defStyleAttr, defStyleRes);
//            init();
//        }
//
//        private void init() {
//            density = getResources().getDisplayMetrics().density;
//            mPaint.setColor(Color.GRAY);
//            mPaint.setStyle(Paint.Style.STROKE);
//            mPaint.setStrokeCap(Paint.Cap.ROUND);
//            mPaint.setStrokeJoin(Paint.Join.ROUND);
//            mPaint.setStrokeWidth(density);
//            setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mbnNavDrawer != null) mbnNavDrawer.setCurrentState(STATE_OPEN);
//                }
//            });
//            prepareForDrawing();
//        }
//
//        public void setCurrentFraction(float currentFraction) {
//            this.currentFraction = currentFraction;
//            prepareForDrawing();
//        }
//
//        private void prepareForDrawing() {
//            mPath.rewind();
//
//            //middle line
//            float halfSize = 20 * density * currentFraction;
//            mPath.moveTo(getWidth() / 2, (getHeight() / 2) - halfSize);
//            mPath.lineTo(getWidth() / 2, (getHeight() / 2) + halfSize);
//
//            //left line
//            float leftX = getWidth() / 2 - (20 * density);
//            mPath.moveTo(leftX, getHeight() / 2);
//            mPath.lineTo(leftX + halfSize, (getHeight() / 2) - halfSize);
//
//            //right line
//            float rightX = getWidth() / 2 + (20 * density);
//            mPath.moveTo(rightX, getHeight() / 2);
//            mPath.lineTo(rightX - halfSize, (getHeight() / 2) - halfSize);
//
//            invalidate();
//        }
//
//
//        @Override
//        protected void onDraw(Canvas canvas) {
//            canvas.save();
//            canvas.rotate(90 * currentFraction);
//            canvas.drawPath(mPath, mPaint);
//            canvas.restore();
//        }
//
//        public void syncWithDrawer(MbnNavDrawer mbnNavDrawer) {
//            mbnNavDrawer.addChangeListener(this);
//            this.mbnNavDrawer = mbnNavDrawer;
//        }
//
//        public void unSyncWithDrawer() {
//            if (mbnNavDrawer != null)
//                mbnNavDrawer.removeChangeListener(this);
//        }
//
//        @Override
//        public void onSlide(float slide) {
//            setCurrentFraction(slide);
//        }
//    }
}
