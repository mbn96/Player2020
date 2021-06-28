package com.br.mreza.musicplayer.newdesign.materialtheme;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


public class BlurOverlayBackground extends View {

    private Bitmap bitmap;
    private boolean show = false;

    public BlurOverlayBackground(Context context) {
        super(context);
    }

    public BlurOverlayBackground(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BlurOverlayBackground(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BlurOverlayBackground(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
