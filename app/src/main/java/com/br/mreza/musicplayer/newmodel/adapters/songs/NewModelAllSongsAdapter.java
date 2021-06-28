package com.br.mreza.musicplayer.newmodel.adapters.songs;


import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.mreza.musicplayer.AlbumArtTaskForSongs;
import com.br.mreza.musicplayer.DataBaseHolder;
import com.br.mreza.musicplayer.MbnController;
import com.br.mreza.musicplayer.R;

import java.util.ArrayList;

public class NewModelAllSongsAdapter extends NewModelSongsBaseAdapter<NewModelAllSongsAdapter.AllSongsHolder> {

    private Context context;

    public NewModelAllSongsAdapter(Context context) {
        this.context = context;
        update();
    }

    public void update() {
        setSongsIDs((ArrayList<Long>) DataBaseHolder.getInstance(context).getSongDAO().loadAllSongsIdsOrderedByName());
    }

    @Override
    public boolean changeTheQueue() {
        return true;
    }

    @Override
    public void changeOptionVisibility(boolean state) {

    }

    @NonNull
    @Override
    public AllSongsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.all_music_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, parent, false);
        return new AllSongsHolder(view);
    }

    public class AllSongsHolder extends NewModelSongsBaseAdapter.NewModelHolder {
        private TextView songName;
        private TextView artist;
        private TextView extra;
        private ImageView option;
        private ImageView art;
        private String mData;

        public AllSongsHolder(View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.allsongs_list_item_name);
            artist = itemView.findViewById(R.id.allsongs_list_item_artist);
            extra = itemView.findViewById(R.id.allsongs_list_item_extra);
            option = itemView.findViewById(R.id.allsongs_list_item_option);
            art = itemView.findViewById(R.id.art_for_list);
            option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onOptionClicked();
                }
            });
        }


        @Override
        protected void onBindForSubclasses() {
            mData = currentSong.getPath();
            new AlbumArtTaskForSongs(itemView.getContext(), mData, (int) currentID) {
                @Override
                public void onFinish(Bitmap bitmap, String data) {
                    if (currentSong != null && data.equals(mData)) {
                        art.setImageBitmap(bitmap);
                    }
                }
            };

            songName.setText(currentSong.getTitle());
            artist.setText(currentSong.getArtistTitle());
            extra.setText(MbnController.makeMillisToTime(currentSong.getDuration()));
        }


        @Override
        protected void unBind() {
            art.setImageBitmap(null);
            mData = "";
        }

        @Override
        protected void checkSelect() {

        }
    }
}
