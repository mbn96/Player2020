package com.br.mreza.musicplayer.newdesign;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.br.mreza.musicplayer.DataBaseArtists;
import com.br.mreza.musicplayer.DataBaseHolder;
import com.br.mreza.musicplayer.R;
import com.br.mreza.musicplayer.newmodel.search.adapters.SongsSearchAdapter;

import java.util.ArrayList;

import mbn.libs.fragmanager.BaseFragment;
import mbn.libs.fragmanager.CustomFragmentSwipeBackAnimator;
import mbn.libs.fragmanager.TouchEaterRecyclerView;


public class ArtistFragment extends BaseFragment {

    public static ArtistFragment newInstance(long artistID) {
        Bundle args = new Bundle();
        args.putLong("AR_ID", artistID);
        ArtistFragment fragment = new ArtistFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private long artistID;
    private DataBaseArtists artist;

    private TextView name;
    private TextView albs;
    private TextView trks;
    private TouchEaterRecyclerView recyclerView;
    private SongsSearchAdapter adapter;

    @Override
    public boolean hasAppBar() {
        return false;
    }

    @Override
    public int getAnimationMode() {
        return CustomFragmentSwipeBackAnimator.ANIM_BOTTOM_NORMAL;
    }

    @Override
    public View onCreateViewForChild(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.artist_page_layout, container, false);
    }

//    @Override
//    public View makeToolBar(LayoutInflater inflater) {
//        return null;
//    }

    @Override
    public void makeState(Bundle state) {

    }

    @Override
    public void checkForRestoreState() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        artistID = getArguments().getLong("AR_ID");
        artist = DataBaseHolder.getInstance(getContext()).getSongDAO().getArtist(artistID);

        name = view.findViewById(R.id.artist_name_item);
        albs = view.findViewById(R.id.artist_albs_item);
        trks = view.findViewById(R.id.artist_trks_item);

        name.setText(artist.getName());
        albs.setText(Integer.toString(artist.getNumberOfAlbums()));
        trks.setText(Integer.toString(artist.getNumberOfTracks()));

        recyclerView = view.findViewById(R.id.artist_page_songs_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SongsSearchAdapter(getContext());
        recyclerView.setAdapter(adapter);
        adapter.setSongsIDs((ArrayList<Long>) DataBaseHolder.getInstance(getContext()).getSongDAO().loadAllSongsIdsForArtist(artistID));
    }
}
