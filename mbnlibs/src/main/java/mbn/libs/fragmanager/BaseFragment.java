package mbn.libs.fragmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static mbn.libs.fragmanager.CustomFragmentSwipeBackAnimator.ANIM_BOTTOM_FANCY;
import static mbn.libs.fragmanager.CustomFragmentSwipeBackAnimator.ANIM_BOTTOM_NORMAL;
import static mbn.libs.fragmanager.CustomFragmentSwipeBackAnimator.ANIM_CUSTOM_VERTICAL;
import static mbn.libs.fragmanager.CustomFragmentSwipeBackAnimator.ANIM_DEFAULT;


public abstract class BaseFragment {

    private CustomFragmentSwipeBackAnimator fragmentSwipeBackManager;
    private FragmentManager fragmentManager;
    private View toolbar;
    private boolean isFirstTime = true;
    private int onState = 0;
    private Bundle arguments;
    private Context context;
    private View view;
    private View userView;
    private boolean isDestroyed = false;
    boolean canStartAnim = true;
    boolean isLayoutDone = false;

    private float appBarSize;

    private final static String CIRCLE_CENTER = "POINT";
    private final static String HAS_RESULT_ID = "has_res_id";
    private final static String RESULT_ID = "resultId";
    private final static String VIEW_SAVED_INSTANCE = "viewSaveInstance";

    protected String TAG = "BaseFragment_ " + getClass().getSimpleName() + " _test";

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public Bundle getArguments() {
        return arguments;
    }

    public void setArguments(Bundle arguments) {
        this.arguments = arguments;
    }

    public Context getContext() {
        return context;
    }

    protected void startActivity(Intent intent, Bundle option) {
        context.startActivity(intent, option);
    }

    protected void startActivity(Intent intent) {
        startActivity(intent, null);
    }

    protected BaseActivity getActivity() {
        return (BaseActivity) fragmentSwipeBackManager.getActivity();
    }

    public void start(CustomFragmentSwipeBackAnimator fragmentManager) {
        fragmentSwipeBackManager = fragmentManager;
        this.fragmentManager = fragmentManager.getFragmentManager();
        context = fragmentManager.getContext();
        getAppBar().fragmentAdded(this);
        if (view == null) createView(fragmentManager.getInflater(), fragmentManager);
        fragmentManager.addFragmentView(getView(), this);
        onStart();
    }

    public void destroy() {
        setState(0);
        onStop();
        getFragmentSwipeBackManager().getAppBar().fragmentRemoved(this);
        if (hasResultId()) ResultReceivingManager.removeReceiver(getResultID());
        fragmentSwipeBackManager.removeFragmentView(getView(), this);
        context = null;
        fragmentSwipeBackManager = null;
        fragmentManager = null;
        setArguments(null);
        isDestroyed = true;
    }

    public View getView() {
        return view;
    }

    protected View getUserView() {
        return userView;
    }

    public CustomFragmentSwipeBackAnimator getFragmentSwipeBackManager() {
        return fragmentSwipeBackManager;
    }

