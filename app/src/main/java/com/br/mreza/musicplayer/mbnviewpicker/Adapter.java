package com.br.mreza.musicplayer.mbnviewpicker;


import android.view.View;

import java.util.Observable;

public abstract class Adapter extends Observable {

    public abstract int getItemCount();

    public abstract View getView(int pos);

    public abstract void prepareViewForPosition(View view, int pos);

    public abstract void viewOutOfSight(View view, int pos);

    final int getInternalViewSight() {
        int count = getViewCountInSight();
        return count % 2 == 0 ? count + 1 : count;
    }

    /**
     * @return Should be an odd number. otherwise it will automatically be added by one.
     */
    public abstract int getViewCountInSight();

}
