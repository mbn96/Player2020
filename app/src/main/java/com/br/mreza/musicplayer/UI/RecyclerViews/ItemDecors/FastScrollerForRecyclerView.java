package com.br.mreza.musicplayer.UI.RecyclerViews.ItemDecors;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;


public class FastScrollerForRecyclerView extends RecyclerView.ItemDecoration implements RecyclerView.OnItemTouchListener {

    private float density;
    private float touchAbsorbWidth;
    private float actualWidth;
    private float handleBarHeight;

    private int itemCount;
    private int currentItem;
    private int height;
    private float currentPosition;
    private float visualPosition;
//    private float pixelPerItem;

    private boolean inTouch;

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private GestureDetectorCompat gestureDetector;
    private Context context;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paint_track;
    private Path path = new Path();
    private FastScrollListener listener;
    private boolean isVisible = false;
    private int timeToHide = 3000;
    private float moveFactor = 0;
    private ValueAnimator animator;

    private Runnable countDown = new Runnable() {
        @Override
        public void run() {
            if (timeToHide > 0) {
                timeToHide -= 500;
                setVisible(true);
            } else {
                setVisible(false);
            }
            recyclerView.postDelayed(this, 500);
        }
    };

    public void setVisible(boolean visible) {
        if (this.isVisible != visible) {
            isVisible = visible;
            recyclerView.invalidate();
            animate();
        }
    }

    public FastScrollerForRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        context = recyclerView.getContext();
        gestureDetector = new GestureDetectorCompat(recyclerView.getContext(), gestureListener);
        recyclerView.addOnItemTouchListener(this);
        density = context.getResources().getDisplayMetrics().density;
        touchAbsorbWidth = density * 60;
        actualWidth = 60 * density;
        handleBarHeight = 120 * density;

        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(4 * density);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(24 * density);
        paint_track = new Paint(paint);
        paint_track.setStrokeWidth(density);
        paint_track.setColor(Color.LTGRAY);
        createThePath();
        recyclerView.post(countDown);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                timeToHide = 3000;
            }
        });
    }

    private void adapterChanged(RecyclerView recyclerView) {
        adapter = recyclerView.getAdapter();
        itemCount = adapter.getItemCount();
        height = (int) (recyclerView.getHeight() - handleBarHeight);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getAdapter() != null && moveFactor < 1) {
            if (parent.getAdapter().getItemCount() != itemCount || !parent.getAdapter().equals(adapter)) {
                adapterChanged(parent);
            }
            c.save();
            c.translate(moveFactor * (actualWidth / 2), 0);
            actualDrawing(parent, c);
            c.restore();
        }
    }

    private void actualDrawing(RecyclerView recyclerView, Canvas canvas) {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View child = recyclerView.getChildAt(i);
            if (child.getBottom() > 0) {
                currentItem = recyclerView.getChildAdapterPosition(child);
                break;
            }
        }

        visualPosition = height * (currentItem / (float) itemCount);

        if (inTouch) {
            paint.setColor(Color.parseColor("#00b4ff"));
            if (listener != null) {
                drawBubble(canvas, listener.getItemFirstLetter(currentItem));
            }
        } else {
            paint.setColor(Color.GRAY);
            currentPosition = height * (currentItem / (float) itemCount);
        }
        paint.setStyle(Paint.Style.STROKE);
        float x = canvas.getWidth() - (actualWidth / 2);
        paint.setStrokeWidth(density);
        canvas.drawLine(x, 5 * density, x, canvas.getHeight() - (5 * density), paint_track);
        paint.setStrokeWidth(4 * density);
        canvas.drawLine(x, visualPosition, x, visualPosition + handleBarHeight, paint);
    }

    private void createThePath() {
        path.rewind();
        float x = 0 - (actualWidth / 2) - (3 * density);
//        float x = recyclerView.getWidth() + (actualWidth / 2) + (155 * density);
        path.moveTo(x, 0);
        path.lineTo(x - (40 * density), 0);
        path.arcTo(x - 90 * density, 0, x - 40 * density, 60 * density, -90, -180, false);
        path.lineTo(x, 60 * density);
        path.close();
    }

    private void drawBubble(Canvas canvas, String letter) {
        paint.setStyle(Paint.Style.FILL);
        float x = canvas.getWidth() - (actualWidth / 2) - (5 * density);
        float y = visualPosition + (handleBarHeight / 2);
//        canvas.drawCircle(x - (45 * density), visualPosition + (handleBarHeight / 2), 35 * density, paint);
        canvas.save();
        canvas.translate(canvas.getWidth(), y - (30 * density));
        canvas.drawPath(path, paint);
        paint.setColor(Color.WHITE);
        canvas.drawText(letter, -90 * density, 39 * density, paint);
//        canvas.drawText(letter, x - (45 * density) - (6 * density), visualPosition + (handleBarHeight / 2) + (10 * density), paint);
        canvas.restore();

        paint.setColor(Color.parseColor("#FF3F51B5"));
    }

    private void processTouch(float y) {
        currentPosition += y;
        currentPosition = currentPosition < 0 ? 0 : currentPosition;
        currentPosition = currentPosition > height ? height : currentPosition;
        currentItem = (int) (itemCount * (currentPosition / height));
        recyclerView.scrollToPosition(currentItem);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if (e.getActionMasked() == MotionEvent.ACTION_DOWN && adapter != null) {
            inTouch = false;
            if (rv.getWidth() - e.getX() <= touchAbsorbWidth && isTouchingHandle(e) && isVisible) {
                inTouch = true;
//                Log.i(TAG, "onInterceptTouchEvent: " + e.getY());
                gestureDetector.onTouchEvent(e);
                rv.invalidate();
                rv.getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            }
        } else {
            if (inTouch) gestureDetector.onTouchEvent(e);
        }
        return inTouch;
    }


    private boolean isTouchingHandle(MotionEvent event) {
//        float barStart = height * (currentItem / (float) itemCount);
        float barStart = currentPosition;
        return event.getY() >= barStart && event.getY() <= barStart + handleBarHeight;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        if (e.getActionMasked() == MotionEvent.ACTION_UP || e.getActionMasked() == MotionEvent.ACTION_CANCEL) {
            inTouch = false;
            rv.invalidate();
        }
    }

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            Log.i(TAG, "onScroll: " + distanceY);
            processTouch(-distanceY);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    };

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public void setListener(FastScrollListener listener) {
        this.listener = listener;
    }

    public interface FastScrollListener {
        String getItemFirstLetter(int itemPos);
    }

    private void animate() {
        if (animator != null) {
            animator.cancel();
            animator.removeAllUpdateListeners();
        }
        float end = isVisible ? 0 : 1;
        animator = ValueAnimator.ofFloat(moveFactor, end);
        animator.setDuration(400).setInterpolator(new OvershootInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                moveFactor = (float) animation.getAnimatedValue();
                recyclerView.invalidate();
            }
        });
        animator.start();
    }

}
