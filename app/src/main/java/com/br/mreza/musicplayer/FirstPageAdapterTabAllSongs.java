package com.br.mreza.musicplayer;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.mreza.musicplayer.UI.RecyclerViews.ItemDecors.FastScrollerForRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FirstPageAdapterTabAllSongs extends RecyclerView.Adapter<FirstPageAdapterTabAllSongs.AllSongsHolder> {


    private ArrayList<DataSong> songsCodesList = new ArrayList<>();

    private ArrayList<AllSongsHolder> holders = new ArrayList<>();

    private AllSongsListener listener;

    private HelperForFastScroll helperForFastScroll;

    Context context;

    private RecyclerView recyclerView;

    private FastScrollerForRecyclerView.FastScrollListener fastScrollListener = new FastScrollerForRecyclerView.FastScrollListener() {
        @Override
        public String getItemFirstLetter(int itemPos) {
            return songsCodesList.get(itemPos).getTitle().substring(0, 1);
        }
    };

    public FirstPageAdapterTabAllSongs(AllSongsListener listener, Context context, RecyclerView recyclerView, FastScrollerForRecyclerView fastScrollerForRecyclerView) {
//        this.songsCodesList = songsList;
        this.listener = listener;
        this.context = context;
        this.recyclerView = recyclerView;
        fastScrollerForRecyclerView.setListener(fastScrollListener);
        prepare();
    }


    public void prepare() {

        songsCodesList.clear();

        songsCodesList.addAll(DataBaseHolder.getAllSongs(context));

        notifyDataSetChanged();
//        songsCodesList.addAll(DataBaseHolder.getInstance(context).getSongDAO().loadAllSongs());


    }

    public void setHelperForFastScroll(HelperForFastScroll helperForFastScroll) {
        this.helperForFastScroll = helperForFastScroll;
    }


    @Override
    public AllSongsHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutId = R.layout.all_music_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);


        return new AllSongsHolder(view);
    }

    @Override
    public void onBindViewHolder(AllSongsHolder holder, int position) {

        try {
            holder.onBind();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (helperForFastScroll != null) {
//            helperForFastScroll.newBind(position, allSongsMap.get(songsCodesList.get(position)).getTitle().charAt(0));
        }

    }


    @Override
    public void onViewRecycled(AllSongsHolder holder) {
        super.onViewRecycled(holder);


        holder.unbind();

    }

    @Override
    public int getItemCount() {
        return songsCodesList.size();
    }


    private void notifyHolders() {


        for (AllSongsHolder h : holders) {

            try {
                h.checkSelect();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    class AllSongsHolder extends RecyclerView.ViewHolder {

        TextView songName;
        TextView artist;
        TextView extra;
        ImageView option;
        ImageView art;
        View whole;
        private String mData;

        //        AlbumArtLoaderTask loaderTask = new AlbumArtLoaderTask(context) {
//            @Override
//            void onFinish(Bitmap bitmap) {
//
//                art.setImageBitmap(bitmap);
////                art.setImageBitmap(MbnUtils.roundedBitmap(bitmap));
//                art.setVisibility(View.VISIBLE);
//            }
//        };
        private AsyncTask<Void, Void, Void> task;


//        String nameTitle;
//        String artistTitle;

        public AllSongsHolder(View itemView) {
            super(itemView);
            whole = itemView;
            songName = itemView.findViewById(R.id.allsongs_list_item_name);
            artist = itemView.findViewById(R.id.allsongs_list_item_artist);
            extra = itemView.findViewById(R.id.allsongs_list_item_extra);
            option = itemView.findViewById(R.id.allsongs_list_item_option);
            art = itemView.findViewById(R.id.art_for_list);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listener.onClick(getAdapterPosition(), songsCodesList, songsCodesList.get(getAdapterPosition()));

                    recyclerView.scrollToPosition(getAdapterPosition());

//                    listener.onClick(getAdapterPosition(), songsCodesList.get(getAdapterPosition()));


                    notifyHolders();

                }
            });

            option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listener.onOption(getAdapterPosition(), songsCodesList.get(getAdapterPosition()).getPath());
//                    listener.onOption(getAdapterPosition(), songsCodesList.get(getAdapterPosition()));
                }
            });

//            itemView.setPivotY(0);
//            itemView.setPivotX(0);
//            itemView.setrot(-45);
//            itemView.animate().rotationY(0).setDuration(900).setInterpolator(new BounceInterpolator());


            holders.add(this);

        }


        private void checkSelect() {

//            try {
//
//                if (currentTrack.getPath().equals(songsCodesList.get(getAdapterPosition()).getPath())) {
//
//                    whole.setBackgroundColor(Color.parseColor("#50f5f5f5"));
//
//                } else {
//
//                    whole.setBackgroundColor(Color.argb(0, 0, 0, 0));
//                }
//
//            } catch (Exception ignored) {
//            }


        }

        @SuppressLint("StaticFieldLeak")
        public void onBind() {


//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//
//                    try {
//
//                        final String titleST = songsCodesList.get(getAdapterPosition()).getTitleForApp();
//                        final String artistST = songsCodesList.get(getAdapterPosition()).getArtistForApp();
//
//                        songName.post(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                try {
//                                    songName.setText(titleST);
////            songName.setText(allSongsMap.get(songsCodesList.get(getAdapterPosition())).getTitle());
//                                    artist.setText(artistST);
////            artist.setText(allSongsMap.get(songsCodesList.get(getAdapterPosition())).getArtistTitle());
//                                    extra.setText(MbnController.makeMillisToTime(songsCodesList.get(getAdapterPosition()).getDuration()));
//
//                                    checkSelect();
//
//                                    whole.setVisibility(View.VISIBLE);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//
//                            }
//                        });
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }).start();

