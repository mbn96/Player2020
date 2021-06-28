package com.br.mreza.musicplayer.newdesign.newblurdecor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;

import com.br.mreza.musicplayer.newdesign.BackgroundFullScreenImageView;


public class PageBlurBack extends BackgroundFullScreenImageView {

    private Path crop = new Path();
    private int top;

    public PageBlurBack(Context context) {
        super(context);
    }

    public PageBlurBack(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PageBlurBack(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCropTop(int top) {
        if (this.top != top) {
            this.top = top;
            crop.rewind();
            crop.addRect(0, top, getRight(), getScreenHeight(), Path.Direction.CCW);
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(crop);
        super.onDraw(canvas);
        canvas.restore();
    }
}
