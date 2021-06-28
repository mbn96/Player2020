package mbn.libs.fragmanager;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import mbn.libs.R;
import mbn.libs.fragmanager.views.StatusBarExtender;

import static mbn.libs.fragmanager.CustomFragmentSwipeBackAnimator.ANIM_DIALOG_TTB;

public abstract class BaseBottomSheetDialog extends BaseMbnDialog {

    private BottomSheetBehavior bottomSheetBehavior;
    private StatusBarExtender statusBarExtender;

    @Override
    public final void onPrepareDialog() {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) getLayoutInflater().inflate(R.layout.bottom_sheet_layout, getDialogBase(), false);
        LinearLayout bottomSheetLayout = (LinearLayout) coordinatorLayout.getChildAt(0);
        statusBarExtender = (StatusBarExtender) bottomSheetLayout.getChildAt(0);

        View bottomUserView = getBottomLayout(coordinatorLayout);
        ViewGroup.LayoutParams layoutParams = bottomUserView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            bottomUserView.setLayoutParams(layoutParams);
        }
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        bottomSheetLayout.addView(bottomUserView);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setPeekHeight((int) (getFragmentSwipeBackManager().getHeight() * 0.6f));
        bottomSheetBehavior.setFitToContents(true);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_HIDDEN) {
                    getFragmentSwipeBackManager().popFragment();
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                if (Float.isNaN(v)) v = 0;
                statusBarExtender.setSlideFraction(v);
//                Log.i(TAG, "onSlide: fraction: " + v);

            }
        });




        addLayout(coordinatorLayout);
    }


    protected abstract View getBottomLayout(ViewGroup container);

    protected StatusBarExtender getStatusBarExtender() {
        return statusBarExtender;
    }

    @CallSuper
    @Override
    protected void statusBarSizeChanged() {
        super.statusBarSizeChanged();
        statusBarExtender.getLayoutParams().height = (int) getFragmentSwipeBackManager().getStatusBarSize();
        statusBarExtender.requestLayout();
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        statusBarExtender.getLayoutParams().height = (int) getFragmentSwipeBackManager().getStatusBarSize();
        statusBarExtender.requestLayout();
    }

    @Override
    public void onFractionChange(float fraction) {
        if (fraction > 1) {
            super.onFractionChange(1);
        } else if (fraction < 0) {
            super.onFractionChange(0);
        } else {
            super.onFractionChange(fraction);
        }
    }

    @Override
    public boolean canInterceptTouches() {
        return false;
    }

    @Override
    public int getAnimationMode() {
        return ANIM_DIALOG_TTB;
    }

    @Override
    public void makeState(Bundle state) {

    }

    @Override
    public void checkForRestoreState() {

    }
}
