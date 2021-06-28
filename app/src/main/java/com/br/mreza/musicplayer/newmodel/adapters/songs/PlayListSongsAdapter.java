package com.br.mreza.musicplayer.newmodel.adapters.songs;


import android.content.Context;
import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.mreza.musicplayer.AlbumArtTaskForSongs;
import com.br.mreza.musicplayer.DataBasePlayList;
import com.br.mreza.musicplayer.MBN.customViews.MbnRoundCheckBoxLite_square;
import com.br.mreza.musicplayer.MbnController;
import com.br.mreza.musicplayer.R;

import static androidx.recyclerview.widget.RecyclerView.NO_POSITION;

public class PlayListSongsAdapter extends SelectableAdapterWithSwipe<PlayListSongsAdapter.Holder> {


    private DataBasePlayList playList;


    public PlayListSongsAdapter(Context context, RecyclerView recyclerView, DataBasePlayList playList) {
        super(context, recyclerView);
        this.playList = playList;
        setFlags(SELECT_FLAG | DELETE_FLAG);
        update();
    }


    @Override
    public int getState(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.itemView.findViewById(R.id.all_list_root_swipe_item).getTranslationX() == 0) {
            return STATE_IDLE;
        }
        float diff = viewHolder.itemView.findViewById(R.id.all_list_root_swipe_item).getTranslationX() - (85 * getDensity());

        if (diff > -5 && diff < 5) {
            return STATE_SELECTED;
        }
        return STATE_READY_FOR_DEL;
    }


    @Override
    public View setCurrentView(RecyclerView.ViewHolder viewHolder) {


        return viewHolder.itemView.findViewById(R.id.all_list_root_swipe_item);
    }


    @Override
    public void addToSelectedList(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getAdapterPosition() != NO_POSITION) {

            addToSelect(getIdForPosition(viewHolder.getAdapterPosition()));

            MbnRoundCheckBoxLite_square checkBoxLite_square = viewHolder.itemView.findViewById(R.id.list_item_selected_box);
            checkBoxLite_square.setChecked(true);
        }
    }


    @Override
    public void removeFromSelectedList(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getAdapterPosition() != NO_POSITION) {
            removeFromSelected(getIdForPosition(viewHolder.getAdapterPosition()));
            MbnRoundCheckBoxLite_square checkBoxLite_square = viewHolder.itemView.findViewById(R.id.list_item_selected_box);
            checkBoxLite_square.setChecked(false);
        }
    }

    private void update() {

//        Comparator<DataSong> comparator = new Comparator<DataSong>() {
//            @Override
//            public int compare(DataSong o1, DataSong o2) {
//
//                if (o1.getDateAdded() > o2.getDateAdded()) {
//
//                    return -1;
//
//                } else if (o1.getDateAdded() < o2.getDateAdded()) {
//
//                    return 1;
//                }
//
//                return 0;
//            }
//        };
//
//        list.addAll(DataBaseHolder.getAllSongs(getContext()));
//
//        Collections.sort(list, comparator);

        setSongsIDs(playList.getSongIDs(getContext()));


//        for (DataSong s : list) {
//
//            Log.i("songy", s.getTitle());
//
//        }


    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());


        return new Holder(inflater.inflate(R.layout.music_list_item_for_base_select_botton, parent, false));
    }

    @Override
    public boolean changeTheQueue() {
        return true;
    }

    @Override
    public void changeOptionVisibility(boolean state) {

    }


    class Holder extends NewModelSongsBaseAdapter.NewModelHolder {

        private TextView songName;
        private TextView artist;
        private TextView extra;
        private CheckBox option;
        private ImageView art;
        private View whole;
        private Button deleteButton;


        public Holder(final View itemView) {
            super(itemView);

            whole = itemView.findViewById(R.id.all_list_root_swipe_item);
            songName = itemView.findViewById(R.id.allsongs_list_item_name);
            artist = itemView.findViewById(R.id.allsongs_list_item_artist);
            extra = itemView.findViewById(R.id.allsongs_list_item_extra);
            option = itemView.findViewById(R.id.allsongs_list_item_option);
            art = itemView.findViewById(R.id.art_for_list);
            deleteButton = itemView.findViewById(R.id.delete_button);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playList.removeSongWithID(currentID, getContext());
                    itemView.animate().translationX(-300 * getDensity()).alpha(0f).setDuration(500);
                    notifyItemRemoved(getAdapterPosition());

                }
            });

//            option.setClipToOutline(true);


        }


        @Override
        protected void onBindForSubclasses() {
            new AlbumArtTaskForSongs(getContext(), currentSong.getPath(), (int) currentID) {
                @Override
                public void onFinish(Bitmap bitmap, String data) {

                    if (currentSong != null && data.equals(currentSong.getPath())) {

                        art.setImageBitmap(bitmap);
                        art.setVisibility(View.VISIBLE);

                    }

                }
            };

//            loaderTask.set(list.get(getAdapterPosition()).getPath());

            songName.setText(currentSong.getTitle());
//            songName.setText(allSongsMap.get(songsCodesList.get(getAdapterPosition())).getTitle());
            artist.setText(currentSong.getArtistTitle());
//            artist.setText(allSongsMap.get(songsCodesList.get(getAdapterPosition())).getArtistTitle());
            extra.setText(MbnController.makeMillisToTime(currentSong.getDuration()));

            if (isSelected(currentID)) {

                option.setChecked(true);
                whole.setTranslationX(85 * getDensity());
                whole.setElevation(5 * getDensity());
            } else {
                option.setChecked(false);
            }
        }

        @Override
        public void checkSelect() {

            if (isSelected(currentID)) {

                option.setChecked(true);
                whole.setTranslationX(85 * getDensity());
                whole.setElevation(5 * getDensity());


            } else {
                option.setChecked(false);
                whole.animate().translationX(0).setDuration(500).setInterpolator(new OvershootInterpolator());
                whole.setElevation(0);
            }

        }


        @Override
        protected void unBind() {
            art.setImageBitmap(null);
            option.setChecked(false);
            whole.setTranslationX(0);
            whole.setElevation(0);

        }

    }


}
