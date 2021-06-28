package com.br.mreza.musicplayer.MBN.customViews;


import android.content.Context;
import androidx.annotation.Nullable;
import androidx.core.math.MathUtils;
import androidx.core.view.GestureDetectorCompat;

import android.util.AttributeSet;
import android.view.MotionEvent;

public class MbnTouchForceRecyclerView extends MbnSpringEndRecyclerView {


    public MbnTouchForceRecyclerView(Context context) {
        super(context);
    }

    public MbnTouchForceRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MbnTouchForceRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(e);
    }
}
