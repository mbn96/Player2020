package com.br.mreza.musicplayer.p2020.design;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.audiofx.AudioEffect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.br.mreza.musicplayer.MBN.bitmapUtils.MbnUtils;
import com.br.mreza.musicplayer.R;
import com.br.mreza.musicplayer.StorageUtils;
import com.br.mreza.musicplayer.newdesign.MainFragment;
import com.br.mreza.musicplayer.newdesign.asyncloader.AsyncLoaderManager;
import com.br.mreza.musicplayer.newmodel.theme.ThemeEngine;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import mbn.libs.fragmanager.BaseActivity;
import mbn.libs.fragmanager.BaseFragment;
import mbn.libs.fragmanager.CropCenterDrawable;
import mbn.libs.fragmanager.CustomFragmentSwipeBackAnimator;

import static android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;


public class MainActivity2020 extends BaseActivity {

    private AudioManager audioManager;
//    private Rect backBounds = new Rect();
//    private CropCenterDrawable cropCenterDrawable = new CropCenterDrawable(true);

//    private Handler handler;
//
//    private Runnable garbageCollector = new Runnable() {
//        @Override
//        public void run() {
//            System.gc();
//            handler.postDelayed(this, 3000);
//        }
//    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
//        setContentView(R.layout.base_activity);
//
//        setSwipeManager((CustomFragmentSwipeBackAnimator) findViewById(R.id.base_container));
//        if (savedInstanceState == null) {
//            getSwipeManager().start(getSupportFragmentManager(), new MainFragment());
//            getSwipeManager().manageFragsStates();
//        }
//
////        progressBar = findViewById(R.id.first_page_load_indicator);
////        progressBar.setVisibility(View.GONE);
////        imageView = findViewById(R.id.back_of_all_app);
//        MediaSessionHolder.getMediaSession(getApplicationContext());

//        handler = new Handler(Looper.getMainLooper());
//        handler.post(garbageCollector);
    }

    @Override
    public int layoutID() {
        return R.layout.base_activity;
    }

    @Override
    public CustomFragmentSwipeBackAnimator introduceTheManager() {
        return findViewById(R.id.base_container);
    }

    @Override
    public BaseFragment getFirstFragment() {
        return new MainFragment2020();
//        return new MainFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        getSwipeManager().setActivityStarted(true);
    }


    @Override
    protected void onStop() {
        super.onStop();
//        getSwipeManager().setActivityStarted(false);
    }

//    @Override
//    public void onChange(Bitmap original, Bitmap blur) {
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
////            Palette.from(original).generate(new Palette.PaletteAsyncListener() {
////                @Override
////                public void onGenerated(Palette palette) {
////                    getWindow().setBackgroundDrawable(new ColorDrawable(palette.getDominantColor(Color.LTGRAY)));
////                }
////            });
//        if (StorageUtils.getThemeType() == 0) {
//            cropCenterDrawable.setBitmap(blur);
//            getWindow().setBackgroundDrawable(cropCenterDrawable);
////            getWindow().setBackgroundDrawable(null);
//        }
////        }
//    }

//    private ThemeEngine.DefaultThemeCallback themeCallback = new ThemeEngine.DefaultThemeCallback(false, true, true) {
//        @Override
//        public void onProcessFinished() {
//
//        }
//
//        @Override
//        public void onAccentColor(int color) {
//            if (StorageUtils.getThemeType() == 1) {
//                getWindow().setNavigationBarColor(MbnUtils.colorWhitener2(color, 30));
//            }
//        }
//
//        @Override
//        public void onBlurBitmap(Bitmap blur) {
//            if (StorageUtils.getThemeType() == 0) {
//                cropCenterDrawable.setBitmap(blur);
//                getWindow().setBackgroundDrawable(cropCenterDrawable);
////            getWindow().setBackgroundDrawable(null);
//            }
//        }
//    };

