package com.br.mreza.musicplayer.p2020.adapters;

import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.NonNull;

import com.br.mreza.musicplayer.AlbumArtTaskForAlbums;
import com.br.mreza.musicplayer.DataBaseAlbum;
import com.br.mreza.musicplayer.MySongDAO;
import com.br.mreza.musicplayer.newdesign.NewAlbumFrag;
import com.br.mreza.musicplayer.newmodel.database.DataBaseManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import mbn.libs.backgroundtask.BaseTaskHolder;
import mbn.libs.fragmanager.CustomFragmentSwipeBackAnimator;
import mbn.libs.utils.JavaUtils;

public class MainFragAlbumAdapter extends MainFragBaseRecycler_adapter<MainFragAlbumAdapter.AlbumHolder> implements BaseTaskHolder.ResultReceiver {

    private ArrayList<DataBaseAlbum> albumList = new ArrayList<>();
    private int maxSize = 10;

    @Override
    public void onResult(Object result, Object info) {
        if ((result instanceof List) && info.equals("albums")) {
            if (!((List) result).isEmpty()) {
                albumList.clear();
                for (int i = 0; i < ((List) result).size() && albumList.size() < maxSize; i++) {
                    DataBaseAlbum album = (DataBaseAlbum) ((List) result).get(i);
                    if (album.getData() != null) albumList.add(album);
                }
            }
            setItemCount(albumList.size());
        }
    }


    public MainFragAlbumAdapter(int maxSize) {
        this.maxSize = maxSize;
    }

    public void update() {
        DataBaseManager.getManager().executeCustomTask(JavaUtils.getMethod(MySongDAO.class, "loadAllAlbums"), "albums", true, this);
    }

    @Override
    protected AlbumHolder initiateHolder(View view, int type) {
        return new AlbumHolder(view, type);
    }

    @Override
    protected String getHeaderText() {
        return "Albums";
    }

    @Override
    protected void onFootClick() {
        //TODO:implement...
    }

    @Override
    protected void onHeaderClick() {

    }


    public class AlbumHolder extends MainFragBaseRecycler_adapter.BaseHolder {

        private String imageData;
        private DataBaseAlbum album;

        AlbumHolder(@NonNull View itemView, int type) {
            super(itemView, type);
        }


        @Override
        protected void onClick() {
            CustomFragmentSwipeBackAnimator.getINSTANCE().addFragment(NewAlbumFrag.newInstance(album.getId(), album.getData(), album.getName()));
        }

        @Override
        protected void onBind() {
            album = albumList.get(getAdapterPosition() - 1);
            imageData = album.getData();
            String name = album.getName();
            textView.setText(name);

            new AlbumArtTaskForAlbums(itemView.getContext(), imageData, 0) {
                @Override
                protected void onFinish(Bitmap bitmap, String data) {
                    if (Objects.equals(imageData, data))
                        imageView.setImageBitmap(bitmap);
                }
            };
        }
    }

}
