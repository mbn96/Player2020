package com.br.mreza.musicplayer.mbnviewpicker;


import android.view.View;

import static com.br.mreza.musicplayer.mbnviewpicker.MbnViewPicker.ORI_HORIZONTAL;

public class ItemMover {

    public void onPositionChanged(View view, MbnViewPicker parent, float pos, int pageLimit, int orientation) {
        view.setAlpha(1 - Math.abs(pos / pageLimit));
        if (orientation == ORI_HORIZONTAL)
            view.setTranslationX(pos * view.getWidth());
        else {
            view.setTranslationY(pos * view.getHeight());
//            view.setPivotY(-view.getHeight());
//            view.setPivotX(view.getWidth() / 2.4f);
//            view.setRotationX((180 / (parent.getItemsInSight())) * -pos);
        }
        if (Math.abs(pos) > pageLimit) view.setVisibility(View.GONE);
        else view.setVisibility(View.VISIBLE);
    }


}
