package com.br.mreza.musicplayer.newdesign;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.br.mreza.musicplayer.DataBaseHolder;
import com.br.mreza.musicplayer.MBN.customViews.MbnScrollingTextView;
import com.br.mreza.musicplayer.MbnController;
import com.br.mreza.musicplayer.R;
import com.br.mreza.musicplayer.StorageUtils;
import com.br.mreza.musicplayer.newdesign.materialtheme.pageDecorationMaterialTheme;
import com.br.mreza.musicplayer.newdesign.materialtheme.PlayerPageBackgroundMaterial;
import com.br.mreza.musicplayer.newmodel.adapters.songs.NewModelCurrentQueueAdapter;

import java.util.ArrayList;

import mbn.libs.fragmanager.BaseFragment;
import mbn.libs.fragmanager.CustomFragmentSwipeBackAnimator;
import mbn.libs.fragmanager.TouchEaterRecyclerView;


public class NewAlbumFrag extends BaseFragment implements View.OnClickListener {


    private NewModelCurrentQueueAdapter adapter;
    private View shuffleAllButton;
    private View playAllButton;

    public static NewAlbumFrag newInstance(long albumCode, String data, String text) {

        final Bundle args = new Bundle();
        args.putLong("name", albumCode);
        args.putString("data", data);
        args.putString("text", text);
        args.putInt("type", 1);
        final NewAlbumFrag fragment = new NewAlbumFrag();
        fragment.setArguments(args);
//        fragment.setStyle(STYLE_NO_FRAME, android.support.design.R.style.Base_ThemeOverlay_AppCompat_Dark);
        return fragment;
    }


    @Override
    public boolean canInterceptTouches() {
        return !recyclerView.canScrollVertically(-1) && super.canInterceptTouches();
    }

    @Override
    public int getAnimationMode() {
        return CustomFragmentSwipeBackAnimator.ANIM_BOTTOM_NORMAL;
    }


    @Override
    public View onCreateViewForChild(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = inflater.getContext();
        return inflater.inflate(R.layout.newmodel_album_layout, container, false);
//        return inflater.inflate(R.layout.album_bottom_sheet_new_design, container, false);
    }

//    @Override
//    public View makeToolBar(LayoutInflater inflater) {
//        return null;
//    }

    @Override
    public boolean hasAppBar() {
        return false;
    }

    @Override
    public void makeState(Bundle state) {

    }

    @Override
    public void checkForRestoreState() {

    }


    private Context context;
    private int albumPosition;
    private long album;
    //    Toolbar toolbar;
    //    private ImageButton shuffleAllButton;
//    private ImageButton selectButton;
//    private ImageButton optionButton;
//    private TextView title;
//    private ImageView imageView;
    private TouchEaterRecyclerView recyclerView;


    @Override
    public boolean canPopThisFragment() {
        if (adapter.isInSelectionMode()) {
            adapter.cancelSelect();
            return false;
        }
        return true;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        switch (getArguments().getInt("type")) {
            case 0:
                albumPosition = getArguments().getInt("pos");
//                album = albumsList.get(albumPosition);
                break;
            case 1:

                album = getArguments().getLong("name");

                break;


        }


        recyclerView = view.findViewById(R.id.album_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        MbnScrollingTextView titleTextView = view.findViewById(R.id.album_title_text);
        titleTextView.setText(getArguments().getString("text"));

        TextView durationText = view.findViewById(R.id.album_duration);
        durationText.setText("Play time: " + MbnController.makeMillisToTime(DataBaseHolder.getInstance(getContext()).getSongDAO().albumDuration(album)));

        Bitmap bitmap = MbnController.getCoverForAlbums(getArguments().getString("data"));
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.night_rain_2);
        }

