package mbn.libs.utils.views;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class BadgerImageButton extends androidx.appcompat.widget.AppCompatImageButton {

    private boolean showBadge = false;
    private float density = getResources().getDisplayMetrics().density;
    private float badgeRadios = 7 * density;
    private float emptyRadios = badgeRadios + (5 * density);
    //    private Path clipPath = new Path();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int accentColor = 0xffff0e0d;

    public BadgerImageButton(Context context) {
        super(context);
        init();
    }

    public BadgerImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BadgerImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        badgeRadios = Math.min(w, h) * 0.1f;
        emptyRadios = badgeRadios + (badgeRadios * 1.7f);
//        clipPath.rewind();
//        clipPath.addCircle(emptyRadios, emptyRadios, emptyRadios, Path.Direction.CW);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void init() {
        paint.setColor(accentColor);
        paint.setStyle(Paint.Style.FILL);
    }

    public int getAccentColor() {
        return accentColor;
    }

    public void setAccentColor(int accentColor) {
        this.accentColor = accentColor;
        paint.setColor(accentColor);
        invalidate();
    }

    public boolean isShowBadge() {
        return showBadge;
    }

    public void setShowBadge(boolean showBadge) {
        this.showBadge = showBadge;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.save();
//        if (showBadge && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            canvas.clipOutPath(clipPath);
//        }
        super.onDraw(canvas);
//        canvas.restore();
        if (showBadge)
            canvas.drawCircle(emptyRadios, emptyRadios, badgeRadios, paint);
    }
}
