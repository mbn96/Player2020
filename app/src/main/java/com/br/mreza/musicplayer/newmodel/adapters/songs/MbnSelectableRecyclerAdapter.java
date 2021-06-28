package com.br.mreza.musicplayer.newmodel.adapters.songs;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.br.mreza.musicplayer.DataSong;

import java.util.ArrayList;

public abstract class MbnSelectableRecyclerAdapter<T, H extends MbnSelectableRecyclerAdapter.Holder> extends RecyclerView.Adapter<H> {

    private ArrayList<T> selectedSongs = new ArrayList<>();

    public void cancelSelect() {
        selectedSongs.clear();
        onSelectionCanceled();
    }

    public abstract void onSelectionCanceled();

    public abstract void onSelectionStarted();

    public boolean isInSelectionMode() {
        return !selectedSongs.isEmpty();
    }

    public boolean backPress() {
        if (isInSelectionMode()) {
            cancelSelect();
            return true;
        }
        return false;
    }

    public ArrayList<T> getSelectedSongs() {
        return selectedSongs;
    }

    public void addToSelect(T song) {
        if (!selectedSongs.contains(song)) {
            selectedSongs.add(song);
            if (selectedSongs.size() == 1) {
                onSelectionStarted();
            }
        }
    }

    public void removeFromSelected(T song) {
        if (selectedSongs.contains(song)) selectedSongs.remove(selectedSongs.indexOf(song));
        if (selectedSongs.isEmpty()) cancelSelect();
    }

    public boolean isSelected(T id) {
        return selectedSongs.contains(id);
    }

    public abstract DataSong getSelected();


    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.onBind();
    }

    @Override
    public void onViewRecycled(@NonNull Holder holder) {
        super.onViewRecycled((H) holder);
        holder.unBind();
    }

    public abstract static class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
        }


        protected abstract void onBind();

        protected abstract void unBind();

        protected abstract void checkSelect();

    }

}