//    private Rect getBackRect(Bitmap bitmap) {
////        int screenWidth = getWindow().getDecorView().getWidth();
//        int screenWidth = getResources().getDisplayMetrics().widthPixels;
////        int screenHeight = getWindow().getDecorView().getHeight();
//        int screenHeight = getResources().getDisplayMetrics().heightPixels;
//
//        float factor = Math.max(((float) screenWidth) / bitmap.getWidth(), ((float) screenHeight) / bitmap.getHeight());
//
//        int useW = (int) (factor * bitmap.getWidth());
//        int useH = (int) (factor * bitmap.getHeight());
//
//        int diffHalfWidth = (useW - screenWidth) / 2;
//        int diffHalfHeight = (useH - screenHeight) / 2;
//
//        backBounds.set(-diffHalfWidth, -diffHalfHeight, screenWidth + diffHalfWidth, screenHeight + diffHalfHeight);
//        return backBounds;
//    }


    @Override
    protected void onResume() {
        super.onResume();
//        getSwipeManager().manageFragsStates();
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
////            getWindow().getAttributes().
//        }

        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN );
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//            getWindow().setNavigationBarColor(Color.argb(190, 255, 255, 255));


        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
        }
         */
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN /* For low profile */   /* | View.SYSTEM_UI_FLAG_LOW_PROFILE*/);

        getWindow().setStatusBarColor(Color.TRANSPARENT);

        if (StorageUtils.getThemeType() == 0) {
            getWindow().setNavigationBarColor(Color.WHITE);

//            getWindow().setNavigationBarColor(Color.argb(210, 255, 255, 255));
//            BackImageChangeHandler.instance.addCallback(this);
//            BackImageChangeHandler.instance.force(getApplicationContext());
        } else {
            getWindow().setBackgroundDrawable(null);
            getWindow().setNavigationBarColor(Color.WHITE);

        }

//        ThemeEngine.getINSTANCE().registerCallback(themeCallback);
//        ThemeEngine.getINSTANCE().getResult(themeCallback);

        reg();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        getSwipeManager().onPause();

//        ThemeEngine.getINSTANCE().unRegisterCallback(themeCallback);
        unReg();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ClientManager.closeClient(getApplicationContext());
//        handler.removeCallbacks(garbageCollector);
    }


//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        if (savedInstanceState == null) {
//            getSwipeManager().start(getSupportFragmentManager(), new MainFragment());
//            getSwipeManager().manageFragsStates();
//        } else {
//            getSwipeManager().resumeFromSaveState(savedInstanceState, getSupportFragmentManager());
//        }
//        super.onRestoreInstanceState(savedInstanceState);
//    }
//
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        getSwipeManager().createSavedInstanceState(outState);
//        super.onSaveInstanceState(outState);
//
//    }


    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
//    private SectionsPagerAdapter mSectionsPagerAdapter;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
//    private MbnVerticalViewPager mViewPager;
//    private LinearLayout linearLayout;
//    private LinearLayout bottomLayout;
//    private ImageButton previous;
//    private ImageButton play;
//    private ImageButton next;
//    private NavigationView navigationView;

//    private AdvancedMbnBlur mbnBlur = new AdvancedMbnBlur();

//    ImageView imageView;
    Bitmap backBit;
    String songCode = "";
//    ProgressBar progressBar;


    public final static String REFRESH_KEY = "DO_REFRESH_NOW";
    private LocalBroadcastManager manager;
    private BottomSheetBehavior bottomSheetBehavior;
    private View fragUnder;


