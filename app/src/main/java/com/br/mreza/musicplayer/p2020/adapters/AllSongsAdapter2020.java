package com.br.mreza.musicplayer.p2020.adapters;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.br.mreza.musicplayer.newmodel.adapters.songs.NewModelCurrentQueueAdapter;

public class AllSongsAdapter2020 extends NewModelCurrentQueueAdapter {
    private View normalLayout;

    public AllSongsAdapter2020(Context context, ItemTouchHelper touchHelper, View optionView, View normalLayout, FragmentManager fragmentManager) {
        super(context, touchHelper, optionView, fragmentManager);
        this.normalLayout = normalLayout;
    }


    @Override
    public boolean changeTheQueue() {
        return true;
    }

    @Override
    public void changeOptionVisibility(boolean state) {
        super.changeOptionVisibility(state);
        if (normalLayout != null) {
            if (state) normalLayout.setVisibility(View.INVISIBLE);
            else normalLayout.setVisibility(View.VISIBLE);
        }
    }
}
