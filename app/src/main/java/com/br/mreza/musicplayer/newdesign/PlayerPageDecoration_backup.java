package com.br.mreza.musicplayer.newdesign;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.br.mreza.musicplayer.UI.RecyclerViews.ItemDecors.BlurDecor;


public class PlayerPageDecoration_backup extends BlurDecor {

    private View topMovingView;
    private boolean receiverTouch = false;
    private boolean canIntercept = false;


    public boolean canIntercept() {
//        Log.i(TAG, "canIntercept: " + canIntercept);
        return false;
    }

    public PlayerPageDecoration_backup(Context context, View topMovingView) {
        super(context);
        this.topMovingView = topMovingView;
        topMovingView.setClickable(false);
        topMovingView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        canIntercept = receiverTouch;
//                        Log.i(TAG, "onTouch: " + receiverTouch);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        canIntercept = false;
                        break;
                }


                return receiverTouch;
            }
        });
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        doTheDrawing(c, parent);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (!shouldMove(parent)) {
            mPath.rewind();
            mPath.addRect(0, 0, c.getWidth(), topMovingView.getHeight(), Path.Direction.CCW);
            c.save();
            c.clipPath(mPath);
            doTheScaling(c, parent);
            c.restore();
        } else {
            c.scale(0, 0);
        }
    }

    @Override
    protected void doTheDrawing(Canvas canvas, RecyclerView parent) {
        mPath.rewind();
        if (shouldMove(parent)) {
            topMovingView.setTranslationY(parent.getChildAt(0).getTop() - topMovingView.getHeight());
            topMovingView.setElevation(0);
            receiverTouch = false;
            mPath.addRect(0, parent.getChildAt(0).getTop() - topMovingView.getHeight(), canvas.getWidth(), canvas.getHeight(), Path.Direction.CCW);
        } else {
            topMovingView.setTranslationY(0);
            topMovingView.setElevation(3 * getDensity());
            receiverTouch = true;
            mPath.addRect(0, topMovingView.getHeight(), canvas.getWidth(), canvas.getHeight(), Path.Direction.CCW);
        }

        if (imageBitmap != null) {
            canvas.save();
            canvas.clipPath(mPath);
            doTheScaling(canvas, parent);
            canvas.restore();
        }
    }


    private boolean shouldMove(RecyclerView parent) {
        return parent.getChildAdapterPosition(parent.getChildAt(0)) == 0 && parent.getChildAt(0).getTop() > topMovingView.getHeight();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        if (parent.getPaddingTop() != topMovingView.getHeight())
//            parent.setPadding(parent.getPaddingLeft(), topMovingView.getHeight(), parent.getPaddingRight(), parent.getPaddingBottom());
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = parent.getHeight();
        }
    }
}
