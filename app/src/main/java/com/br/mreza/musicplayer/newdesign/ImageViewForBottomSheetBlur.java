package com.br.mreza.musicplayer.newdesign;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class ImageViewForBottomSheetBlur extends androidx.appcompat.widget.AppCompatImageView {
    private float offset = 0;
    private float top = 0;

    public ImageViewForBottomSheetBlur(Context context) {
        super(context);
    }

    public ImageViewForBottomSheetBlur(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewForBottomSheetBlur(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setParentTop(float parentTop) {
        this.top = parentTop;
    }

    public void setOffset(float offset) {
        this.offset = offset;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(0, (top * (1f - offset)) - getHeight() * (1f - offset));
        canvas.clipRect(0, 0, getWidth(), getHeight());
        super.onDraw(canvas);
        canvas.restore();
    }
}