//            whole.setVisibility(View.VISIBLE);


//            songName.post(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });

//            if (task != null) {
//                task.cancel(true);
//            }
//
////            System.out.println("aaaaaaaaa  instan");
//
//            task = new AsyncTask<Void, Void, Void>() {
//
//                String titleST = null;
//                String artistST = null;
//
//                @Override
//                protected Void doInBackground(Void... voids) {
//
////                    System.out.println("aaaaaaaaa doStart");
//
//                    try {
//                        titleST = songsCodesList.get(getAdapterPosition()).getTitleForApp();
//                        artistST = songsCodesList.get(getAdapterPosition()).getArtistForApp();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
////                    System.out.println("aaaaaaaaa doEnd");
//
//                    return null;
//                }
//
//                @Override
//                protected void onPostExecute(Void aVoid) {
//                    super.onPostExecute(aVoid);
//
////                    System.out.println("aaaaaaaaa postStart");
//
//
//                    try {
//                        songName.setText(titleST);
////            songName.setText(allSongsMap.get(songsCodesList.get(getAdapterPosition())).getTitle());
//                        artist.setText(artistST);
////            artist.setText(allSongsMap.get(songsCodesList.get(getAdapterPosition())).getArtistTitle());
//                        extra.setText(MbnController.makeMillisToTime(songsCodesList.get(getAdapterPosition()).getDuration()));
//
//                        if (currentTrack.getPath().equals(songsCodesList.get(getAdapterPosition()).getPath())) {
//
//                            whole.setBackgroundColor(Color.parseColor("#50f5f5f5"));
//
//                        } else {
//
//                            whole.setBackgroundColor(Color.argb(0, 0, 0, 0));
//                        }
//
//                        whole.setVisibility(View.VISIBLE);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
////                    System.out.println("aaaaaaaaa postEnd");
//
//
//                }
//            }.execute();

            mData = songsCodesList.get(getAdapterPosition()).getPath();

            new AlbumArtTaskForSongs(context, mData, (int) songsCodesList.get(getAdapterPosition()).getId()) {
                @Override
                public void onFinish(Bitmap bitmap, String data) {

                    if (data.equals(mData)) {

                        art.setImageBitmap(bitmap);
//                        art.setVisibility(View.VISIBLE);


                    }

                }
            };

//            loaderTask.set(songsCodesList.get(getAdapterPosition()).getPath());

            songName.setText(songsCodesList.get(getAdapterPosition()).getTitle());
//            songName.setText(allSongsMap.get(songsCodesList.get(getAdapterPosition())).getTitle());
            artist.setText(songsCodesList.get(getAdapterPosition()).getArtistTitle());
//            artist.setText(allSongsMap.get(songsCodesList.get(getAdapterPosition())).getArtistTitle());
            extra.setText(MbnController.makeMillisToTime(songsCodesList.get(getAdapterPosition()).getDuration()));

            checkSelect();

            whole.setVisibility(View.VISIBLE);

//            whole.setPivotX(0f);
//            whole.setRotation(20f);
//            whole.animate().rotation(0f).setDuration(300).start();
        }

        void unbind() {
//            whole.setVisibility(View.INVISIBLE);

//            art.setVisibility(View.INVISIBLE);

            art.setImageBitmap(null);
            mData = "";

        }


//        class HandleThread extends HandlerThread {
//
//
//            public HandleThread(String name) {
//                super(name);
//            }
//
//            public HandleThread(String name, int priority) {
//                super(name, priority);
//            }
//
//            @Override
//            public void run() {
//                super.run();
//
//                try {
//                    songName.setText(songsCodesList.get(getAdapterPosition()).getTitleForApp());
////            songName.setText(allSongsMap.get(songsCodesList.get(getAdapterPosition())).getTitle());
//                    artist.setText(songsCodesList.get(getAdapterPosition()).getArtistForApp());
////            artist.setText(allSongsMap.get(songsCodesList.get(getAdapterPosition())).getArtistTitle());
//                    extra.setText(MbnController.makeMillisToTime(songsCodesList.get(getAdapterPosition()).getDuration()));
//
//                    if (currentTrack.getPath().equals(songsCodesList.get(getAdapterPosition()).getPath())) {
//
//                        whole.setBackgroundColor(Color.parseColor("#50f5f5f5"));
//
//                    } else {
//
//                        whole.setBackgroundColor(Color.argb(0, 0, 0, 0));
//                    }
//
//                    whole.setVisibility(View.VISIBLE);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//        }

    }

    public interface AllSongsListener {

        void onClick(int position, List<DataSong> songs, DataSong song);

        void onOption(int position, String songCode);


    }

    public interface HelperForFastScroll {


        void newBind(int pos, char letter);

    }
}
