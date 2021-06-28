//package com.br.mreza.musicplayer;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.design.widget.AppBarLayout;
//import android.support.design.widget.CollapsingToolbarLayout;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.ImageView;
//
//import static com.br.mreza.musicplayer.ListMaker.albumsList;
//
////import static com.br.mreza.musicplayer.ListMaker.albumContents;
//
//public class AlbumActivity extends AppCompatActivity {
//
////    ListView songsList;
////    ArrayAdapter listAdapter;
//
//    AppBarLayout appLayout;
//    ImageView album_art;
//    private int pos;
//
//
//    RecyclerView recyclerViewList;
//    Window window;
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.menu_album, menu);
//
//        return true;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_album);
//        CollapsingToolbarLayout toolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
//        toolbar.setCollapsedTitleTextColor(Color.BLACK);
//        Toolbar realTollBar = (Toolbar) findViewById(R.id.toolbarAlbum);
//        setSupportActionBar(realTollBar);
////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//
////        songsList = (ListView) findViewById(R.id.list_dynamic);
//
//        appLayout = (AppBarLayout) findViewById(R.id.app_bar);
//
//
////        setSupportActionBar(appLayout);
//
//        album_art = (ImageView) findViewById(R.id.album_cover_art_Image);
//
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//
////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        recyclerViewList = (RecyclerView) findViewById(R.id.recycleList);
//
//
//        Intent caller = getIntent();
//
//        pos = caller.getIntExtra("albumPos", 0);
//
////        List<String> songsName = new ArrayList<>();
//
//
//        final MbnAlbum album = albumsList.get(pos);
//
////        for (ListActivity.MbnSong song : songsFiles) {
////
////            songsName.add(song.getTitle());
////
////        }
////
////        System.out.println(songsName);
//
////        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songsName);
//
////        songsList.setAdapter(listAdapter);
//
//        try {
//
//            window = this.getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_FULLSCREEN);
////            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
////            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//            Bitmap b = album.getCover(this);
//            if (b != null) {
//                album_art.setImageBitmap(b);
//            }
//
//
//            appLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//                @Override
//                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//
//
////                    System.out.println(verticalOffset);
//                    album_art.setTranslationY(verticalOffset / 3);
//
////                    double scaleInt = 50000 + verticalOffset;
////                    double scale = scaleInt / 1000;
////                    scale = 51d - scale;
////
//////                    System.out.println(scaleInt+"----"+scale);
//////                    System.out.println(360 / 1000);
////
////
////                    album_art.setScaleX((float) scale);
////                    album_art.setScaleY((float) scale);
//
////                    try {
////                        float scale = -verticalOffset / 100;
////                        album_art.setScaleX(scale);
////                        album_art.setScaleY(scale);
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                    }
//
//                }
//            });
//
////            Drawable d = new BitmapDrawable(getResources(), b);
//
////            d.setBounds(0, 0, b.getWidth(), b.getHeight());
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                d.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
////            }
////            d.setLayoutDirection()
//
////            appLayout.setBackground(d);
//
////            appLayout.setBackgroundColor(MbnBitmapBucket.averageFinder(b));
//
////            window.setStatusBarColor(MbnBitmapBucket.averageFinder(b));
//
//
////            appLayout.setMinimumHeight(500);
//
//
//        } catch (Exception ignored) {
//        }
//
//
//        toolbar.setTitle(album.getName());
//
////        setSupportActionBar(toolbar);
//
//
////        songsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////
////                Intent intentOne = new Intent(getApplicationContext(), PlayerService.class);
////                intentOne.putExtra("alPos", pos);
////                intentOne.putExtra("sPos", position);
////
////                startService(intentOne);
////
////
////                Intent intentPlayer = new Intent(getApplicationContext(), PlayerActivity.class);
////
////                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AlbumActivity.this);
////
////
////                startActivity(intentPlayer,options.toBundle());
////
////
////            }
////        });
//
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                Intent intentOne = new Intent(getApplicationContext(), PlayerService.class);
////                intentOne.putExtra("alPos", pos);
////                intentOne.putExtra("sPos", 0);
////
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////                    startForegroundService(intentOne);
////                } else {
////                    startService(intentOne);
////                }
//
//
//                MbnController.change(getApplicationContext(), album, null);
//
////                PlayerFragment.newInstance().show(getSupportFragmentManager(), "MBN");
//
////                Intent intentPlayer = new Intent(getApplicationContext(), PlayerActivity.class);
//
////                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AlbumActivity.this);
//
////                startActivity(intentPlayer, options.toBundle());
//
//
//            }
//        });
////        songsList.setMinimumHeight(1000);
//
//
////        GridLayoutManager m = new GridLayoutManager(this, 4);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerViewList.setLayoutManager(layoutManager);
//        recyclerViewList.setHasFixedSize(true);
//
//
//        AlbumAdapter adapter = new AlbumAdapter(album, new AlbumAdapter.OnMbnListClick() {
//            @Override
//            public void onClick(String code, View view) {
//
//
////                Intent intentOne = new Intent(getApplicationContext(), PlayerService.class);
////                intentOne.putExtra("alPos", pos);
////                intentOne.putExtra("sPos", position);
////
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////                    startForegroundService(intentOne);
////                } else {
////                    startService(intentOne);
////                }
//
//                MbnController.change(getApplicationContext(), album, code);
//
////                Intent intentPlayer = new Intent(getApplicationContext(), PlayerActivity.class);
//
////                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AlbumActivity.this);
//
//
////                startActivity(intentPlayer, options.toBundle());
//
//            }
//
//            @Override
//            public void onOptionClick(int potion, View v) {
//
//
//            }
//        });
//
//        recyclerViewList.setAdapter(adapter);
//
//
//    }
//
//
//}