//    public static class LoaderAsync extends AsyncTask<Void, Void, Void> {
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
////            System.out.println("iiiiiiiiiiiiiiiiiiii  start");
//
////            finder();
//
////            Comparator<MbnSong> comparator = new Comparator<MbnSong>() {
////
////
////                @Override
////                public int compare(MbnSong o1, MbnSong o2) {
////
////
////                    return o1.getTitle().compareTo(o2.getTitle());
////                }
////
////            };
////            Collections.sort(tempList, comparator);
//
////            DataBaseHolder.getInstance(getApplicationContext()).getSongDAO().clearSongsTable();
////
////            DataBaseHolder.getInstance(getApplicationContext()).getSongDAO().addSongs(tempDataSongList);
////            tempDataSongList.clear();
////            tempDataSongList = null;
////
////            DataBaseHolder.getInstance(getApplicationContext()).getSongDAO().clearAlbumsTable();
////
//////            for (String albumName : tempDataAlbumNamesList) {
//////                tempDataAlbumList.add(new DataBaseAlbum(albumName));
//////            }
////
////            DataBaseHolder.getInstance(getApplicationContext()).getSongDAO().addAlbum(tempDataAlbumList);
////            tempDataAlbumList.clear();
////            tempDataAlbumList = null;
////            tempDataAlbumNamesList.clear();
////            tempDataAlbumNamesList = null;
//
//
////            System.out.println(DataBaseHolder.getInstance(getApplicationContext()).getSongDAO().loadAlbum("black ice").get(0).getTitle());
//
//
////            System.out.println(DataBaseHolder.getInstance(getApplicationContext()).getSongDAO().loadAlbum("black ice").get(0).getTitle());
//
////            System.out.println("iiiiiiiiiiiiiiii  mid");
//
////            ListMaker.listReceiver(getApplicationContext());
//
////            System.out.println("iiiiiiiiiiiiiiiii  end");
//
//
//            return null;
//        }
//
////        @Override
////        protected void onPostExecute(Void aVoid) {
////            super.onPostExecute(aVoid);
////
//////            System.out.println("iiiiiiiiiiii  in post 1");
//////            mViewPager.setAdapter(mSectionsPagerAdapter);
////
////
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                getWindow().setStatusBarColor(Color.argb(0, 0, 0, 0));
////            }
//////        getWindow().setStatusBarColor(Color.parseColor("#50f5f5f5"));
////
//////        orangeBottom();
////
////            mbnBlur.setHelper(new AdvancedMbnBlur.MbnAdvancedBlurHelper() {
////                @Override
////                public void onFinish(Bitmap blurred) {
////                    backBit = blurred;
////
////                    imageView.animate().alpha(0f).setDuration(100).start();
////                    imageView.postDelayed(new Runnable() {
////                        @Override
////                        public void run() {
////                            imageView.setImageBitmap(backBit);
////                            imageView.animate().alpha(1f).setDuration(600).start();
////                        }
////                    }, 100);
////
////
////                }
////            });
////
////            try {
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                    mViewPager.setBackgroundColor(getResources().getColor(R.color.player_back_color, getTheme()));
////                } else {
////                    mViewPager.setBackgroundColor(getResources().getColor(R.color.player_back_color));
////
////                }
////            } catch (Resources.NotFoundException e) {
////                e.printStackTrace();
////            }
////            onChange();
//////                imageView.setVisibility(View.VISIBLE);
////
////
////            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
////
////                @Override
////                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
////
////                }
////
////                @Override
////                public void onPageSelected(int position) {
////
////                    switch (position) {
////                        case 1:
////                            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
////                            playerFragmentIsOn = true;
////                            break;
////                        case 0:
////                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
////                            playerFragmentIsOn = false;
////                            break;
////                    }
////                }
////
////                @Override
////                public void onPageScrollStateChanged(int state) {
////
////                }
////            });
////
////
//////            SwipeRefreshLayout refreshLayout = findViewById(R.id.refresh_swipe_down);
////
//////            refreshLayout.setRefreshing(false);
////
////            progressBar.setVisibility(View.GONE);
////
////            DataBaseHolder.forceRefresh(getApplicationContext());
////
//////            System.out.println("iiiiiiiiiiiiiii in post 2");
////        }
//
//    }


//    @Override

