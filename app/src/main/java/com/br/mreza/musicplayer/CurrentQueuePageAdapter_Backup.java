//package com.br.mreza.musicplayer;
//
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.helper.ItemTouchHelper;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//import static com.br.mreza.musicplayer.ListMaker.currentQueue;
//
//public class CurrentQueuePageAdapter_Backup extends RecyclerView.Adapter<CurrentQueuePageAdapter_Backup.Holder> {
//
//    private ArrayList<Holder> holders = new ArrayList<>();
//    private ItemTouchHelper touchHelper;
//
//    private ArrayList<DataSong> songsCodesList = new ArrayList<>();
//    private Context context;
//
//    public CurrentQueuePageAdapter_Backup(Context context, ItemTouchHelper touchHelper) {
//
//        this.context = context;
//        this.touchHelper = touchHelper;
//
//        update();
//
//    }
//
//    public void update() {
//
//        if (!songsCodesList.equals(currentQueue)) {
//            songsCodesList.clear();
//            songsCodesList.addAll(currentQueue);
//            notifyDataSetChanged();
//        }
//
//    }
//
//    @Override
//    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
//
////        Context context = parent.getContext();
//        int layoutId = R.layout.all_music_list_item_for_current_queue;
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(layoutId, parent, false);
//
//        return new Holder(view);
//    }
//
//    @Override
//    public void onViewRecycled(Holder holder) {
//        super.onViewRecycled(holder);
//        holder.unBind();
//    }
//
//    @Override
//    public void onBindViewHolder(Holder holder, int position) {
//
//
//        holder.bind();
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return songsCodesList.size();
//    }
//
//
//    private void notifyHolders() {
//
//
//        for (Holder h : holders) {
//
//            try {
//                h.checkSelect();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//
//
//    }
//
//    class Holder extends RecyclerView.ViewHolder {
//
//        TextView songName;
//        TextView artist;
//        TextView extra;
//        ImageView option;
//        ImageView drag;
//        ImageView art;
//        View whole;
//        String mData;
//
//        @SuppressLint("ClickableViewAccessibility")
//        Holder(View itemView) {
//            super(itemView);
//
//            whole = itemView;
//            songName = itemView.findViewById(R.id.allsongs_list_item_name);
//            artist = itemView.findViewById(R.id.allsongs_list_item_artist);
//            extra = itemView.findViewById(R.id.allsongs_list_item_extra);
//            option = itemView.findViewById(R.id.allsongs_list_item_option);
//            drag = itemView.findViewById(R.id.allsongs_list_item_drag);
//            art = itemView.findViewById(R.id.art_image_view);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//
////                    listener.onClick(getAdapterPosition(), songsCodesList.get(getAdapterPosition()));
//
//                    MbnController.change(context, currentQueue, currentQueue.get(getAdapterPosition()));
//
//
//                    notifyHolders();
//
//                }
//            });
//
//            drag.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    touchHelper.startDrag(Holder.this);
//                    return false;
//                }
//            });
//
//            holders.add(this);
//
//        }
//
//
//        void bind() {
//
//            mData = songsCodesList.get(getAdapterPosition()).getPath();
//
//            new AlbumArtTaskForSongs(context, mData, (int) songsCodesList.get(getAdapterPosition()).getId()) {
//                @Override
//                public void onFinish(Bitmap bitmap, String data) {
//                    if (data.equals(mData)) {
//                        art.setImageBitmap(bitmap);
//                    }
//                }
//            };
//
////            new NewAlbumArtForSongsLoader(new BaseTaskObject.ResultReceiver() {
////                @Override
////                public void onResult(BaseTaskObject.ResultObject result, BaseTaskObject.InfoObject info) {
////                    if (((NewLoaderTask.Info) info).getPath().equals(mData))
////                        art.setImageBitmap(((NewAlbumArtForSongsLoader.Result) result).getBitmap());
////                }
////            }, new NewLoaderTask.Info((int) songsCodesList.get(getAdapterPosition()).getId(), mData, context.getResources()));
//
//            songName.setText(songsCodesList.get(getAdapterPosition()).getTitle());
////            songName.setText(allSongsMap.get(songsCodesList.get(getAdapterPosition())).getTitle());
//            artist.setText(songsCodesList.get(getAdapterPosition()).getArtist());
////            artist.setText(allSongsMap.get(songsCodesList.get(getAdapterPosition())).getArtist());
//            extra.setText(MbnController.makeMillisToTime(songsCodesList.get(getAdapterPosition()).getDuration()));
//
//            checkSelect();
//
//            whole.setVisibility(View.VISIBLE);
//
//
//        }
//
//        void unBind() {
//
//            mData = "";
//            art.setImageBitmap(null);
//
//
//        }
//
//        private void checkSelect() {
//
////            try {
////
////                if (currentTrack.getPath().equals(songsCodesList.get(getAdapterPosition()).getPath())) {
////
////                    whole.setBackgroundColor(Color.argb(180, 255, 255, 255));
////                    whole.setElevation(10f);
//////                    whole.setBackgroundColor(Color.parseColor("#50f5f5f5"));
////
////                } else {
////
////                    whole.setBackgroundColor(Color.argb(0, 0, 0, 0));
////                    whole.setElevation(5f);
////                }
////
////            } catch (Exception ignored) {
////            }
//
//
//        }
//
//    }
//
//}
