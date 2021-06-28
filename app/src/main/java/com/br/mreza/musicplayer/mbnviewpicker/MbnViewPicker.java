package com.br.mreza.musicplayer.mbnviewpicker;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.core.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;


public class MbnViewPicker extends ViewGroup implements Observer {

    public static final int ORI_HORIZONTAL = 0;
    public static final int ORI_VERTICAL = 1;

    private Scroller scroller = new Scroller(getContext());
    private SparseArray<View> views = new SparseArray<>();
    private ArrayList<View> nonLayoutViews = new ArrayList<>();
    private LinkedList<View> pool = new LinkedList<>();
    private Adapter adapter;
    private ItemMover itemMover = new ItemMover();


    private int itemCount;
    private int outOfSightLimit;
    private int itemsInSight;
    private float itemSize;
    private int currentPosition;
    private float currentFraction;
    private float maxFraction;
    private int orientation = ORI_HORIZONTAL;
    private ValueAnimator animator;
    private boolean firstTime = true;
    private int startPoint;
    private Paint paint = new Paint();
    private float density;
    private VelocityTracker velocityTracker;
    private boolean ignore;
    private boolean working;
    private GestureDetectorCompat gestureDetectorCompat;
    private float fingerSlop;
    private ViewConfiguration viewConfiguration;
    private float currentVelocity;
    private ItemChangeListener listener;

    public void setListener(ItemChangeListener listener) {
        this.listener = listener;
    }

    public void setAdapter(Adapter adapter) {
        if (this.adapter != null) this.adapter.deleteObserver(this);
        this.adapter = adapter;
        adapter.addObserver(this);
        init();
    }

    public void setItemMover(ItemMover itemMover) {
        this.itemMover = itemMover;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
        if (adapter != null) init();
    }

    public MbnViewPicker(Context context) {
        super(context);
    }

    public MbnViewPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MbnViewPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MbnViewPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public int getItemCount() {
        return itemCount;
    }