    public View findViewById(@IdRes int id) {
        return view.findViewById(id);
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setState(int onState) {
        if (onState != this.onState) {
            this.onState = onState;
            if (onState == 0) onPause();
            else {
                onResume();
                getActivity().setTouchListener((BaseActivity.TouchDispatch) view);
            }
        }
    }

    public boolean isFirstTime() {
        return isFirstTime;
    }

    void setFirstTime(boolean firstTime) {
        isFirstTime = firstTime;
    }

    public boolean canHavePending() {
        return false;
    }

    protected void statusBarSizeChanged() {
        calculateAndSetTopPadding();
    }


    public void setCanStartAnim(boolean canStartAnim) {
        this.canStartAnim = canStartAnim;
        if (canStartAnim) checkForAnim();
    }

    public boolean canInterceptTouches() {
        if (getAnimationMode() == ANIM_BOTTOM_FANCY || getAnimationMode() == ANIM_BOTTOM_NORMAL || getAnimationMode() == ANIM_CUSTOM_VERTICAL) {
            return ((BaseFrameLayoutForFrags) view).canIntercept();
        }
        return true;
    }

    public boolean canPopThisFragment() {
        return true;
    }

    public boolean hasAppBar() {
        return true;
    }

    public void onFractionChange(float fraction) {
    }

    public void endAnimIsStarting() {
    }

    public void startAnimIsStarting() {
    }

    public void setCirclePoint(Point circlePoint) {
        if (getArguments() == null) setArguments(new Bundle());
        getArguments().putParcelable(CIRCLE_CENTER, circlePoint);
    }

    protected LayoutInflater getLayoutInflater() {
        return fragmentSwipeBackManager.getInflater();
    }

    protected Resources getResources() {
        return context.getResources();
    }

    public Point getCirclePoint() {
        if (getArguments() != null) {
            Point point = getArguments().getParcelable(CIRCLE_CENTER);
            if (point != null) return point;
        }
        return new Point(150, 150);
    }

    public @CustomFragmentSwipeBackAnimator.AnimMode
    int getAnimationMode() {
        return ANIM_DEFAULT;
    }


    public View getToolbar() {
        return toolbar;
    }

    public void setToolbar(View toolbar) {
        if (hasAppBar()) {
            this.toolbar = toolbar;
            getToolbarHolder().setAppBarView(toolbar);
        }
    }

    public void setToolbarElevation(float elevation) {
        if (hasAppBar()) {
            getToolbarHolder().setElevation(elevation);
        }
    }

    public void setToolBarBackgroundColor(int color) {
        if (hasAppBar())
            getToolbarHolder().setBackgroundColor(color);
    }

    public float getDisplayDensity() {
        return fragmentSwipeBackManager.getDensity();
    }

    public void setToolBarBackgroundBitmap(Bitmap backgroundBitmap) {
        if (hasAppBar())
            getToolbarHolder().setBackgroundBitmap(backgroundBitmap);
    }

    public void setToolbarSize(float value, int unit) {
        if (hasAppBar())
            getToolbarHolder().setToolbarHeight(value, unit);
    }

    public void setToolbarScrollingSize(int value) {
        if (hasAppBar())
            getToolbarHolder().setScrollingPartSize(value);
    }

    private void setToolbarScrollFraction(float fraction) {
        if (hasAppBar())
            getToolbarHolder().setFractionOfScroll(fraction);
    }

    protected CustomAppBar getAppBar() {
        return fragmentSwipeBackManager.getAppBar();
    }

    protected ViewGroup getToolbarContainer() {
        return getToolbarHolder().getBaseLayout();
    }

    protected CustomAppBar.ToolbarHolder getToolbarHolder() {
        return getAppBar().getToolbarForFragment(this);
    }

    protected void calculateAndSetTopPadding() {
        if (hasAppBar())
            getToolbarHolder().invokeListener();
    }

    private void createView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        if (hasAppBar()) {
            fragmentSwipeBackManager.getAppBar().getToolbarForFragment(this).setMode(CustomAppBar.MODE_DEFAULT);
        } else if (this instanceof BaseMbnDialog) {
            fragmentSwipeBackManager.getAppBar().getToolbarForFragment(this).setMode(CustomAppBar.MODE_NONE);
        } else {
            fragmentSwipeBackManager.getAppBar().getToolbarForFragment(this).setMode(CustomAppBar.MODE_CUSTOM);
        }
        BaseFrameLayoutForFrags frameLayoutForFrags = new BaseFrameLayoutForFrags(getContext(), getToolbarHolder()) {
            @Override
            void layoutWithSize() {
                isLayoutDone = true;
                checkForAnim();
            }
        };
        if (!(this instanceof BaseMbnDialog)) {
            frameLayoutForFrags.addView(userView = onCreateViewForChild(inflater, container, getArguments()));
        }
        view = frameLayoutForFrags;
        onViewCreated(view, getArguments());

        if (getArguments() != null && getArguments().getSparseParcelableArray(VIEW_SAVED_INSTANCE) != null) {
//            view.restoreHierarchyState(getArguments().getSparseParcelableArray(VIEW_SAVED_INSTANCE));
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.restoreHierarchyState(getArguments().getSparseParcelableArray(VIEW_SAVED_INSTANCE));
                }
            }, 200);
