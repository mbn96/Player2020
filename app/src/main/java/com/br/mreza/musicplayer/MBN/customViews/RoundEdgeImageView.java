package com.br.mreza.musicplayer.MBN.customViews;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

public class RoundEdgeImageView extends androidx.appcompat.widget.AppCompatImageView {


    private Path mPath;

    public RoundEdgeImageView(Context context) {
        super(context);
    }

    public RoundEdgeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);


    }

    public RoundEdgeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getSize(widthMeasureSpec) > 0) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(widthMeasureSpec));
        } else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPath = new Path();


        mPath.rewind();

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPath.addRoundRect(0, 0, getWidth(), getHeight(), 8 * displayMetrics.density, 8 * displayMetrics.density, Path.Direction.CW);
        }


    }


    @Override
    protected void onDraw(Canvas canvas) {

//        canvas.save();
//        canvas.clipPath(mPath);
        super.onDraw(canvas);
//        canvas.restore();

    }
}
