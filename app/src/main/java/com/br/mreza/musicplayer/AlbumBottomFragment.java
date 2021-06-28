package com.br.mreza.musicplayer;


import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.widget.Toolbar;
//
//public class AlbumBottomFragment extends CustomFullScreenBottomSheet implements View.OnClickListener {
//
//
//    private Context context;
//    private int albumPosition;
//    private long album;
//    Toolbar toolbar;
//    //    private ImageButton shuffleAllButton;
////    private ImageButton selectButton;
////    private ImageButton optionButton;
////    private TextView title;
//    private ImageView imageView;
//    private RecyclerView recyclerView;
//
//    private int scrollPos = 0;
//
//    public static AlbumBottomFragment newInstance(long albumCode, String data, String text) {
//
//        final Bundle args = new Bundle();
//        args.putLong("name", albumCode);
//        args.putString("data", data);
//        args.putString("text", text);
//        args.putInt("type", 1);
//        final AlbumBottomFragment fragment = new AlbumBottomFragment();
//        fragment.setArguments(args);
////        fragment.setStyle(STYLE_NO_FRAME, android.support.design.R.style.Base_ThemeOverlay_AppCompat_Dark);
//        return fragment;
//    }
//
//    public static AlbumBottomFragment newInstance(int position) {
//
//        final AlbumBottomFragment fragment = new AlbumBottomFragment();
//        final Bundle args = new Bundle();
//        args.putInt("pos", position);
//        args.putInt("type", 0);
//        fragment.setArguments(args);
//        return fragment;
//
//
//    }
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        context = inflater.getContext();
//
//        return inflater.inflate(R.layout.album_bottom_sheet_new_design, container, false);
//    }
//
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//
//        switch (getArguments().getInt("type")) {
//            case 0:
//                albumPosition = getArguments().getInt("pos");
////                album = albumsList.get(albumPosition);
//                break;
//            case 1:
//
//                album = getArguments().getLong("name");
//
//                break;
//
//
//        }
//
//
//        recyclerView = view.findViewById(R.id.album_recycler);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);
//
//        recyclerView.setAdapter(new AlbumAdapterForFragment(album, new AlbumAdapterForFragment.OnMbnListClick() {
//            @Override
//            public void onClick(List<DataSong> albumList, DataSong song) {
//
//
//                MbnController.change(context, albumList, song);
//
//
//            }
//
//            @Override
//            public void onOptionClick(int position, View v) {
//
//            }
//        }, getContext()));
//
//        Bitmap bitmap = MbnController.getCoverForAlbums(getArguments().getString("data"));
//
//        imageView = view.findViewById(R.id.album_cover);
//
//        if (bitmap != null) {
//            imageView.setImageBitmap(bitmap);
//        }else {
//            imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.night_rain_2));
//        }
//
////
////        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
////            @Override
////            public void onScrolled(RecyclerView recy, int dx, int dy) {
////                super.onScrolled(recy, dx, dy);
////
////
////                scrollPos -= dy;
////
//////                Log.i("RECY", String.valueOf(recy.getScrollY()));
////
////                imageView.setTranslationY(scrollPos / 2);
////
////            }
////        });
//
//
//        orangeButtons(view);
////        orangeToolBar(view);
//
//
//    }
//
////    private void orangeToolBar(View view) {
////
////        CollapsingToolbarLayout toolbar = view.findViewById(R.id.collapingToolbar);
////
////        toolbar.setCollapsedTitleTextColor(Color.GRAY);
////
////        toolbar.setTitle(album.getName());
////
////        toolbar.setExpandedTitleColor(Color.WHITE);
////
////
////    }
//
//    private void orangeButtons(View view) {
//
//        toolbar = getView().findViewById(R.id.toolbar);
//
//        toolbar.setTitle(getArguments().getString("text"));
////        title = view.findViewById(R.id.textView_album);
//
//
////        title.setText(getArguments().getString("text"));
//
////        shuffleAllButton = view.findViewById(R.id.Button_shuffle);
////        optionButton = view.findViewById(R.id.Button_option);
////        selectButton = view.findViewById(R.id.Button_select);
//
////        shuffleAllButton.setOnClickListener(this);
////        optionButton.setOnClickListener(this);
////        selectButton.setOnClickListener(this);
//
//
//    }
//
//    @Override
//    public void onClick(View view) {
//
//        int id = view.getId();
//
//        switch (id) {
//            case R.id.Button_shuffle:
//
//                StorageUtils.setShuffle(context, true);
////                MbnController.change(context, album, null);
//
//                break;
//            case R.id.Button_select:
//
//                break;
//            case R.id.Button_option:
//
//                break;
//
//        }
//
//    }
//}
