package com.br.mreza.musicplayer.newmodel.search.adapters;

import android.content.Context;

import com.br.mreza.musicplayer.DataBaseArtists;
import com.br.mreza.musicplayer.newmodel.adapters.ArtistsAdapter;

import java.util.ArrayList;


public class ArtistsSearchAdapter extends ArtistsAdapter {

    public ArtistsSearchAdapter(Context context) {
        super(context);
    }

    public void setArtists(ArrayList<DataBaseArtists> artists) {
        dataBaseArtists.clear();
        dataBaseArtists.addAll(artists);
        update();
    }

    @Override
    protected void update() {
        notifyDataSetChanged();
    }
}
