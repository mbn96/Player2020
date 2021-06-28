package com.br.mreza.musicplayer.MBN.customViews;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.animation.OvershootInterpolator;

public class MbnRoundCheckBoxLite extends androidx.appcompat.widget.AppCompatCheckBox {

    private Paint paintForTick;
    private Paint paintForOutline;
    private Paint paintForFill;
//    private Paint paintForDraw;

    private Path pathForTick;
    private Path pathForOutline;
    private Path pathForFill;

    private float displayDensity;


    private ValueAnimator animator;

    public MbnRoundCheckBoxLite(Context context) {
        super(context);
        init();
    }

    public MbnRoundCheckBoxLite(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MbnRoundCheckBoxLite(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {

        displayDensity = getResources().getDisplayMetrics().density;

        paintForTick = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintForTick.setStrokeJoin(Paint.Join.ROUND);
        paintForTick.setStrokeCap(Paint.Cap.ROUND);
        paintForTick.setStyle(Paint.Style.STROKE);
        paintForTick.setStrokeWidth(displayDensity * 3);
        paintForTick.setColor(Color.WHITE);
//        paintForTick.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));


        paintForFill = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintForFill.setStyle(Paint.Style.FILL);
        paintForFill.setColor(Color.CYAN);
//        paintForFill.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));


        paintForOutline = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintForOutline.setStyle(Paint.Style.STROKE);
        paintForOutline.setColor(Color.GRAY);
        paintForOutline.setStrokeWidth(displayDensity * 1);

//        paintForDraw = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paintForDraw.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));


        pathForFill = new Path();
        pathForTick = new Path();
        pathForOutline = new Path();
        animateCustom(isChecked());

    }


    @Override
    public void setChecked(boolean checked) {
        if (checked != isChecked()) {
            animateCustom(checked);
        }
        super.setChecked(checked);

    }

    private void painter(float value) throws Exception {


        float w = getWidth(), h = getHeight();


        float factor = 10f;
        pathForTick.rewind();
        pathForOutline.rewind();
        pathForFill.rewind();
        pathForOutline.addCircle(w / 2, h / 2, (((w / 2) - (displayDensity * factor - 5)) * value) + displayDensity * 3, Path.Direction.CCW);
//        pathForOutline.addCircle(w / 2, h / 2, (w / 2) - displayDensity, Path.Direction.CCW);

        pathForFill.addCircle(w / 2, h / 2, (((w / 2) - (displayDensity * factor - 5)) * value) + displayDensity * 3, Path.Direction.CCW);

        if (value <= 0.5f) {
            float diffY = (h / 2) - (displayDensity * factor);
            float diffX = (w / 3) - (displayDensity * factor);
            float valueForUse = value * 2;

//            Log.e("check", String.valueOf(diffX * valueForUse));


            pathForTick.moveTo(displayDensity * factor, h / 2);
            pathForTick.lineTo((displayDensity * factor) + (diffX * valueForUse), (h / 2) + (diffY * valueForUse));


        } else {
            float diffY = h - (displayDensity * factor * 2);
            float diffX = ((w * 2) / 3) - (displayDensity * factor);
            float valueForUse = (value - 0.5f) * 2;

//            Log.e("check", String.valueOf(valueForUse));

            pathForTick.moveTo(displayDensity * factor, h / 2);

            pathForTick.lineTo(w / 3, h - (displayDensity * factor));

//            pathForTick.moveTo(w / 2, h - (displayDensity * factor));
            pathForTick.lineTo((w / 3) + (diffX * valueForUse), (h - (displayDensity * factor)) - (diffY * valueForUse));

        }

        if (value == 0 && !isChecked()) {
            pathForTick.rewind();
        }

        invalidate();

    }


    private void animateCustom(boolean b) {

//        Log.e("check", "met");


        float start, end;

        if (b) {
            start = 0f;
            end = 1f;
        } else {
            start = 1f;
            end = 0f;
        }

        if (animator != null) {
            animator.cancel();
        }


//        new Interpolator(){
//            @Override
//            public float getInterpolation(float input) {
//                return 0;
//            }
//        }


        animator = ValueAnimator.ofFloat(start, end);

        animator.setInterpolator(new OvershootInterpolator());
        animator.setDuration(800);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                try {
                    painter((Float) animation.getAnimatedValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        animator.start();


    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);


        canvas.drawPath(pathForFill, paintForFill);
        canvas.drawPath(pathForTick, paintForTick);
        canvas.drawPath(pathForOutline, paintForOutline);


    }
}
