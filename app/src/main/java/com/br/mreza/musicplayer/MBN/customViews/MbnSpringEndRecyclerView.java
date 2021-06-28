package com.br.mreza.musicplayer.MBN.customViews;


import android.animation.ValueAnimator;
import android.content.Context;
import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;

import com.br.mreza.musicplayer.UI.RecyclerViews.ItemDecors.SpringOnScrollEnding;

public class MbnSpringEndRecyclerView extends RecyclerView {

    private boolean ignore = false;
    private SpringOnScrollEnding springOnScrollEnding;
    private GestureDetectorCompat gestureDetectorCompat;
    private int pivot = 0;
    private ValueAnimator animator;
    private boolean active = true;

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {

        private float disY;

        @Override
        public boolean onDown(MotionEvent e) {
            active = true;
            disY = 0;
            return super.onDown(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            if (!active) {
                return false;
            }

            if (e2.getY() - e1.getY() > 0 && !canScrollVertically(-1)) {
                pivot = getHeight();
                float scale;
//                if ((e2.getY() - e1.getY()) > getHeight() / 4) {
//                    scale = 0.25f;
//                } else {
//                Log.i("loooooooooo", String.valueOf(Math.log10(Math.pow(e2.getY() - e1.getY(), 15))));
//                Log.i("loooooooooo", String.valueOf(Math.log10(getHeight())));
//                scale = (float) (Math.log10(Math.pow(e2.getY() - e1.getY(), 10)) / getHeight());
//                scale = ((Math.abs(e2.getY() - e1.getY())) / (getHeight() * 2));
                scale = ((Math.abs(disY)) / (getHeight() * 2));
//                }
                springOnScrollEnding.setCurrentScale(scale, pivot);
                disY += distanceY;
                return true;
            } else if (e2.getY() - e1.getY() < 0 && !canScrollVertically(1)) {
                pivot = 0;
                float scale;
//                if (Math.abs(e2.getY() - e1.getY()) > getHeight() / 2) {
//                    scale = 0.5f;
//                } else {
//                scale = ((Math.abs(e2.getY() - e1.getY())) / (getHeight() * 2));
                scale = ((Math.abs(disY)) / (getHeight() * 2));

//                }
                springOnScrollEnding.setCurrentScale(scale, pivot);
                disY += distanceY;
                return true;
            }


            if ((animator == null || !animator.isRunning()) && springOnScrollEnding.getCurrentScale() != 0) {
                animateScale();
                active = false;
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    };


    private void animateScale() {
        if (animator != null) {
            animator.removeAllUpdateListeners();
            animator.removeAllListeners();
            animator.cancel();
            animator = null;
        }

        if (springOnScrollEnding.getCurrentScale() != 0) {
            animator = ValueAnimator.ofFloat(springOnScrollEnding.getCurrentScale(), 0);
            animator.setDuration(500);
            animator.setInterpolator(new OvershootInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    springOnScrollEnding.setCurrentScale((Float) animation.getAnimatedValue(), pivot);
                }
            });
//            animator.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    active = true;
//                }
//            });
            animator.start();
        }

    }

    public MbnSpringEndRecyclerView(Context context) {
        super(context);
    }

    public MbnSpringEndRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MbnSpringEndRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SpringOnScrollEnding getSpringOnScrollEnding() {
        return springOnScrollEnding;
    }

    public void setSpringOnScrollEnding(SpringOnScrollEnding springOnScrollEnding) {
        this.springOnScrollEnding = springOnScrollEnding;
        gestureDetectorCompat = new GestureDetectorCompat(getContext(), gestureListener);
        addItemDecoration(springOnScrollEnding);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (springOnScrollEnding != null) {
            gestureDetectorCompat.onTouchEvent(e);
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (springOnScrollEnding != null) {
            if (e.getActionMasked() == MotionEvent.ACTION_CANCEL || e.getActionMasked() == MotionEvent.ACTION_UP) {
                animateScale();
            }
            gestureDetectorCompat.onTouchEvent(e);
        }
        return super.onTouchEvent(e);
    }
}
