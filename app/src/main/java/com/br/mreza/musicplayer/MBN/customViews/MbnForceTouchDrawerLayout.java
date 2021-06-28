package com.br.mreza.musicplayer.MBN.customViews;


import android.content.Context;
import androidx.drawerlayout.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MbnForceTouchDrawerLayout extends DrawerLayout {

    public MbnForceTouchDrawerLayout(Context context) {
        super(context);
    }

    public MbnForceTouchDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MbnForceTouchDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        getParent().requestDisallowInterceptTouchEvent(true);

        return super.onInterceptTouchEvent(ev);
    }
}