//    protected void onCreate(Bundle savedInstanceState) {
////        Emojiconize.activity(this).go();
//        super.onCreate(savedInstanceState);
////        BackImageChangeHandler.instance.start(getApplication());
////        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
////        getWindow().setExitTransition(new Fade(Fade.OUT));
////        getWindow().setExitTransition(new Slide(GravityCompat.START));
//        setContentView(R.layout.activity_first_content_for_image_background);
//        progressBar = findViewById(R.id.first_page_load_indicator);
//        progressBar.setVisibility(View.GONE);
//        imageView = findViewById(R.id.back_of_all_app);
//
////        imageView.setVisibility(View.INVISIBLE);
//
//        final View fragTop = findViewById(R.id.fragment2);
//
//        final MbnImageButtonChangeIcon buttonChangeIcon = fragTop.findViewById(R.id.player_frag_top_play_button);
//
//        bottomSheetBehavior = BottomSheetBehavior.from(fragTop);
//
//        bottomSheetBehavior.setPeekHeight((int) (getResources().getDisplayMetrics().density * 70));
//
//        fragUnder = findViewById(R.id.fragment);
//
//
//        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//
//                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
//                    fragUnder.setVisibility(View.INVISIBLE);
//                }
//
//                switch (newState) {
//
//                    case BottomSheetBehavior.STATE_COLLAPSED:
//                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                        buttonChangeIcon.setVisibility(View.VISIBLE);
//
//                        break;
//                    case BottomSheetBehavior.STATE_EXPANDED:
//                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                        buttonChangeIcon.setVisibility(View.INVISIBLE);
//                        break;
//                }
//
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//
//                fragUnder.setVisibility(View.VISIBLE);
//                fragUnder.setAlpha(1 - slideOffset);
//                buttonChangeIcon.setAlpha(1 - slideOffset);
//                fragUnder.setTranslationY(-slideOffset * fragUnder.getHeight() / 3);
//            }
//        });
//
//
//        Log.i("sttttt", String.valueOf(bottomSheetBehavior.getState()));
//
//        bottomSheetBehavior.setState(bottomSheetBehavior.getState());
//
////        Toast.makeText(this, currentTrackCode, Toast.LENGTH_LONG).show();
////
////        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
////        toolbar.setTitle("MBN Music Player");
//////        toolbar.setTitleTextColor(Color.BLACK);
//////        toolbar.setSubtitleTextColor(Color.BLACK);
////
////        setSupportActionBar(toolbar);
//
////        linearLayout = (LinearLayout) findViewById(R.id.main_content);
////        bottomLayout = (LinearLayout) findViewById(R.id.now_paying_bottom_layout);
////        previous = (ImageButton) findViewById(R.id.now_playing_preBton);
//
//
//        // Create the Adapter that will return a fragment for each of the three
//        // primary sections of the activity.
////        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//
//        // Set up the ViewPager with the sections Adapter.
////        mViewPager = findViewById(R.id.verticalViewPager);
//
//
//        MediaSessionHolder.getMediaSession(getApplicationContext());
//
////        new AsyncTask<Void, Void, Void>() {
////            @Override
////            protected Void doInBackground(Void... voids) {
////
////                finder();
////
////                Comparator<MbnSong> comparator = new Comparator<MbnSong>() {
////
////
////                    @Override
////                    public int compare(MbnSong o1, MbnSong o2) {
////
////
////                        return o1.getTitle().compareTo(o2.getTitle());
////                    }
////
////                };
////                Collections.sort(tempList, comparator);
////
////                ListMaker.listReceiver(tempList, getApplicationContext());
////
////
////                return null;
////            }
////
////            @Override
////            protected void onPostExecute(Void aVoid) {
////                super.onPostExecute(aVoid);
////                mViewPager.setAdapter(mSectionsPagerAdapter);
////
////
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                    getWindow().setStatusBarColor(Color.argb(0, 0, 0, 0));
////                }
//////        getWindow().setStatusBarColor(Color.parseColor("#50f5f5f5"));
////
//////        orangeBottom();
////
////                mbnBlur.setHelper(new AdvancedMbnBlur.MbnAdvancedBlurHelper() {
////                    @Override
////                    public void onFinish(Bitmap blurred) {
////                        backBit = blurred;
////
////                        imageView.animate().alpha(0f).setDuration(100).start();
////                        imageView.postDelayed(new Runnable() {
////                            @Override
////                            public void run() {
////                                imageView.setImageBitmap(backBit);
////                                imageView.animate().alpha(1f).setDuration(600).start();
////                            }
////                        }, 100);
////
////
////                    }
////                });
////
////                try {
////                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                        mViewPager.setBackgroundColor(getResources().getColor(R.color.player_back_color, getTheme()));
////                    } else {
////                        mViewPager.setBackgroundColor(getResources().getColor(R.color.player_back_color));
////
////                    }
////                } catch (Resources.NotFoundException e) {
////                    e.printStackTrace();
////                }
////                onChange();
//////                imageView.setVisibility(View.VISIBLE);
////
////
////                mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
////
////                    @Override
////                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
////
////                    }
////
////                    @Override
////                    public void onPageSelected(int position) {
////
////                        switch (position) {
////                            case 1:
////                                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
////                                playerFragmentIsOn = true;
////                                break;
////                            case 0:
////                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
////                                playerFragmentIsOn = false;
////                                break;
////                        }
////                    }
////
////                    @Override
////                    public void onPageScrollStateChanged(int state) {
////
////                    }
////                });
////
////                progressBar.setVisibility(View.GONE);
////
////            }
////        }.execute();
//
//
////        mViewPager.setAdapter(mSectionsPagerAdapter);
//
////        mViewPager.setOffscreenPageLimit(2);
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(Color.argb(0, 0, 0, 0));
//        }
////        getWindow().setStatusBarColor(Color.parseColor("#50f5f5f5"));
//
////        orangeBottom();
//
////        mbnBlur.setHelper(new AdvancedMbnBlur.MbnAdvancedBlurHelper() {
////            @Override
////            public void onFinish(Bitmap blurred) {
////                backBit = blurred;
////
////                imageView.animate().alpha(0f).setDuration(100).start();
////                imageView.postDelayed(new Runnable() {
////                    @Override
////                    public void run() {
////                        imageView.setImageBitmap(backBit);
////                        imageView.animate().alpha(1f).setDuration(600).start();
////                    }
////                }, 100);
////
////
////            }
////        });
//
////        try {
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                mViewPager.setBackgroundColor(getResources().getColor(R.color.player_back_color, getTheme()));
////            } else {
////                mViewPager.setBackgroundColor(getResources().getColor(R.color.player_back_color));
////
////            }
////        } catch (Resources.NotFoundException e) {
////            e.printStackTrace();
////        }
//
//
////        onChange();
////                imageView.setVisibility(View.VISIBLE);
//
//
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//
//
////        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
////
////            @Override
////            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
////
////            }
////
////            @Override
////            public void onPageSelected(int position) {
////
////                switch (position) {
////
////                    case 0:
////                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
////                        playerFragmentIsOn = false;
////                        break;
////                    default:
////                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
////                        playerFragmentIsOn = true;
////                        break;
////                }
////            }
////
////            @Override
////            public void onPageScrollStateChanged(int state) {
////
////            }
////        });
//
//
////        SwipeRefreshLayout refreshLayout = findViewById(R.id.refresh_swipe_down);
//
////            refreshLayout.setRefreshing(false);
//
//
//
////        new DataLoaderAsync.Loader().execute(getApplicationContext());
//
//
////        mViewPager.setAdapter(mSectionsPagerAdapter);
////        imageView = (ImageView) findViewById(R.id.back_of_all_app);
////
////
////        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
////
////            @Override
////            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
////
////            }
////
////            @Override
////            public void onPageSelected(int position) {
////
////                switch (position) {
////                    case 1:
////                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
////                        playerFragmentIsOn = false;
////                        break;
////                    case 0:
////                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
////                        playerFragmentIsOn = true;
////                        break;
////                }
////            }
////
////            @Override
////            public void onPageScrollStateChanged(int state) {
////
////            }
////        });
////
////
//////        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//////        tabLayout.setupWithViewPager(mViewPager);
////
////
//////        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//////        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//////                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//////        drawer.setDrawerListener(toggle);
//////        toggle.syncState();
//////
//////        navigationView = (NavigationView) findViewById(R.id.nav_view);
//////        navigationView.setNavigationItemSelectedListener(this);
//////
//////        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
//////            @Override
//////            public void onDrawerSlide(View drawerView, float slideOffset) {
//////
//////
////////                linearLayout.setTranslationX((slideOffset * navigationView.getWidth()));
//////
//////
//////                System.out.println(slideOffset);
//////
//////            }
//////
//////            @Override
//////            public void onDrawerOpened(View drawerView) {
//////
//////            }
//////
//////            @Override
//////            public void onDrawerClosed(View drawerView) {
//////
//////            }
//////
//////            @Override
//////            public void onDrawerStateChanged(int newState) {
//////
//////            }
//////        });
////
//////        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//////        fab.setOnClickListener(new View.OnClickListener() {
//////            @Override
//////            public void onClick(View view) {
//////
//////                if (PlayerService.player!=null) {
//////                    Intent intentPlayer = new Intent(getApplicationContext(), PlayerActivity.class);
//////
//////                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(FirstActivity.this);
//////
//////
//////                    startActivity(intentPlayer, options.toBundle());
//////                }
////////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////////                        .setAction("Action", n).show();
//////            }
//////        });
////
//////        getSupportActionBar().setTitle("MBN Music Player");
////
////
//////        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_fist_activity);
//////        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//////            @Override
//////            public void onClick(View view) {
//////                PlayerFragment.newInstance().show(getSupportFragmentManager(), "MBN");
//////            }
//////        });
////
//////        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
////
////
//////        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
////
////        //        DrawerLayout drawer = view.findViewById(R.id.drawer_layout);
//////        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//////                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//////        drawer.setDrawerListener(toggle);
//////        toggle.syncState();
//////
//////        navigationView = view.findViewById(R.id.nav_view);
//////        navigationView.setNavigationItemSelectedListener(this);
//////
//////        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
//////            @Override
//////            public void onDrawerSlide(View drawerView, float slideOffset) {
//////
//////
//////                linearLayout.setTranslationX((slideOffset * navigationView.getWidth()));
//////
//////
//////                System.out.println(slideOffset);
//////
//////            }
//////
//////            @Override
//////            public void onDrawerOpened(View drawerView) {
//////
//////            }
//////
//////            @Override
//////            public void onDrawerClosed(View drawerView) {
//////
//////            }
//////
//////            @Override
//////            public void onDrawerStateChanged(int newState) {
//////
//////            }
//////        });
////
////        getWindow().setStatusBarColor(Color.argb(0, 0, 0, 0));
//////        getWindow().setStatusBarColor(Color.parseColor("#50f5f5f5"));
////
//////        orangeBottom();
////
////        mbnBlur.setHelper(new AdvancedMbnBlur.MbnAdvancedBlurHelper() {
////            @Override
////            public void onFinish(Bitmap blurred) {
////                backBit = blurred;
////
////                imageView.animate().alpha(0f).setDuration(100).start();
////                imageView.postDelayed(new Runnable() {
////                    @Override
////                    public void run() {
////                        imageView.setImageBitmap(backBit);
////                        imageView.animate().alpha(1f).setDuration(600).start();
////                    }
////                }, 100);
////
////
////            }
////        });
//
//
//    }

