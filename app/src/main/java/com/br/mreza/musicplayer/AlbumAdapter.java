//package com.br.mreza.musicplayer;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//import static com.br.mreza.musicplayer.ListMaker.allSongsMap;
//
//
//class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.SongHolder> {
//
//
//    private OnMbnListClick listClick;
//
//
//    private ArrayList<String> songs;
//
//    AlbumAdapter(MbnAlbum album, OnMbnListClick listClick) {
//
//        songs = new ArrayList<>();
//
//
//        songs.addAll(album.getSongsCodes());
//
//        this.listClick = listClick;
//    }
//
//
//
//    @Override
//    public SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//
//        Context context = parent.getContext();
//        int layoutId = R.layout.list_layout_recycle;
//        LayoutInflater inflater = LayoutInflater.from(context);
//
//        View view = inflater.inflate(layoutId, parent, false);
//
//
//        return new SongHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(SongHolder holder, int position) {
//
//        boolean divider = false;
//        if (position != songs.size() - 1) {
//
//            divider = true;
//        }
//
//        holder.bind(position, divider);
//    }
//
//    @Override
//    public int getItemCount() {
//        return songs.size();
//    }
//
//
//    class SongHolder extends RecyclerView.ViewHolder {
//
//        TextView songPos;
//        TextView songTitle;
//        View dividerView;
//
//        SongHolder(View itemView) {
//            super(itemView);
//
//            songPos = itemView.findViewById(R.id.song_pos_tv);
//            songTitle = itemView.findViewById(R.id.song_title_tv);
//            dividerView = itemView.findViewById(R.id.list_divider);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(final View v) {
//
//                    listClick.onClick(songs.get(getAdapterPosition()), v);
//
//                }
//            });
//
//
//        }
//
//
//        void bind(int pos, boolean divider) {
//
//            songPos.setText(Integer.toString(pos + 1));
//
////            String title = mbnSongs.get(getAdapterPosition()).getTitle();
//            String title = allSongsMap.get(songs.get(getAdapterPosition())).getTitle();
//
//            if (title.length() > 30) {
//
//                title = title.substring(0, 25) + "...";
//            }
//
//
//            songTitle.setText(title);
//
//            if (divider) {
//                dividerView.setVisibility(View.VISIBLE);
//            } else {
//
//                dividerView.setVisibility(View.INVISIBLE);
//            }
//
//
//        }
//
//
//    }
//
//
//    interface OnMbnListClick {
//
//        void onClick(String Code, View v);
//
//        void onOptionClick(int position, View v);
//
//
//    }
//
//
//}
