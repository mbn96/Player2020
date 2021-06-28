package mbn.libs.utils.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import mbn.libs.R;
import mbn.libs.imagelibs.imageworks.MbnUtils;

public class SimpleProgress_seekBar extends View implements ProgressBar_base.MBN_SeekBar {

    private ProgressBar_base.SeekBarListener listener;
    private float progress = 0;
    protected float density = getResources().getDisplayMetrics().density;
    private int minHeight = (int) (10 * density);
    private float padding = 2 * density;
    private float sidesPadding = padding + (12 * density);
    protected Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mPath = new Path();
    protected int accentColor = 0xffa0b0ff;
    protected int accentColor_empty = MbnUtils.alphaChanger(accentColor, 100);
    private float activeLength;
    private boolean isSeekbar = false;

    public SimpleProgress_seekBar(Context context) {
        super(context);
        init(null);
    }

    protected void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public SimpleProgress_seekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SimpleProgress_seekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public SimpleProgress_seekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    protected void init(@Nullable AttributeSet attributeSet) {
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.FILL);

        if (attributeSet != null) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attributeSet, R.styleable.CustomViews, 0, 0);
            try {
                setAccentColor(ColorStateList.valueOf(typedArray.getColor(R.styleable.CustomViews_accentColor, 0)));
            } finally {
                typedArray.recycle();
            }
        }
    }

    public void setAccentColor(int accentColor) {
        this.accentColor = accentColor;
        accentColor_empty = MbnUtils.alphaChanger(accentColor, 100);
        invalidate();
    }

    public void setAccentColor(ColorStateList accentColor) {
        if (accentColor.getDefaultColor() != 0) setAccentColor(accentColor.getDefaultColor());
        invalidate();
    }

    protected void setSidesPadding(float sidesPadding) {
        this.sidesPadding = sidesPadding;
    }

    public int getAccentColor() {
        return accentColor;
    }

    public boolean isSeekbar() {
        return isSeekbar;
    }

    public void setSeekbar(boolean seekbar) {
        isSeekbar = seekbar;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        float hCenter = h / 2f;
        mPath.rewind();
        mPath.addRoundRect(sidesPadding, hCenter - (minHeight / 2f) + padding, w - sidesPadding, hCenter + (minHeight / 2f) - padding, minHeight, minHeight, Path.Direction.CW);
        activeLength = w - (2 * sidesPadding);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void setProgress(float progress) {
        progress = progress > 1 ? 1 : progress;
        progress = progress < 0 ? 0 : progress;
        this.progress = progress;
        if (listener != null) {
            listener.onProgressChanged(this, progress, false);
        }
        invalidate();
    }

    @Override
    public float getProgress() {
        return progress;
    }

    @Override
    public void setListener(ProgressBar_base.SeekBarListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        } else {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), minHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(accentColor_empty);
        canvas.drawPath(mPath, mPaint);
        canvas.save();
        canvas.clipRect(0, 0, sidesPadding + (progress * activeLength), getHeight());
        mPaint.setColor(accentColor);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isSeekbar) {
            return false;
        }
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);
            if (listener != null) {
                listener.onTouchStart(this);
            }
        }
        float x = event.getX();

        if (x > sidesPadding && x < (getWidth() - sidesPadding)) {
            progress = (x - sidesPadding) / activeLength;
        } else if (x < sidesPadding) {
            progress = 0;
        } else {
            progress = 1;
        }

        progress = progress > 1 ? 1 : progress;
        progress = progress < 0 ? 0 : progress;

        if (listener != null) {
            listener.onProgressChanged(this, progress, true);
        }
        invalidate();

        if (listener != null && (event.getActionMasked() == MotionEvent.ACTION_UP || event.getActionMasked() == MotionEvent.ACTION_CANCEL)) {
            listener.onTouchEnd(this);
        }

        return true;
    }
}
