package com.br.mreza.musicplayer.newmodel.search.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.mreza.musicplayer.AlbumArtTaskForSongs;
import com.br.mreza.musicplayer.MbnController;
import com.br.mreza.musicplayer.R;
import com.br.mreza.musicplayer.newmodel.adapters.songs.NewModelSongsBaseAdapter;


public class SongsSearchAdapter extends NewModelSongsBaseAdapter<SongsSearchAdapter.SearchHolder> {

    private Context context;

    public SongsSearchAdapter(Context context) {
        this.context = context;
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
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.all_music_list_item_for_current_queue;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, parent, false);

        return new SearchHolder(view);
    }


    public class SearchHolder extends NewModelSongsBaseAdapter.NewModelHolder {
        private TextView songName;
        private TextView artist;
        private TextView extra;
        private ImageView option;
        private ImageView drag;
        private ImageView art;
        private View whole;

        public SearchHolder(View itemView) {
            super(itemView);
            whole = itemView;
            songName = itemView.findViewById(R.id.allsongs_list_item_name);
            artist = itemView.findViewById(R.id.allsongs_list_item_artist);
            extra = itemView.findViewById(R.id.allsongs_list_item_extra);
            option = itemView.findViewById(R.id.allsongs_list_item_option);
            drag = itemView.findViewById(R.id.allsongs_list_item_drag);
            art = itemView.findViewById(R.id.art_image_view);

            option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptionClicked();
                }
            });

        }

        @Override
        protected void unBind() {

        }

        @Override
        protected void checkSelect() {

        }

        @Override
        protected void onBindForSubclasses() {
            art.setVisibility(View.INVISIBLE);
            new AlbumArtTaskForSongs(context, currentSong.getPath(), (int) currentID) {
                @Override
                public void onFinish(Bitmap bitmap, String data) {
                    if (currentSong != null && data.equals(currentSong.getPath())) {
                        art.setImageBitmap(bitmap);
                        art.setVisibility(View.VISIBLE);
//                        art.setScaleX(0.35f);
//                        art.animate().scaleX(1).setDuration(300).setInterpolator(new OvershootInterpolator(3)).start();
                    }
                }
            };

            songName.setText(currentSong.getTitle());
            artist.setText(currentSong.getArtistTitle());
            extra.setText(MbnController.makeMillisToTime(currentSong.getDuration()));
        }
    }
}
