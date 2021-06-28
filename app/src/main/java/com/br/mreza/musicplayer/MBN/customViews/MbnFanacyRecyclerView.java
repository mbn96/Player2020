package com.br.mreza.musicplayer.MBN.customViews;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;


public class MbnFanacyRecyclerView extends RecyclerView {

    private float olY;

    public MbnFanacyRecyclerView(Context context) {
        super(context);
    }

    public MbnFanacyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MbnFanacyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }




    @Override
    public boolean onTouchEvent(MotionEvent e) {


        switch (e.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:


                olY = e.getY();
                break;

            case MotionEvent.ACTION_MOVE:

                float y = e.getY();


                if (!canScrollVertically(-1)) {

                    setPivotY(getHeight());

//                    scrollTo(0, 0);


//                    setScaleY((float) (1 / (Math.log10(Math.abs(y - olY)))));
                    setScaleY(1 - ((y - olY) / 5000));
//                    invalidate();

                    break;
                }

                if (!canScrollVertically(1)) {

                    setPivotY(0);

                    setScaleY(1 + ((y - olY) / 5000));

//                    invalidate();

                    break;
                }


                break;

//                if (y - olY > 0) {
//
//                    if (!canScrollVertically(-1)) {
//
//                        setTranslationY(y - olY);
//
//                    }
//
//                } else {
//
//                    if (!canScrollVertically(1)) {
//
//                        setTranslationY(y - olY);
//
//                    }
//
//                }


            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:


                if (getScaleY() != 1) {

                    animate().scaleY(1f).setDuration(500).setInterpolator(new OvershootInterpolator());

                }


                break;


        }


        return super.onTouchEvent(e);
    }
}