    public int getItemsInSight() {
        return itemsInSight;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    private void init() {
        gestureDetectorCompat = new GestureDetectorCompat(getContext(), gestureListener);
        viewConfiguration = ViewConfiguration.get(getContext());
        fingerSlop = viewConfiguration.getScaledTouchSlop();
        density = getResources().getDisplayMetrics().density;
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GRAY);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
        setWillNotDraw(false);


        scroller.forceFinished(true);
        views.clear();
        nonLayoutViews.clear();
        itemCount = adapter.getItemCount();
        itemsInSight = adapter.getInternalViewSight();
        outOfSightLimit = 1 + ((itemsInSight - 1) / 2);
        currentPosition = 0;
        currentFraction = 0;
        removeAllViews();
        for (int i = 0; i <= itemsInSight + 5; i++) {
            if (i < itemCount)
                getViewForPos(i);
        }
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = r - l;
        int height = b - t;
        itemSize = orientation == ORI_HORIZONTAL ? width / itemsInSight : height / itemsInSight;
        maxFraction = (itemCount - 1) * itemSize;
        startPoint = (int) (orientation == ORI_HORIZONTAL ? (width / 2) - (itemSize / 2) : (height / 2) - (itemSize / 2));
        switch (orientation) {
            case ORI_HORIZONTAL:
                if (changed) {
                    for (int i = 0; i < views.size(); i++) {
                        View v = views.get(views.keyAt(i));
                        v.layout(startPoint, 0, (int) (startPoint + itemSize), height);
                    }
                } else {
                    for (View v : nonLayoutViews) {
                        v.layout(startPoint, 0, (int) (startPoint + itemSize), height);
                    }
                }
                break;
            case ORI_VERTICAL:
                if (changed) {
                    for (int i = 0; i < views.size(); i++) {
                        View v = views.get(views.keyAt(i));
                        v.layout(0, startPoint, width, (int) (startPoint + itemSize));
                    }
                } else {
                    for (View v : nonLayoutViews) {
                        v.layout(0, startPoint, width, (int) (startPoint + itemSize));
                    }
                }
                break;
        }
        nonLayoutViews.clear();
        if (firstTime)
            selectItemInternal(currentPosition);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (orientation) {
            case ORI_HORIZONTAL:
                canvas.drawLine(startPoint, 5 * density, startPoint, getHeight() - 5 * density, paint);
                canvas.drawLine(startPoint + itemSize, 5 * density, itemSize + startPoint, getHeight() - 5 * density, paint);
                break;
            default:
                canvas.drawLine(20 * density, startPoint, getWidth() - 20 * density, startPoint, paint);
                canvas.drawLine(20 * density, startPoint + itemSize, getWidth() - 20 * density, startPoint + itemSize, paint);
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int vW = 0;
        int vH = 0;

        switch (orientation) {
            case ORI_HORIZONTAL:
                vH = height;
                vW = width / itemsInSight;
                break;
            case ORI_VERTICAL:
                vH = height / itemsInSight;
                vW = width;
                break;
        }

        for (View v : nonLayoutViews) {
            v.measure(MeasureSpec.makeMeasureSpec(vW, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(vH, MeasureSpec.EXACTLY));
        }
        setMeasuredDimension(width, height);
    }

    private void selectItemInternal(int pos) {
        firstTime = false;
        currentPosition = pos;
        currentFraction = pos * itemSize;
        for (int i = 0; i < views.size(); i++) {
            int key = views.keyAt(i);
            View v = views.get(key);
            if (Math.abs(key - pos) > outOfSightLimit) {
                putViewInPool(v, key);
                i--;
            } else {
                itemMover.onPositionChanged(v, this, key - pos, outOfSightLimit, orientation);
            }
        }

        if ((pos + outOfSightLimit) < itemCount) {
            if (views.get(pos + outOfSightLimit) == null) {
                getViewForPos(pos + outOfSightLimit);
            }
        }
        if ((pos - outOfSightLimit) >= 0) {
            if (views.get(pos - outOfSightLimit) == null) {
                getViewForPos(pos - outOfSightLimit);
            }
        }

    }

    private void putViewInPool(View view, int key) {
        views.remove(key);
        adapter.viewOutOfSight(view, key);
        view.setVisibility(GONE);
        pool.add(view);
    }

    private View getViewForPos(final int pos) {
        View v = pool.poll();
        if (v != null) {
            adapter.prepareViewForPosition(v, pos);
            views.put(pos, v);
        } else {
            v = adapter.getView(pos);
            views.put(pos, v);
            nonLayoutViews.add(v);
            addView(v);
        }
        v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentItem(pos);
            }
        });
        return v;
    }


    private void onFractionChanged(float fraction) {
        fraction = fraction > maxFraction ? maxFraction : fraction;
        fraction = fraction < 0 ? 0 : fraction;
        if (fraction >= 0 && fraction <= maxFraction) {
            currentFraction = fraction;

            float practicalFraction = fraction / itemSize;
            int pos = currentPosition = (int) (practicalFraction + 0.5f);

            for (int i = 0; i < itemCount; i++) {
                if (Math.abs(i - practicalFraction) > outOfSightLimit) {
                    if (views.get(i) != null) putViewInPool(views.get(i), i);
                } else {
                    View v = views.get(i);
                    if (v == null) v = getViewForPos(i);
                    itemMover.onPositionChanged(v, this, i - practicalFraction, outOfSightLimit, orientation);
                }
            }

//            for (int i = 0; i < views.size(); i++) {
//                int key = views.keyAt(i);
//                View v = views.get(key);
//                if (Math.abs(key - pos) > outOfSightLimit) {
//                    putViewInPool(v, key);
//                    i--;
//                } else {
//                    itemMover.onPositionChanged(v, this, key - practicalFraction, outOfSightLimit, orientation);
//                }
//            }
//
//
//            if ((pos + outOfSightLimit) < itemCount) {
//                if (views.get(pos + outOfSightLimit) == null) {
//                    Log.i(TAG, "onFractionChanged: " + pos + outOfSightLimit);
//                    getViewForPos(pos + outOfSightLimit);
//                }
//            }
//            if ((pos - outOfSightLimit) >= 0) {
//                if (views.get(pos - outOfSightLimit) == null) {
//                    getViewForPos(pos - outOfSightLimit);
//                }
//            }
        }

    }

    public void setCurrentItem(int pos) {
        pos = pos >= itemCount ? itemCount - 1 : pos;
        pos = pos < 0 ? 0 : pos;

        currentPosition = pos;
        settleAnimation();
    }

    private void settleAnimation() {
        if (animator != null) {
            try {
                animator.removeAllUpdateListeners();
                animator.removeAllListeners();
                animator.cancel();
                animator = null;
            } catch (Exception ignored) {
            }
        }

        animator = ValueAnimator.ofFloat(currentFraction, currentPosition * itemSize);
        animator.setInterpolator(new OvershootInterpolator());
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                onFractionChanged((Float) animation.getAnimatedValue());
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                selectItemInternal(currentPosition);
                if (listener != null) listener.onCurrentItemChanged(currentPosition);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_UP || ev.getActionMasked() == MotionEvent.ACTION_CANCEL) {
            velocityTracker.recycle();
            ignore = false;
            working = false;
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);
            scroller.forceFinished(true);
            removeCallbacks(flingRunnable);
            ignore = false;
            working = false;
            try {
                velocityTracker.recycle();
            } catch (Exception ignored) {
            }
            velocityTracker = VelocityTracker.obtain();
            velocityTracker.addMovement(ev);
        }
        return gestureDetectorCompat.onTouchEvent(ev) || super.onInterceptTouchEvent(ev);

    }

    private void findCurrentVelocity() {
        velocityTracker.computeCurrentVelocity(1000);
        switch (orientation) {
            case ORI_VERTICAL:
                currentVelocity = velocityTracker.getYVelocity();
                break;

            case ORI_HORIZONTAL:
                currentVelocity = velocityTracker.getXVelocity();
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

            if (Math.abs(currentVelocity) >= viewConfiguration.getScaledMinimumFlingVelocity()) {
                scroller.fling((int) currentFraction, 0, (int) -currentVelocity, 0, 0, (int) maxFraction, 0, 0);
                post(flingRunnable);
            } else {
//                if (getActiveAnim() == ANIM_CIRCLE_REVEAL) circleRevealAnimation(true);
                settleAnimation();
            }


            velocityTracker.recycle();
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

        private void sendToFractionMethod(float fraction) {
            fraction = currentFraction + fraction;
            onFractionChanged(fraction);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {


            switch (orientation) {
                case ORI_VERTICAL:
                    if (working) {
                        sendToFractionMethod(distanceY);
//                        sendToFractionMethod(e2.getY() - e1.getY());
                        return true;
                    }
                    if (Math.abs(e2.getY() - e1.getY()) > fingerSlop) {
                        working = true;
                        if (animator != null) {
                            try {
                                animator.removeAllUpdateListeners();
                                animator.removeAllListeners();
                                animator.cancel();
                                animator = null;
                            } catch (Exception ignored) {
                            }
                        }
                        sendToFractionMethod(distanceY);
//                        sendToFractionMethod(e2.getY() - e1.getY());
                        return true;
                    }
                    break;

                case ORI_HORIZONTAL:
                    if (working) {
                        sendToFractionMethod(distanceX);
//                        sendToFractionMethod(e2.getX() - e1.getX());
                        return true;
                    }
                    if (Math.abs(e2.getX() - e1.getX()) > fingerSlop) {
                        working = true;
                        if (animator != null) {
                            try {
                                animator.removeAllUpdateListeners();
                                animator.removeAllListeners();
                                animator.cancel();
                                animator = null;
                            } catch (Exception ignored) {
                            }
                        }
                        sendToFractionMethod(distanceX);
//                        sendToFractionMethod(e2.getX() - e1.getX());
                        return true;
                    }
                    break;
            }
            return false;
        }

//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//
//            Log.i("touch", String.valueOf(velocityY));
//
//
//            if (velocityX > 0) {
//                isFling = 1;
//            } else {
//                isFling = 2;
//            }
//
//            return super.onFling(e1, e2, velocityX, velocityY);
//        }
    };

    private Runnable flingRunnable = new Runnable() {
        @Override
        public void run() {
            scroller.computeScrollOffset();
            boolean notFinish = scroller.computeScrollOffset();
            onFractionChanged(scroller.getCurrX());
            if (notFinish) {
                post(this);
            } else {
                settleAnimation();
                removeCallbacks(this);
            }
        }
    };


    @Override
    public void update(Observable o, Object arg) {
        init();
    }

    public interface ItemChangeListener {
        void onCurrentItemChanged(int pos);
    }
}
