//package com.br.mreza.musicplayer;
//
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.support.v4.app.FragmentManager;
//import android.support.v7.widget.helper.ItemTouchHelper;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.br.mreza.musicplayer.newmodel.adapters.songs.MbnSelectableRecyclerAdapter;
//
//import java.util.ArrayList;
//
//import static com.br.mreza.musicplayer.ListMaker.currentQueue;
//
//public class CurrentQueuePageAdapter extends MbnSelectableRecyclerAdapter {
//
//    private ArrayList<Holder> holders = new ArrayList<>();
//    private ItemTouchHelper touchHelper;
//
//    private ArrayList<DataSong> songsCodesList = new ArrayList<>();
//    private Context context;
//    private View optionView;
//    private FragmentManager fragmentManager;
//
//    public CurrentQueuePageAdapter(Context context, ItemTouchHelper touchHelper, View optionView, FragmentManager fragmentManager) {
//
//        this.context = context;
//        this.touchHelper = touchHelper;
//        this.optionView = optionView;
//        this.fragmentManager = fragmentManager;
//        manageSelectionOptionView();
//        update();
//
//    }
//
//    private void manageSelectionOptionView() {
//        optionView.findViewById(R.id.cancle_select_playF).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cancelSelect();
//            }
//        });
//        optionView.findViewById(R.id.addto_select_playF).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CreateNew_AddTo_RemoveFrom_Playlist_Fragment.newInstance(getSelectedSongs(), true).show(fragmentManager, "addF");
//                cancelSelect();
//            }
//        });
//        optionView.findViewById(R.id.removefrom_select_playF).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CreateNew_AddTo_RemoveFrom_Playlist_Fragment.newInstance(getSelectedSongs(), false).show(fragmentManager, "addF");
//                cancelSelect();
//            }
//        });
//        optionView.setVisibility(View.INVISIBLE);
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
//
//    @Override
//    public int getItemCount() {
//        return songsCodesList.size();
//    }
//
//    @Override
//    public void cancelSelect() {
//        super.cancelSelect();
//        notifyHolders();
//        optionView.setVisibility(View.INVISIBLE);
//    }
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
//    @Override
//    public DataSong getSelected() {
//        return null;
//    }
//
//    public class Holder extends MbnSelectableRecyclerAdapter.Holder {
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
//                    DataSong song = currentQueue.get(getAdapterPosition());
//
//                    if (isInSelectionMode()) {
//                        if (getSelectedSongs().contains(song))
//                            removeFromSelected(song);
//                        else addToSelect(song);
//                    } else {
//                        MbnController.change(context, currentQueue, currentQueue.get(getAdapterPosition()));
//                    }
//
//
//                    checkSelect();
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
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    addToSelect(currentQueue.get(getAdapterPosition()));
//                    optionView.setVisibility(View.VISIBLE);
//                    checkSelect();
//                    return true;
//                }
//            });
//
//            holders.add(this);
//
//        }
//
//
//        @Override
//        protected void onBind() {
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
//            artist.setText(songsCodesList.get(getAdapterPosition()).getArtistTitle());
////            artist.setText(allSongsMap.get(songsCodesList.get(getAdapterPosition())).getArtistTitle());
//            extra.setText(MbnController.makeMillisToTime(songsCodesList.get(getAdapterPosition()).getDuration()));
//
//            checkSelect();
//
////            whole.setVisibility(View.VISIBLE);
//
//
//        }
//
//
//        @Override
//        protected void unBind() {
//
//            mData = "";
//            art.setImageBitmap(null);
//
//
//        }
//
//        private void checkSelect() {
//
//
//            DataSong song = currentQueue.get(getAdapterPosition());
//
//            if (getSelectedSongs().contains(song))
//                whole.setBackgroundColor(Color.argb(145, 200, 200, 200));
//            else
////                whole.setBackgroundColor(Color.argb(0, 255, 255, 255));
//                if (StorageUtils.getThemeType() == 0) {
//                    whole.setBackground(null);
//                } else {
//                    whole.setBackground(null);
//                }
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
