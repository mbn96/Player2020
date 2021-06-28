package mbn.packfragmentmanager.fragmanager;//package com.br.mreza.musicplayer.fragmanager;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.graphics.Point;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//
//import static com.br.mreza.musicplayer.fragmanager.CustomFragmentSwipeBackAnimator.ANIM_DEFAULT;
//
//
//public abstract class BaseFragment_backup extends Fragment {
//
//
//    private CustomFragmentSwipeBackAnimator fragmentSwipeBackManager;
//    private View toolbar;
//    private boolean isFirstTime = true;
//    private int onState = 0;
//
//    private final static String CIRCLE_CENTER = "POINT";
//    private final static String HAS_RESULT_ID = "has_res_id";
//    private final static String RESULT_ID = "resultId";
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        fragmentSwipeBackManager = ((BaseActivity) getActivity()).getSwipeManager();
//    }
//
//    public CustomFragmentSwipeBackAnimator getFragmentSwipeBackManager() {
//        return fragmentSwipeBackManager;
//    }
//
//
//    public void setState(int onState) {
//        if (onState != this.onState) {
//            this.onState = onState;
//            if (onState == 0) offScreen();
//            else onScreen();
//        }
////        if (onState == 1) getView().setVisibility(View.VISIBLE);
//    }
//
//    public boolean isFirstTime() {
//        return isFirstTime;
//    }
//
//    public void setFirstTime(boolean firstTime) {
//        isFirstTime = firstTime;
//    }
//
//    public boolean canHavePending() {
//        return false;
//    }
//
//    public boolean canInterceptTouches() {
//        return true;
//    }
//
//    public boolean canPopThisFragment() {
//        return true;
//    }
//
//    public boolean hasAppBar() {
//        return true;
//    }
//
//    public void onFractionChange(float fraction) {
//    }
//
//    public void endAnimIsStarting() {
//    }
//
//    public void startAnimIsStarting() {
//    }
//
//    public void setCirclePoint(Point circlePoint) {
//        if (getArguments() == null) setArguments(new Bundle());
//        getArguments().putParcelable(CIRCLE_CENTER, circlePoint);
//    }
//
//    public Point getCirclePoint() {
//        if (getArguments() != null) {
//            Point point = getArguments().getParcelable(CIRCLE_CENTER);
//            if (point != null) return point;
//        }
//        return new Point(150, 150);
//    }
//
//    public int getAnimationMode() {
//        return ANIM_DEFAULT;
//    }
//
//    public abstract void onScreen();
//
//    public abstract void offScreen();
//
//    public View getToolbar() {
//        return toolbar;
//    }
//
//    public void setToolbar(View toolbar) {
//        this.toolbar = toolbar;
//        getFragmentSwipeBackManager().getAppBar().addView(toolbar);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if (getFragmentSwipeBackManager() == null) {
//            try {
//                getFragmentManager().beginTransaction().remove(this).commit();
//            } catch (Exception ignored) {
//            }
//            return null;
//        }
//        if (hasAppBar()) {
//            setToolbar(makeToolBar(inflater));
//        } else {
//            setToolbar(new View(getContext()));
//        }
//        BaseFrameLayoutForFrags frameLayoutForFrags = new BaseFrameLayoutForFrags(inflater.getContext()) {
//            @Override
//            void layoutWithSize() {
//                checkForAnim();
//            }
//        };
//        if (!(this instanceof BaseMbnDialog))
//            frameLayoutForFrags.addView(onCreateViewForChild(inflater, container, savedInstanceState));
//        return frameLayoutForFrags;
////        return onCreateViewForChild(inflater, container, savedInstanceState);
//    }
//
//    public abstract View onCreateViewForChild(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);
//
//    public abstract View makeToolBar(LayoutInflater inflater);
//
//    public abstract Bundle makeState(Bundle state);
//
//    public abstract void checkForRestoreState();
//
//    public Bundle getStateAndArguments() {
//        if (getArguments() == null) setArguments(new Bundle());
//        return makeState(getArguments());
//    }
//
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        if (isFirstTime()) view.setVisibility(View.INVISIBLE);
//        view.setBackgroundColor(Color.WHITE);
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
////        getFragmentManager().beginTransaction().hide(this).commit();
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        checkForRestoreState();
////        if (isFirstTime()) {
////            setFirstTime(false);
////            getFragmentSwipeBackManager().startAnimIfNeeded(BaseFragment.this);
////
//////            getView().postDelayed(new Runnable() {
//////                @Override
//////                public void run() {
//////                    getFragmentSwipeBackManager().startAnimIfNeeded(BaseFragment.this);
//////                }
//////            }, 30);
////
////        }
//
////        getFragmentManager().beginTransaction().show(this).commit();
//
//    }
//
//    protected void cropBlurBack() {
//    }
//
//    private void checkForAnim() {
//        if (isFirstTime()) {
//            setFirstTime(false);
//            getFragmentSwipeBackManager().startAnimIfNeeded(BaseFragment_backup.this);
//
////            getView().postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    getFragmentSwipeBackManager().startAnimIfNeeded(BaseFragment.this);
////                }
////            }, 30);
//        }
//        if (this instanceof BaseMbnDialog) {
//            cropBlurBack();
//        }
//    }
//
////    @Override
////    public void onHiddenChanged(boolean hidden) {
////        super.onHiddenChanged(hidden);
////        if (!hidden) {
////            if (isFirstTime()) {
////                setFirstTime(false);
////                getFragmentSwipeBackManager().startAnimIfNeeded(BaseFragment.this);
////
//////            getView().postDelayed(new Runnable() {
//////                @Override
//////                public void run() {
//////                    getFragmentSwipeBackManager().startAnimIfNeeded(BaseFragment.this);
//////                }
//////            }, 30);
////
////            }
////        }
////    }
//
//
//    //    -------------   result receiving parts -------------------  //
//
//    private int getResultID() {
//        if (getArguments() == null) setArguments(new Bundle());
//        if (!getArguments().getBoolean(HAS_RESULT_ID, false)) generateResultID();
//        return getArguments().getInt(RESULT_ID);
//    }
//
//    private void generateResultID() {
//        int id = ResultReceivingManager.generateID();
//        getArguments().putInt(RESULT_ID, id);
//        getArguments().putBoolean(HAS_RESULT_ID, true);
//    }
//
//    /**
//     * Fragments who wishes to have result receiver (each fragment can have only one receiver at a time) should call this method
//     * on their onResume method.
//     */
//    public void registerResultReceiver(ResultReceivingManager.ResultReceiver resultReceiver) {
//        int id = getResultID();
//        if (ResultReceivingManager.hasThisReceiver(id)) {
//            if (ResultReceivingManager.getReceiver(id).equals(resultReceiver)) return;
//            ResultReceivingManager.addReceiver(id, resultReceiver);
//            return;
//        }
//        ResultReceivingManager.addReceiver(id, resultReceiver);
//    }
//
//}
