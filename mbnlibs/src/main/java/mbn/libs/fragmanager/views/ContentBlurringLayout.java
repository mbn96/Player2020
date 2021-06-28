package mbn.libs.fragmanager.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import mbn.libs.imagelibs.imageworks.MbnUtils;

public class ContentBlurringLayout extends FrameLayout {

    private int maxBlurRadius = 18;
    private float blurProgress = 0f;
    private final float downScale = 1f / 5;
    //    private final float upScale = 1 / downScale;
    private final Rect rectS = new Rect();
    private final Rect rectD = new Rect();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public ContentBlurringLayout(@NonNull Context context, int maxBlurRadius) {
        super(context);
        this.maxBlurRadius = maxBlurRadius;
    }

    public ContentBlurringLayout(@NonNull Context context) {
        super(context);
//        setLayerType(LAYER_TYPE_SOFTWARE, paint);
    }

    public ContentBlurringLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        setLayerType(LAYER_TYPE_SOFTWARE, paint);
//        post(test);
    }

    public ContentBlurringLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        setLayerType(LAYER_TYPE_SOFTWARE, paint);
    }

    public ContentBlurringLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
//        setLayerType(LAYER_TYPE_SOFTWARE, paint);
    }

    public int getMaxBlurRadius() {
        return maxBlurRadius;
    }

    public void setMaxBlurRadius(int maxBlurRadius) {
        this.maxBlurRadius = maxBlurRadius;
        invalidate();
    }

    public float getBlurProgress() {
        return blurProgress;
    }

    public void setBlurProgress(float blurProgress) {
        this.blurProgress = blurProgress;
        invalidate();
    }

    private Bitmap getBlurBitmap() {
        rectD.set(0, 0, getWidth(), getHeight());
        rectS.set(0, 0, (int) (getWidth() * downScale), (int) (getHeight() * downScale));
        return Bitmap.createBitmap(rectS.width(), rectS.height(), Bitmap.Config.ARGB_8888);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (blurProgress == 0) {
            if (getLayerType() != LAYER_TYPE_NONE) {
                setLayerType(LAYER_TYPE_NONE, null);
            }
            super.dispatchDraw(canvas);
        } else {
            if (getLayerType() != LAYER_TYPE_SOFTWARE) {
                setLayerType(LAYER_TYPE_SOFTWARE, null);
            }
            Bitmap blur = getBlurBitmap();
            Canvas bCanvas = new Canvas(blur);
            bCanvas.scale(downScale, downScale);
            super.dispatchDraw(bCanvas);
            MbnUtils.renderScriptBlur(getContext(), blur, (int) (maxBlurRadius * blurProgress), true);
            canvas.drawBitmap(blur, null, rectD, paint);
        }
    }

    private Runnable test = new Runnable() {
        @Override
        public void run() {
            blurProgress += .01;
            if (blurProgress > 1) {
                blurProgress = 0;
            }
            setBlurProgress(blurProgress);
            post(this);
        }
    };

//    @Override
//    public void draw(Canvas canvas) {
//        Log.i("MBN_LOG", "draw: ");
//        if (blurProgress == 0) {
//            super.draw(canvas);
//        } else {
//            Bitmap blur = getBlurBitmap();
//            Canvas bCanvas = new Canvas(blur);
//            bCanvas.scale(downScale, downScale);
//            super.draw(bCanvas);
//            MbnUtils.stackBlur(blur, (int) (maxBlurRadius * blurProgress), true);
//            canvas.drawBitmap(blur, rectS, rectD, null);
//        }
//    }
}
