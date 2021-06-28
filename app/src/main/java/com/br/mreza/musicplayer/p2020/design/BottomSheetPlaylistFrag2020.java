package com.br.mreza.musicplayer.p2020.design;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.br.mreza.musicplayer.CreateNew_AddTo_RemoveFrom_Playlist_Fragment;
import com.br.mreza.musicplayer.DataBaseHolder;
import com.br.mreza.musicplayer.DataBasePlayList;
import com.br.mreza.musicplayer.MBN.customViews.MbnRecyclerDecor;
import com.br.mreza.musicplayer.R;
import com.br.mreza.musicplayer.newmodel.adapters.songs.MbnSelectableRecyclerAdapter;
import com.br.mreza.musicplayer.newmodel.adapters.songs.Most_Recently_PlayedAdaptor;
import com.br.mreza.musicplayer.newmodel.adapters.songs.PlayListSongsAdapter;
import com.br.mreza.musicplayer.newmodel.adapters.songs.RecentlyAddedAdapter;

import java.util.ArrayList;

import mbn.libs.fragmanager.BaseBottomSheetDialog;
import mbn.libs.fragmanager.BaseFragment;
import mbn.libs.fragmanager.CustomAppBar;
import mbn.libs.imagelibs.imageworks.MbnUtils;

import static mbn.libs.fragmanager.CustomFragmentSwipeBackAnimator.ANIM_BOTTOM_NORMAL;


public class BottomSheetPlaylistFrag2020 extends BaseBottomSheetDialog {

    private RecyclerView recyclerView;
    private MbnSelectableRecyclerAdapter recyclerAdapter;
    private TextView title;
    private ArrayList<DataBasePlayList> playLists;
    private DataBasePlayList currentPlaylist;
//    private ViewGroup appBar;


//    @Override
//    public boolean canInterceptTouches() {
//        return !recyclerView.canScrollVertically(-1);
//    }

//    @Override
//    public int getAnimationMode() {
//        return ANIM_BOTTOM_NORMAL;
//    }

    public static BottomSheetPlaylistFrag2020 newInstance(int type, int id) {


        Bundle args = new Bundle();

        args.putInt("TYPE", type);


        if (type == 2) {

            args.putInt("ID", id);

        }


        BottomSheetPlaylistFrag2020 fragment = new BottomSheetPlaylistFrag2020();
        fragment.setArguments(args);
        return fragment;
    }


    private void makeList(View view) {

        recyclerView = view.findViewById(R.id.playlist_show_songs);

        title = view.findViewById(R.id.playlist_name_text);

        switch (getArguments().getInt("TYPE")) {

            case 0:
                makeRecent();
                break;
            case 1:
                makeRecentlyPlayed();
                break;
            case 3:
                makeMostPlayed();
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        recyclerAdapter = new RecentlyAddedAdapter(getContext(), recyclerView);

        recyclerView.addItemDecoration(new MbnRecyclerDecor(getContext()));
//
//        recyclerAdapter = new CurrentQueuePageAdapter(getContext());

        recyclerView.setAdapter(recyclerAdapter);


    }

    private void makeRecentlyPlayed() {
        title.setText("Recently Played");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        recyclerAdapter = new Most_Recently_PlayedAdaptor(getContext(), recyclerView, 0);

        recyclerView.addItemDecoration(new MbnRecyclerDecor(getContext()));
//
//        recyclerAdapter = new CurrentQueuePageAdapter(getContext());

        recyclerView.setAdapter(recyclerAdapter);
    }

    private void makeMostPlayed() {
        title.setText("Most Played");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        recyclerAdapter = new Most_Recently_PlayedAdaptor(getContext(), recyclerView, 1);

        recyclerView.addItemDecoration(new MbnRecyclerDecor(getContext()));
//
//        recyclerAdapter = new CurrentQueuePageAdapter(getContext());

        recyclerView.setAdapter(recyclerAdapter);
    }

//    @Override
//    public View onCreateViewForChild(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.playlist_show_bottom_layout, container, false);
//    }

//    @Override
//    public View makeToolBar(LayoutInflater inflater) {
//        return null;
//    }

    @Override
    protected View getBottomLayout(ViewGroup c) {
        setCanStartAnim(false);
        ViewGroup page = (ViewGroup) getLayoutInflater().inflate(R.layout.playlist_show_bottom_layout, getDialogBase(), false);
        makeList(page);
        makeButtons(page);
        getStatusBarExtender().setAccentColor(Color.WHITE);
        getView().postDelayed(() -> setCanStartAnim(true), 100);
        return page;
    }

    @Override
    public void makeState(Bundle state) {

    }

    @Override
    public void checkForRestoreState() {

    }

//    @Override
//    public boolean hasAppBar() {
//        return true;
//    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        setToolbar(getLayoutInflater().inflate(R.layout.play_list_appbar_layout, getToolbarHolder().getBaseLayout(), false));
//        setToolbarSize(1, CustomAppBar.UNIT_VIEW_SIZE);
//        setToolbarScrollingSize(50);
//        setToolBarBackgroundColor(MbnUtils.colorWhitener2(Color.BLUE, 5));
//        setToolbarElevation(getDisplayDensity() * 5);
//        makeList(view);
//        makeButtons(getToolbar());
//    }

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
