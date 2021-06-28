package com.br.mreza.musicplayer.newdesign.customviews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;

import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;

import mbn.libs.imagelibs.imageworks.MbnUtils;


public class NewPlayButton extends View {


    private String TAG = "rippleButton";

    private RippleDrawable rippleDrawable;
    private boolean hasSetBound = false;

    public enum PlayingState {
        STATE_PLAYING(1),
        STATE_PAUSED(0);

        PlayingState(int st) {
            state = st;
        }

        final int state;
    }

    private PlayingState state = PlayingState.STATE_PAUSED;
    private float currentFraction = 1;
    private ValueAnimator animator;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint shadowPaint_icon;
    //    private Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path leftPath, rightPath, backGround;

    private float density = getResources().getDisplayMetrics().density;
    private float lineWidth = density * 6;
    private float baseShapeWidth = 25 * density;
    private float baseHeight = baseShapeWidth / 2;
    private float baseDistant = 0.4f * baseShapeWidth;
//    private int accentColor = Color.parseColor("#00b4ff");

    private AnticipateInterpolator anticipateInterpolator = new AnticipateInterpolator(3);
    private AccelerateInterpolator accelerateDecelerateInterpolator = new AccelerateInterpolator(2);
    private OvershootInterpolator overshootInterpolator = new OvershootInterpolator(3);


    public NewPlayButton(Context context) {
        super(context);
        init();
    }

    public NewPlayButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NewPlayButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public NewPlayButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setPlayerIconColor(int playerIconColor) {
        paint.setColor(playerIconColor);
        invalidate();
    }

    public void setAccentColor(int accentColor) {
//        setBackground(null);
        shadowPaint.setColor(accentColor);
//        shadowPaint.setColor(Color.argb(0, 0, 0, 0));
        shadowPaint.setShadowLayer(7 * density, 0, 5 * density, MbnUtils.alphaChanger(accentColor, 180));
//        shadowPaint.setShadowLayer(8 * density, 0, 5 * density, Color.argb(110, Color.red(accentColor), Color.green(accentColor), Color.blue(accentColor)));
        invalidate();

        if (rippleDrawable == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                rippleDrawable = (RippleDrawable) getForeground();
            }
        }
//        if (rippleDrawable != null) {
//            rippleDrawable.setColorFilter(accentColor, PorterDuff.Mode.SRC_IN);
//        }

//        rippleDrawable.setBounds((int) (getCenterX() - (34 * density)), (int) (getCenterY() - (34 * density)), (int) (getCenterX() + (34 * density)), (int) (getCenterY() + (34 * density)));

    }

    private float getCenterX() {
        return getWidth() / 2;
    }

    private float getCenterY() {
        return getHeight() / 2;
    }

    private void init() {
//        setElevation(5 * density);


        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(lineWidth);

        shadowPaint.setStrokeCap(Paint.Cap.ROUND);
        shadowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        shadowPaint.setColor(Color.argb(0, 0, 0, 0));
//        shadowPaint.setAlpha(0);
        shadowPaint.setStrokeJoin(Paint.Join.ROUND);
        shadowPaint.setStrokeWidth(lineWidth * (2f / 3));
        shadowPaint.setShadowLayer(1.5f * density, 1 * density, 2 * density, Color.GRAY);

        shadowPaint_icon = new Paint(shadowPaint);
        shadowPaint_icon.setStyle(Paint.Style.FILL_AND_STROKE);
        shadowPaint_icon.setColor(Color.GRAY);

        setLayerType(LAYER_TYPE_SOFTWARE, null);


//        circlePaint.setColor(accentColor);
//        circlePaint.setStyle(Paint.Style.FILL);


        leftPath = new Path();
        rightPath = new Path();
        backGround = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        lineDrawer();
        backGround.rewind();
        backGround.addCircle(getWidth() / 2, getHeight() / 2, 34 * density, Path.Direction.CCW);
    }

    private void lineDrawer() {
        leftPath.rewind();
        rightPath.rewind();
        float cenX = getWidth() / 2;
        float cenY = getHeight() / 2;

        // ----- left path ----- //
        float leftX = (float) (cenX - baseDistant + ((0.4 * baseDistant) * currentFraction));

        leftPath.moveTo(leftX, cenY - baseHeight);
        leftPath.lineTo(leftX, cenY + baseHeight);

        // ----- right path -----//
        float rightX = cenX + baseDistant;
        float topAndBottomX = rightX - ((1.6f * baseDistant) * currentFraction);
        float centerX = rightX + ((0.5f * baseDistant) * currentFraction);

        rightPath.moveTo(topAndBottomX, cenY - baseHeight);
        rightPath.lineTo(centerX, cenY);
        rightPath.lineTo(topAndBottomX, cenY + baseHeight);


        invalidate();

    }


    private void startAnim() {
        if (animator != null) {
            try {
                animator.removeAllListeners();
                animator.removeAllUpdateListeners();
                animator.cancel();
            } catch (Exception ignored) {
            }
        }
        float endValue = state == PlayingState.STATE_PLAYING ? 0 : 1;

        animator = ValueAnimator.ofFloat(currentFraction, endValue);
        animator.setDuration(350);

        if (endValue == 0) animator.setInterpolator(overshootInterpolator);
        else animator.setInterpolator(anticipateInterpolator);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentFraction = (float) animation.getAnimatedValue();
                lineDrawer();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                animator.removeAllListeners();
                animator.removeAllUpdateListeners();
                animator = null;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animator.removeAllListeners();
                animator.removeAllUpdateListeners();
                animator = null;
            }
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!hasSetBound && rippleDrawable != null) {
            rippleDrawable.setBounds((int) (getCenterX() - (36 * density)), (int) (getCenterY() - (36 * density)), (int) (getCenterX() + (36 * density)), (int) (getCenterY() + (36 * density)));
            hasSetBound = true;
        }

        canvas.drawPath(backGround, shadowPaint);
//        rippleDrawable.draw(canvas);

        canvas.drawPath(leftPath, shadowPaint_icon);
        canvas.drawPath(rightPath, shadowPaint_icon);

        canvas.drawPath(leftPath, paint);
        canvas.drawPath(rightPath, paint);
    }


    public PlayingState getState() {
        return state;
    }

//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getActionMasked()) {
//            case MotionEvent.ACTION_DOWN:
////                paint.setColor(Color.CYAN);
////                invalidate();
//                if (rippleDrawable != null) {
////                    rippleDrawable.setHotspot(event.getX(), event.getY());
//                    rippleDrawable.setState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled});
//                    rippleDrawable.setVisible(true, true);
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                if (rippleDrawable != null) {
//                    rippleDrawable.setHotspot(event.getX(), event.getY());
//                    rippleDrawable.setVisible(true, true);
//                    rippleDrawable.setState(new int[]{});
//                }
////                paint.setColor(Color.WHITE);
////                invalidate();
//                break;
//        }
//        return super.onTouchEvent(event);
//    }

//    @Override
//    public boolean performClick() {
//        if (rippleDrawable != null) {
////            rippleDrawable.setHotspot(0, 0);
//            rippleDrawable.setHotspot(getCenterX(), 0);
//            rippleDrawable.setVisible(true, true);
//            rippleDrawable.setState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled});
////            rippleDrawable.setHotspot(getCenterX(), 0);
////            rippleDrawable.setState(getDrawableState());
//
//            postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (rippleDrawable != null) {
//                        rippleDrawable.setState(getDrawableState());
//                    }
//                }
//            }, 200);
//        }
//        return super.performClick();
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    public void setState(PlayingState state) {
        this.state = state;
        startAnim();
    }
}