        PlayerPageBackgroundMaterial backgroundMaterial = view.findViewById(R.id.album_background);
        backgroundMaterial.setImgBitmap(bitmap);

//        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.START) {
//
//            @Override
//            public boolean isLongPressDragEnabled() {
//                return false;
//            }
//
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//
////                Collections.swap(currentQueue, viewHolder.getAdapterPosition(), target.getAdapterPosition());
////
////                recyclerView.getAdapter().notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
////
////                StorageUtils.setCurrent(getContext());
//
//                //TODO: implement
//
//                return true;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//
////                viewHolder.itemView.animate().translationX(0f).setDuration(400);
//
////                if (currentQueue.indexOf(getCurrentTrack()) == viewHolder.getAdapterPosition()) {
////                    MbnController.next(getContext());
////                }
////
////                currentQueue.remove(viewHolder.getAdapterPosition());
////                currentQueueList.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
////                MbnController.makeUnshuffledList();
////                StorageUtils.setCurrent(getContext());
//
//                //TODO: implement
//
//            }
//        };
//        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleCallback);
        adapter = new NewModelCurrentQueueAdapter(getContext(), null, view.findViewById(R.id.selection_options), getFragmentManager()) {
            @Override
            public boolean changeTheQueue() {
                return true;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.setSongsIDs((ArrayList<Long>) DataBaseHolder.getInstance(getContext()).getSongDAO().loadAllSongsIdsForAlbum(album));
//        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new pageDecorationMaterialTheme(getContext(), view.findViewById(R.id.album_topview), recyclerView, backgroundMaterial) {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
//        if (parent.getPaddingTop() != topMovingView.getHeight())
//            parent.setPadding(parent.getPaddingLeft(), topMovingView.getHeight(), parent.getPaddingRight(), parent.getPaddingBottom());
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.top = (int) (parent.getHeight() / 1.1f);
                }
            }
        });


//        recyclerView.setAdapter(new AlbumAdapterForFragment(album, new AlbumAdapterForFragment.OnMbnListClick() {
//            @Override
//            public void onClick(List<DataSong> albumList, DataSong song) {
//                ArrayList<Long> songsIDs = new ArrayList<>();
//                for (DataSong s : albumList) {
//                    songsIDs.add(s.getId());
//                }
//                DataBaseManager.getManager().changeTrackAndQueue(songsIDs, song.getId());
//            }
//
//            @Override
//            public void onOptionClick(int position, View v) {
//
//            }
//        }, getContext(), getArguments().getString("text"), bitmap));

//        recyclerView.addItemDecoration(new AlbumAdapterForFragment.ImageMover());

//        imageView = view.findViewById(R.id.album_cover);
//
//        if (bitmap != null) {
//            imageView.setImageBitmap(bitmap);
//        } else {
//            imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.night_rain_2));
//        }

//
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recy, int dx, int dy) {
//                super.onScrolled(recy, dx, dy);
//
//
//                scrollPos -= dy;
//
////                Log.i("RECY", String.valueOf(recy.getScrollY()));
//
//                imageView.setTranslationY(scrollPos / 2);
//
//            }
//        });


        orangeButtons(view);
//        orangeToolBar(view);


    }

//    private void orangeToolBar(View view) {
//
//        CollapsingToolbarLayout toolbar = view.findViewById(R.id.collapingToolbar);
//
//        toolbar.setCollapsedTitleTextColor(Color.GRAY);
//
//        toolbar.setTitle(album.getName());
//
//        toolbar.setExpandedTitleColor(Color.WHITE);
//
//
//    }

    private void orangeButtons(View view) {

//        toolbar = getView().findViewById(R.id.toolbar);

//        toolbar.setTitle(getArguments().getString("text"));
//        title = view.findViewById(R.id.textView_album);


//        title.setText(getArguments().getString("text"));

        shuffleAllButton = view.findViewById(R.id.album_shuffle_butt);
        playAllButton = view.findViewById(R.id.album_play_butt);
//        optionButton = view.findViewById(R.id.Button_option);
//        selectButton = view.findViewById(R.id.Button_select);

        shuffleAllButton.setOnClickListener(this);
        playAllButton.setOnClickListener(this);
//        optionButton.setOnClickListener(this);
//        selectButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.album_shuffle_butt:
                StorageUtils.setShuffle(getContext(), true);
                adapter.changeCurrentQueueAndTrack(-1);
                break;
            case R.id.album_play_butt:
                StorageUtils.setShuffle(getContext(), false);
                adapter.changeCurrentQueueAndTrack(-1);
                break;
            case R.id.Button_option:

                break;
        }
    }


}
