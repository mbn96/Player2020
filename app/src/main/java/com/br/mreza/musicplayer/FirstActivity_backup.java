//package com.br.mreza.musicplayer;
//
//import android.app.ActivityOptions;
//import android.content.Intent;
//import android.graphics.Color;
//import android.media.audiofx.AudioEffect;
//import android.os.Bundle;
//import android.support.design.widget.NavigationView;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.view.ViewPager;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.ListPopupWindow;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.br.mreza.musicplayer.MBN.customViews.MbnRecyclerFastScroller;
//
//import static com.br.mreza.musicplayer.ListMaker.albumsList;
//import static com.br.mreza.musicplayer.ListMaker.allSongsAlbum;
//import static com.br.mreza.musicplayer.ListMaker.allSongsCodes;
//import static com.br.mreza.musicplayer.ListMaker.currentQueue;
//import static com.br.mreza.musicplayer.ListMaker.currentQueueCodes;
//import static com.br.mreza.musicplayer.ListMaker.currentTrackCode;
//import static com.br.mreza.musicplayer.ListMaker.playLists;
//
////import static com.br.mreza.musicplayer.ListMaker.albums;
//
//public class FirstActivity_backup extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
//
//    /**
//     * The {@link android.support.v4.view.PagerAdapter} that will provide
//     * fragments for each of the sections. We use a
//     * {@link FragmentPagerAdapter} derivative, which will keep every
//     * loaded fragment in memory. If this becomes too memory intensive, it
//     * may be best to switch to a
//     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
//     */
//    private SectionsPagerAdapter mSectionsPagerAdapter;
//
//
//    /**
//     * The {@link ViewPager} that will host the section contents.
//     */
//    private ViewPager mViewPager;
//    private LinearLayout linearLayout;
//    private LinearLayout bottomLayout;
//    private ImageButton previous;
//    private ImageButton play;
//    private ImageButton next;
//    private NavigationView navigationView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_first_content_for_image_background);
//
//        Toast.makeText(this, currentTrackCode, Toast.LENGTH_LONG).show();
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("MBN Music Player");
////        toolbar.setTitleTextColor(Color.BLACK);
////        toolbar.setSubtitleTextColor(Color.BLACK);
//
//        setSupportActionBar(toolbar);
//
//        linearLayout = (LinearLayout) findViewById(R.id.main_content);
//        bottomLayout = (LinearLayout) findViewById(R.id.now_paying_bottom_layout);
//        previous = (ImageButton) findViewById(R.id.now_playing_preBton);
//
//
//        // Create the Adapter that will return a fragment for each of the three
//        // primary sections of the activity.
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//
//        // Set up the ViewPager with the sections Adapter.
//        mViewPager = (ViewPager) findViewById(R.id.container);
//        mViewPager.setAdapter(mSectionsPagerAdapter);
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(mViewPager);
//
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        navigationView = (NavigationView) findViewById(R.id.nav_view);
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
////        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
////        fab.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////                if (PlayerService.player!=null) {
////                    Intent intentPlayer = new Intent(getApplicationContext(), PlayerActivity.class);
////
////                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(FirstActivity.this);
////
////
////                    startActivity(intentPlayer, options.toBundle());
////                }
//////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//////                        .setAction("Action", n).show();
////            }
////        });
//
////        getSupportActionBar().setTitle("MBN Music Player");
//
//
////        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_fist_activity);
////        floatingActionButton.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                PlayerFragment.newInstance().show(getSupportFragmentManager(), "MBN");
////            }
////        });
//
////        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//
//        linearLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//
////        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//        getWindow().setStatusBarColor(Color.argb(0, 0, 0, 0));
////        getWindow().setStatusBarColor(Color.parseColor("#50f5f5f5"));
//
//        orangeBottom();
//
//
//    }
//
//
//    private void orangeBottom() {
//
//
//        bottomLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
////                PlayerFragment.newInstance().show(getSupportFragmentManager(), "nowPlaying");
//
//            }
//        });
//
//
//    }
//
////    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        // Inflate the menu; this adds items to the action bar if it is present.
////        getMenuInflater().inflate(R.menu.menu_first, menu);
////        return true;
////    }
////
////    @Override
////    public boolean onOptionsItemSelected(MenuItem item) {
////        // Handle action bar item clicks here. The action bar will
////        // automatically handle clicks on the Home/Up button, so long
////        // as you specify a parent activity in AndroidManifest.xml.
////        int id = item.getItemId();
////
////        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_settings) {
////
////        }
////
////        return super.onOptionsItemSelected(item);
////    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//    }
//
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
////            intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, 0);
//            if (intent.resolveActivity(getPackageManager()) != null) {
//
//                startActivityForResult(intent, 9919);
//
//            }
//
//        } else if (id == R.id.nav_cutter) {
//
//        }
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
//
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    /**
//     * A placeholder fragment containing a simple view.
//     */
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
//                    MbnController.change(getContext(), allSongsAlbum, item.getSongCode());
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
//            },getContext()));
//        }
//
//        private void nowPlayingTabMaker() {
//
//
//            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//            list.setLayoutManager(layoutManager);
//            list.setHasFixedSize(true);
//            list.setAdapter(new FirstPageAdapterTabAllSongs(currentQueueCodes, new FirstPageAdapterTabAllSongs.AllSongsListener() {
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
//            },getContext()));
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
//
//    /**
//     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
//     * one of the sections/tabs/pages.
//     */
//    public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//        public SectionsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            // getItem is called to instantiate the fragment for the given page.
//            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);
//        }
//
//        @Override
//        public int getCount() {
//            // Show 3 total pages.
//            return 5;
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
//}
