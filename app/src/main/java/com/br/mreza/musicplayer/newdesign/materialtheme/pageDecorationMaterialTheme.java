package com.br.mreza.musicplayer.newdesign.materialtheme;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.br.mreza.musicplayer.MBN.bitmapUtils.MbnUtils;
import com.br.mreza.musicplayer.newdesign.PlayerPageDecoration;


public class pageDecorationMaterialTheme extends PlayerPageDecoration {

    protected HeightChangerView backgroundView;
    protected Rect rect = new Rect();
    protected GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.argb(50, 0, 0, 0),
            Color.argb(0, 0, 0, 0)});


    protected volatile int accent = Color.WHITE;

    public void setAccent(int accent) {
//        this.accent = Color.argb(10, Color.red(accent), Color.green(accent), Color.blue(accent));
        this.accent = MbnUtils.colorAlphaChanger(MbnUtils.colorWhitener2(accent, 30), 210);
    }

    public pageDecorationMaterialTheme(Context context, View topMovingView, RecyclerView recyclerView, HeightChangerView background) {
        super(context, topMovingView, recyclerView);
        backgroundView = background;
//        topMovingView.setBackgroundColor(Color.WHITE);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (!shouldMove(parent)) {
            drawShadowForTopView(c);
        }
    }

    @Override
    protected void doTheDrawing(Canvas canvas, RecyclerView parent) {
        super.doTheDrawing(canvas, parent);
        if (shouldMove(parent))
            backgroundView.setScreenHeight(parent.getChildAt(0).getTop() - topMovingView.getHeight());
        else if (backgroundView.getScreenHeight() != 0)
            backgroundView.setScreenHeight(0);
    }

    private void drawShadowForTopView(Canvas canvas) {
        gradientDrawable.setBounds(0, topMovingView.getHeight(), canvas.getWidth(), (int) (topMovingView.getHeight() + (10 * getDensity())));
        gradientDrawable.draw(canvas);
    }

    @Override
    protected void doTheScaling(Canvas canvas, RecyclerView parent) {
//        canvas.drawColor(Color.WHITE);
//        super.doTheScaling(canvas, parent);
        if (imageBitmap != null && false) {
            int screenWidth = canvas.getWidth();
            int screenHeight = shouldMove(parent) ? parent.getChildAt(0).getTop() - topMovingView.getHeight() : 0;

            float factor = Math.max(((float) screenWidth) / imageBitmap.getWidth(), ((float) screenHeight) / imageBitmap.getHeight());

            int useW = (int) (factor * imageBitmap.getWidth());
            int useH = (int) (factor * imageBitmap.getHeight());

            int diffHalfWidth = (useW - screenWidth) / 2;
            int diffHalfHeight = (useH - screenHeight) / 2;

            rect.set(-diffHalfWidth, -diffHalfHeight, screenWidth + diffHalfWidth, screenHeight + diffHalfHeight);
            canvas.drawBitmap(imageBitmap, null, rect, null);
        }
//        canvas.drawColor(almostWhite);
        canvas.drawColor(accent);
    }

    public interface HeightChangerView {
        void setScreenHeight(int screenHeight);

        int getScreenHeight();
    }

}
