package com.br.mreza.musicplayer;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.mreza.musicplayer.MBN.bitmapUtils.MbnUtils;

import java.util.ArrayList;

public class SearchTabAdapter extends RecyclerView.Adapter<SearchTabAdapter.SearchTabHolder> {


    private Context contextForThis;

    private final Bitmap bitmapNew;

    private SearchTabListener listener;

    private ArrayList<MbnSearchEngine.SearchItem> searchItemArrayList = new ArrayList<>();

    public void setSearchItemArrayList(ArrayList<MbnSearchEngine.SearchItem> list) {

        searchItemArrayList.clear();

        searchItemArrayList.addAll(list);

        notifyDataSetChanged();


    }

    public void setListener(SearchTabListener listener) {
        this.listener = listener;
    }

    public SearchTabAdapter(Context context) {

        contextForThis = context;

        bitmapNew = MbnUtils.roundedBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.night_rain_1));

    }


    @Override
    public int getItemViewType(int position) {


        return searchItemArrayList.get(position).getType();
    }

    @Override
    public SearchTabHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutId = 0;
        switch (viewType) {

            case 0:
                layoutId = R.layout.search_item_header_layout;
                break;
            case 1:
                layoutId = R.layout.all_music_list_item;
                break;
            case 2:
                layoutId = R.layout.search_item_album_layout;
                break;


        }

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, parent, false);

        return new SearchTabHolder(view, viewType);
    }


    @Override
    public void onBindViewHolder(SearchTabHolder holder, int position) {

        holder.onBind();

    }


    @Override
    public int getItemCount() {
        return searchItemArrayList.size();
    }


    class SearchTabHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView title;
        private TextView artist;
        private int type;


        public SearchTabHolder(View itemView, int type) {
            super(itemView);

            this.type = type;

            switch (type) {
                case 0:
                    title = itemView.findViewById(R.id.search_header_textView);
                    break;
                case 1:
                    title = itemView.findViewById(R.id.allsongs_list_item_name);
                    artist = itemView.findViewById(R.id.allsongs_list_item_artist);
                    break;

                case 2:
                    title = itemView.findViewById(R.id.search_album_textView);
                    imageView = itemView.findViewById(R.id.search_album_imageview);
                    break;

            }


            if (type == 1 || type == 2) {


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        listener.onClick(searchItemArrayList.get(getAdapterPosition()));

                    }
                });

            }


        }


        public void onBind() {

            switch (type) {

                case 0:
                    title.setText(searchItemArrayList.get(getAdapterPosition()).getHeader());

                    break;
                case 1:
                    title.setText(searchItemArrayList.get(getAdapterPosition()).getSong().getTitle());
                    artist.setText(searchItemArrayList.get(getAdapterPosition()).getSong().getArtistTitle());
                    break;
                case 2:

                    title.setText(searchItemArrayList.get(getAdapterPosition()).getAlbum().getName());
                    new ImageLoader().execute();

                    break;
            }

        }


        class ImageLoader extends AsyncTask<Void, Void, Bitmap> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                imageView.setAlpha(0f);
            }

            @Override
            protected Bitmap doInBackground(Void... voids) {


                Bitmap bitmap = null;
                try {
                    bitmap = MbnController.getCoverForAlbums(searchItemArrayList.get(getAdapterPosition()).getAlbum().getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (bitmap != null) {

                    return MbnUtils.roundedBitmap(bitmap);
                }


                return bitmapNew;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);

                imageView.setImageBitmap(bitmap);
                imageView.setAlpha(1f);
                imageView.setScaleX(0f);
                imageView.setScaleY(0f);
                imageView.animate().scaleX(1f).scaleY(1f).setDuration(100).start();


            }
        }


    }

    public interface SearchTabListener {


        void onClick(MbnSearchEngine.SearchItem item);

    }
}
