package com.br.mreza.musicplayer.MBN.customViews;


import android.content.Context;
import android.util.AttributeSet;

public class MbnCustomSizeImageView extends androidx.appcompat.widget.AppCompatImageView {

    public MbnCustomSizeImageView(Context context) {
        super(context);
    }

    public MbnCustomSizeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MbnCustomSizeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


        getLayoutParams().height = w * 3 / 2;

        requestLayout();


    }
}
