package com.br.mreza.musicplayer;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//import static com.br.mreza.musicplayer.ListMaker.albumContents;

public class FirstPageAdapterTabAlbums extends RecyclerView.Adapter<FirstPageAdapterTabAlbums.AlbumHolder> {

    private Bitmap bitmapNew;

    private AlbumTabListener listClick;

    protected List<DataBaseAlbum> albumList = new ArrayList<>();

    private Context contextForThis;

    public FirstPageAdapterTabAlbums(Context context, AlbumTabListener listClick) {
//        this.albumList = albumList;

        contextForThis = context;

//        bitmapNew = BitmapFactory.decodeResource(context.getResources(), R.drawable.night_rain_1);
//        bitmapNew = MbnUtils.roundedBitmapWithChin(BitmapFactory.decodeResource(cont.getResources(), R.drawable.night_rain_1));
        this.listClick = listClick;

        updateData();

    }


    public void updateData() {

        albumList.clear();
//        System.out.println("iiiiiiiii B");
        albumList.addAll(DataBaseHolder.getInstance(contextForThis).getSongDAO().loadAllAlbums());
//        System.out.println("iiiiiiiiiiii A");
        notifyDataSetChanged();


    }


    @NonNull
    @Override
    public AlbumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutId = R.layout.album_list_layout_backup_two;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);


        return new AlbumHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumHolder holder, int position) {


        holder.bind(position);


    }

    @Override
    public void onViewRecycled(AlbumHolder holder) {
        super.onViewRecycled(holder);


        holder.unbind();

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }


    class AlbumHolder extends RecyclerView.ViewHolder {

        private ImageView albumCover;
        //        SoftRoundEdgeImageView albumCover;
        //        ImageView option;
        private TextView albumTitle;

        private TextView numberOfSongs;

        private String mData = "";


        AlbumHolder(final View itemView) {
            super(itemView);


            itemView.findViewById(R.id.item_base_layout).setClipToOutline(true);

            albumCover = itemView.findViewById(R.id.first_list_art_cover);

//            albumCover.setClipToOutline(true);
//            option = itemView.findViewById(R.id.album_list_option);
            albumTitle = itemView.findViewById(R.id.first_list_album_title);

            numberOfSongs = itemView.findViewById(R.id.album_list_number);
//            bitmap = BitmapFactory.decodeResource(itemView.getResources(), R.drawable.night_rain_1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    listClick.onClick(albumList.get(getAdapterPosition()).getId(), albumList.get(getAdapterPosition()).getData(), albumCover, albumList.get(getAdapterPosition()).getName());

                }


            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    listClick.onOptionClick(getAdapterPosition(), itemView);


                    return true;
                }
            });

//            option.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });


        }


        void unbind() {

            mData = "";

//            albumCover.setAlpha(0f);

//            albumCover.setImageBitmap(null);


        }


        void bind(int pos) {

//            itemView.setAlpha(0);


//            albumCover.setVisibility(View.INVISIBLE);
//            albumCover.setAlpha(0f);

            mData = albumList.get(getAdapterPosition()).getData();

//            if (albumCover.getWidth() > 0) {


//            albumCover.setImageBitmap(BitmapFactory.decodeFile(mData));

            new AlbumArtTaskForAlbums(contextForThis, mData, 0) {
                @Override
                public void onFinish(Bitmap bitmap, String data) {

//                    if (data != null && mData != null && data.equals(mData)) {

                    albumCover.setImageBitmap(bitmap);

//                    }

                }
            };

//            new ImageGetter().execute(pos);


//            try {
//                Bitmap art = BitmapFactory.decodeByteArray(PlayerService.albumContents.get(getAdapterPosition()).get(0).getCover(), 0, PlayerService.albumContents.get(getAdapterPosition()).get(0).getCover().length);
//                albumCover.setImageBitmap(art);
//            } catch (Exception e) {
//
//                albumCover.setImageBitmap(BitmapFactory.decodeResource(albumCover.getResources(), R.drawable.night_rain_1));
//
//            }

            String title = albumList.get(getAdapterPosition()).getName();

            String num = "Tracks: " + albumList.get(getAdapterPosition()).getNumberOfTracks();

//            if (title.length() > 18) {
//
//                title = title.substring(0, 14) + "...";
//
//            }

            albumTitle.setText(title);
            numberOfSongs.setText(num);


        }


        class ImageGetter extends AsyncTask<Integer, Void, Bitmap> {

            private int pos;

            @Override
            protected Bitmap doInBackground(Integer... params) {

//                System.out.println(Thread.currentThread().getName());

                pos = params[0];

                Bitmap art;
                try {
                    art = MbnController.getCoverForAlbums(albumList.get(getAdapterPosition()).getData());
//                    albumCover.setImageBitmap(art);
//                    return null;
                    return art;
                } catch (Exception e) {

//                    albumCover.setImageBitmap(BitmapFactory.decodeResource(albumCover.getResources(), R.drawable.night_rain_1));

//                    art = BitmapFactory.decodeResource(get, R.drawable.night_rain_1);
                }


                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);

                if (pos == getAdapterPosition()) {

//                    albumCover.animate().alpha(1f).setDuration(250).start();


                    if (bitmap != null) {
//                        albumCover.setmBitmap(bitmap);
//                    albumCover.setImageBitmap(MbnUtils.roundedBitmapWithChin(bitmap));
                    } else {

//                        albumCover.setmBitmap(bitmapNew);
                    }


//                    albumCover.animate().alpha(1).setDuration(300);
                }

//                itemView.animate().alpha(1).setDuration(200);
            }
        }


    }

    public interface AlbumTabListener {

        void onClick(long id, String data, View v, String name);

        void onOptionClick(int position, View v);


    }


}
