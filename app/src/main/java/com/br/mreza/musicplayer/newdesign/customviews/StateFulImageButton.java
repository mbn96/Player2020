package com.br.mreza.musicplayer.newdesign.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.SparseIntArray;


public class StateFulImageButton extends androidx.appcompat.widget.AppCompatImageButton {

    private SparseIntArray states = new SparseIntArray();
    private int currentState = -1;
    private boolean isActive = true;
    private int accentColor = Color.BLUE;
    private StateClickListener clickListener;


    public StateFulImageButton(Context context) {
        super(context);
        init();
    }

    public StateFulImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StateFulImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (clickListener != null) {
//                    clickListener.onClick(currentState);
//                }
//            }
//        });
    }

    public void setCurrentState(int currentState) {
        if (currentState != this.currentState) {
            this.currentState = currentState;
            setImageResource(states.get(currentState));
        }
    }

    public void addToStates(int state, int drawable) {
        states.put(state, drawable);
    }

    public void removeFromStates(int state) {
        states.delete(state);
    }

    public void clearStates() {
        states.clear();
        currentState = -1;
        setImageDrawable(null);
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setActive(boolean active) {
        isActive = active;
        if (active)
            setImageTintList(ColorStateList.valueOf(accentColor));
        else setImageTintList(ColorStateList.valueOf(Color.LTGRAY));
        invalidate();
    }


    public void setAccentColor(int accentColor) {
        this.accentColor = accentColor;
        if (isActive) {
            setImageTintList(ColorStateList.valueOf(accentColor));
            invalidate();
        }
    }

    public void setClickListener(StateClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface StateClickListener {
        void onClick(int currentState);
    }
}
