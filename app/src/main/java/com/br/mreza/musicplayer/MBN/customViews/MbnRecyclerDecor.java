package com.br.mreza.musicplayer.MBN.customViews;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class MbnRecyclerDecor extends RecyclerView.ItemDecoration {


    private int scale;
    private Paint mPaint;

    public MbnRecyclerDecor(Context context) {
        super();

        scale = (int) context.getResources().getDisplayMetrics().density;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(scale);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
//        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setAlpha(150);

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        c.save();
        for (int i = 0; i < parent.getChildCount(); i++) {

            int y = parent.getChildAt(i).getBottom();

            c.drawLine(scale * 90, (float) (y + (scale * 0.5)), parent.getWidth() - (scale * 30), (float) (y + (scale * 0.5)), mPaint);

//            c.drawLine(scale * 95, (float) (y + (scale * 3)), parent.getWidth() - (scale * 35), (float) (y + (scale * 3)), mPaint);

//            c.drawRoundRect(scale * 90, y, parent.getWidth() - (scale * 30), y + (6 * scale), 20, 20, mPaint);

        }
        c.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);

        outRect.set(0, 0, 0, scale);


    }
}
