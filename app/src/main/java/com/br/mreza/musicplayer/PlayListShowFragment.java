package com.br.mreza.musicplayer;


import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.br.mreza.musicplayer.MBN.customViews.MbnRecyclerDecor;
import com.br.mreza.musicplayer.newmodel.adapters.songs.MbnSelectableRecyclerAdapter;
import com.br.mreza.musicplayer.newmodel.adapters.songs.PlayListSongsAdapter;
import com.br.mreza.musicplayer.newmodel.adapters.songs.RecentlyAddedAdapter;

import java.util.ArrayList;

public class PlayListShowFragment extends BottomSheetDialogFragment {


    RecyclerView recyclerView;

    MbnSelectableRecyclerAdapter recyclerAdapter;

    TextView title;

    private ArrayList<DataBasePlayList> playLists;

    private DataBasePlayList currentPlaylist;


    public static PlayListShowFragment newInstance(int type, int id) {


        Bundle args = new Bundle();

        args.putInt("TYPE", type);


        if (type == 2) {

            args.putInt("ID", id);

        }


        PlayListShowFragment fragment = new PlayListShowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.playlist_show_bottom_layout, container, false);
    }


    private void makeList(View view) {

        recyclerView = view.findViewById(R.id.playlist_show_songs);

        title = view.findViewById(R.id.playlist_name_text);

        switch (getArguments().getInt("TYPE")) {

            case 0:
                makeRecent();
                break;
            case 1:
                break;
            case 2:
                makePlayList();
                break;


        }

    }

    private void makePlayList() {
        playLists = (ArrayList<DataBasePlayList>) DataBaseHolder.getPlayLists(getContext());
        findCurrentPlaylist(getArguments().getInt("ID"));
        title.setText(getName());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        recyclerAdapter = new PlayListSongsAdapter(getContext(), recyclerView, currentPlaylist);

        recyclerView.addItemDecoration(new MbnRecyclerDecor(getContext()));

        recyclerView.setAdapter(recyclerAdapter);

//
//        recyclerAdapter = new CurrentQueuePageAdapter(getContext());

    }

    private void makeRecent() {

        title.setText("Recently Added");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        recyclerAdapter = new RecentlyAddedAdapter(getContext(), recyclerView);

        recyclerView.addItemDecoration(new MbnRecyclerDecor(getContext()));
//
//        recyclerAdapter = new CurrentQueuePageAdapter(getContext());

        recyclerView.setAdapter(recyclerAdapter);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        makeList(view);
        makeButtons(view);
    }

    private void makeButtons(View view) {

        ImageButton cancelSelect = view.findViewById(R.id.cancel_select);
        cancelSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerAdapter.cancelSelect();
            }
        });


        ImageButton addToPlaylist = view.findViewById(R.id.add_to_playlist);
        addToPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNew_AddTo_RemoveFrom_Playlist_Fragment.newInstance(recyclerAdapter.getSelectedSongs(), true).show(getFragmentManager(), "newPlaylist");
            }
        });

    }

    private void findCurrentPlaylist(int id) {

        for (DataBasePlayList pl : playLists) {

            if (pl.getId() == id) {
                currentPlaylist = pl;
                return;
            }
        }

    }

    private String getName() {

        return currentPlaylist.getName();
    }

}
