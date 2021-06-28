package com.br.mreza.musicplayer.newdesign.materialtheme;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;


public class PlayerPageViewPager extends ViewPager implements pageDecorationMaterialTheme.HeightChangerView {

    private int screenHeight;
    private boolean inZoneTouch = false;

    private String TAG = "viewPager";

    public PlayerPageViewPager(@NonNull Context context) {
        super(context);
    }

    public PlayerPageViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getScreenHeight() {
        return screenHeight;
    }

    private void setScreenHeightForChild() {
        for (int i = 0; i < getChildCount(); i++) {
            PlayerPageBackgroundMaterial img = (PlayerPageBackgroundMaterial) ((FrameLayout) getChildAt(i)).getChildAt(0);
            img.setScreenHeight(screenHeight);
        }
    }

    @Override
    public void setAdapter(@Nullable PagerAdapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            ((PlayerPagePagerAdapter) adapter).setScreenHeight(screenHeight);
        }
    }

    @Override
    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
        setScreenHeightForChild();
        if (getAdapter() != null) {
            ((PlayerPagePagerAdapter) getAdapter()).setScreenHeight(screenHeight);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        inZoneTouch = false;
        return ev.getY() < screenHeight && super.onInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!inZoneTouch) {
            inZoneTouch = ev.getY() < screenHeight && super.onTouchEvent(ev);
        } else {
            return super.onTouchEvent(ev);
        }
        return inZoneTouch;
    }
}
