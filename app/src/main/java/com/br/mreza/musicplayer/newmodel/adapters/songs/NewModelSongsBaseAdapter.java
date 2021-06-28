package com.br.mreza.musicplayer.newmodel.adapters.songs;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.br.mreza.musicplayer.DataSong;
import com.br.mreza.musicplayer.newdesign.NewSongOptionDialog_NewMenuApi;
import com.br.mreza.musicplayer.newmodel.database.DataBaseManager;
import com.br.mreza.musicplayer.newmodel.database.SelfShrinkMap;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.ArrayList;

import mbn.libs.backgroundtask.BaseTaskHolder;
import mbn.libs.fragmanager.CustomFragmentSwipeBackAnimator;


public abstract class NewModelSongsBaseAdapter<H extends NewModelSongsBaseAdapter.NewModelHolder> extends MbnSelectableRecyclerAdapter<Long, H> {

//    private static String TAG = "ADAPTER";

    private ArrayList<Long> songsIDs = new ArrayList<>();
    private SelfShrinkMap<DataSong> songsMap = new SelfShrinkMap<>();
    private ArrayList<NewModelHolder> holders = new ArrayList<>();
    private BaseTaskHolder.ResultReceiver songReceiver = new BaseTaskHolder.ResultReceiver() {
        @Override
        public void onResult(Object result, Object info) {
            putSongInMap((DataSong) result);
        }
    };

    @Override
    public int getItemCount() {
        return songsIDs.size();
    }

    public void setSongsIDs(ArrayList<Long> iDs) {
        songsIDs.clear();
        songsIDs.addAll(iDs);
        notifyDataSetChanged();
    }

    public long getIdForPosition(int position) {
        return songsIDs.get(position);
    }

    private void putSongInMap(DataSong song) {
        if (song != null && song instanceof DataSong) {
            long songId = song.getId();
            songsMap.put(songId, song);
            notifyItemChanged(songsIDs.indexOf(songId));
        }
    }

    public void setTrackAsCurrentTrack(long id) {
        DataBaseManager.getManager().setCurrentTrack(id);
    }

    public void changeCurrentQueueAndTrack(long id) {
        DataBaseManager.getManager().changeTrackAndQueue(new ArrayList<>(songsIDs), id);
    }

    public abstract boolean changeTheQueue();

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public int getPositionForID(long id) {
        if (songsIDs.contains(id)) {
            return songsIDs.indexOf(id);
        } else return 0;
    }

    private DataSong getDataForID(long id) {
        DataSong song = songsMap.get(id);
        if (song == null) {
            DataBaseManager.getManager().getSong(id, songReceiver);
        }
        return song;
    }

    @Override
    public void onSelectionStarted() {
        changeOptionVisibility(true);
    }

    @Override
    public void onSelectionCanceled() {
        notifyHolders();
        changeOptionVisibility(false);
    }

    protected ArrayList<NewModelHolder> getHolders() {
        return holders;
    }

    private void notifyHolders() {
        for (NewModelHolder h : holders) {
            try {
                h.checkSelect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public abstract void changeOptionVisibility(boolean state);

    @Override
    public DataSong getSelected() {
        return null;
    }

    public abstract class NewModelHolder extends MbnSelectableRecyclerAdapter.Holder {

        protected long currentID;
        protected DataSong currentSong;


        public NewModelHolder(View itemView) {
            super(itemView);
            holders.add(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isInSelectionMode()) {
                        if (isSelected(currentID))
                            removeFromSelected(currentID);
                        else addToSelect(currentID);
                    } else if (changeTheQueue()) {
                        changeCurrentQueueAndTrack(currentID);
                    } else {
                        setTrackAsCurrentTrack(currentID);
                    }
                    checkSelect();
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    addToSelect(currentID);
                    checkSelect();
                    return true;
                }
            });

        }

        @Override
        protected void onBind() {
            currentID = getIdForPosition(getAdapterPosition());
            currentSong = getDataForID(currentID);
            if (currentSong == null) {
                itemView.setVisibility(View.INVISIBLE);
            } else {
                itemView.setVisibility(View.VISIBLE);
                checkSelect();
                onBindForSubclasses();
            }
        }

        protected abstract void onBindForSubclasses();

        protected void onOptionClicked() {
            CustomFragmentSwipeBackAnimator.getINSTANCE().addFragment(NewSongOptionDialog_NewMenuApi.newInstance(currentID, false));
        }

    }

}
