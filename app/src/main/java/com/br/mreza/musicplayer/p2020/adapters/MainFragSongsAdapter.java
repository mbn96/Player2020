package com.br.mreza.musicplayer.p2020.adapters;

import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;

import com.br.mreza.musicplayer.AlbumArtTaskForSongs;
import com.br.mreza.musicplayer.DataSong;
import com.br.mreza.musicplayer.newmodel.database.DataBaseManager;

import java.util.ArrayList;

import mbn.libs.backgroundtask.BaseTaskHolder;
import mbn.libs.utils.SelfShrinkMap;

public abstract class MainFragSongsAdapter extends MainFragBaseRecycler_adapter<MainFragSongsAdapter.SongHolder> implements BaseTaskHolder.ResultReceiver {

    private ArrayList<Long> ids = new ArrayList<>();
    private SelfShrinkMap<DataSong> songsMap = new SelfShrinkMap<>(15);
    private String headerText;

    @Override
    public void onResult(Object result, Object info) {
        if (result instanceof DataSong) {
            long id = (long) info;
            songsMap.put(id, (DataSong) result);
            if (ids.contains(id)) {
                int pos = ids.indexOf(id);
                notifyItemChanged(pos + 1);
            }
        }
    }

    public MainFragSongsAdapter(String headerText) {
        this.headerText = headerText;
    }

    public void setIds(ArrayList<Long> songIds) {
        ids.clear();
        ids.addAll(songIds);
        setItemCount(ids.size());
    }

    @Override
    protected SongHolder initiateHolder(View view, int type) {
        return new SongHolder(view, type);
    }

    @Override
    protected String getHeaderText() {
        return headerText;
    }

    @Override
    protected void onHeaderClick() {

    }

    public abstract void onSongClick(long songId);


    public class SongHolder extends MainFragBaseRecycler_adapter.BaseHolder {
        private DataSong song;
        private long id;

        SongHolder(@NonNull View itemView, int type) {
            super(itemView, type);
        }


        @Override
        protected void onClick() {
            onSongClick(id);
        }

        @Override
        protected void onBind() {
//            int posTest = getAdapterPosition();
            id = ids.get(getAdapterPosition() - 1);
            song = songsMap.get(id);
            if (song == null) {
                DataBaseManager.getManager().getSong(id, MainFragSongsAdapter.this);
                return;
            }

            textView.setText(song.getTitle());
            imageView.setVisibility(View.INVISIBLE);

            DataBaseManager.getManager().getSongArtWork(id, 400, new BaseTaskHolder.ResultReceiver() {
                @Override
                public void onResult(Object result, Object info) {
                    if (song != null && id == (long) info) {
                        imageView.setImageBitmap((Bitmap) result);
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            });

//            new AlbumArtTaskForSongs(itemView.getContext(), song.getPath(), (int) id) {
//                @Override
//                public void onFinish(Bitmap bitmap, String data) {
//                    if (song != null && data.equals(song.getPath())) {
//                        imageView.setImageBitmap(bitmap);
//                        imageView.setVisibility(View.VISIBLE);
//                        imageView.setScaleX(0.35f);
//                        imageView.animate().scaleX(1).setDuration(300).setInterpolator(new OvershootInterpolator(3)).start();
//                    }
//                }
//            };

        }
    }
}
