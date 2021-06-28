package com.br.mreza.musicplayer.newmodel.adapters.songs;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.mreza.musicplayer.AlbumArtTaskForSongs;
import com.br.mreza.musicplayer.CreateNew_AddTo_RemoveFrom_Playlist_Fragment;
import com.br.mreza.musicplayer.MbnController;
import com.br.mreza.musicplayer.R;
import com.br.mreza.musicplayer.StorageUtils;

public class NewModelCurrentQueueAdapter extends NewModelSongsBaseAdapter<NewModelCurrentQueueAdapter.CurrentHolder> {


    private Context context;
    protected View optionView;
    private FragmentManager fragmentManager;
    private ItemTouchHelper touchHelper;
    private float density;
    private int titleColor = Color.BLACK, contextColor = Color.BLACK;

    public NewModelCurrentQueueAdapter(Context context, ItemTouchHelper touchHelper, View optionView, FragmentManager fragmentManager) {
        this.context = context;
        density = context.getResources().getDisplayMetrics().density;
        this.optionView = optionView;
        this.fragmentManager = fragmentManager;
        this.touchHelper = touchHelper;
        manageSelectionOptionView();
        changeOptionVisibility(false);
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public void setContextColor(int contextColor) {
        this.contextColor = contextColor;
    }

    protected void manageSelectionOptionView() {
        optionView.findViewById(R.id.cancle_select_playF).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelSelect();
            }
        });
        optionView.findViewById(R.id.addto_select_playF).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNew_AddTo_RemoveFrom_Playlist_Fragment.newInstance(getSelectedSongs(), true).show(fragmentManager, "addF");
                cancelSelect();
            }
        });
        optionView.findViewById(R.id.removefrom_select_playF).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNew_AddTo_RemoveFrom_Playlist_Fragment.newInstance(getSelectedSongs(), false).show(fragmentManager, "addF");
                cancelSelect();
            }
        });
    }


    @Override
    public boolean changeTheQueue() {
        return false;
    }

    @NonNull
    @Override
    public CurrentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layoutId = R.layout.all_music_list_item_for_current_queue;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, parent, false);

        return new CurrentHolder(view);
    }


    @Override
    public void changeOptionVisibility(boolean state) {
        if (state) optionView.setVisibility(View.VISIBLE);
        else optionView.setVisibility(View.INVISIBLE);
    }


    public class CurrentHolder extends NewModelSongsBaseAdapter.NewModelHolder {


        private TextView songName;
        private TextView artist;
        private TextView extra;
        private ImageView option;
        private ImageView drag;
        private ImageView art;
        private View whole;
        ColorStateList stateList;

        public CurrentHolder(View itemView) {
            super(itemView);
            whole = itemView;
            songName = itemView.findViewById(R.id.allsongs_list_item_name);
            artist = itemView.findViewById(R.id.allsongs_list_item_artist);
            extra = itemView.findViewById(R.id.allsongs_list_item_extra);
            option = itemView.findViewById(R.id.allsongs_list_item_option);
            drag = itemView.findViewById(R.id.allsongs_list_item_drag);
            art = itemView.findViewById(R.id.art_image_view);

            if (touchHelper != null) {
                drag.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        touchHelper.startDrag(CurrentHolder.this);
                        return false;
                    }
                });
            } else {
                drag.setVisibility(View.GONE);
            }

            option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptionClicked();
                }
            });

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

            songName.setTextColor(titleColor);
            artist.setTextColor(contextColor);
            extra.setTextColor(contextColor);

            if (stateList == null || stateList.getDefaultColor() != contextColor) {
                stateList = ColorStateList.valueOf(contextColor);
                option.setImageTintList(stateList);
            }
        }

        @Override
        protected void unBind() {

        }

        @Override
        protected void checkSelect() {
            if (isSelected(currentID))
                whole.setBackgroundColor(Color.argb(145, 200, 200, 200));
            else if (StorageUtils.getThemeType() == 0) {
                whole.setBackground(null);
            } else {
                whole.setBackground(null);
            }
        }
    }
}
