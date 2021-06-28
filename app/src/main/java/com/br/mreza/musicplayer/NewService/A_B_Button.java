package com.br.mreza.musicplayer.NewService;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.util.AttributeSet;
import android.view.View;


public class A_B_Button extends androidx.appcompat.widget.AppCompatTextView {

    private ColoredSpan aSpan = new ColoredSpan(Color.CYAN);
    private ColoredSpan bSpan = new ColoredSpan(Color.CYAN);


    public A_B_Button(Context context) {
        super(context);
        init();
        check();
    }

    public A_B_Button(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        check();
    }

    public A_B_Button(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        check();
    }

    private void init() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicInfoHolder.setA_B();
            }
        });
    }


    public void check() {
        SpannableString string = new SpannableString("A / B");

        if (MusicInfoHolder.hasA())
            string.setSpan(aSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


//        DynamicDrawableSpan metricAffectingSpan = new DynamicDrawableSpan() {
//            @Override
//            public Drawable getDrawable() {
//                Drawable shapeDrawable =
//                        new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.ic_cancel_button));
////                        new ShapeDrawable(new OvalShape());
//                shapeDrawable.setBounds(0, 0, 50, 50);
//                return shapeDrawable;
//            }
//        };

        if (MusicInfoHolder.hasB())
            string.setSpan(bSpan, 4, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        setText(string);

    }


    private class ColoredSpan extends CharacterStyle {

        private int color;

        ColoredSpan(int color) {
            this.color = color;
        }

        @Override
        public void updateDrawState(TextPaint tp) {
            tp.setColor(Color.rgb(0, 150, 0));
//            tp.setColor(color);
            tp.setShadowLayer(2, 0, 0, color);
        }
    }

}
