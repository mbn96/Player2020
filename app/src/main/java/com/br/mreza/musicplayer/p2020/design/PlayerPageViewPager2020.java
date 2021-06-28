package com.br.mreza.musicplayer.p2020.design;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.br.mreza.musicplayer.newdesign.materialtheme.PlayerPageBackgroundMaterial;
import com.br.mreza.musicplayer.newdesign.materialtheme.PlayerPagePagerAdapter;
import com.br.mreza.musicplayer.newdesign.materialtheme.pageDecorationMaterialTheme;


public class PlayerPageViewPager2020 extends ViewPager implements pageDecorationMaterialTheme.HeightChangerView {

    private int screenHeight;
    private boolean inZoneTouch = false;

    private String TAG = "viewPager";

    public PlayerPageViewPager2020(@NonNull Context context) {
        super(context);
    }

    public PlayerPageViewPager2020(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getScreenHeight() {
        return screenHeight;
    }

//    private void setScreenHeightForChild() {
//        for (int i = 0; i < getChildCount(); i++) {
//            PlayerPageBackgroundMaterial img = (PlayerPageBackgroundMaterial) ((FrameLayout) getChildAt(i)).getChildAt(0);
//            img.setScreenHeight(screenHeight);
//        }
//    }

    @Override
    public void setAdapter(@Nullable PagerAdapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            ((pageDecorationMaterialTheme.HeightChangerView) adapter).setScreenHeight(screenHeight);
        }
    }

    @Override
    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
//        setScreenHeightForChild();
        if (getAdapter() != null) {
            ((pageDecorationMaterialTheme.HeightChangerView) getAdapter()).setScreenHeight(screenHeight);
        }
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        int save = canvas.save();
        float tY = (getHeight() - screenHeight) * 0.5f;
//        int currentItem = getCurrentItem();
        canvas.translate(0, -tY);
        canvas.clipRect((getCurrentItem() - 1) * getWidth(), 0, (getCurrentItem() + 2) * getWidth(), screenHeight + tY);
        super.draw(canvas);
        canvas.restoreToCount(save);
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