//            view.restoreHierarchyState(getArguments().getSparseParcelableArray(VIEW_SAVED_INSTANCE));
        }
//        return onCreateViewForChild(inflater, container, savedInstanceState);


        //
        calculateAndSetTopPadding();
        //
    }

    public abstract View onCreateViewForChild(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

//    public abstract View makeToolBar(LayoutInflater inflater);

    public abstract void makeState(Bundle state);

    public abstract void checkForRestoreState();

    Bundle getStateAndArguments() {
        if (getArguments() == null) setArguments(new Bundle());
        SparseArray<Parcelable> sparseArray = new SparseArray<>();
        getView().saveHierarchyState(sparseArray);
        getArguments().putSparseParcelableArray(VIEW_SAVED_INSTANCE, sparseArray);
        makeState(getArguments());
        return getArguments();
    }

    @SuppressLint("ClickableViewAccessibility")
    @CallSuper
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (isFirstTime()) view.setVisibility(View.INVISIBLE);
        view.setBackgroundColor(Color.WHITE);
//        view.setOnTouchListener((v, event) -> true);
//        getFragmentManager().beginTransaction().hide(this).commit();
    }

    @CallSuper
    public void onResume() {
        checkForRestoreState();
    }

    public void onStart() {
    }

    public void onPause() {
    }

    public void onStop() {
    }

    protected void cropBlurBack() {
    }

    public void showOptionMenu(int menuId) {
        fragmentSwipeBackManager.showOptionMenu(menuId);
    }

    public void showOptionMenu(int menuId, String header) {
        fragmentSwipeBackManager.showOptionMenu(menuId, header);
    }

    public void showOptionMenu(int menuId, String header, int gravity) {
        fragmentSwipeBackManager.showOptionMenu(menuId, header, gravity);
    }

    protected void onOptionMenuItemClicked(int itemId) {
    }

    void checkForAnim() {
        if (isLayoutDone && canStartAnim) {
            if (isFirstTime()) {
                setFirstTime(false);
                getFragmentSwipeBackManager().startAnimIfNeeded(BaseFragment.this);
                if (this instanceof BaseMbnDialog) {
                    ((BaseMbnDialog) this).invalidateBackBlur();
                }
            }
        }
        if (this instanceof BaseMbnDialog) {
            cropBlurBack();
        }
    }

    private boolean hasResultId() {
        if (getArguments() == null) setArguments(new Bundle());
        return getArguments().getBoolean(HAS_RESULT_ID, false);
    }

    protected int getResultID() {
        if (getArguments() == null) setArguments(new Bundle());
        if (!hasResultId()) generateResultID();
        return getArguments().getInt(RESULT_ID);
    }

    private void generateResultID() {
        int id = ResultReceivingManager.generateID();
        getArguments().putInt(RESULT_ID, id);
        getArguments().putBoolean(HAS_RESULT_ID, true);
    }

    /**
     * Fragments who wishes to have result receiver (each fragment can have only one receiver at a time) should call this method
     * on their onResume method.
     */
    protected void registerResultReceiver(ResultReceivingManager.ResultReceiver resultReceiver) {
        int id = getResultID();
        if (ResultReceivingManager.hasThisReceiver(id)) {
            if (ResultReceivingManager.getReceiver(id).equals(resultReceiver)) return;
            ResultReceivingManager.addReceiver(id, resultReceiver);
            return;
        }
        ResultReceivingManager.addReceiver(id, resultReceiver);
    }


    public static abstract class ReverseFragment extends BaseFragment {
        @Override
        public void start(CustomFragmentSwipeBackAnimator fragmentManager) {
            super.start(fragmentManager);
            canStartAnim = true;
            isLayoutDone = true;
            checkForAnim();
        }
    }

}
