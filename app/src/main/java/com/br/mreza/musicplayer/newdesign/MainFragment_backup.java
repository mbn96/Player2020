//package com.br.mreza.musicplayer.newdesign;
//
//import android.annotation.SuppressLint;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Bitmap;
//import android.media.audiofx.AudioEffect;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.NavigationView;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.content.LocalBroadcastManager;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.view.ViewPager;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.br.mreza.musicplayer.BackgroundForUI.BackImageChangeHandler;
//import com.br.mreza.musicplayer.DataBaseHolder;
//import com.br.mreza.musicplayer.DataSong;
//import com.br.mreza.musicplayer.FirstPageAdapterTabAlbums;
//import com.br.mreza.musicplayer.FirstPageAdapterTabAllSongs;
//import com.br.mreza.musicplayer.MBN.customViews.MbnRecyclerFastScroller;
//import com.br.mreza.musicplayer.MBN.customViews.MbnScrollingTextView;
//import com.br.mreza.musicplayer.MbnController;
//import com.br.mreza.musicplayer.MbnSearchEngine;
//import com.br.mreza.musicplayer.PlayListsBottomFragment;
//import com.br.mreza.musicplayer.R;
//import com.br.mreza.musicplayer.SearchTabAdapter;
//import com.br.mreza.musicplayer.UI.RecyclerViews.ItemDecors.BlurDecor;
//import com.br.mreza.musicplayer.fragmanager.BaseFragment;
//import com.br.mreza.musicplayer.fragmanager.CustomFragmentSwipeBackAnimator;
//
//import java.util.List;
//
//import static com.br.mreza.musicplayer.DataBaseHolder.LOADING_DONE;
//import static com.br.mreza.musicplayer.FirstActivity.REFRESH_KEY;
//import static com.br.mreza.musicplayer.ListMaker.currentTrack;
//
//
//public class MainFragment_backup extends BaseFragment implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, BackImageChangeHandler.ImageChangeCallback {
//
//    private LinearLayout nowPlayingLayout;
//    private MbnScrollingTextView nowPlayingTitle;
//    private ImageView cover;
//
//    private BroadcastReceiver trackChange = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            nowPlayingTitle.setText(currentTrack.getTitle());
//        }
//    };
//
//    @Override
//    public void onChange(Bitmap original, Bitmap blur) {
//        cover.setImageBitmap(blur);
//    }
//
//    @Override
//    public void onScreen() {
//
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        LocalBroadcastManager.getInstance(getContext()).registerReceiver(trackChange, new IntentFilter(MbnController.To_FRAGMENT));
//        BackImageChangeHandler.instance.addCallback(this);
//        BackImageChangeHandler.instance.force(getContext());
//    }
//
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(trackChange);
//        BackImageChangeHandler.instance.removeCallback(this);
//    }
//
//    @Override
//    public void offScreen() {
//
//    }
//
//    @Override
//    public View onCreateViewForChild(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        contextForThis = inflater.getContext();
//        return inflater.inflate(R.layout.activity_first_for_drawer, container, false);
//    }
//
//    @Override
//    public boolean hasAppBar() {
//        return false;
//    }
//
//    @Override
//    public View makeToolBar(LayoutInflater inflater) {
//        return null;
//    }
//
//    @Override
//    public Bundle makeState(Bundle state) {
//        return state;
//    }
//
//    @Override
//    public void checkForRestoreState() {
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        cover = view.findViewById(R.id.cover_new_back);
////        view.setBackgroundColor(Color.argb(255 / 2, 180, 180, 180));
//
//        nowPlayingLayout = view.findViewById(R.id.now_playing_new);
//
//        nowPlayingLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getFragmentSwipeBackManager().addFragment(new PlayerFragmentTelegramy());
//            }
//        });
//
//        nowPlayingTitle = view.findViewById(R.id.now_playing_new_title);
//        nowPlayingTitle.setText(currentTrack.getTitle());
//
//        setHasOptionsMenu(true);
//
//        Toolbar toolbar = view.findViewById(R.id.toolbar);
//        toolbar.setTitle("   Music Player");
//
//        AppCompatActivity compatActivity = (AppCompatActivity) getActivity();
//
//        compatActivity.setSupportActionBar(toolbar);
//
//
//        linearLayout = view.findViewById(R.id.main_content);
//        view.findViewById(R.id.appbar).setClipToOutline(true);
////        bottomLayout = view.findViewById(R.id.now_paying_bottom_layout);
////        previous = view.findViewById(R.id.now_playing_preBton);
////        play = view.findViewById(R.id.now_playing_playBton);
////        next = view.findViewById(R.id.now_playing_nextBton);
//
//        // Create the Adapter that will return a fragment for each of the three
//        // primary sections of the activity.
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
//        mSectionsPagerAdapter.notifyDataSetChanged();
//        // Set up the ViewPager with the sections Adapter.
//        mViewPager = view.findViewById(R.id.container);
//        mViewPager.setAdapter(mSectionsPagerAdapter);
//        TabLayout tabLayout = view.findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(mViewPager);
//
//
//        DrawerLayout drawer = view.findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        navigationView = view.findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//
//
//                linearLayout.setTranslationX((slideOffset * navigationView.getWidth()));
//
//
//                System.out.println(slideOffset);
//
//            }
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//
//            }
//
//            @Override
//            public void onDrawerStateChanged(int newState) {
//
//            }
//        });
//
////        mViewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//
//
////        orangeBottom();
//
//
//    }
//
////    public static ListsFragment newInstance() {
////
////        Bundle args = new Bundle();
////
////        ListsFragment fragment = new ListsFragment();
////        fragment.setArguments(args);
////        return fragment;
////    }
//
//    Context contextForThis;
//
//    private ViewPager mViewPager;
//    private LinearLayout linearLayout;
//    private LinearLayout bottomLayout;
//    private ImageButton previous;
//    private ImageButton play;
//    private ImageButton next;
//    private NavigationView navigationView;
//    private SectionsPagerAdapter mSectionsPagerAdapter;
//
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//
//        super.onCreateOptionsMenu(menu, inflater);
//
//        inflater.inflate(R.menu.menu_first, menu);
//
//
//    }
//
////    @Override
////    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
////        super.onViewCreated(view, savedInstanceState);
////
////        setHasOptionsMenu(true);
////
////
////        Toolbar toolbar = view.findViewById(R.id.toolbar);
////        toolbar.setTitle("   Music Player");
////
////        AppCompatActivity compatActivity = (AppCompatActivity) getActivity();
////
////        compatActivity.setSupportActionBar(toolbar);
////
////
////        linearLayout = view.findViewById(R.id.main_content);
////        view.findViewById(R.id.appbar).setClipToOutline(true);
//////        bottomLayout = view.findViewById(R.id.now_paying_bottom_layout);
//////        previous = view.findViewById(R.id.now_playing_preBton);
//////        play = view.findViewById(R.id.now_playing_playBton);
//////        next = view.findViewById(R.id.now_playing_nextBton);
////
////        // Create the Adapter that will return a fragment for each of the three
////        // primary sections of the activity.
////        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
////        mSectionsPagerAdapter.notifyDataSetChanged();
////        // Set up the ViewPager with the sections Adapter.
////        mViewPager = view.findViewById(R.id.container);
////        mViewPager.setAdapter(mSectionsPagerAdapter);
////        TabLayout tabLayout = view.findViewById(R.id.tabs);
////        tabLayout.setupWithViewPager(mViewPager);
////
////
////        DrawerLayout drawer = view.findViewById(R.id.drawer_layout);
////        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
////                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
////        drawer.setDrawerListener(toggle);
////        toggle.syncState();
////
////        navigationView = view.findViewById(R.id.nav_view);
////        navigationView.setNavigationItemSelectedListener(this);
////
////        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
////            @Override
////            public void onDrawerSlide(View drawerView, float slideOffset) {
////
////
////                linearLayout.setTranslationX((slideOffset * navigationView.getWidth()));
////
////
////                System.out.println(slideOffset);
////
////            }
////
////            @Override
////            public void onDrawerOpened(View drawerView) {
////
////            }
////
////            @Override
////            public void onDrawerClosed(View drawerView) {
////
////            }
////
////            @Override
////            public void onDrawerStateChanged(int newState) {
////
////            }
////        });
////
//////        mViewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
////
////
//////        orangeBottom();
////
////    }
//
////    private void orangeBottom() {
////
////        play.setOnClickListener(this);
////        next.setOnClickListener(this);
////        previous.setOnClickListener(this);
////
////
//////        bottomLayout.setOnTouchListener(new View.OnTouchListener() {
//////            @Override
//////            public boolean onTouch(View view, MotionEvent motionEvent) {
//////                return false;
//////            }
//////        });
////
//////        bottomLayout.setOnClickListener(new View.OnClickListener() {
//////            @Override
//////            public void onClick(View view) {
//////
//////
//////                PlayerFragment.newInstance().show(activity.getSupportFragmentManager(), "nowPlaying");
//////
//////            }
//////        });
////
////
////    }
//
//    @Override
//    public void onClick(View view) {
//
//
//        int id = view.getId();
//
//
////        switch (id) {
////            case R.id.now_playing_nextBton:
//////                bindService();
////                MbnController.next(getContext());
////                break;
////            case R.id.now_playing_playBton:
////                if (playerState) {
////                    MbnController.pause(getContext());
////                } else {
////                    MbnController.play(getContext());
////                }
////                break;
////            case R.id.now_playing_preBton:
////                MbnController.previous(getContext());
////                break;
////        }
//    }
//
//
//    //
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_setting) {
//
//            // Handle the camera action
//        } else if (id == R.id.nav_timer) {
//
////            TimerFragment.newInstance().show(getActivity().getSupportFragmentManager(), "timer");
//            getFragmentSwipeBackManager().addFragment(new TimerChooseDialog());
//
//        } else if (id == R.id.nav_equ) {
//
//
//            Intent intent = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
////            intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
////            intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, 0);
//            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
//
//                startActivityForResult(intent, 9919);
//
//            }
//
//        } else if (id == R.id.nav_cutter) {
//
//        } else if (id == R.id.nav_playlist) {
//
//
//            PlayListsBottomFragment.newInstance().show(getFragmentManager(), "playLists");
//
//        }
//        DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
//
//
//    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
//
//        public SectionsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public android.support.v4.app.Fragment getItem(int position) {
//            // getItem is called to instantiate the fragment for the given page.
//            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);
//        }
//
//        @Override
//        public int getCount() {
//            // Show 3 total pages.
//            return 3;
//        }
//
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "Albums";
//                case 1:
//                    return "All Songs";
////                case 2:
////                    return "PlayLists";
//                case 2:
//                    return "Search";
//                case 3:
//                    return "Now Playing";
//            }
//            return null;
//        }
//    }
//
//
//    public static class PlaceholderFragment extends android.support.v4.app.Fragment implements BackImageChangeHandler.ImageChangeCallback {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//        private RecyclerView list;
//        private MbnRecyclerFastScroller scroller;
//        private TextView textViewFastScroll;
//        private ImageView background;
//
//        private BlurDecor blurDecor;
//
//        public PlaceholderFragment() {
//        }
//
//
//        @Override
//        public void onChange(Bitmap original, Bitmap blur) {
//
//            if (background != null && blurDecor != null) {
//                blurDecor.setImageBitmap(blur);
//                background.setImageBitmap(original);
//                list.invalidateItemDecorations();
//            }
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
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            final View rootView;
//            if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
//
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
//                blurDecor = new BlurDecor(rootView.getContext());
//                list = rootView.findViewById(R.id.first_page_recycler_view);
//                background = rootView.findViewById(R.id.list_back_image);
//                list.addItemDecoration(blurDecor);
//                scroller = rootView.findViewById(R.id.fast_scroll_main_page);
//                scroller.setVisibility(View.GONE);
//                textViewFastScroll = rootView.findViewById(R.id.fast_scroll_text);
//                textViewFastScroll.setVisibility(View.GONE);
//
//                final SwipeRefreshLayout refreshLayout = rootView.findViewById(R.id.refresh_swipe_down);
//
//                refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//                    @Override
//                    public void onRefresh() {
//
//
//                        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(REFRESH_KEY));
////                        getContext().sendBroadcast(new Intent(REFRESH_KEY));
//
//                        refreshLayout.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                refreshLayout.setRefreshing(false);
//
////                                list.getAdapter().notifyDataSetChanged();
//
//                            }
//                        }, 1200);
//
//                    }
//                });
//
//
////                if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
////                    albumTabMaker();
////                } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
////                    allSongsTabMaker();
////                    scroller.setTextView(textViewFastScroll);
////                    scroller.setVisibility(View.VISIBLE);
////                } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
////                    playlistTabMaker();
////                }
//////                else if (getArguments().getInt(ARG_SECTION_NUMBER) == 4) {
//////                    nowPlayingTabMaker();
//////                }
////
////                scroller.setRecyclerView(list);
//            }
//
//            return rootView;
//        }
//
//
//        BroadcastReceiver receiver;
//
//        @Override
//        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//            super.onViewCreated(view, savedInstanceState);
//
//
//            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
////                System.out.println("iiiiiiiii start");
//                albumTabMaker();
////                System.out.println("iiiiiiiiii end");
//            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
//                allSongsTabMaker();
//                scroller.setTextView(textViewFastScroll);
//                scroller.setVisibility(View.VISIBLE);
//            }
////            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
//////                playlistTabMaker();
////            }
////                else if (getArguments().getInt(ARG_SECTION_NUMBER) == 4) {
////                    nowPlayingTabMaker();
////                }
//
////            scroller.setRecyclerView(list);
//
//
//        }
//
//        private void handleSearchClicks(MbnSearchEngine.SearchItem item) {
//
//            int type = item.getType();
//
//            switch (type) {
//                case 1:
//
//                    MbnController.change(getContext(), DataBaseHolder.getAllSongs(getContext()), item.getSong());
//
//                    break;
//                case 2:
//
////                    AlbumBottomFragment.newInstance(item.getAlbum().getHashCode()).show(getFragmentManager(), "album");
//
//                    break;
//
//
//            }
//
//
//        }
//
//        @SuppressLint("StaticFieldLeak")
//        private void searchTabMaker(final RecyclerView recyclerView, final EditText editText) {
//
//
////            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
////            recyclerView.setLayoutManager(layoutManager);
////            recyclerView.setHasFixedSize(false);
////
////            final MbnSearchEngine searchEngine = new MbnSearchEngine(getContext());
////
////            final SearchTabAdapter adapter = new SearchTabAdapter(getContext());
////
////            recyclerView.setAdapter(adapter);
////
////            adapter.setListener(new SearchTabAdapter.SearchTabListener() {
////                @Override
////                public void onClick(MbnSearchEngine.SearchItem item) {
////                    handleSearchClicks(item);
////
////                }
////            });
////
////
////            editText.addTextChangedListener(new TextWatcher() {
////                @Override
////                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
////
////                }
////
////                @Override
////                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
////
////                    searchEngine.inPut(editText.getText().toString());
////
////                    adapter.setSearchItemArrayList(searchEngine.getList());
////
////                }
////
////                @Override
////                public void afterTextChanged(Editable editable) {
////
////                }
////            });
//
//
//            final MbnSearchEngine[] searchEngine = new MbnSearchEngine[1];
//            final SearchTabAdapter[] adapter = new SearchTabAdapter[1];
//            final LinearLayoutManager[] layoutManager = new LinearLayoutManager[1];
//
//            new AsyncTask<Void, Void, Void>() {
//                @Override
//                protected Void doInBackground(Void... voids) {
//
//
//                    layoutManager[0] = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//
//
//                    searchEngine[0] = new MbnSearchEngine(getContext());
//
//                    adapter[0] = new SearchTabAdapter(getContext());
//
//
//                    adapter[0].setListener(new SearchTabAdapter.SearchTabListener() {
//                        @Override
//                        public void onClick(MbnSearchEngine.SearchItem item) {
//                            handleSearchClicks(item);
//
//                        }
//                    });
//
//
//                    editText.addTextChangedListener(new TextWatcher() {
//                        @Override
//                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                        }
//
//                        @Override
//                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                            searchEngine[0].inPut(editText.getText().toString());
//
//                            adapter[0].setSearchItemArrayList(searchEngine[0].getList());
//
//                        }
//
//                        @Override
//                        public void afterTextChanged(Editable editable) {
//
//                        }
//                    });
//
//
//                    return null;
//                }
//
//                @Override
//                protected void onPostExecute(Void aVoid) {
//                    super.onPostExecute(aVoid);
//
//
//                    recyclerView.setLayoutManager(layoutManager[0]);
//                    recyclerView.setHasFixedSize(false);
//                    recyclerView.setAdapter(adapter[0]);
//
//
//                }
//            }.execute();
//
//        }
//
////        private void playlistTabMaker() {
////
////            GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
////
////            list.setLayoutManager(manager);
////
////            list.setHasFixedSize(true);
////            list.setAdapter(new FirstPageAdapterTabAlbums(getContext(), playLists, new FirstPageAdapterTabAlbums.AlbumTabListener() {
////
////                @Override
////                public void onClick(String name, String data, View view) {
////
//////                    Intent albumActivity = new Intent(getActivity(), AlbumActivity.class);
////
//////                    albumActivity.putExtra("albumPos", position);
////
////                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "img_art_trans");
////
////
//////                    startActivity(albumActivity, options.toBundle());
////
////                }
////
////                @Override
////                public void onOptionClick(final int potionList, final View v) {
////
////
////                    String[] listItems = {"Play next", "Set as...", "Add to playlist"};
////                    final ListPopupWindow popupWindowCompat = new ListPopupWindow(getActivity());
////
////                    popupWindowCompat.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems));
////
////                    popupWindowCompat.setAnchorView(v);
////                    popupWindowCompat.setWidth(android.support.v7.widget.ListPopupWindow.WRAP_CONTENT);
////                    popupWindowCompat.setHeight(android.support.v7.widget.ListPopupWindow.WRAP_CONTENT);
////                    popupWindowCompat.setModal(true);
////                    popupWindowCompat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                        @Override
////                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////
//////                            Intent albumActivity = new Intent(getActivity(), AlbumActivity.class);
////
//////                            albumActivity.putExtra("albumPos", potionList);
////
////                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), v, "img_art_trans");
////
////
//////                            startActivity(albumActivity, options.toBundle());
////
////
////                            popupWindowCompat.dismiss();
////                        }
////                    });
////                    popupWindowCompat.show();
////
////
////                }
////            }));
////            scroller.setRecyclerView(list);
////
////
////        }
//
//        @SuppressLint("StaticFieldLeak")
//        private void allSongsTabMaker() {
//
//
//            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//            list.setLayoutManager(layoutManager);
//            list.setHasFixedSize(true);
//
//            new AsyncTask<Void, Void, FirstPageAdapterTabAllSongs>() {
//                @Override
//                protected FirstPageAdapterTabAllSongs doInBackground(Void... voids) {
//                    return new FirstPageAdapterTabAllSongs(new FirstPageAdapterTabAllSongs.AllSongsListener() {
//                        @Override
//                        public void onClick(int position, List<DataSong> songs, DataSong songCode) {
//
//
//                            MbnController.change(getContext(), songs, songCode);
//
//                        }
//
//                        @Override
//                        public void onOption(int position, String songCode) {
//
//                        }
//                    }, getContext(), list);
//                }
//
//                @Override
//                protected void onPostExecute(final FirstPageAdapterTabAllSongs firstPageAdapterTabAllSongs) {
//                    super.onPostExecute(firstPageAdapterTabAllSongs);
//                    list.setAdapter(firstPageAdapterTabAllSongs);
//
////                    list.addItemDecoration(new MbnRecyclerDecor(getContext()));
//                    scroller.setRecyclerView(list);
//
//                    receiver = new BroadcastReceiver() {
//                        @Override
//                        public void onReceive(Context context, Intent intent) {
//
//                            firstPageAdapterTabAllSongs.prepare();
//
//                        }
//                    };
//
//
////                    getContext().registerReceiver(receiver, new IntentFilter(LOADING_DONE));
//
//                }
//
//
//            }.execute();
//
////            list.setAdapter(new FirstPageAdapterTabAllSongs(allSongsCodes, new FirstPageAdapterTabAllSongs.AllSongsListener() {
////                @Override
////                public void onClick(int position, String songCode) {
////
////
////                    MbnController.change(getContext(), allSongsAlbum, songCode);
////
////                }
////
////                @Override
////                public void onOption(int position, String songCode) {
////
////                }
////            }, getContext()));
//
//        }
//
////        private void nowPlayingTabMaker() {
////
////
////            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
////            list.setLayoutManager(layoutManager);
////            list.setHasFixedSize(true);
////            list.setAdapter(new FirstPageAdapterTabAllSongs(currentQueue, new FirstPageAdapterTabAllSongs.AllSongsListener() {
////                @Override
////                public void onClick(int position, String songCode) {
////
////
////                    MbnController.change(getContext(), currentQueue, songCode);
////
////                }
////
////                @Override
////                public void onOption(int position, String songCode) {
////
////                }
////            }, getContext()));
////
////
////        }
//
//
//        @Override
//        public void onResume() {
//            super.onResume();
//            BackImageChangeHandler.instance.addCallback(this);
//            BackImageChangeHandler.instance.force(getContext());
//            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
//                case 1:
//                    receiver = new BroadcastReceiver() {
//                        @Override
//                        public void onReceive(Context context, Intent intent) {
//
//                            FirstPageAdapterTabAlbums tabAlbums = (FirstPageAdapterTabAlbums) list.getAdapter();
//                            tabAlbums.updateData();
//
//                        }
//                    };
//                    break;
//                case 2:
//                    receiver = new BroadcastReceiver() {
//                        @Override
//                        public void onReceive(Context context, Intent intent) {
//
//                            FirstPageAdapterTabAllSongs tabAlbums = (FirstPageAdapterTabAllSongs) list.getAdapter();
//                            tabAlbums.prepare();
//
//                        }
//                    };
//                    break;
//            }
//
//            if (getArguments().getInt(ARG_SECTION_NUMBER) != 3) {
//                LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(LOADING_DONE));
//            }
////            getContext().registerReceiver(receiver, new IntentFilter(LOADING_DONE));
//
//
//        }
//
//        @SuppressLint("StaticFieldLeak")
//        private void albumTabMaker() {
//
//            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//
//            float dens = displayMetrics.density;
////
////
//            float size = 150 * dens;
////
////
//            int count = (int) (displayMetrics.widthPixels / size);
//
//            GridLayoutManager manager = new GridLayoutManager(getContext(), count);
//
//            list.setLayoutManager(manager);
//
//            list.setHasFixedSize(true);
//
//
////
//            new AsyncTask<Void, Void, FirstPageAdapterTabAlbums>() {
//                @Override
//                protected FirstPageAdapterTabAlbums doInBackground(Void... params) {
//
//
//                    FirstPageAdapterTabAlbums adapterTabAlbums = new FirstPageAdapterTabAlbums(getContext(), new FirstPageAdapterTabAlbums.AlbumTabListener() {
//
//                        @Override
//                        public void onClick(long id, String data, View view, String name) {
//
////                            AlbumBottomFragment.newInstance(id, data, name).show(getFragmentManager(), "album");
//                            CustomFragmentSwipeBackAnimator.getINSTANCE().addFragment(NewAlbumFrag.newInstance(id, data, name));
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
//                        }
//
//                        @Override
//                        public void onOptionClick(final int potionList, final View v) {
//
//
////                            String[] listItems = {"Play next", "Set as...", "Add to playlist"};
////                            final ListPopupWindow popupWindowCompat = new ListPopupWindow(getActivity());
////
////                            popupWindowCompat.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems));
////
////                            popupWindowCompat.setAnchorView(v);
////                            popupWindowCompat.setWidth(android.support.v7.widget.ListPopupWindow.WRAP_CONTENT);
////                            popupWindowCompat.setHeight(android.support.v7.widget.ListPopupWindow.WRAP_CONTENT);
////                            popupWindowCompat.setModal(true);
////                            popupWindowCompat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                                @Override
////                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////
////                                    Intent albumActivity = new Intent(getActivity(), FirstActivity.class);
////
////                                    albumActivity.putExtra("albumPos", potionList);
////
////                                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), v, "img_art_trans");
////
////
////                                    startActivity(albumActivity, options.toBundle());
////
////
////                                    popupWindowCompat.dismiss();
////                                }
////                            });
////                            popupWindowCompat.show();
//
//
//                        }
//                    });
//
//
////                    try {
////                        Thread.sleep(5000);
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
//
//                    return adapterTabAlbums;
//                }
//
//                @Override
//                protected void onPostExecute(final FirstPageAdapterTabAlbums albums) {
//                    super.onPostExecute(albums);
//
////                    System.out.println("iiiiiiiiiiiiiii before set");
//
//                    list.setAdapter(albums);
//
////                    System.out.println("iiiiiiiiiiiiiii after set");
//
//                    scroller.setRecyclerView(list);
//
//                    receiver = new BroadcastReceiver() {
//                        @Override
//                        public void onReceive(Context context, Intent intent) {
//
//                            System.out.println("hhhhhhhhhhhhh");
//
//                            albums.updateData();
//
//                        }
//                    };
//
//
////                    getContext().registerReceiver(receiver, new IntentFilter(LOADING_DONE));
//
////                    System.out.println("iiiiiiiiiiii after scroll");
//
//
//                }
//            }.execute();
//
//
////            list.setAdapter(adapterTabAlbums);
//
////            list.setAdapter(new FirstPageAdapterTabAlbums(getContext(), albumsList, new FirstPageAdapterTabAlbums.AlbumTabListener() {
////
////                @Override
////                public void onClick(int position, View view) {
////
////                    AlbumBottomFragment.newInstance(position).show(getFragmentManager(), "album");
////
//////                    Intent albumActivity = new Intent(getActivity(), AlbumActivity.class);
//////
//////                    albumActivity.putExtra("albumPos", position);
//////
//////                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "img_art_trans");
//////
//////
//////                    startActivity(albumActivity, options.toBundle());
////
////                }
////
////                @Override
////                public void onOptionClick(final int potionList, final View v) {
////
////
////                    String[] listItems = {"Play next", "Set as...", "Add to playlist"};
////                    final ListPopupWindow popupWindowCompat = new ListPopupWindow(getActivity());
////
////                    popupWindowCompat.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems));
////
////                    popupWindowCompat.setAnchorView(v);
////                    popupWindowCompat.setWidth(android.support.v7.widget.ListPopupWindow.WRAP_CONTENT);
////                    popupWindowCompat.setHeight(android.support.v7.widget.ListPopupWindow.WRAP_CONTENT);
////                    popupWindowCompat.setModal(true);
////                    popupWindowCompat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                        @Override
////                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////
////                            Intent albumActivity = new Intent(getActivity(), AlbumActivity.class);
////
////                            albumActivity.putExtra("albumPos", potionList);
////
////                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), v, "img_art_trans");
////
////
////                            startActivity(albumActivity, options.toBundle());
////
////
////                            popupWindowCompat.dismiss();
////                        }
////                    });
////                    popupWindowCompat.show();
////
////
////                }
////            }));
//
//        }
//
//
//        @Override
//        public void onPause() {
//            super.onPause();
//            Log.i("PAUSE", "yes it paused");
//            BackImageChangeHandler.instance.removeCallback(this);
//            if (getArguments().getInt(ARG_SECTION_NUMBER) != 3) {
//
//                try {
//                    LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
////                    getContext().unregisterReceiver(receiver);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//        }
//
//
//    }
//
//
//}
