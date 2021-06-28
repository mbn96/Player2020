package com.br.mreza.musicplayer.MBN.customViews;


import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.br.mreza.musicplayer.R;

public class MbnVerticalViewPager extends ViewPager {


    private float scaleForDen;

    public MbnVerticalViewPager(Context context) {
        super(context);
        init();
    }

    public MbnVerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {

        setPageTransformer(true, new MbnTransform());

        setOverScrollMode(OVER_SCROLL_NEVER);

        scaleForDen = getResources().getDisplayMetrics().density;


    }


    private class MbnTransform implements PageTransformer {
        @Override
        public void transformPage(View page, float position) {


            if (position < -1) {

                page.setAlpha(0f);

            } else if (position <= 1) {
                page.setAlpha(1f);

                page.setVisibility(VISIBLE);


                page.setElevation(0f);

                page.setTranslationX(page.getWidth() * -position);
                if (position >= 0) {
                    page.setTranslationY((position * page.getHeight()) + (70 * scaleForDen) * -Math.abs(position));
                    page.setElevation(10);
                } else {
                    page.setTranslationY((position * page.getHeight()) + (70 * scaleForDen) * Math.abs(position));

                }
//                page.setTranslationY(0.2f * position * page.getHeight());


                if (position == -1) {
                    page.setVisibility(INVISIBLE);
                }
//
                if (position < 0) {
                    page.setAlpha(1 + position);
                }

                try {
                    page.findViewById(R.id.player_frag_top_play_button).setAlpha(Math.abs(position));
                    if (position == 0) {
                        page.findViewById(R.id.player_frag_top_play_button).setVisibility(INVISIBLE);
                    } else {
                        page.findViewById(R.id.player_frag_top_play_button).setVisibility(VISIBLE);
                    }
                } catch (Exception ignored) {

                }


//
//                if (position < 0) {
//
////                page.setPivotY(getHeight());
////                page.setPivotX(getWidth()/2);
//
//                    page.setRotationX(position * 90);
////                    page.setRotationY(position * 90);
//
//                    page.setScaleX(position + 1);
//                    page.setScaleY(position + 1);
//                }
            } else {
                page.setAlpha(0f);
            }


        }
    }


    private MotionEvent xToY(MotionEvent event) {


        float width = getWidth();

        float height = getHeight();


        float newX = (event.getY() / height) * width;
        float newY = (event.getX() / width) * height;

        event.setLocation(newX, newY);


        return event;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        boolean how = super.onInterceptTouchEvent(xToY(ev));

        xToY(ev);

        return how;

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(xToY(ev));
    }
}

