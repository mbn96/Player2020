package mbn.libs.utils.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class NewStyleProgressBar extends MagneticProgressBar {
    public NewStyleProgressBar(Context context) {
        super(context);
    }

    public NewStyleProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NewStyleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NewStyleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init(@Nullable AttributeSet attributeSet) {
        super.init(attributeSet);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2 * density);
        ballsRadios = 4 * density;
    }



    @Override
    protected void prepareForDraw() {
//        super.prepareForDraw();
    }

    @Override
    protected void drawDecor(Canvas canvas) {
    }

    @Override
    protected void drawFullPart(Canvas canvas) {
        mPaint.setColor(accentColor);
        float halfD = travelDistance / 2f;
        for (int i = 0; i < ballsHeights.length; i++) {
            canvas.drawLine(ballsRadios * ((i * 3) + 1), (getHeight() / 2f) - (halfD * ballsHeights[i]), ballsRadios * ((i * 3) + 3),
                    (getHeight() / 2f) + (halfD * ballsHeights[i]), mPaint);
        }
    }

    @Override
    protected void drawEmptyPart(Canvas canvas) {
        mPaint.setColor(accentColor_empty);
        float halfD = travelDistance / 2f;
        for (int i = 0; i < ballsHeights.length; i++) {
            canvas.drawLine(ballsRadios * ((i * 3) + 1), (getHeight() / 2f) - (halfD * ballsHeights[i]), ballsRadios * ((i * 3) + 3),
                    (getHeight() / 2f) + (halfD * ballsHeights[i]), mPaint);
        }
    }
}
