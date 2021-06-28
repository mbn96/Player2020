package com.br.mreza.musicplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class AlbumAdapterForFragment extends RecyclerView.Adapter<AlbumAdapterForFragment.SongHolderFragment> {


    private OnMbnListClick listClick;

    private Context contextForThis;

    long album;

    String albumName;
    Bitmap albumCover;

    private List<DataSong> songs;
    private List<DataSong> songsForSend;

    public AlbumAdapterForFragment(long album, OnMbnListClick listClick, Context context, String albumName, Bitmap albumCover) {

        songs = new ArrayList<>();
        songsForSend = new ArrayList<>();

        contextForThis = context;

        this.album = album;

        update();

        this.albumName = albumName;
        this.albumCover = albumCover;

        this.listClick = listClick;
    }


    public void update() {

        songs.clear();
        songsForSend.clear();

        songs.add(null);
        songs.add(null);
        songs.add(null);


        songs.addAll(DataBaseHolder.getInstance(contextForThis).getSongDAO().loadAlbum(album));
        songsForSend.addAll(DataBaseHolder.getInstance(contextForThis).getSongDAO().loadAlbum(album));


//        for (DataSong song : DataBaseHolder.getInstance(contextForThis).getSongDAO().loadAlbum(album)) {
//
//            if (song != null) {
//
////                System.out.println(song.getTitle()+"  sssssssssssssss "+song.getAlbumTitle());
//
//                if (song.getAlbumTitle().equals(album)) {
//
//                    songs.add(song);
//                }
//            }
//        }

        notifyDataSetChanged();

    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 3;
            default:
                return 2;
        }

//
//        if (position == 0) {
//
//            return 1;
//        }


//        return 2;
    }

    @NonNull
    @Override
    public SongHolderFragment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        switch (viewType) {
            case 2:
                Context context = parent.getContext();
                int layoutId = R.layout.list_layout_recycle;
//        if (viewType == 1) {
//            layoutId = R.layout.blank;
//        }
                LayoutInflater inflater = LayoutInflater.from(context);

                View view = inflater.inflate(layoutId, parent, false);


                return new SongHolderFragment(view, viewType);
            case 0:
                ImageView imageView = new ImageView(parent.getContext());
                imageView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400,
                                imageView.getResources().getDisplayMetrics())));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return new SongHolderFragment(imageView, viewType);
            case 3:
                View view1 = new View(parent.getContext());
                RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.5f, view1.getResources().getDisplayMetrics()));
//                layoutParams.setMargins(200, 0, 200, 0);
                view1.setLayoutParams(layoutParams);
                view1.setBackgroundColor(Color.LTGRAY);
                return new SongHolderFragment(view1, viewType);
            default:
                TextView textView = new TextView(parent.getContext());
                textView.setGravity(Gravity.CENTER);
                textView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setBackgroundColor(Color.WHITE);
                textView.setTextSize(18);
                textView.setPadding(20, (int) (24 * textView.getResources().getDisplayMetrics().density), 20, 20);
                return new SongHolderFragment(textView, viewType);
        }
    }

    @Override
    public void onBindViewHolder(SongHolderFragment holder, int position) {

        boolean divider = false;
        if (position != songs.size() - 1) {

            divider = true;
        }

        holder.bind(position, divider);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }


    class SongHolderFragment extends RecyclerView.ViewHolder {

        TextView songPos;
        TextView songTitle;
        View dividerView;

        SongHolderFragment(View itemView, int type) {
            super(itemView);

//            itemView.getParent().requestDisallowInterceptTouchEvent(true);

            switch (type) {
                case 0:
                    ((ImageView) itemView).setImageBitmap(albumCover);
                    break;
                case 1:
                    ((TextView) itemView).setText(albumName);
                    break;
                case 2:
                    songPos = itemView.findViewById(R.id.song_pos_tv);
                    songTitle = itemView.findViewById(R.id.song_title_tv);
                    dividerView = itemView.findViewById(R.id.list_divider);

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {

                            listClick.onClick(songsForSend, songs.get(getAdapterPosition()));

                        }
                    });

                    break;
            }

//            if (type == 2) {
////                songPos = itemView.findViewById(R.id.song_pos_tv);
////                songTitle = itemView.findViewById(R.id.song_title_tv);
////                dividerView = itemView.findViewById(R.id.list_divider);
////
////                itemView.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(final View v) {
////
////                        listClick.onClick(songsForSend, songs.get(getAdapterPosition()));
////
////                    }
////                });
////            }
//
//            }
        }


        void bind(int pos, boolean divider) {
            if (pos > 2) {
                songPos.setText(Integer.toString(pos - 2));

//            String title = mbnSongs.get(getAdapterPosition()).getTitle();
                String title = songs.get(getAdapterPosition()).getTitle();

                if (title.length() > 30) {

                    title = title.substring(0, 25) + "...";
                }


                songTitle.setText(title);

                if (divider) {
                    dividerView.setVisibility(View.VISIBLE);
                } else {

                    dividerView.setVisibility(View.INVISIBLE);
                }
            }


        }

    }


    public interface OnMbnListClick {

        void onClick(List<DataSong> album, DataSong track);

        void onOptionClick(int position, View v);


    }


    public static class ImageMover extends RecyclerView.ItemDecoration {


        Paint paint = new Paint();

        public ImageMover() {
            paint.setColor(Color.WHITE);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

            View child = parent.getChildAt(0);
            if (parent.getChildAdapterPosition(child) == 0) {
                child.setTranslationY(-child.getTop() / 3);
            }
            drawWhite(c, parent);

            super.onDraw(c, parent, state);
        }


        private void drawWhite(Canvas canvas, RecyclerView parent) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                if (isLast(parent.getChildAt(i), parent)) {
                    canvas.drawRect(0, parent.getChildAt(i).getBottom(), parent.getWidth(), parent.getHeight(), paint);
                }
            }

        }

        private boolean isLast(View view, RecyclerView parent) {
            return parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (isLast(view, parent)) {
                float size = parent.getResources().getDisplayMetrics().density * 340;

                int childCount = parent.getChildCount();
                int sum = 0;

                for (int i = 0; i < childCount; i++) {
                    sum += parent.getChildAt(i).getHeight();
                }

                if (sum <= parent.getHeight()) {
                    outRect.bottom = (int) (parent.getHeight() - sum + size);
                }

            } else {
                outRect.bottom = 0;
            }
        }
    }

}
