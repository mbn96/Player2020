package com.br.mreza.musicplayer.newmodel.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.br.mreza.musicplayer.DataBaseArtists;
import com.br.mreza.musicplayer.DataBaseHolder;
import com.br.mreza.musicplayer.R;
import com.br.mreza.musicplayer.newdesign.ArtistFragment;

import java.util.ArrayList;

import mbn.libs.fragmanager.CustomFragmentSwipeBackAnimator;


public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ArtistHolder> {

    private Context context;
    protected ArrayList<DataBaseArtists> dataBaseArtists = new ArrayList<>();

    public ArtistsAdapter(Context context) {
        this.context = context;
        update();
    }

    protected void update() {
        dataBaseArtists.clear();
        dataBaseArtists.addAll(DataBaseHolder.getInstance(context).getSongDAO().loadAllArtists());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArtistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.artist_list_item_layout, parent, false);
        return new ArtistHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistHolder holder, int position) {
        holder.onBind();
    }

    @Override
    public int getItemCount() {
        return dataBaseArtists.size();
    }

    class ArtistHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView albs;
        private TextView trks;

        ArtistHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.artist_name_item);
            albs = itemView.findViewById(R.id.artist_albs_item);
            trks = itemView.findViewById(R.id.artist_trks_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomFragmentSwipeBackAnimator.getINSTANCE().addFragment(ArtistFragment.newInstance(dataBaseArtists.get(getAdapterPosition()).getId()));
                }
            });
        }


        @SuppressLint("SetTextI18n")
        private void onBind() {
            DataBaseArtists artist = dataBaseArtists.get(getAdapterPosition());
            name.setText(artist.getName());
            albs.setText(Integer.toString(artist.getNumberOfAlbums()));
            trks.setText(Integer.toString(artist.getNumberOfTracks()));
        }

    }

}