//    public static void finder(Context context) {
//
////        ListMaker.listReceiver(getApplicationContext());
//
//
////        tempList = new ArrayList<>();
//
//
//        List<DataSong> tempDataSongList = new ArrayList<>();
//        List<String> tempDataAlbumNamesList = new ArrayList<>();
//        List<DataBaseAlbum> tempDataAlbumList = new ArrayList<>();
//
//        ContentResolver contentResolver = context.getContentResolver();
//
//        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
//        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
//        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);
//
//
//        tempDataAlbumNamesList.clear();
//        tempDataAlbumList.clear();
//        tempDataSongList.clear();
//
//        if (cursor != null && cursor.getCount() > 0) {
//            while (cursor.moveToNext()) {
//                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
//                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
//                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
//                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
//                int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
//                long date = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));
//                long albumID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
//
//
////                tempList.add(new MbnSong(title, album, data, artist, duration, date));
//                tempDataSongList.add(new DataSong(title, duration, artist, album, data, date, albumID));
//
//                if (!tempDataAlbumNamesList.contains(album)) {
//                    tempDataAlbumNamesList.add(album);
//                    tempDataAlbumList.add(new DataBaseAlbum(album, data, albumID));
//                }
//
//
////                        audioList.add(new Audio(data, title, album, artist));
//            }
//        }
//        assert cursor != null;
//        cursor.close();
//
//
//        DataBaseHolder.getInstance(context).getSongDAO().clearSongsTable();
//
//        DataBaseHolder.getInstance(context).getSongDAO().addSongs(tempDataSongList);
//        tempDataSongList.clear();
//        tempDataSongList = null;
//
//        DataBaseHolder.getInstance(context).getSongDAO().clearAlbumsTable();
//
////            for (String albumName : tempDataAlbumNamesList) {
////                tempDataAlbumList.add(new DataBaseAlbum(albumName));
////            }
//
//        DataBaseHolder.getInstance(context).getSongDAO().addAlbum(tempDataAlbumList);
//        tempDataAlbumList.clear();
//        tempDataAlbumList = null;
//        tempDataAlbumNamesList.clear();
//        tempDataAlbumNamesList = null;
//
//
//        DataBaseHolder.forceRefresh(context);
//
//
//        ListMaker.listReceiver(context);
//
//
////            System.out.println("finder");
//
////            File[] files = rawFile.listFiles();
////
//////            System.out.println(files[3]);
////
////
////            if (files.length > 0) {
////
////                for (File song : files) {
////
////                    if (song.isDirectory()) {
////
////                        finder(song);
////                    } else {
////
////                        if (song.getName().toLowerCase().endsWith(pattern)) {
////
////                            list.add(new MbnSong(song));
////
////                            counter++;
////
////                            publishProgress(counter);
//////                            System.out.println(song.getName());
////
////
////                        }
////
////
////                    }
////
////
////                }
////            }
//
//
//    }


