package com.br.mreza.musicplayer.MBN.customViews;


import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MbnForceTouchViewPager extends ViewPager {
    public MbnForceTouchViewPager(Context context) {
        super(context);
    }

    public MbnForceTouchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {


        getParent().requestDisallowInterceptTouchEvent(true);

        return super.onInterceptTouchEvent(ev);
    }
}
