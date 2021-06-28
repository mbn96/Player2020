//package com.br.mreza.musicplayer;
//
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.LocalBroadcastManager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.br.mreza.musicplayer.fragmanager.CustomFragmentSwipeBackAnimator;
//import com.br.mreza.musicplayer.newdesign.AlbumCoverShowDialog;
//import com.br.mreza.musicplayer.newdesign.NewSongOptionDialog;
//
//import static com.br.mreza.musicplayer.MbnController.To_FRAGMENT;
//
//public class SwipeFragment extends Fragment {
//
//    private TextView title;
//    private TextView artist;
//    private String mData;
//    private ImageView albumArtImage;
//    private int num;
//    private BroadcastReceiver fragmentReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
////            try {
////                title.setText(currentQueue.get(num).getTitle());
////                artist.setText(currentQueue.get(num).getArtistTitle());
////                mData = currentQueue.get(num).getPath();
////                new AlbumArtTaskForSongs(context, mData, (int) currentQueue.get(num).getId()) {
////                    @Override
////                    public void onFinish(Bitmap bitmap, String data) {
////                        if (data.equals(mData)) {
////                            albumArtImage.setImageBitmap(bitmap);
////                        }
////                    }
////                };
////            } catch (Exception ignored) {
////            }
//
//
//        }
//    };
//
//    public static SwipeFragment newInstance(int num) {
//
//        Bundle args = new Bundle();
//
//        args.putInt("NUMBER", num);
//
//        SwipeFragment fragment = new SwipeFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//
//        return inflater.inflate(R.layout.fargment_for_swipe_now_playing, container, false);
//    }
//
//
//    private void orangeTexts(View view) {
//
////        visualizerView = view.findViewById(R.id.mbn_visy);
//
////        MediaSessionHolder.visyHelper.setVisualizerView(visualizerView);
//
////        time = view.findViewById(R.id.player_frag_time_text);
//        title = view.findViewById(R.id.player_frag_title_text);
////        title.setText(currentQueue.get(num).getTitle());
////        album = view.findViewById(R.id.frag_album_text);
//        artist = view.findViewById(R.id.player_frag_artist_text);
////        artist.setText(currentQueue.get(num).getArtistTitle());
////        bitrate = view.findViewById(R.id.player_frag_bitrate_text);
////        numberInQU = view.findViewById(R.id.player_frag_number_text);
//        albumArtImage = view.findViewById(R.id.player_frag_album_art);
//
//        albumArtImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                CustomFragmentSwipeBackAnimator.getINSTANCE().addFragment(new AlbumCoverShowDialog());
//
//
//            }
//        });
//
//
//        artist.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//
//                CustomFragmentSwipeBackAnimator.getINSTANCE().addFragment(new NewSongOptionDialog());
//
////                SongOptionDialog.newInstance().show(getFragmentManager(), "options");
//
//
//                return true;
//            }
//        });
//
//        title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CustomFragmentSwipeBackAnimator.getINSTANCE().addFragment(new AlbumCoverShowDialog());
//
//            }
//        });
//
////        title.setHelper(new MbnScrollingTextView.TextViewSwipeHelper() {
////            @Override
////            public void onSwipe(boolean direction) {
////
////                if (direction) {
////                    MbnController.previous(getContext());
////                } else {
////                    MbnController.next(getContext());
////                }
////
////            }
////        });
//
//
//    }
//
//
//    private void reg() {
//
//        IntentFilter filter = new IntentFilter(To_FRAGMENT);
////        IntentFilter filter2 = new IntentFilter(ON_TICK);
//
//        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
//
//        localBroadcastManager.registerReceiver(fragmentReceiver, filter);
////        localBroadcastManager.registerReceiver(tickReceive, filter2);
//
//
//    }
//
//    private void unReg() {
//
//        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
//
//        localBroadcastManager.unregisterReceiver(fragmentReceiver);
////        localBroadcastManager.unregisterReceiver(tickReceive);
//
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        reg();
////        title.setText(currentQueue.get(num).getTitle());
////        artist.setText(currentQueue.get(num).getArtistTitle());
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        unReg();
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        num = getArguments().getInt("NUMBER");
//
//        orangeTexts(view);
//
//    }
//}
