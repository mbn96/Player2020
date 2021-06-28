package mbn.libs.fragmanager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import static mbn.libs.fragmanager.MbnNavDrawer.STATE_CLOSED;
import static mbn.libs.fragmanager.MbnNavDrawer.STATE_OPEN;


public class MbnUpPageButton extends View implements MbnNavDrawer.MbnNavDrawerChangeListener {

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mPath = new Path();

    private int mState = STATE_CLOSED;
    private float currentFraction = 0;
    private float density;
    private MbnNavDrawer mbnNavDrawer;

    public MbnUpPageButton(Context context) {
        super(context);
        init();
    }

    public MbnUpPageButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MbnUpPageButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MbnUpPageButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        density = getResources().getDisplayMetrics().density;
        mPaint.setColor(Color.rgb(100, 100, 100));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(density * 3);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mbnNavDrawer != null) mbnNavDrawer.setCurrentState(STATE_OPEN);
            }
        });
        prepareForDrawing();
//        prepareForDrawingNewStyle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        prepareForDrawing();
//        prepareForDrawingNewStyle();

    }

    public void setCurrentFraction(float currentFraction) {
        this.currentFraction = currentFraction;
        prepareForDrawing();
//        prepareForDrawingNewStyle();

    }

    private void prepareForDrawing() {
        mPath.rewind();

        mPaint.setStrokeWidth(density * (5 - (2.2f * currentFraction)));

        if (currentFraction == 0) {
            float halfSize = 10 * density * currentFraction;
            mPath.moveTo(getWidth() / 2, (getHeight() / 2) - halfSize);
            mPath.lineTo(getWidth() / 2, (getHeight() / 2) + 1);

            //left line
            float leftX = getWidth() / 2 - (10 * density);
            mPath.moveTo(leftX, getHeight() / 2);
            mPath.lineTo(leftX + halfSize, (getHeight() / 2) + 1);

            //right line
            float rightX = getWidth() / 2 + (10 * density);
            mPath.moveTo(rightX, getHeight() / 2);
            mPath.lineTo(rightX - halfSize, (getHeight() / 2) + 1);
            invalidate();
            return;
        }

        //middle line
        float halfSize = 10 * density * currentFraction;
        mPath.moveTo(getWidth() / 2, (getHeight() / 2) - halfSize);
        mPath.lineTo(getWidth() / 2, (getHeight() / 2) + halfSize);

        //left line
        float leftX = getWidth() / 2 - (10 * density);
        mPath.moveTo(leftX, getHeight() / 2);
        mPath.lineTo(leftX + halfSize, (getHeight() / 2) - halfSize);

        //right line
        float rightX = getWidth() / 2 + (10 * density);
        mPath.moveTo(rightX, getHeight() / 2);
        mPath.lineTo(rightX - halfSize, (getHeight() / 2) - halfSize);

        invalidate();
    }

    private void prepareForDrawingNewStyle() {
        mPath.rewind();

        mPaint.setStrokeWidth(density * (5 - (2 * currentFraction)));

        float leftX = getWidth() / 2 - (10 * density) * (1 - currentFraction * 2);
        float rightX = getWidth() / 2 + (10 * density) * (1 - currentFraction * 2);
        float useFraction = 2 * (currentFraction - 0.5f);
        float halfSize = 10 * density * useFraction;

        //middle line
        if (currentFraction >= 0.5f) {
            mPath.moveTo(getWidth() / 2, (getHeight() / 2) - halfSize);
            mPath.lineTo(getWidth() / 2, (getHeight() / 2) + halfSize);
        } else {
            mPath.moveTo(getWidth() / 2, getHeight() / 2);
            mPath.lineTo(getWidth() / 2, getHeight() / 2);

            mPath.moveTo(leftX, getHeight() / 2);
            mPath.lineTo(leftX, getHeight() / 2);

            mPath.moveTo(rightX, getHeight() / 2);
            mPath.lineTo(rightX, getHeight() / 2);


            invalidate();
            return;
        }
//        leftX = getWidth() / 2 - (10 * density);
//        rightX = getWidth() / 2 + (10 * density);
        //left line
        mPath.moveTo(getWidth() / 2, (getHeight() / 2) - halfSize);
        mPath.lineTo((getWidth() / 2) - halfSize, getHeight() / 2);
//        mPath.lineTo((getWidth() / 2) - halfSize, ((getHeight() / 2) - halfSize) + (halfSize * useFraction));

        //right line
        mPath.moveTo(getWidth() / 2, (getHeight() / 2) - halfSize);
        mPath.lineTo((getWidth() / 2) + halfSize, getHeight() / 2);
//        mPath.lineTo((getWidth() / 2) + halfSize, ((getHeight() / 2) - halfSize) + (halfSize * useFraction));

        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
//        if (currentFraction >= 0.5)
//            canvas.rotate(90 * -(2 * (currentFraction - 0.5f)), canvas.getWidth() / 2, canvas.getHeight() / 2);
        canvas.rotate(-90 * currentFraction, canvas.getWidth() / 2, canvas.getHeight() / 2);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
    }

    public void syncWithDrawer(MbnNavDrawer mbnNavDrawer) {
        mbnNavDrawer.addChangeListener(this);
        this.mbnNavDrawer = mbnNavDrawer;
    }

    public void unSyncWithDrawer() {
        if (mbnNavDrawer != null)
            mbnNavDrawer.removeChangeListener(this);
    }

    @Override
    public void onSlide(float slide) {
        setCurrentFraction(slide);
    }

}
