package com.br.mreza.musicplayer;

import android.content.Context;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class MbnBottomSheetBehavior<V extends View> extends BottomSheetBehavior<V> {

    public MbnBottomSheetBehavior() {
    }

    public MbnBottomSheetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, V child, MotionEvent event) {

        return false;
    }
}
