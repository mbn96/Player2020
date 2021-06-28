package com.br.mreza.musicplayer.UI.RecyclerViews.ItemDecors;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Canvas;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;


public class SpringOnScrollEnding extends RecyclerView.ItemDecoration implements View.OnTouchListener {


    private RecyclerView recyclerView;
    private GestureDetectorCompat gestureDetectorCompat;

    private ValueAnimator getToNormalAnimator;
    private ValueAnimator flingAnimator;

    private int currentPosition = 0;
    private int scrollPositionSum = 0;

    private float currentScale = 0;
    private int pivot = 0;

    public SpringOnScrollEnding(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
//        recyclerView.addOnItemTouchListener(this);
        gestureDetectorCompat = new GestureDetectorCompat(recyclerView.getContext(), gestureListener);
        recyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        c.scale(0, 0);

    }

    public void setCurrentScale(float scale, int pivot) {
        currentScale = scale;
        this.pivot = pivot;
        recyclerView.invalidate();
    }

    public float getCurrentScale() {
        return currentScale;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
//        Log.i("inscr", String.valueOf(currentPosition) + "___ in draw");

        c.translate(0, currentPosition);

        c.scale(1, 1 - currentScale, 0, pivot);
    }


    private void cancelAnimators() {
        if (flingAnimator != null) {
            flingAnimator.cancel();
        }
        if (getToNormalAnimator != null) {
            getToNormalAnimator.cancel();
        }

    }

//    @Override
//    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//
//        if (e.getActionMasked() == MotionEvent.ACTION_DOWN) {
//            currentPosition = 0;
//            scrollPositionSum = 0;
//            cancelAnimators();
//        }
//
//        if (e.getActionMasked() == MotionEvent.ACTION_CANCEL || e.getActionMasked() == MotionEvent.ACTION_UP) {
//            if (currentPosition != 0) {
//                normalAnimator();
//            }
//            scrollPositionSum = 0;
//        }
//
//        return gestureDetectorCompat.onTouchEvent(e);
//    }
//
//    @Override
//    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//
//        if (e.getActionMasked() == MotionEvent.ACTION_DOWN) {
//            currentPosition = 0;
//            scrollPositionSum = 0;
//            cancelAnimators();
//        }
//
//        if (e.getActionMasked() == MotionEvent.ACTION_CANCEL || e.getActionMasked() == MotionEvent.ACTION_UP) {
//            if (currentPosition != 0) {
//                normalAnimator();
//            }
//            scrollPositionSum = 0;
//        }
//
//        gestureDetectorCompat.onTouchEvent(e);
//    }
//
//    @Override
//    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent e) {
        if (e.getActionMasked() == MotionEvent.ACTION_DOWN) {
            currentPosition = 0;
            scrollPositionSum = 0;
            cancelAnimators();
        }

        if (e.getActionMasked() == MotionEvent.ACTION_CANCEL || e.getActionMasked() == MotionEvent.ACTION_UP) {
            if (currentPosition != 0) {
                normalAnimator();
            }
            scrollPositionSum = 0;
        }

        return gestureDetectorCompat.onTouchEvent(e);
    }


    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {


        @Override
        public boolean onDown(MotionEvent e) {
            return !recyclerView.canScrollVertically(-1) || !recyclerView.canScrollVertically(1) || super.onDown(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {


            if (e2.getY() - e1.getY() > 0 && !recyclerView.canScrollVertically(-1)) {
                currentPosition = (int) (e2.getY() - e1.getY() + scrollPositionSum);
                recyclerView.invalidate();
                return true;
            } else if (e2.getY() - e1.getY() < 0 && !recyclerView.canScrollVertically(1)) {
                currentPosition = (int) (e2.getY() - e1.getY() + scrollPositionSum);
                recyclerView.invalidate();
                return true;
            }

            //                currentPosition = (int) (e2.getY() - e1.getY());
//                recyclerView.invalidate();
            return false;
        }
    };


    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (currentPosition != 0 && (currentPosition * (currentPosition - dy)) > 0) {
                currentPosition -= dy;
            } else {
                currentPosition = 0;
            }

            scrollPositionSum += dy;

            if (dy != 0 && recyclerView.getScrollState() == 2) {
                if (dy > 0) {
                    if (!recyclerView.canScrollVertically(1)) {
//                        Log.i("inscr", dy + "___" + recyclerView.getScrollState() + "___" + recyclerView.canScrollVertically(1));

                        flingAnimator(dy);

                    }
                } else if (!recyclerView.canScrollVertically(-1)) {
                    flingAnimator(dy);
                }
            }
        }
    };


    private void flingAnimator(int speed) {

        if (flingAnimator != null) {
            flingAnimator.cancel();
        }

        flingAnimator = ValueAnimator.ofInt(-speed / 2, 0);
        flingAnimator.setDuration(Math.abs(speed));
        flingAnimator.setInterpolator(new DecelerateInterpolator());
        flingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

//                Log.i("inscr", String.valueOf(animation.getAnimatedValue()));

                currentPosition += (int) animation.getAnimatedValue();
                recyclerView.invalidate();
            }
        });
        flingAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                normalAnimator();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        flingAnimator.start();

    }


    private void normalAnimator() {

        if (getToNormalAnimator != null) {
            getToNormalAnimator.cancel();
        }

        getToNormalAnimator = ValueAnimator.ofInt(currentPosition, 0);
        getToNormalAnimator.setDuration(Math.abs(500));
        getToNormalAnimator.setInterpolator(new OvershootInterpolator());
        getToNormalAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                Log.i("inscr", String.valueOf(animation.getAnimatedValue()));

                currentPosition = (int) animation.getAnimatedValue();
                recyclerView.invalidate();
            }
        });
        getToNormalAnimator.start();


    }

}
