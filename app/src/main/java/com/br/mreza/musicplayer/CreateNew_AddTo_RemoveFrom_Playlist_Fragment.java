package com.br.mreza.musicplayer;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class CreateNew_AddTo_RemoveFrom_Playlist_Fragment extends BottomSheetDialogFragment {


    private static final String SONGS_IDS = "song_isd";
    private static final String ADD_REMOVE = "add_remove";

    AppCompatEditText playlistNameEditText;
    AppCompatImageButton addPlaylist;
    RecyclerView recyclerViewPlayLists;

    long[] songsIDS;
    private boolean addOrRemove;

    ArrayList<Long> songsIdsArrayList;

    public static CreateNew_AddTo_RemoveFrom_Playlist_Fragment newInstance(List<Long> songs, boolean addOrRemove) {
        Bundle args = new Bundle();
        if (songs != null) {
            long[] ids = new long[songs.size()];
            for (int i = 0; i < songs.size(); i++) {
                ids[i] = songs.get(i);
            }
            args.putLongArray(SONGS_IDS, ids);
        }

        args.putBoolean(ADD_REMOVE, addOrRemove);
        CreateNew_AddTo_RemoveFrom_Playlist_Fragment fragment = new CreateNew_AddTo_RemoveFrom_Playlist_Fragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_playlist_fragment_layout, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findWidgets();

    }

    @SuppressWarnings("ConstantConditions")
    private void findWidgets() {
        addOrRemove = getArguments().getBoolean(ADD_REMOVE, true);
        playlistNameEditText = getView().findViewById(R.id.create_new_plalist_name_edittext);
        addPlaylist = getView().findViewById(R.id.create_playlist_button);
        if (!addOrRemove) {
            playlistNameEditText.setVisibility(View.GONE);
            addPlaylist.setVisibility(View.GONE);
        }
        songsIDS = getArguments().getLongArray(SONGS_IDS);
        if (songsIDS.length > 0) {
            songsIdsArrayList = new ArrayList<>();
            for (long id : songsIDS) {
                songsIdsArrayList.add(id);
            }
            getView().findViewById(R.id.playlist_header).setVisibility(View.VISIBLE);
            makeList();
        }
        prepareAddButton();
    }

    private void prepareAddButton() {

        addPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playlistNameEditText.getText().toString().length() > 0) {
                    int res = DataBaseHolder.addToPlayLists(getContext(), songsIdsArrayList, playlistNameEditText.getText().toString());
                    if (res == DataBaseHolder.PLAY_LIST_ADDED) {
                        Toast.makeText(getContext(), "Playlist successfully created.", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "The name already exists.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please enter a name.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void makeList() {
        //noinspection ConstantConditions
        recyclerViewPlayLists = getView().findViewById(R.id.playlists_exist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewPlayLists.setHasFixedSize(true);
        recyclerViewPlayLists.setLayoutManager(layoutManager);
        recyclerViewPlayLists.setAdapter(new SelectPlaylistAdapter());

    }


    class SelectPlaylistAdapter extends RecyclerView.Adapter<Holder> {


        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new Holder(inflater.inflate(R.layout.select_playlist_simple_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.onBind();
        }

        @Override
        public int getItemCount() {
            return DataBaseHolder.getPlayLists(getContext()).size();
        }
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView nameText;

        Holder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.playlist_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addOrRemove) {
                        DataBaseHolder.getPlayLists(getContext()).get(getAdapterPosition()).addSongsWithIDs(songsIdsArrayList, getContext());
                        Toast.makeText(getContext(), "Songs successfully added to playlist.", Toast.LENGTH_SHORT).show();
                    } else {
                        DataBaseHolder.getPlayLists(getContext()).get(getAdapterPosition()).removeSongsWithIDs(songsIdsArrayList, getContext());
                        Toast.makeText(getContext(), "Songs successfully removed from playlist.", Toast.LENGTH_SHORT).show();
                    }
                    dismiss();
                }
            });
        }


        void onBind() {
            nameText.setText(DataBaseHolder.getPlayLists(getContext()).get(getAdapterPosition()).getName());
        }

    }

}
