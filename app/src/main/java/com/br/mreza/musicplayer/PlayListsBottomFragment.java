package com.br.mreza.musicplayer;


import android.os.Bundle;

import androidx.annotation.Nullable;

import com.br.mreza.musicplayer.p2020.design.BottomSheetPlaylistFrag2020;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.br.mreza.musicplayer.newdesign.newmodelfrags.PlayListShowFragmentNew;

import mbn.libs.fragmanager.CustomFragmentSwipeBackAnimator;


public class PlayListsBottomFragment extends BottomSheetDialogFragment {


    public static PlayListsBottomFragment newInstance() {

        Bundle args = new Bundle();

        PlayListsBottomFragment fragment = new PlayListsBottomFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.playlist_bottom_layout, container, false);
    }

    private View.OnClickListener buttonsListener = v -> {
        switch (v.getId()) {
            case R.id.playlist_recent:
                CustomFragmentSwipeBackAnimator.getINSTANCE().addFragment(BottomSheetPlaylistFrag2020.newInstance(0, 0));
                break;
            case R.id.playlist_mostplayed:
                CustomFragmentSwipeBackAnimator.getINSTANCE().addFragment(BottomSheetPlaylistFrag2020.newInstance(3, 0));
                break;
            case R.id.playlist_recently_played:
                CustomFragmentSwipeBackAnimator.getINSTANCE().addFragment(BottomSheetPlaylistFrag2020.newInstance(1, 0));
                break;
        }
        dismiss();
    };

    private void buttons(View view) {

        Button recent = view.findViewById(R.id.playlist_recent);
        Button mostPlayed = view.findViewById(R.id.playlist_mostplayed);
        Button recentlyPlayed = view.findViewById(R.id.playlist_recently_played);

        recent.setOnClickListener(buttonsListener);
        mostPlayed.setOnClickListener(buttonsListener);
        recentlyPlayed.setOnClickListener(buttonsListener);

//        recent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CustomFragmentSwipeBackAnimator.getINSTANCE().addFragment(PlayListShowFragmentNew.newInstance(0, 0));
//                dismiss();
//            }
//        });
//
//
//        mostPlayed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CustomFragmentSwipeBackAnimator.getINSTANCE().addFragment(PlayListShowFragmentNew.newInstance(3, 0));
//
//            }
//        });

    }

    private void playLists() {
        //noinspection ConstantConditions
        recyclerView = getView().findViewById(R.id.playlist_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new SelectPlaylistAdapter());


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttons(view);
        playLists();

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

        public Holder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.playlist_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomFragmentSwipeBackAnimator.getINSTANCE().addFragment(BottomSheetPlaylistFrag2020.newInstance(2, DataBaseHolder.getPlayLists(getContext()).get(getAdapterPosition()).getId()));
                    dismiss();
                }
            });
        }


        void onBind() {
            nameText.setText(DataBaseHolder.getPlayLists(getContext()).get(getAdapterPosition()).getName());
        }

    }
}
