package mbn.libs.utils.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;

import mbn.libs.R;
import mbn.libs.imagelibs.imageworks.MbnUtils;

public class MagneticProgressBar extends SimpleProgress_seekBar {

    private static final float HALF_A_PI = (float) (Math.PI / 2);

    private Type type;
    protected float ballsRadios;
    private float baseLine = ballsRadios + density;
    protected float travelDistance;
    private int ballsCount;
    private float fallOut = 0.4f;
    private float fallOutFactor_eachSide;
    protected float[] ballsHeights;
    private int fullBackgroundColor = MbnUtils.alphaChanger(accentColor, 180);
    private int emptyBackgroundColor = MbnUtils.alphaChanger(accentColor, 80);

    private Path ballPath = new Path();
    private Path linePath = new Path();

    ValueAnimator animator;

    public MagneticProgressBar(Context context) {
        super(context);

    }

    public MagneticProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public MagneticProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public MagneticProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    @Override
    protected void init(@Nullable AttributeSet attributeSet) {
        super.init(attributeSet);
        setSidesPadding(0);
        setMinHeight((int) (density * 30));
        setAccentColor(accentColor);
        ballsRadios = 2 * density;

        if (attributeSet != null) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attributeSet, R.styleable.CustomViews, 0, 0);
            try {
                int t = typedArray.getInteger(R.styleable.CustomViews_progressBarMode, 0);
                Type type = null;
                switch (t) {
                    case 0:
                        type = Type.Loading;
                        break;
                    case 1:
                        type = Type.ProgressBar;
                        break;
                    case 2:
                        type = Type.SeekBar;
                        break;
                }
                setType(type);
            } finally {
                typedArray.recycle();
            }
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        ballsCount = (int) (w / (3 * ballsRadios));
        fallOutFactor_eachSide = 1f / ((int) (ballsCount * (fallOut / 2f)));
        baseLine = ballsRadios + density;
        travelDistance = h - (2 * baseLine);
        ballsHeights = new float[ballsCount];
        setSidesPadding(0);
        super.onSizeChanged(w, h, oldw, oldh);
        checkForType();
    }

    @Override
    public void setAccentColor(int accentColor) {
        if (type == Type.Loading) {
            this.accentColor = accentColor_empty = accentColor;
            fullBackgroundColor = emptyBackgroundColor = MbnUtils.alphaChanger(accentColor, 120);
        } else {
            fullBackgroundColor = MbnUtils.alphaChanger(accentColor, 150);
            emptyBackgroundColor = MbnUtils.alphaChanger(accentColor, 80);
            super.setAccentColor(accentColor);
        }
    }

    private void calculateHeights() {
        float bumpBall = (getProgress() * ballsCount);
        float value;
        for (int i = 0; i < ballsHeights.length; i++) {
            value = (i - bumpBall) * fallOutFactor_eachSide;
            value = Math.min(1, value);
            value = Math.max(-1, value);
            ballsHeights[i] = (float) Math.pow(Math.sin(HALF_A_PI + (value * HALF_A_PI)), 2);
        }
    }

//    private void calculateHeights_2() {
//        float bumpBall = (getProgress() * ballsCount);
//        float n;
//        float value;
//        float finalV;
//        int count = 0;
//        for (int i = 0; i < ballsHeights.length; i++) {
//            n = i + 1;
//            finalV = 0;
//            count = 0;
//            for (float j = -1; j <= 1; j += 0.1f) {
//                n = i + j + 1;
//                value = (n - bumpBall) * fallOutFactor_eachSide;
//                value = Math.min(1, value);
//                value = Math.max(-1, value);
//                finalV += (float) Math.pow(Math.sin(HALF_A_PI + (value * HALF_A_PI)), 2);
//                count++;
//            }
//            ballsHeights[i] = finalV / count;
//        }
//    }

    private void drawThePaths() {
        ballPath.rewind();
//        linePath.rewind();
//        linePath.moveTo(getWidth(), getHeight());
//        linePath.lineTo(0, getHeight());
//        linePath.lineTo(0, getHeight() - baseLine);
        int n;
        for (int i = 0; i < ballsHeights.length; i++) {
            n = i + 1;
            ballPath.addCircle(ballsRadios * ((n * 3) - 1), getHeight() - (baseLine + (ballsHeights[i] * travelDistance)), ballsRadios, Path.Direction.CW);
            // TODO: 6/8/2020 Decide of want inside color...
//            linePath.lineTo(ballsRadios * ((n * 3) - 1), getHeight() - (baseLine + (ballsHeights[i] * travelDistance)));
        }
        linePath.close();
    }

    protected void prepareForDraw() {
        drawThePaths();
    }

    protected void drawFullPart(Canvas canvas) {
        if (type != Type.Loading) {
//            mPaint.setColor(fullBackgroundColor);
//            canvas.drawPath(linePath, mPaint);
        }
        mPaint.setColor(accentColor);
        canvas.drawPath(ballPath, mPaint);
    }

    protected void drawEmptyPart(Canvas canvas) {
        if (type != Type.Loading) {
//            mPaint.setColor(emptyBackgroundColor);
//            canvas.drawPath(linePath, mPaint);
        }
        mPaint.setColor(accentColor_empty);
        canvas.drawPath(ballPath, mPaint);
    }

    protected void drawDecor(Canvas canvas) {
        mPaint.setColor(accentColor);
        canvas.drawCircle(getWidth() * getProgress(), getHeight() - (2 * ballsRadios), 2 * ballsRadios, mPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calculateHeights();
        prepareForDraw();

        canvas.save();
        canvas.clipRect(0, 0, getWidth() * getProgress(), getHeight());
        drawFullPart(canvas);
        canvas.restore();

        canvas.save();
        canvas.clipRect(getWidth() * getProgress(), 0, getWidth(), getHeight());
        drawEmptyPart(canvas);
        canvas.restore();

        drawDecor(canvas);
    }

    public void setType(Type type) {
        this.type = type;
        setAccentColor(accentColor);
        if (type == Type.SeekBar) {
            setSeekbar(true);
        } else {
            setSeekbar(false);
        }
        checkForType();
        invalidate();
    }

    private void checkForType() {
        if (animator != null) {
            try {
                animator.end();
                animator.cancel();
            } catch (Exception ignored) {
            }
        }
        if (type == Type.Loading) {
            animator = ValueAnimator.ofFloat(0, 1).setDuration((long) ((getWidth() / density) * 20f));
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.addUpdateListener(animation -> {
                setProgress((Float) animation.getAnimatedValue());
            });
            animator.start();
        }
    }

    public enum Type {
        ProgressBar, SeekBar, Loading
    }

}
