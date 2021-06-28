package mbn.libs.fragmanager.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class StatusBarExtender extends View {

    private float density = getResources().getDisplayMetrics().density;
    private int accentColor = Color.WHITE;
    private float maxCornerAngle = density * 40;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path = new Path();
    private float slideFraction;
    private float[] corners = new float[8];

    public StatusBarExtender(Context context) {
        super(context);
        init();
    }

    public StatusBarExtender(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint.setColor(accentColor);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculate();
    }

    public void setAccentColor(int accentColor) {
        this.accentColor = accentColor;
        paint.setColor(accentColor);
        invalidate();
    }

    public void setMaxCornerAngle(float maxCornerAngle) {
        this.maxCornerAngle = maxCornerAngle;
        calculate();
    }

    public void setSlideFraction(float slideFraction) {
        this.slideFraction = slideFraction;
        calculate();
    }

    private void calculate() {
        float scale = 2.5f;
//        float fraction = (float) (Math.pow(slideFraction, scale));
        float fraction = (float) (Math.pow(scale, slideFraction) / scale);

        path.rewind();
        for (int i = 0; i < 4; i++) {
            corners[i] = 2 * maxCornerAngle * (1 - fraction);
        }
        path.addRoundRect(0, getHeight() * (1 - fraction), getWidth(), getHeight(), corners, Path.Direction.CW);
//        path.addRoundRect(0, (getHeight() * 0.5f) * (1 - fraction), getWidth(), getHeight(), corners, Path.Direction.CW);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }
}