//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putBoolean("EXP", bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED);
//    }


//    @Override
//    protected void onResume() {
//        super.onResume();
////        onChange();
////        BackImageChangeHandler.instance.start(getApplication());
//        BackImageChangeHandler.instance.addCallback(this);
//        BackImageChangeHandler.instance.force(getApplicationContext());
//
//        reg();
//    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        if (savedInstanceState != null) {
//            if (savedInstanceState.getBoolean("EXP", false)) {
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                fragUnder.setVisibility(View.INVISIBLE);
//
//            }
//
//        }
//    }

    //    private void onChange() {
//
//        try {
//            if (!songCode.equals(currentTrack.getPath())) {
//
//                songCode = "" + currentTrack.getPath();
//
//                mbnBlur.makeBlur(this, currentTrack.getBlurredCover());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        BackImageChangeHandler.instance.removeCallback(this);
//        unReg();
//    }

    private void reg() {

//        IntentFilter filter = new IntentFilter(To_FRAGMENT);

        manager = LocalBroadcastManager.getInstance(getApplicationContext());

//        manager.registerReceiver(receiver, filter);

        manager.registerReceiver(refreshReceiver, new IntentFilter(REFRESH_KEY));

    }

    private void unReg() {


//        manager.unregisterReceiver(receiver);

        manager.unregisterReceiver(refreshReceiver);

    }

    private BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

//            new LoaderAsync().execute();
//            finder(getApplicationContext());

            AsyncLoaderManager.INSTANCE.refresh(getApplicationContext());

//            Comparator<MbnSong> comparator = new Comparator<MbnSong>() {
//
//
//                @Override
//                public int compare(MbnSong o1, MbnSong o2) {
//
//
//                    return o1.getTitle().compareTo(o2.getTitle());
//                }
//
//            };
//            Collections.sort(tempList, comparator);


        }
    };

//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
////            onChange();
//
//        }
//    };


    //    private void orangeBottom() {
//
//
//        bottomLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                PlayerFragment.newInstance().show(getSupportFragmentManager(), "nowPlaying");
//
//            }
//        });
//
//
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_first, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        try {
            Intent intent = new Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION);
            intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
