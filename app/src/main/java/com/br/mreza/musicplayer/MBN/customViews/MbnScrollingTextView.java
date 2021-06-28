package com.br.mreza.musicplayer.MBN.customViews;


import android.content.Context;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;


public class MbnScrollingTextView extends androidx.appcompat.widget.AppCompatTextView {

    private Scroller scroller;
    private float x;
    private float y;
    private boolean ok;
    private int mTouchSlop;
    private boolean direction;
    private TextViewSwipeHelper helper;
    private int length;
    private double factor;
    private int timeFactor = 6;

    private String TAG = "mbn_text";

    public MbnScrollingTextView(Context context) {
        super(context);
        init();
    }

    public MbnScrollingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MbnScrollingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop() + 100;

//        scroller = new Scroller(getContext(), new Interpolator() {
//            @Override
//            public float getInterpolation(float input) {
//                float sin = (float) Math.abs(Math.sin((Math.PI) * (input)) * 0.5);
//                if (input < 0.5f) {
//                    return sin;
//                }
////                double extraFactor = (1d / factor) / 2d;
////                return (float) (extraFactor * ((factor) + (factor - sin)));
//                return (float) (0.5 + (0.5 - sin));
////                return (float) ((Math.pow((input - 0.5), 2)));
//            }
//        });
        scroller = new Scroller(getContext(), new AccelerateDecelerateInterpolator());
        setScroller(scroller);

    }

    public void setHelper(TextViewSwipeHelper helper) {
        this.helper = helper;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setSingleLine();
        setEllipsize(null);
        setHorizontallyScrolling(true);
        checkForScroll();
    }


    private void checkForScroll() {

        if (scroller != null) {
            scroller.setFinalX(0);
            scroller.abortAnimation();
            removeCallbacks(firstAnim);
            removeCallbacks(secondAnim);
        }

        TextPaint textPaint = getPaint();
        Rect rect = new Rect();
        String text = getText().toString();
        textPaint.getTextBounds(text, 0, text.length(), rect);
        length = rect.width();

        if (length > getWidth()) {
//            factor = (double) length / getWidth();
//            factor++;
//            factor = 1 / factor;
//            startScrolling(length);
//            setScroller(scroller);
            post(firstAnim);
        } else {
//            setScroller(null);
            int extra = getWidth() - length;
            scrollTo(-extra / 2, 0);
            invalidate();
        }
    }

    private Runnable firstAnim = new Runnable() {
        @Override
        public void run() {
            int time = length * timeFactor;
            scroller.abortAnimation();
            scroller.startScroll(0, 0, length, 0, time);
            invalidate();
            postDelayed(secondAnim, time + 100);
        }
    };

    private Runnable secondAnim = new Runnable() {
        @Override
        public void run() {
            int time = getWidth() * timeFactor;
            scroller.abortAnimation();
            scroller.startScroll(-getWidth(), 0, getWidth(), 0, time);
            invalidate();
            postDelayed(firstAnim, time + 1000);
        }
    };

//    private void startScrolling(int length) {
//
//
////        scroller.startScroll(-getWidth(), 0, length, 0, getText().length() * 300);
//        scroller.startScroll(-getWidth(), 0, length + getWidth(), 0, getText().length() * 300);
//
//
//    }


    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        try {
            scroller.forceFinished(true);
            scroller.abortAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (getWidth() > 0) {
            checkForScroll();
        }

    }

//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//
//        switch (event.getAction()) {
//
//            case MotionEvent.ACTION_DOWN:
//                x = event.getX();
//                y = event.getY();
//                getParent().requestDisallowInterceptTouchEvent(true);
//                ok = false;
//                return true;
//            case MotionEvent.ACTION_MOVE:
//                float newX, newY;
//
//
//                newX = event.getX();
//                newY = event.getY();
//                float absXdiff = Math.abs(newX - x);
//                float absYdiff = Math.abs(newY - y);
//
//                if (absYdiff >= mTouchSlop) {
//
//                    return false;
//                }
//
//                if (absXdiff > absYdiff && absXdiff >= mTouchSlop) {
//
//
//                    getParent().requestDisallowInterceptTouchEvent(true);
//
//                    ok = true;
//
//                    direction = (newX - x) > 0;
//
//                } else {
//                    ok = false;
//                }
//
//                return true;
//
//            case MotionEvent.ACTION_CANCEL & MotionEvent.ACTION_UP:
//
//
//                if (ok) {
//                    check();
//                }
//
//                break;
//
//        }
//
//
//        return super.onTouchEvent(event);
//    }


    private void check() {

        if (helper != null) {

            helper.onSwipe(direction);


        }


    }

    public interface TextViewSwipeHelper {

        void onSwipe(boolean direction);

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (length <= getWidth()) {
            int extra = getWidth() - length;
            scrollTo(-extra / 2, 0);
        }
//        if (scroller != null) {
//            if (scroller.isFinished()) {
//                checkForScroll();
//            }
//        }
    }
}
