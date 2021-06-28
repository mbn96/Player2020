package mbn.libs.fragmanager;//package mbn.packfragmentmanager.fragmanager;
//
//
//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.animation.ValueAnimator;
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.Parcel;
//import android.os.Parcelable;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.view.GestureDetectorCompat;
//import android.support.v4.view.animation.FastOutSlowInInterpolator;
//import android.support.v7.app.AppCompatActivity;
//import android.util.AttributeSet;
//import android.view.GestureDetector;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.VelocityTracker;
//import android.view.View;
//import android.view.ViewAnimationUtils;
//import android.view.ViewConfiguration;
//import android.view.ViewGroup;
//import android.view.animation.DecelerateInterpolator;
//import android.view.animation.OvershootInterpolator;
//import android.widget.FrameLayout;
//
//import java.util.ArrayList;
//
//public class CustomFragmentSwipeBackAnimator_backup2 extends FrameLayout {
//
//    public static CustomFragmentSwipeBackAnimator_backup2 INSTANCE;
//
//    public static CustomFragmentSwipeBackAnimator_backup2 getINSTANCE() {
//        return INSTANCE;
//    }
//
//    public static void setINSTANCE(CustomFragmentSwipeBackAnimator_backup2 INSTANCE) {
//        CustomFragmentSwipeBackAnimator_backup2.INSTANCE = INSTANCE;
//    }
//
//    public static final int ANIM_DEFAULT = 0;
//    public static final int ANIM_CUSTOM_VERTICAL = 6;
//    public static final int ANIM_CUSTOM_HORIZONTAL = 7;
//    public static final int ANIM_BOTTOM_NORMAL = 1;
//    public static final int ANIM_BOTTOM_FANCY = 2;
//    public static final int ANIM_CIRCLE_REVEAL = 3;
//    public static final int ANIM_DIALOG_RTL = 4;
//    public static final int ANIM_DIALOG_TTB = 5;
//
//    public static final String STATE_KEY = "manager_mbn_state_key";
//
////
////    private BaseFragment pendingFragment;
////    private Runnable pendingRunnable = new Runnable() {
////        @Override
////        public void run() {
////            if (pendingFragment != null) addFragment(pendingFragment);
////        }
////    };
//
//
//    private Runnable checkPendingTaskRunnable = new Runnable() {
//        @Override
//        public void run() {
//            checkForTask();
//        }
//    };
//
//    private ArrayList<BaseFragment> fragments = new ArrayList<>();
//
//    public FragmentManager getFragmentManager() {
//        return fragmentManager;
//    }
//
//    public LayoutInflater getInflater() {
//        return LayoutInflater.from(getContext());
//    }
//
//    public AppCompatActivity getActivity() {
//        return activity;
//    }
//
//    private FragmentManager fragmentManager;
//    private AppCompatActivity activity;
//    private CustomAppBar appBar;
//    private FrameLayout fragmentsContainer;
//    private OptionMenuContainer optionMenuContainer;
//    private int containerId;
//    private Animator animator;
//    private float currentFraction;
//    private float currentVelocity;
//    private int isFling = 0;
//    private int fingerSlop;
//    private ViewConfiguration viewConfiguration;
//    private int moveTouchEventCount = 0;
//    private float density = getContext().getResources().getDisplayMetrics().density;
//    private int statusBarSize = (int) (24 * density);
//    private int startPadding = 50;
//    private boolean ignore = false;
//    private boolean working = false;
//    private boolean activityStarted = false;
//    private boolean addInProgress = false;
//    private boolean reversAdd = false;
//    private float multiplier = 1;
//
//    private String TAG = "mbn_test";
//
//    private VelocityTracker velocityTracker;
//
//    //    private int width;
//    private GestureDetectorCompat gestureDetectorCompat;
//
//    public CustomFragmentSwipeBackAnimator_backup2(@NonNull Context context) {
//        super(context);
//    }
//
//    public CustomFragmentSwipeBackAnimator_backup2(@NonNull Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public CustomFragmentSwipeBackAnimator_backup2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    public CustomFragmentSwipeBackAnimator_backup2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }
//
//    public int getStatusBarSize() {
//        return statusBarSize;
//    }
//
//    public void startAnimIfNeeded(BaseFragment fragment) {
//        if (fragment.equals(fragments.get(fragments.size() - 1)) && !(fragment instanceof BaseFragment.ReverseFragment)) {
//            if (getActiveAnim() == ANIM_CIRCLE_REVEAL) {
//                circleRevealAnimation(true);
//                return;
//            }
//            animation(1, 0, fragment);
//        } else addInProgress = false;
//    }
//
//    public void onMenuItemClicked(int itemId) {
//        fragments.get(fragments.size() - 1).onOptionMenuItemClicked(itemId);
//    }
//
//    private void addFragmentContainer() {
//        fragmentsContainer = new FrameLayout(getContext());
//        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        addView(fragmentsContainer, layoutParams);
//    }
//
//    private void addOptionMenuContainer() {
//        optionMenuContainer = new OptionMenuContainer(getContext(), this);
//        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        addView(optionMenuContainer, layoutParams);
//    }
//
//    private void addAppBar() {
//        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (110 * density), Gravity.TOP);
//        appBar = new CustomAppBar(getContext());
//        appBar.setPadding(0, (int) (24 * density), 0, 0);
//        appBar.setBackgroundColor(Color.parseColor("#344558"));
//        appBar.setFragments(fragments);
////        appBar.setVisibility(GONE);
//        addView(appBar, layoutParams);
//    }
//
//    public CustomAppBar getAppBar() {
//        return appBar;
//    }
//
//    public void start(FragmentManager fragmentManager, BaseFragment first, AppCompatActivity activityCompat) {
//        activity = activityCompat;
//        this.fragmentManager = fragmentManager;
////        setVisibility(INVISIBLE);
////        setDrawingCacheEnabled(true);
////        density = getContext().getResources().getDisplayMetrics().density;
//        addFragmentContainer();
//        addAppBar();
//        addOptionMenuContainer();
////        this.appBar = appBar;
//        containerId = getId();
//        viewConfiguration = ViewConfiguration.get(getContext());
//        fingerSlop = viewConfiguration.getScaledTouchSlop();
//
//        first.setFirstTime(false);
//        first.start(this);
//        fragments.add(first);
////        width = first.getView().getWidth();
//        gestureDetectorCompat = new GestureDetectorCompat(getContext(), gestureListener);
////        manageFragsStates();
//    }
//
//    public void startReverseAdd(BaseFragment.ReverseFragment fragment) {
//        if (addInProgress) return;
//        if (animator == null || !animator.isRunning()) {
//            addInProgress = true;
//            fragments.add(fragment);
//            reversAdd = true;
//            fragment.start(this);
//            manageFragsStates();
//        }
//    }
//
//    public void showOptionMenu(int menuId) {
//        optionMenuContainer.show(menuId);
//    }
//
//    private Bitmap getDrawingBitmap() {
//        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        int save = canvas.save();
//        draw(canvas);
//        canvas.restoreToCount(save);
//        return bitmap;
//    }
//
//    public void addFragment(BaseFragment fragment) {
//        optionMenuContainer.dismiss();
//        if (addInProgress) return;
//        if (animator == null || !animator.isRunning()) {
//            addInProgress = true;
//            fragments.add(fragment);
//            if (fragment instanceof BaseMbnDialog) {
////                destroyDrawingCache();
//                ((BaseMbnDialog) fragment).setBackgroundCache(getContext(), getDrawingBitmap());
//            }
//            fragment.start(this);
////            fragmentManager.beginTransaction().hide(fragment).commitNow();
//
//
////            if (getActiveAnim() == ANIM_CIRCLE_REVEAL) {
////                circleRevealAnimation(true);
////                manageFragsStates();
////                return;
////            }
////            animation(1, 0);
//            manageFragsStates();
//
//        } else if (isItOkToAdd()) {
//            pendingTasks.add(new AddTask(fragment));
//        }
//    }
//
//
//    public void removeFragmentView(View child) {
//        fragmentsContainer.removeView(child);
//    }
//
//    public void addFragmentView(View child, BaseFragment fragment) {
//        if (fragment instanceof BaseMbnDialog) {
//            addView(child);
//        } else {
//            fragmentsContainer.addView(child);
//        }
//    }
//
//    public boolean popFragment() {
//        if (optionMenuContainer.dismiss()) return true;
//        if (!fragments.get(fragments.size() - 1).canPopThisFragment()) return true;
//
//        if (fragments.size() < 2) {
//            return false;
//        }
//        if (animator == null || !animator.isRunning()) {
//            if (getActiveAnim() == ANIM_CIRCLE_REVEAL) {
//                circleRevealAnimation(false);
//                return true;
//            }
//            animation(currentFraction, 1, fragments.get(fragments.size() - 1));
//        } else {
//            pendingTasks.add(new PopTask());
//        }
//        return true;
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (ev.getActionMasked() == MotionEvent.ACTION_UP || ev.getActionMasked() == MotionEvent.ACTION_CANCEL) {
//            try {
//                velocityTracker.recycle();
//            } catch (Exception ignored) {
//            }
//            ignore = false;
//            working = false;
//            if (reversAdd) {
//                addInProgress = false;
//                reversAdd = false;
//                animation(1, 0, fragments.get(fragments.size() - 1));
//                return false;
//            }
//            reversAdd = false;
//        }
//
//        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
//            moveTouchEventCount = 0;
//            ignore = false;
//            working = false;
//            reversAdd = false;
//            multiplier = 1;
//            try {
//                velocityTracker.recycle();
//            } catch (Exception ignored) {
//            }
//            velocityTracker = VelocityTracker.obtain();
//            velocityTracker.addMovement(ev);
//            gestureDetectorCompat.onTouchEvent(ev);
//        }
//
//        if (reversAdd) {
//            working = true;
//            currentFraction = 1;
//            multiplier = -1;
//            return true;
//        }
////        moveTouchEventCount++;
//
//        if (fragments.get(fragments.size() - 1).canInterceptTouches() && (ev.getActionMasked() == MotionEvent.ACTION_MOVE)) {
//            boolean gesRes = false;
//            try {
//                gesRes = gestureDetectorCompat.onTouchEvent(ev);
//            } catch (Exception ignored) {
//            }
//            return gesRes;
//        } else {
//            return super.onInterceptTouchEvent(ev);
//        }
//    }
//
//
//    private void findCurrentVelocity() {
//
//        velocityTracker.computeCurrentVelocity(1000);
//
//        switch (getActiveAnim()) {
//            case ANIM_DIALOG_TTB:
//            case ANIM_CIRCLE_REVEAL:
//            case ANIM_CUSTOM_VERTICAL:
//            case ANIM_BOTTOM_FANCY:
//            case ANIM_BOTTOM_NORMAL:
//                currentVelocity = velocityTracker.getYVelocity();
//                break;
//            case ANIM_DIALOG_RTL:
//            case ANIM_DEFAULT:
//            case ANIM_CUSTOM_HORIZONTAL:
//                currentVelocity = velocityTracker.getXVelocity();
//                break;
//        }
//
//
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        velocityTracker.addMovement(event);
//        try {
//            gestureDetectorCompat.onTouchEvent(event);
//        } catch (Exception ignored) {
//        }
//        if ((event.getActionMasked() == MotionEvent.ACTION_UP || event.getActionMasked() == MotionEvent.ACTION_CANCEL) && fragments.size() > 1) {
//            findCurrentVelocity();
//            afterTouch();
////            if (currentVelocity >= viewConfiguration.getScaledMinimumFlingVelocity()) {
////                if (getActiveAnim() == ANIM_CIRCLE_REVEAL) circleRevealAnimation(false);
////                else animation(currentFraction, 1);
////
////            } else if (-currentVelocity >= viewConfiguration.getScaledMinimumFlingVelocity()) {
//////                if (getActiveAnim() == ANIM_CIRCLE_REVEAL) circleRevealAnimation(true);
////                animation(currentFraction, 0);
////
////            } else if (currentFraction > 0.5f) {
////                if (getActiveAnim() == ANIM_CIRCLE_REVEAL) circleRevealAnimation(false);
////                else animation(currentFraction, 1);
////            } else {
//////                if (getActiveAnim() == ANIM_CIRCLE_REVEAL) circleRevealAnimation(true);
////                animation(currentFraction, 0);
////            }
//            try {
//                velocityTracker.recycle();
//            } catch (Exception ignored) {
//            }
//            ignore = false;
//            working = false;
//            reversAdd = false;
//            return true;
//        }
//
////        gestureDetectorCompat.onTouchEvent(event);
////        return true;
//
////        return super.onTouchEvent(event) && gestureDetectorCompat.onTouchEvent(event);
//
//        return true;
////        return super.onTouchEvent(event) || gestureDetectorCompat.onTouchEvent(event);
//    }
//
//    private void afterTouch() {
//        if (currentVelocity >= viewConfiguration.getScaledMinimumFlingVelocity()) {
//            if (getActiveAnim() == ANIM_CIRCLE_REVEAL) circleRevealAnimation(false);
//            else animation(currentFraction, 1, fragments.get(fragments.size() - 1));
//        } else if (-currentVelocity >= viewConfiguration.getScaledMinimumFlingVelocity()) {
//            animation(currentFraction, 0, fragments.get(fragments.size() - 1));
//
//        } else if (currentFraction > 0.5f) {
//            if (getActiveAnim() == ANIM_CIRCLE_REVEAL) circleRevealAnimation(false);
//            else animation(currentFraction, 1, fragments.get(fragments.size() - 1));
//        } else {
//            animation(currentFraction, 0, fragments.get(fragments.size() - 1));
//        }
//    }
//
//    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
//
//        private void sendToFractionMethod(float fraction) {
//            if (fraction <= 1 && fraction >= 0) {
//                fractionGetter(fraction);
//                return;
//            }
//            fraction = fraction > 1 ? 1 : fraction;
//            fraction = fraction < 0 ? 0 : fraction;
//            fractionGetter(fraction);
//        }
//
//        @Override
//        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//
//            int y1, y2;
//            int x1, x2;
//
//            x1 = (int) (e1.getX() + 0.5f);
//            y1 = (int) (e1.getY() + 0.5f);
//
//            x2 = (int) (e2.getX() + 0.5f);
//            y2 = (int) (e2.getY() + 0.5f);
//
//            if (ignore) {
////                Log.i(TAG, "onScroll: " + "ignore");
//                return false;
//            }
//
//
//            switch (getActiveAnim()) {
//                case ANIM_DIALOG_TTB:
//                case ANIM_CIRCLE_REVEAL:
//                case ANIM_BOTTOM_FANCY:
//                case ANIM_CUSTOM_VERTICAL:
//                case ANIM_BOTTOM_NORMAL:
//                    if (!working && Math.abs(x2 - x1) > fingerSlop * 3) {
////                        Log.i(TAG, "onScroll: " + "not working");
//                        ignore = true;
//                        return false;
//                    }
//                    if (working) {
//                        if (reversAdd) {
//                            sendToFractionMethod(1 - (multiplier * (y2 - y1) / getHeight()));
//                        } else
//                            sendToFractionMethod(multiplier * (y2 - y1) / getHeight());
//                        return true;
//                    }
//                    if ((animator == null || !animator.isRunning()) && y2 - y1 > fingerSlop && fragments.size() > 1) {
//                        working = true;
////                        sendToFractionMethod((y2 - y1) / getHeight());
//                        return true;
//                    }
//                    break;
//                case ANIM_DIALOG_RTL:
//                case ANIM_CUSTOM_HORIZONTAL:
//                case ANIM_DEFAULT:
//                    if (!working && Math.abs(y2 - y1) > fingerSlop * 3) {
//                        ignore = true;
//                        return false;
//                    }
//                    if (working) {
//                        if (reversAdd)
//                            sendToFractionMethod(1 - (multiplier * (x2 - x1) / getWidth()));
//                        else
//                            sendToFractionMethod(multiplier * (x2 - x1) / getWidth());
//                        return true;
//                    }
//                    if ((animator == null || !animator.isRunning()) && x2 - x1 > fingerSlop && fragments.size() > 1) {
//                        working = true;
////                        sendToFractionMethod((x2 - x1) / getWidth());
//                        return true;
//                    }
//                    break;
//            }
//
//
//            return false;
//        }
//
////        @Override
////        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
////
////            Log.i("touch", String.valueOf(velocityY));
////
////
////            if (velocityX > 0) {
////                isFling = 1;
////            } else {
////                isFling = 2;
////            }
////
////            return super.onFling(e1, e2, velocityX, velocityY);
////        }
//    };
//
////
////    @Override
////    public boolean onTouch(View v, MotionEvent event) {
////
////
////        if ((event.getActionMasked() == MotionEvent.ACTION_UP || event.getActionMasked() == MotionEvent.ACTION_CANCEL) && fragments.size() > 1) {
////            if (isFling == 1) {
////                animation(currentFraction, 1);
////            } else if (isFling == 2) {
////                animation(currentFraction, 0);
////            } else if (currentFraction > 0.3f) {
////                animation(currentFraction, 1);
////            } else {
////                animation(currentFraction, 0);
////            }
////
////            ignore = false;
////            working = false;
////            return true;
////        }
////
////        gestureDetectorCompat.onTouchEvent(event);
////        return true;
////    }
//
//    private void circleRevealAnimation(boolean star_end) {
//        currentFraction = 0;
//        if (animator != null) {
//            animator.cancel();
//            animator.removeAllListeners();
//            try {
//                ((ValueAnimator) animator).removeAllUpdateListeners();
//            } catch (Exception ignored) {
//            }
//            animator = null;
//        }
//        View top = fragments.get(fragments.size() - 1).getView();
//
//        top.setElevation(50);
//        fragments.get(fragments.size() - 2).getView().setElevation(0);
//
//        int x = fragments.get(fragments.size() - 1).getCirclePoint().x;
//        int y = fragments.get(fragments.size() - 1).getCirclePoint().y;
//
//        top.setScaleY(1);
//        top.setScaleX(1);
//
//        if (star_end) {
//            animator = ViewAnimationUtils.createCircularReveal(top, x, y, 0, (float) Math.hypot(getWidth(), getHeight()));
////            animator = ViewAnimationUtils.createCircularReveal(top, (int) (getWidth() - (startPadding * density)), (int) (getHeight() - (startPadding * density)), 0, (float) Math.hypot(getWidth(), getHeight()));
//            top.setVisibility(VISIBLE);
//            appBar.manageVisibilities();
//            animator.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//                    fragments.get(fragments.size() - 1).startAnimIsStarting();
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    addInProgress = false;
//                    post(checkPendingTaskRunnable);
////                    checkForTask();
//                }
//            });
//        } else {
//            animator = ViewAnimationUtils.createCircularReveal(top, x, y, (float) Math.hypot(getWidth(), getHeight()), 0);
////            animator = ViewAnimationUtils.createCircularReveal(top, (int) (getWidth() - (startPadding * density)), (int) (getHeight() - (startPadding * density)), (float) Math.hypot(getWidth(), getHeight()), 0);
//            animator.addListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//                    fragments.get(fragments.size() - 1).endAnimIsStarting();
//                    fragments.get(fragments.size() - 2).getView().setVisibility(VISIBLE);
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    popInternal();
//                    currentFraction = 0;
//                    post(checkPendingTaskRunnable);
////                    checkForTask();
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animation) {
//
//                }
//            });
//        }
//        animator.setDuration(450);
//        animator.setInterpolator(new DecelerateInterpolator());
//        animator.start();
//    }
//
//    private void animation(float start, float end, BaseFragment fragment) {
//        if (animator != null) {
//            animator.cancel();
//            animator.removeAllListeners();
//            if (animator instanceof ValueAnimator) {
//                ((ValueAnimator) animator).removeAllUpdateListeners();
//            }
//            animator = null;
//        }
//
//        isFling = 0;
//        animator = ValueAnimator.ofFloat(start, end);
//        ((ValueAnimator) animator).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//
//                fractionGetter((Float) animation.getAnimatedValue());
//
//            }
//        });
//        if (getActiveAnim() != ANIM_CIRCLE_REVEAL) {
//            if (end == 1) {
//                animator.setDuration((long) (Math.abs(end - start) * 500f));
//
//                if ((Math.abs(end - start) == 1))
//                    animator.setInterpolator(new FastOutSlowInInterpolator());
//                else animator.setInterpolator(new DecelerateInterpolator());
//
//                animator.addListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//                        fragments.get(fragments.size() - 1).endAnimIsStarting();
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        popInternal();
//                        currentFraction = 0;
//                        post(checkPendingTaskRunnable);
////                        checkForTask();
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animation) {
//
//                    }
//                });
//            } else if (end == 0) {
//                animator.setDuration((long) (Math.abs(end - start) * 600f));
//                animator.setInterpolator(new FastOutSlowInInterpolator());
//                animator.addListener(new AnimatorListenerAdapter() {
//
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//                        fragments.get(fragments.size() - 1).startAnimIsStarting();
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        appBar.manageVisibilities();
//                        currentFraction = 0;
//                        addInProgress = false;
//                        post(checkPendingTaskRunnable);
////                        checkForTask();
//                    }
//                });
//            }
//        }
//        if (fragments.get(fragments.size() - 1) instanceof BaseMbnDialog) {
//            animator.setDuration((long) (Math.abs(end - start) * 400f));
//            animator.setInterpolator(new OvershootInterpolator());
//        }
//        if (fragments.get(fragments.size() - 1).equals(fragment)) {
//            animator.start();
//        }
//    }
//
//    private int getActiveAnim() {
//        return fragments.get(fragments.size() - 1).getAnimationMode();
//    }
//
//    private void popInternal() {
////        fragmentManager.beginTransaction().remove(fragments.get(fragments.size() - 1)).commitNow();
//        BaseFragment f = fragments.get(fragments.size() - 1);
//        appBar.removeView(f.getToolbar());
//        fragments.remove(f);
//        manageFragsStates();
//        f.destroy();
//        appBar.manageVisibilities();
//    }
//
//    private void fractionGetter(float fraction) {
////        if (fraction <= 1 && fraction >= 0) {
//        if (fraction <= 1) {
//            View top = fragments.get(fragments.size() - 1).getView();
//            View under = fragments.get(fragments.size() - 2).getView();
//
//            fragments.get(fragments.size() - 1).onFractionChange(fraction);
////            fragments.get(fragments.size() - 2).onFractionChange(fraction);
//
//            int height = top.getHeight();
//            int width = top.getWidth();
//
//            appBar.fractionGetter(fraction, getActiveAnim());
//
//            switch (getActiveAnim()) {
//                case ANIM_BOTTOM_FANCY:
//                    top.setElevation(0);
//                    under.setElevation(50);
//
////                    top.setScaleX(1 - fraction / 3);
////                    top.setScaleY(1 - fraction / 3);
//
//                    under.setTranslationY(height * (-1 + fraction));
//                    top.setTranslationY((float) (height * (0.3 * fraction)));
//                    break;
//                case ANIM_BOTTOM_NORMAL:
//                    top.setElevation(50);
//                    under.setElevation(0);
//
//                    top.setTranslationY(height * fraction);
//                    under.setTranslationY((float) (height * (-0.3 + (0.3 * fraction))));
//                    break;
//                case ANIM_DEFAULT:
//                    top.setElevation(50);
//                    under.setElevation(0);
//
//
//                    top.setTranslationX(width * fraction);
//                    under.setTranslationX((float) (width * (-0.3 + (0.3 * fraction))));
//                    break;
//                case ANIM_CIRCLE_REVEAL:
//                case ANIM_CUSTOM_VERTICAL:
//                case ANIM_CUSTOM_HORIZONTAL:
//                case ANIM_DIALOG_RTL:
//                case ANIM_DIALOG_TTB:
//                    top.setElevation(50);
//                    under.setElevation(0);
//                    break;
//            }
//
//            if (1 - fraction < 0.001f) {
//                top.setVisibility(INVISIBLE);
//            } else {
//                top.setVisibility(VISIBLE);
//            }
//            under.setVisibility(VISIBLE);
//
//            if (fraction == 0 && !(fragments.get(fragments.size() - 1) instanceof BaseMbnDialog)) {
//                top.setElevation(50);
//                under.setElevation(0);
//                under.setVisibility(INVISIBLE);
//            }
//
//            currentFraction = fraction;
//        }
//    }
//
//
//    public void onPause() {
//        for (int i = 0; i < fragments.size(); i++) {
//            fragments.get(i).setState(0);
//        }
//    }
//
//    public void onDestroy() {
//        for (int i = 0; i < fragments.size(); i++) {
//            fragments.get(i).destroy();
//        }
//    }
//
//    public void manageFragsStates() {
//        for (int i = 0; i < fragments.size() - 1; i++) {
//            fragments.get(i).setState(0);
//        }
//        fragments.get(fragments.size() - 1).setState(1);
//
////        if (activityStarted) {
////            for (int i = 0; i < fragments.size() - 1; i++) {
////                fragments.get(i).getView().setOnTouchListener(null);
////            }
////            if (fragments.size() > 0) {
////                fragments.get(fragments.size() - 1).getView().setOnTouchListener(this);
////            }
////        }
//    }
//
//    public void createSavedInstanceState(Bundle bundle) {
////        Log.i("mbbbbbbbbbbbbb", String.valueOf(getChildCount()));
////        Log.i("mbbbbbbbbbbbbb", getChildAt(0).getClass().getSimpleName());
//        ArrayList<FragDataToResume> frags = new ArrayList<>();
//
//        for (BaseFragment frg : fragments) {
//            FragDataToResume message = new FragDataToResume(frg.getStateAndArguments(), frg.getClass().getName());
//            frags.add(message);
//        }
//        bundle.putParcelableArrayList(STATE_KEY, frags);
//    }
//
//
//    public void resumeFromSaveState(Bundle bundle, FragmentManager manager, AppCompatActivity activity) {
//        try {
////            for (Fragment fr : manager.getFragments()) {
////                try {
////                    manager.beginTransaction().remove(fr).commitNow();
////                } catch (Exception ignored) {
////                }
////            }
//            ResultReceivingManager.reset();
//            ClassLoader classLoader = getContext().getClassLoader();
//            fragments.clear();
//            ArrayList<FragDataToResume> messages = bundle.getParcelableArrayList(STATE_KEY);
//            BaseFragment first = null;
//            if (messages != null) {
//                first = (BaseFragment) classLoader.loadClass(getData(messages.get(0)).aClass).newInstance();
//                first.setArguments(messages.get(0).args);
//                start(manager, first, activity);
//                for (int i = 1; i < messages.size(); i++) {
//                    FragDataToResume dataToResume = messages.get(i);
////                    FragDataToResume dataToResume = getData(mes.obj);
//                    BaseFragment fragment = (BaseFragment) classLoader.loadClass(dataToResume.aClass).newInstance();
//                    fragment.setArguments(dataToResume.args);
//                    fragments.add(fragment);
//                    fragment.setFirstTime(false);
//                    fragment.start(this);
//                    fragment.getView().setVisibility(INVISIBLE);
//                }
//                manageFragsStates();
//                appBar.manageVisibilities();
//                if (fragments.size() > 1)
//                    fractionGetter(0);
//            }
//        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
////    public void reset() {
////        FragmentTransaction transaction = fragmentManager.beginTransaction();
////        for (BaseFragment f : fragments) {
////            transaction.remove(f);
////        }
////        transaction.commitNow();
////
////    }
//
//    private FragDataToResume getData(Object o) {
//        return (FragDataToResume) o;
//    }
//
//    public void setActivityStarted(boolean activityStarted) {
//        this.activityStarted = activityStarted;
////        if (activityStarted) {
////            manageFragsStates();
////        }
//    }
//
//    static class FragDataToResume implements Parcelable {
//
//        private Bundle args;
//        private String aClass;
//
//        public FragDataToResume(Bundle args, String aClass) {
//            this.args = args;
//            this.aClass = aClass;
//        }
//
//
//        protected FragDataToResume(Parcel in) {
//            args = in.readBundle();
//            aClass = in.readString();
//        }
//
//        public static final Creator<FragDataToResume> CREATOR = new Creator<FragDataToResume>() {
//            @Override
//            public FragDataToResume createFromParcel(Parcel in) {
//                return new FragDataToResume(in);
//            }
//
//            @Override
//            public FragDataToResume[] newArray(int size) {
//                return new FragDataToResume[size];
//            }
//        };
//
//        @Override
//        public int describeContents() {
//            return 0;
//        }
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//            dest.writeBundle(args);
//            dest.writeString(aClass);
//        }
//    }
//
//
////    --------------------------------------------//
////    -------------------------------------------//
////    ----------  Task pending part  -----------//
//
//    private static final int TASK_POP = 0;
//    private static final int TASK_ADD = 1;
//
//    private ArrayList<PendingTask> pendingTasks = new ArrayList<>();
//
//    private abstract class PendingTask {
//        abstract int getType();
//    }
//
//    private class PopTask extends PendingTask {
//        @Override
//        int getType() {
//            return TASK_POP;
//        }
//    }
//
//    private class AddTask extends PendingTask {
//        @Override
//        int getType() {
//            return TASK_ADD;
//        }
//
//        AddTask(BaseFragment addFrag) {
//            this.addFrag = addFrag;
//        }
//
//        private BaseFragment addFrag;
//    }
//
//    private boolean isItOkToAdd() {
//        for (PendingTask t : pendingTasks) {
//            if (t.getType() == TASK_ADD) return false;
//        }
//        return true;
//    }
//
//    private void checkForTask() {
//        if (pendingTasks.size() > 0) {
//            PendingTask task = pendingTasks.remove(0);
//            switch (task.getType()) {
//                case TASK_POP:
//                    popFragment();
//                    break;
//                case TASK_ADD:
//                    addFragment(((AddTask) task).addFrag);
//                    break;
//            }
//        }
//    }
//}
