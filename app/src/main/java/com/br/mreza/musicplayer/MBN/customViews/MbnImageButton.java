package com.br.mreza.musicplayer.MBN.customViews;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MbnImageButton extends androidx.appcompat.widget.AppCompatImageButton {


    public MbnImageButton(Context context) {
        super(context);
    }

    public MbnImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MbnImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private boolean checkIt(float x, float y) {

        return (x > 0 && x < getWidth()) && (y > 0 && y < getHeight());


    }

    private ColorStateList stateList;

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            stateList = getImageTintList();
            setImageTintList(ColorStateList.valueOf(Color.CYAN));

        } else if (event.getActionMasked() == MotionEvent.ACTION_UP || event.getActionMasked() == MotionEvent.ACTION_CANCEL) {

            setImageTintList(stateList);

        } else {

            float x, y;

            x = event.getX();
            y = event.getY();

            if (!checkIt(x, y)) {

                setImageTintList(stateList);
//                setImageTintList(ColorStateList.valueOf(Color.BLACK));

            }

        }


        return super.onTouchEvent(event);
    }
}
