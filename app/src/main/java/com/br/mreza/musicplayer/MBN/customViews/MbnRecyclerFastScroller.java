package com.br.mreza.musicplayer.MBN.customViews;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.br.mreza.musicplayer.FirstPageAdapterTabAllSongs;

public class MbnRecyclerFastScroller extends View implements FirstPageAdapterTabAllSongs.HelperForFastScroll {

    Paint mPaint;
    Paint mPaint2;
    Path mPath;
    Path mPath2;

    int centerX;
    int trackPathHeight;
    int diffEchSide = 10;
    int thumbHalf = 40;

    float trackHeight;

    int max = 1000;
    int progress = 5;

    FastScrollerListener listener;

    FirstPageAdapterTabAllSongs adapterTabAllSongs;
    RecyclerView recyclerView;

    TextView textView;


    public MbnRecyclerFastScroller(Context context) {
        super(context);
        makeChanges();


    }

    public MbnRecyclerFastScroller(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        makeChanges();
    }

    public MbnRecyclerFastScroller(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        makeChanges();
    }

    public MbnRecyclerFastScroller(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        makeChanges();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        makeChanges();
    }

    private void makeChanges() {

        mPath = new Path();
        mPath2 = new Path();

        mPaint = new Paint();
//        mPaint.setColor(Color.GRAY);
        mPaint.setColor(Color.parseColor("#fffaf1"));
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5f);
        mPaint2 = new Paint();
//        mPaint2.setColor(Color.MAGENTA);
        mPaint2.setColor(Color.parseColor("#c8f5f5f5"));
        mPaint2.setAntiAlias(true);
        mPaint2.setStrokeCap(Paint.Cap.ROUND);
        mPaint2.setStrokeJoin(Paint.Join.ROUND);
        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setStrokeWidth(15f);

        centerX = getWidth() / 2;
        trackPathHeight = getHeight() - (2 * diffEchSide);

        trackHeight = trackPathHeight - (2 * thumbHalf);

        setProgress(progress);


    }

    public Paint getmPaint() {
        return mPaint;
    }

    public void setmPaint(Paint mPaint) {
        this.mPaint = mPaint;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;

        invalidator();


    }


    public void setListener(FastScrollerListener listener) {
        this.listener = listener;
    }

    public void setRecyclerView(RecyclerView recyclerView) {

        this.recyclerView = recyclerView;
        setMax(recyclerView.getAdapter().getItemCount());

        try {
            adapterTabAllSongs = (FirstPageAdapterTabAllSongs) recyclerView.getAdapter();

            orangeAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void orangeAdapter() {

        adapterTabAllSongs.setHelperForFastScroll(this);

    }


    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    @Override
    public void newBind(int pos, char a) {

        setProgress(pos);

        System.out.println(a);

        try {
            textView.setText("" + a);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    interface FastScrollerListener {

        void onStartTouch(MbnRecyclerFastScroller fastScroller, int progress);

        void onFinishTouch(MbnRecyclerFastScroller fastScroller, int progress);

        void onChange(MbnRecyclerFastScroller fastScroller, int progress);


    }


    private void invalidator() {

        mPath.rewind();
        mPath2.rewind();

        mPath.moveTo(centerX, diffEchSide);
        mPath.lineTo(centerX, diffEchSide + trackPathHeight);

        float factor = trackHeight / max;

        float centerPoint = factor * progress;
        centerPoint += (diffEchSide + thumbHalf);

        mPath2.moveTo(centerX, centerPoint - thumbHalf);

        mPath2.lineTo(centerX, centerPoint + thumbHalf);

        invalidate();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(mPath, mPaint);
        canvas.drawPath(mPath2, mPaint2);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {

            try {
                textView.setVisibility(GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                listener.onFinishTouch(this, progress);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;

        }

        if (action == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);
            try {
                textView.setVisibility(VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                listener.onStartTouch(this, progress);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        getTouch(event.getY());


        return true;
    }


    private void getTouch(float y) {

        float factor = max / trackHeight;

//        System.out.println(factor);
//        System.out.println(y);

        if (y >= diffEchSide + thumbHalf && y <= diffEchSide + thumbHalf + trackHeight) {

            y -= (diffEchSide + thumbHalf);

            int progressTemp = (int) (factor * y);

            try {
                listener.onChange(this, progressTemp);
            } catch (Exception e) {
                e.printStackTrace();
            }

            recyclerView.scrollToPosition(progressTemp);


            setProgress(progressTemp);

        }


    }


}