//            intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, audioID);
//            intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, mediaPlayer.getAudioSessionId());

            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
//            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_setting) {
//            // Handle the camera action
//        } else if (id == R.id.nav_timer) {
//
//            TimerFragment.newInstance().show(getSupportFragmentManager(), "timer");
//
//        } else if (id == R.id.nav_equ) {
//
//
//            Intent intent = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
////            intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
//            intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, audioID);
//            if (intent.resolveActivity(getPackageManager()) != null) {
//
//                startActivityForResult(intent, 9919);
//
//            }
//
//        } else if (id == R.id.nav_cutter) {
//
//        }
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            } else {
//                super.onBackPressed();
//            }
//        }
//    }

    /**
     * A placeholder fragment containing a simple view.
     */
//    public static class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//        private RecyclerView list;
//        private MbnRecyclerFastScroller scroller;
//        private TextView textViewFastScroll;
//
//        public PlaceholderFragment() {
//        }
//
//        @Override
//        public void onPause() {
//            super.onPause();
//
//            System.out.println("frag paused " + getArguments().getInt(ARG_SECTION_NUMBER));
//
//
//        }
//
//
//        @Override
//        public void onStop() {
//
//            System.out.println("frag Stopped " + getArguments().getInt(ARG_SECTION_NUMBER));
//            super.onStop();
//        }
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            final View rootView;
//            if (getArguments().getInt(ARG_SECTION_NUMBER) == 5) {
//
//                rootView = inflater.inflate(R.layout.first_search_fragment, container, false);
//
//                RecyclerView recyclerView = rootView.findViewById(R.id.search_recycle);
//                EditText editText = rootView.findViewById(R.id.search_editText);
//
//                searchTabMaker(recyclerView, editText);
//
//            } else {
//
//                rootView = inflater.inflate(R.layout.fragment_first, container, false);
//                list = rootView.findViewById(R.id.first_page_recycler_view);
//                scroller = rootView.findViewById(R.id.fast_scroll_main_page);
//                scroller.setVisibility(View.GONE);
//                textViewFastScroll = rootView.findViewById(R.id.fast_scroll_text);
//                textViewFastScroll.setVisibility(View.GONE);
//
//
//                if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
//                    albumTabMaker();
//                } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
//                    allSongsTabMaker();
//                    scroller.setTextView(textViewFastScroll);
//                    scroller.setVisibility(View.VISIBLE);
//                } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
//                    playlistTabMaker();
//                } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 4) {
//                    nowPlayingTabMaker();
//                }
//
//                scroller.setRecyclerView(list);
//            }
//
//            return rootView;
//        }
//
//        private void handleSearchClicks(MbnSearchEngine.SearchItem item) {
//
//            int type = item.getType();
//
//            switch (type) {
//                case 1:
//
//                    MbnController.change(getContext(), allSongsAlbum, item.getSong());
//
//                    break;
//                case 2:
//
//                    AlbumBottomFragment.newInstance(item.getAlbum().getHashCode()).show(getFragmentManager(), "album");
//
//                    break;
//
//
//            }
//
//
//        }
//
//        private void searchTabMaker(RecyclerView recyclerView, final EditText editText) {
//
//            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//            recyclerView.setLayoutManager(layoutManager);
//            recyclerView.setHasFixedSize(false);
//
//            final MbnSearchEngine searchEngine = new MbnSearchEngine();
//
//            final SearchTabAdapter adapter = new SearchTabAdapter(getContext());
//
//            recyclerView.setAdapter(adapter);
//
//            adapter.setListener(new SearchTabAdapter.SearchTabListener() {
//                @Override
//                public void onClick(MbnSearchEngine.SearchItem item) {
//                    handleSearchClicks(item);
//
//                }
//            });
//
//
//            editText.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    searchEngine.inPut(editText.getText().toString());
//
//                    adapter.setSearchItemArrayList(searchEngine.getList());
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//
//                }
//            });
//
//
//        }
//
//        private void playlistTabMaker() {
//
//            GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
//
//            list.setLayoutManager(manager);
//
//            list.setHasFixedSize(true);
//            list.setAdapter(new FirstPageAdapterTabAlbums(getContext(), playLists, new FirstPageAdapterTabAlbums.AlbumTabListener() {
//
//                @Override
//                public void onClick(int position, View view) {
//
//                    Intent albumActivity = new Intent(getActivity(), AlbumActivity.class);
//
//                    albumActivity.putExtra("albumPos", position);
//
//                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "img_art_trans");
//
//
//                    startActivity(albumActivity, options.toBundle());
//
//                }
//
//                @Override
//                public void onOptionClick(final int potionList, final View v) {
//
//
//                    String[] listItems = {"Play next", "Set as...", "Add to playlist"};
//                    final ListPopupWindow popupWindowCompat = new ListPopupWindow(getActivity());
//
//                    popupWindowCompat.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems));
//
//                    popupWindowCompat.setAnchorView(v);
//                    popupWindowCompat.setWidth(android.support.v7.widget.ListPopupWindow.WRAP_CONTENT);
//                    popupWindowCompat.setHeight(android.support.v7.widget.ListPopupWindow.WRAP_CONTENT);
//                    popupWindowCompat.setModal(true);
//                    popupWindowCompat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                            Intent albumActivity = new Intent(getActivity(), AlbumActivity.class);
//
//                            albumActivity.putExtra("albumPos", potionList);
//
//                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), v, "img_art_trans");
//
//
//                            startActivity(albumActivity, options.toBundle());
//
//
//                            popupWindowCompat.dismiss();
//                        }
//                    });
//                    popupWindowCompat.show();
//
//
//                }
//            }));
//
//
//        }
//
//        private void allSongsTabMaker() {
//
//
//            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//            list.setLayoutManager(layoutManager);
//            list.setHasFixedSize(true);
//            list.setAdapter(new FirstPageAdapterTabAllSongs(allSongsCodes, new FirstPageAdapterTabAllSongs.AllSongsListener() {
//                @Override
//                public void onClick(int position, String songCode) {
//
//
//                    MbnController.change(getContext(), allSongsAlbum, songCode);
//
//                }
//
//                @Override
//                public void onOption(int position, String songCode) {
//
//                }
//            }));
//        }
//
//        private void nowPlayingTabMaker() {
//
//
//            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//            list.setLayoutManager(layoutManager);
//            list.setHasFixedSize(true);
//            list.setAdapter(new FirstPageAdapterTabAllSongs(currentQueue, new FirstPageAdapterTabAllSongs.AllSongsListener() {
//                @Override
//                public void onClick(int position, String songCode) {
//
//
//                    MbnController.change(getContext(), currentQueue, songCode);
//
//                }
//
//                @Override
//                public void onOption(int position, String songCode) {
//
//                }
//            }));
//
//
//        }
//
//        private void albumTabMaker() {
//
//
//            GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
//
//            list.setLayoutManager(manager);
//
//            list.setHasFixedSize(true);
//            list.setAdapter(new FirstPageAdapterTabAlbums(getContext(), albumsList, new FirstPageAdapterTabAlbums.AlbumTabListener() {
//
//                @Override
//                public void onClick(int position, View view) {
//
//                    AlbumBottomFragment.newInstance(position).show(getFragmentManager(), "album");
//
////                    Intent albumActivity = new Intent(getActivity(), AlbumActivity.class);
////
////                    albumActivity.putExtra("albumPos", position);
////
////                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "img_art_trans");
////
////
////                    startActivity(albumActivity, options.toBundle());
//
//                }
//
//                @Override
//                public void onOptionClick(final int potionList, final View v) {
//
//
//                    String[] listItems = {"Play next", "Set as...", "Add to playlist"};
//                    final ListPopupWindow popupWindowCompat = new ListPopupWindow(getActivity());
//
//                    popupWindowCompat.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems));
//
//                    popupWindowCompat.setAnchorView(v);
//                    popupWindowCompat.setWidth(android.support.v7.widget.ListPopupWindow.WRAP_CONTENT);
//                    popupWindowCompat.setHeight(android.support.v7.widget.ListPopupWindow.WRAP_CONTENT);
//                    popupWindowCompat.setModal(true);
//                    popupWindowCompat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                            Intent albumActivity = new Intent(getActivity(), AlbumActivity.class);
//
//                            albumActivity.putExtra("albumPos", potionList);
//
//                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), v, "img_art_trans");
//
//
//                            startActivity(albumActivity, options.toBundle());
//
//
//                            popupWindowCompat.dismiss();
//                        }
//                    });
//                    popupWindowCompat.show();
//
//
//                }
//            }));
//
//        }
//
//    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
//    private class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//        SectionsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            switch (position) {
//
//                case 0:
//                    return ListsFragment.newInstance();
//                case 1:
//                    return PlayerFragmentNewDesign.newInstance();
////                    return PlayerFragment.newInstance();
//                case 2:
////                    return CurrentQueueFragment.newInstance();
//
//
//            }
//            // getItem is called to instantiate the fragment for the given page.
//            // Return a PlaceholderFragment (defined as a static inner class below).
//            return null;
//        }
//
//        @Override
//        public int getCount() {
//            // Show 3 total pages.
//            return 2;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "Albums";
//                case 1:
//                    return "All songs";
//                case 2:
//                    return "PlayLists";
//                case 4:
//                    return "Search";
//                case 3:
//                    return "Now Playing";
//            }
//            return null;
//        }
//    }


}
