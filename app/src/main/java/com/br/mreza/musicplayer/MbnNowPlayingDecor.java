package com.br.mreza.musicplayer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.br.mreza.musicplayer.newmodel.adapters.songs.NewModelCurrentQueueAdapter;


public class MbnNowPlayingDecor extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private long id;

    public void setId(long id) {
        this.id = id;
    }

    public void setColor(int color) {
        mPaint.setColor(color);
        mPaint.setAlpha(150);
    }

    public MbnNowPlayingDecor() {
        super();


        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

//        mPaint.setColor(Color.GRAY);
        mPaint.setColor(Color.rgb(0, 170, 170));
        mPaint.setAlpha(10);

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        c.save();
        NewModelCurrentQueueAdapter adapter = (NewModelCurrentQueueAdapter) parent.getAdapter();
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (parent.getChildAdapterPosition(parent.getChildAt(i)) == adapter.getPositionForID(id)) {

//                Rect out = new Rect();
//
//                parent.getDecoratedBoundsWithMargins(parent.getChildAt(i), out);
//
//                c.drawRoundRect(new RectF(out), 80, 80, mPaint);

                View child = parent.getChildAt(i);

                c.drawRoundRect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom(), 80, 80, mPaint);
                return;
            }

        }

        c.restore();
    }
}
