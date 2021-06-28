package com.br.mreza.musicplayer.newmodel.adapters.songs;


import android.content.Context;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.OvershootInterpolator;

public abstract class SelectableAdapterWithSwipe<T extends NewModelSongsBaseAdapter.NewModelHolder> extends NewModelSongsBaseAdapter<T> implements RecyclerView.OnItemTouchListener {

    private Context mContext;
    private RecyclerView recyclerView;
    private GestureDetectorCompat gestureDetector;
    private boolean inProgress = false;
    private boolean scrolling = false;
    private float density;
    private int touchSlop;
    private ViewHolder currentViewHolder;
    private int state;
    private View swipeView;
    private float startX;
    private int flags;


    static final int STATE_IDLE = 0;
    static final int STATE_SELECTED = 1;
    static final int STATE_READY_FOR_DEL = -1;

    static final int SELECT_FLAG = 1;
    static final int DELETE_FLAG = SELECT_FLAG << 1;
    private static final int BOTH_FLAG = SELECT_FLAG | DELETE_FLAG;


    SelectableAdapterWithSwipe(Context mContext, RecyclerView recyclerView) {
        this.mContext = mContext;
        this.recyclerView = recyclerView;
        recyclerView.addOnItemTouchListener(this);
        density = mContext.getResources().getDisplayMetrics().density;
        touchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        gestureDetector = new GestureDetectorCompat(getContext(), simpleOnGestureListener);
//        recyclerView.addItemDecoration(new SpringOnScrollEnding(recyclerView));
    }


    public float getDensity() {
        return density;
    }

    public Context getContext() {
        return mContext;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public abstract int getState(ViewHolder viewHolder);

    public abstract View setCurrentView(ViewHolder viewHolder);

    public abstract void addToSelectedList(ViewHolder viewHolder);

    public abstract void removeFromSelectedList(ViewHolder viewHolder);

    private void finish() {
        inProgress = false;
        scrolling = false;
        startX = 0;

        swipeView.setElevation(0);

        switch (state) {
            case STATE_IDLE:
                if (swipeView.getTranslationX() > 30 * density) {
                    addToSelectedList(currentViewHolder);
                    swipeView.setElevation(5 * density);
                    swipeView.animate().translationX(85 * density).setDuration(500).setInterpolator(new OvershootInterpolator());
                    currentViewHolder = null;
                    swipeView = null;
                } else if (swipeView.getTranslationX() < -30 * density) {
                    swipeView.setElevation(5 * density);
                    swipeView.animate().translationX(-85 * density).setDuration(500).setInterpolator(new OvershootInterpolator());
                    currentViewHolder = null;
                    swipeView = null;
                } else {
                    swipeView.animate().translationX(0).setDuration(500).setInterpolator(new OvershootInterpolator());
                    currentViewHolder = null;
                    swipeView = null;
                }
                break;


            case STATE_SELECTED:
                if (swipeView.getTranslationX() < 30 * density) {
                    removeFromSelectedList(currentViewHolder);
                    swipeView.animate().translationX(0).setDuration(500).setInterpolator(new OvershootInterpolator());
                    currentViewHolder = null;
                    swipeView = null;


                } else {
                    swipeView.animate().translationX(0).setDuration(500).setInterpolator(new OvershootInterpolator());
                    currentViewHolder = null;
                    swipeView = null;
                }
                break;


            case STATE_READY_FOR_DEL:
                if (swipeView.getTranslationX() > -30 * density) {
                    swipeView.animate().translationX(0).setDuration(500).setInterpolator(new OvershootInterpolator());
                    currentViewHolder = null;
                    swipeView = null;


                } else {
                    swipeView.animate().translationX(0).setDuration(500).setInterpolator(new OvershootInterpolator());
                    currentViewHolder = null;
                    swipeView = null;
                }
                break;

        }


    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

//        Log.e("inInter", "yeah");

//        if ((e.getActionMasked() == MotionEvent.ACTION_CANCEL || e.getActionMasked() == MotionEvent.ACTION_UP) && inProgress && swipeView != null && currentViewHolder != null) {
//            finish();
//        }
        return gestureDetector.onTouchEvent(e);
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        if (!scrolling) {
            gestureDetector.onTouchEvent(e);
        }

        if ((e.getActionMasked() == MotionEvent.ACTION_CANCEL || e.getActionMasked() == MotionEvent.ACTION_UP) && inProgress && swipeView != null && currentViewHolder != null) {
            finish();
        }

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }


    @SuppressWarnings("FieldCanBeLocal")
    private SimpleOnGestureListener simpleOnGestureListener = new SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        private void handle(MotionEvent e1, MotionEvent e2) {

            recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            inProgress = true;
            swipeView.setElevation(5 * density);
            swipeView.setTranslationX(e2.getX() - e1.getX());

        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            if (scrolling) {
                return false;
            }

            if (!inProgress && Math.abs(e2.getY() - e1.getY()) > touchSlop) {
                scrolling = true;
                return false;
            }

            if (Math.abs(e2.getX() - e1.getX()) > touchSlop && swipeView != null) {

                switch (state) {
                    case STATE_IDLE:
//                        swipeView.getParent().requestDisallowInterceptTouchEvent(true);

                        if (e2.getX() - e1.getX() > 0 && (flags & SELECT_FLAG) == SELECT_FLAG) {
                            handle(e1, e2);
                            return true;
                        }
                        if (e2.getX() - e1.getX() < 0 && (flags & DELETE_FLAG) == DELETE_FLAG) {
                            handle(e1, e2);
                            return true;
                        }

                        return false;
//                        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
//                        inProgress = true;
//                        swipeView.setElevation(5 * density);
//                        swipeView.setTranslationX(e2.getX() - e1.getX());
//                        return true;

                    case STATE_SELECTED:
                        if (e2.getX() - e1.getX() < 0) {
                            recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
                            inProgress = true;
                            swipeView.setElevation(5 * density);
                            swipeView.setTranslationX(startX + e2.getX() - e1.getX());
                            return true;
                        }
                        break;
                    case STATE_READY_FOR_DEL:
                        if (e2.getX() - e1.getX() > 0) {
                            recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
                            inProgress = true;
                            swipeView.setElevation(5 * density);
                            swipeView.setTranslationX(startX + e2.getX() - e1.getX());
                            return true;
                        }
                        break;


                }

            }


            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            try {
                View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                currentViewHolder = recyclerView.findContainingViewHolder(view);
                state = getState(currentViewHolder);
                swipeView = setCurrentView(currentViewHolder);
                startX = swipeView.getTranslationX();
                scrolling = false;
                inProgress = false;
            } catch (Exception exep) {
                currentViewHolder = null;
                swipeView = null;
            }
            return super.onDown(e);
        }
    };


}
