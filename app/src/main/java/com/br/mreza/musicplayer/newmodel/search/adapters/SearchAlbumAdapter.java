package com.br.mreza.musicplayer.newmodel.search.adapters;


import android.content.Context;

import com.br.mreza.musicplayer.DataBaseAlbum;
import com.br.mreza.musicplayer.FirstPageAdapterTabAlbums;

import java.util.ArrayList;

public class SearchAlbumAdapter extends FirstPageAdapterTabAlbums {


    public SearchAlbumAdapter(Context context, AlbumTabListener listClick) {
        super(context, listClick);
    }

    public void setData(ArrayList<DataBaseAlbum> albums) {
        albumList.clear();
        albumList.addAll(albums);
        updateData();
    }

    @Override
    public void updateData() {
        notifyDataSetChanged();
    }
}
