package com.br.mreza.musicplayer.MBN.customViews;


import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MbnFastSwipeViewPager extends ViewPager {


    private float oldX = 0;

    public MbnFastSwipeViewPager(Context context) {
        super(context);
    }

    public MbnFastSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {

            oldX = ev.getX();

        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {


        /* currently working on something else
         Will fix it later .
         */

        //TODO  Fix this .
        if (true) return false;


//        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {

//            oldX = ev.getX();

//        }

        if (ev.getActionMasked() == MotionEvent.ACTION_MOVE) {

            ev.offsetLocation((float) ((ev.getX() - oldX) * 1.5), 0);

        }


        return super.onTouchEvent(ev);
    }
}
