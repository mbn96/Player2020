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
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.helper.ItemTouchHelper;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import java.util.Collections;
//import java.util.List;
//
//import static com.br.mreza.musicplayer.ListMaker.currentQueue;
//import static com.br.mreza.musicplayer.ListMaker.currentTrack;
//import static com.br.mreza.musicplayer.MbnController.To_FRAGMENT;
//
//public class CurrentQueueFragment extends Fragment {
//
//    private CurrentQueuePageAdapter adapter;
//
//    public static CurrentQueueFragment newInstance() {
//
//        Bundle args = new Bundle();
//
//        CurrentQueueFragment fragment = new CurrentQueueFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    private RecyclerView mRecyclerView;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//
//        return inflater.inflate(R.layout.curren_queue_fargment_layout, container, false);
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//
//        mRecyclerView = view.findViewById(R.id.first_page_recycler_view);
//        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        mRecyclerView.setLayoutManager(manager);
//        mRecyclerView.setHasFixedSize(true);
//
////        adapter = new FirstPageAdapterTabAllSongs(new FirstPageAdapterTabAllSongs.AllSongsListener() {
////            @Override
////            public void onClick(int position, List<DataSong> songs, DataSong songCode) {
////
////                MbnController.change(getContext(), currentQueue, songCode);
////
////            }
////
////            @Override
////            public void onOption(int position, String songCode) {
////
////            }
////        }, getContext());
//
//        adapter = new CurrentQueuePageAdapter(getContext());
//        mRecyclerView.setAdapter(adapter);
//
//
//        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.START) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//
//                Collections.swap(currentQueue, viewHolder.getAdapterPosition(), target.getAdapterPosition());
//
//                recyclerView.getAdapter().notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
//
//                StorageUtils.setCurrent(getContext());
//
//                return true;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//
//                if (currentQueue.indexOf(currentTrack) == viewHolder.getAdapterPosition()) {
//                    MbnController.next(getContext());
//                }
//
//                currentQueue.remove(viewHolder.getAdapterPosition());
//                mRecyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
//                MbnController.makeUnshuffledList();
//                StorageUtils.setCurrent(getContext());
//
//            }
//        };
//
//        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
//            @Override
//            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, ItemTouchHelper.DOWN | ItemTouchHelper.UP);
//            }
//
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//
//
//                Collections.swap(currentQueue, viewHolder.getAdapterPosition(), target.getAdapterPosition());
//
//                recyclerView.getAdapter().notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
//
//                return true;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//
//            }
//        };
//
//
//        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleCallback);
////            @Override
////            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
////                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, ItemTouchHelper.DOWN | ItemTouchHelper.UP);
////            }
////
////            @Override
////            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
////
////
////                Collections.swap(currentQueue, viewHolder.getAdapterPosition(), target.getAdapterPosition());
////
////                recyclerView.getAdapter().notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
////
////                return true;
////            }
////
////            @Override
////            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
////
////            }
////        });
//
//
//        touchHelper.attachToRecyclerView(mRecyclerView);
//
//
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        reg();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        reg();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        unReg();
//    }
//
//    private void reg() {
//
//        IntentFilter filter = new IntentFilter(To_FRAGMENT);
//
//        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, filter);
////        getContext().registerReceiver(receiver, filter);
//    }
//
//    private void unReg() {
//
//        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
////        getContext().unregisterReceiver(receiver);
//
//    }
//
//    BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            adapter.update();
//
//        }
//    };
//
//
//}
