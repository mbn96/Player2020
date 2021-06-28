package mbn.libs.utils.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;

import mbn.libs.R;
import mbn.libs.utils.JavaUtils;

public class BubbleSwitch extends androidx.appcompat.widget.AppCompatTextView implements Checkable.CheckableView {

    private NeonButton.Style style;
    private boolean check;
    private Checkable.CheckableListener listener;
    private float density;
    private int totalSwitchHeight;
    private int totalSwitchWidth;
    private int text_switch_space;
    private float borderWidth;
    private float drawBorderWidth;
    private float activeWidth;
    private float thumbWidth;
    private float travelingDistance;
    private boolean isTouchable = true;

    private float switchStartWidth, switchStartHeight;
    //------------------//
    private float finalFraction;
    private float currentFraction;
    private ValueAnimator animator;
    private float cornerRadios;

    private int baseColor = Color.BLACK;
    private int accentColor = 0xff5080bb;
    private Path boredPath = new Path();
    private Path thumbPath = new Path();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int alpha;

    public BubbleSwitch(Context context) {
        super(context);
        init(null);
    }

    public BubbleSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BubbleSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attributeSet) {
        density = getResources().getDisplayMetrics().density;
        calculateSizes();
        setGravity(Gravity.CENTER_VERTICAL | Gravity.START);

        if (attributeSet != null) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attributeSet, R.styleable.CustomViews, 0, 0);
            try {
                setAccentColor(ColorStateList.valueOf(typedArray.getColor(R.styleable.CustomViews_accentColor, 0)));
                setStyle(typedArray.getInteger(R.styleable.CustomViews_style, 0) == 0 ? NeonButton.Style.Light : NeonButton.Style.Dark);
                setCheck(typedArray.getBoolean(R.styleable.CustomViews_check, false));
            } finally {
                typedArray.recycle();
            }
        }

    }

    private void calculateSizes() {
        totalSwitchHeight = (int) (25 * density);
        totalSwitchWidth = (int) (50 * density);
        text_switch_space = (int) (10 * density);
        borderWidth = 4 * density;
        drawBorderWidth = density;
        activeWidth = totalSwitchWidth - (2 * borderWidth);
        thumbWidth = activeWidth * 0.4f;
        travelingDistance = activeWidth - thumbWidth;
        cornerRadios = 50 * density;
        setPadding(0, 0, text_switch_space + totalSwitchWidth, 0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        switchStartWidth = w - totalSwitchWidth;
        switchStartHeight = (h / 2f) - (totalSwitchHeight / 2f);
        boredPath.rewind();
        boredPath.addRoundRect(switchStartWidth + (drawBorderWidth / 2f), switchStartHeight + (drawBorderWidth / 2f),
                w - (drawBorderWidth / 2f), switchStartHeight + totalSwitchHeight - (drawBorderWidth / 2f),
                cornerRadios, cornerRadios, Path.Direction.CW);
        super.onSizeChanged(w, h, oldw, oldh);
        checkAnimator();
    }


    public void setAccentColor(int accentColor) {
        this.accentColor = accentColor;
        invalidate();
    }

    public void setAccentColor(ColorStateList accentColor) {
        setAccentColor(accentColor.getDefaultColor());
    }

    public void setStyle(NeonButton.Style style) {
        this.style = style;
        if (style == NeonButton.Style.Light) {
            setTextColor(ColorStateList.valueOf(Color.DKGRAY));
            baseColor = Color.DKGRAY;
        } else {
            setTextColor(ColorStateList.valueOf(Color.WHITE));
            baseColor = Color.WHITE;
        }
    }

    public NeonButton.Style getStyle() {
        return style;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredHeight() < totalSwitchHeight) {
            setMeasuredDimension(getMeasuredWidth(), totalSwitchHeight);
        }
    }

    private void drawBorderPath(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(baseColor);
        paint.setStrokeWidth(drawBorderWidth);
        canvas.drawPath(boredPath, paint);
    }

    private void drawThumbPath(Canvas canvas) {
        thumbPath.rewind();
        float leftX, rightX, useFraction;
        if (currentFraction > 0.5f) {
            useFraction = (float) JavaUtils.rangeChange(currentFraction, 0.5, 1, 0, 1);
            rightX = getWidth() - borderWidth;
            leftX = switchStartWidth + borderWidth + (travelingDistance * useFraction);
        } else {
            useFraction = (float) JavaUtils.rangeChange(currentFraction, 0, 0.5, 0, 1);
            leftX = switchStartWidth + borderWidth;
            rightX = switchStartWidth + borderWidth + thumbWidth + (travelingDistance * useFraction);
        }

        thumbPath.addRoundRect(leftX, switchStartHeight + borderWidth, rightX,
                switchStartHeight + totalSwitchHeight - borderWidth, cornerRadios, cornerRadios, Path.Direction.CW);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(baseColor);
        canvas.drawPath(thumbPath, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(accentColor);
        alpha = (int) (255 * currentFraction);
        alpha = Math.min(alpha, 255);
        alpha = Math.max(alpha, 0);
        paint.setAlpha(alpha);
//        It's a bit heavy...
//        paint.setMaskFilter(new BlurMaskFilter(7 * density, BlurMaskFilter.Blur.SOLID));
        canvas.drawPath(thumbPath, paint);
//        paint.setMaskFilter(null);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBorderPath(canvas);
        drawThumbPath(canvas);
    }

    private void toggle() {
        check = !check;
        finalFraction = check ? 1 : 0;
        if (listener != null) {
            listener.onCheckChanged(check, true);
        }
        checkAnimator();
    }

    @Override
    public void setCheck(boolean check) {
        if (this.check != check) {
            this.check = check;
            finalFraction = check ? 1 : 0;
            if (listener != null) {
                listener.onCheckChanged(check, false);
            }
            checkAnimator();
        }
    }

    private void checkAnimator() {
        if (animator != null) {
            animator.cancel();
        }
        if (currentFraction != finalFraction) {
            animator = ValueAnimator.ofFloat(currentFraction, finalFraction).setDuration(300);
            animator.setInterpolator(new OvershootInterpolator());
            animator.addUpdateListener(animation -> {
                currentFraction = (float) animation.getAnimatedValue();
                invalidate();
            });
            animator.start();
        }
    }

    public void setTouchable(boolean touchable) {
        isTouchable = touchable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isTouchable) {
            return false;
        }
        boolean returnBool = event.getX() < getWidth()
                && event.getX() > 0
                && event.getY() < getHeight()
                && event.getY() > 0;

        // TODO: 6/9/2020 It's a very simple implementation for testing ... Change it later...
        if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            if (returnBool) toggle();
        }

        return returnBool;
    }

    @Override
    public boolean isChecked() {
        return check;
    }

    @Override
    public void setCheckableListener(Checkable.CheckableListener listener) {
        this.listener = listener;
    }
}
