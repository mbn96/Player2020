package com.br.mreza.musicplayer.newmodel.search;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.br.mreza.musicplayer.FirstPageAdapterTabAlbums;
import com.br.mreza.musicplayer.R;
import com.br.mreza.musicplayer.newdesign.NewAlbumFrag;
import com.br.mreza.musicplayer.newmodel.database.DataBaseManager;
import com.br.mreza.musicplayer.newmodel.search.adapters.ArtistsSearchAdapter;
import com.br.mreza.musicplayer.newmodel.search.adapters.SearchAlbumAdapter;
import com.br.mreza.musicplayer.newmodel.search.adapters.SongsSearchAdapter;
import com.br.mreza.musicplayer.newmodel.theme.ThemeEngine;

import java.util.ArrayList;

import mbn.libs.backgroundtask.BaseTaskHolder;
import mbn.libs.fragmanager.BaseFragment;
import mbn.libs.fragmanager.CustomAppBar;
import mbn.libs.fragmanager.CustomFragmentSwipeBackAnimator;
import mbn.libs.imagelibs.imageworks.MbnUtils;


public class MainSearchFrag extends BaseFragment {

    private EditText editText;
    private ArrayList<SearchPageItem> pageItems = new ArrayList<>();
    private SearchResult searchResult;

    private Point point = new Point(0, 0);

    private BaseTaskHolder.ResultReceiver searchResultReceiver = new BaseTaskHolder.ResultReceiver() {
        @Override
        public void onResult(Object result, Object info) {
            if (editText.getText().toString().equals(info)) {
                searchResult = (SearchResult) result;
                for (SearchPageItem item : pageItems) {
                    item.update();
                }
            }
        }
    };

    private String TAG = "SEARCH_FRAG";
    private ViewPager viewPager;

    @Override
    public boolean hasAppBar() {
        return true;
    }

//    @Override
//    public void startAnimIsStarting() {
//        getUserView().setAlpha(0);
//        getUserView().setTranslationY(-getView().getHeight() / 4);
//        getUserView().setScaleX(0.65f);
//        getUserView().setScaleY(0.65f);
//        getUserView().animate().translationY(0).alpha(1).scaleY(1).scaleX(1).setDuration(450).setInterpolator(new OvershootInterpolator()).start();
//    }
//
//    @Override
//    public void endAnimIsStarting() {
//        getUserView().animate().translationY(-getView().getHeight() / 3).setDuration(200).setInterpolator(new AnticipateInterpolator()).start();
//    }

    @Override
    public int getAnimationMode() {
        return CustomFragmentSwipeBackAnimator.ANIM_BOTTOM_NORMAL;
    }

    @Override
    public Point getCirclePoint() {
        if (point.x == 0 && point.y == 0) {
            point.set(getView().getWidth(), 0);
        }
        return point;
    }

    @Override
    public View onCreateViewForChild(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_layout_new, container, false);
    }

//    @Override
//    public View makeToolBar(LayoutInflater inflater) {
//        return null;
//    }

    @Override
    public void makeState(Bundle state) {

    }

    @Override
    public void checkForRestoreState() {

    }

    private ThemeEngine.DefaultThemeCallback themeCallback = new ThemeEngine.DefaultThemeCallback(false, false, true) {
        @Override
        public void onProcessFinished() {

        }

        @Override
        public void onAccentColor(int color) {
            setToolBarBackgroundColor(MbnUtils.alphaChanger(color,10));
//            setToolBarBackgroundColor(MbnUtils.colorWhitener2(color, 10));
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        ThemeEngine.getINSTANCE().registerCallback(themeCallback);
        ThemeEngine.getINSTANCE().getResult(themeCallback);
    }

    @Override
    public void onPause() {
        super.onPause();
        ThemeEngine.getINSTANCE().unRegisterCallback(themeCallback);
    }

    @Override
    public boolean canInterceptTouches() {
        if (getAnimationMode() == CustomFragmentSwipeBackAnimator.ANIM_CIRCLE_REVEAL) {
            return false;
        }
        for (SearchPageItem item : pageItems) {
            if (item.getPageNumber() == viewPager.getCurrentItem()) {
                return item.canIntercept();
            }
        }
        return super.canInterceptTouches();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbar(getLayoutInflater().inflate(R.layout.search_frag_appbar_layout, getToolbarHolder().getBaseLayout(), false));
//        setToolBarBackgroundColor(MbnUtils.colorWhitener2(Color.RED, 5));
//        setToolBarBackgroundBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.mbn_mp_back_3));
//        setToolBarBackgroundColor(MbnUtils.colorWhitener2(Color.parseColor("#344558"), 5));
        setToolbarSize(3, CustomAppBar.UNIT_VIEW_SIZE);
        setToolbarScrollingSize(100);
        setToolbarElevation(1 * getDisplayDensity());

        editText = getToolbar().findViewById(R.id.search_edit_text);
        viewPager = view.findViewById(R.id.search_frag_viewpager);
        viewPager.setAdapter(new SearchPagerAdapter());
        TabLayout layout = getToolbar().findViewById(R.id.search_frag_tab_layout);
        layout.setupWithViewPager(viewPager);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!editText.getText().toString().isEmpty()) {
                    DataBaseManager.getManager().search(editText.getText().toString(), searchResultReceiver);
                } else {
                    searchResult = new SearchResult(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                    for (SearchPageItem item : pageItems) {
                        item.update();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    private class SearchPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            String title;
            switch (position) {
                case 0:
                    title = "Songs";
                    break;
                case 1:
                    title = "Albums";
                    break;
                default:
                    title = "Artists";
                    break;
            }
            return title;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            SearchPageItem pageItem;
            switch (position) {
                case 0:
                    pageItem = new SongsPage(container.getContext(), (ViewPager) container, position);
                    break;
                case 1:
                    pageItem = new AlbumsPage(container.getContext(), (ViewPager) container, position);
                    break;
                default:
                    pageItem = new ArtistsPage(container.getContext(), (ViewPager) container, position);
                    break;
            }
            pageItems.add(pageItem);
            pageItem.update();
            return pageItem;
        }

        @SuppressWarnings("SuspiciousMethodCalls")
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            pageItems.remove(object);
            container.removeView(((SearchPageItem) object).getRecyclerView());
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return ((SearchPageItem) object).getRecyclerView() == view;
        }
    }

    private class SearchPageItem {
        private RecyclerView recyclerView;
        private Context context;
        private int pageNumber;

        SearchPageItem(Context context, ViewPager viewPager, int position) {
            this.context = context;
            recyclerView = new RecyclerView(context);
            pageNumber = position;
            viewPager.addView(recyclerView);
            prepare();
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public RecyclerView getRecyclerView() {
            return recyclerView;
        }

        public Context getContext() {
            return context;
        }

        public void update() {
        }

        protected void prepare() {
        }

        public boolean canIntercept() {
            return !recyclerView.canScrollVertically(-1);
        }

    }

    private class SongsPage extends SearchPageItem {

        private SongsSearchAdapter songsSearchAdapter;

        SongsPage(Context context, ViewPager viewPager, int position) {
            super(context, viewPager, position);
        }


        @Override
        public void update() {
            if (searchResult != null) {
                songsSearchAdapter.setSongsIDs(searchResult.getSongsID());
            }
        }

        @Override
        protected void prepare() {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            getRecyclerView().setLayoutManager(layoutManager);
            getRecyclerView().setAdapter(songsSearchAdapter = new SongsSearchAdapter(getContext()));
        }
    }

    private class AlbumsPage extends SearchPageItem {

        private SearchAlbumAdapter albumAdapter;

        AlbumsPage(Context context, ViewPager viewPager, int position) {
            super(context, viewPager, position);
        }


        @Override
        public void update() {
            if (searchResult != null) {
                albumAdapter.setData(searchResult.getAlbums());
            }
        }

        @Override
        protected void prepare() {
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
            getRecyclerView().setLayoutManager(layoutManager);
            getRecyclerView().setAdapter(albumAdapter = new SearchAlbumAdapter(getContext(), new FirstPageAdapterTabAlbums.AlbumTabListener() {
                @Override
                public void onClick(long id, String data, View v, String name) {
                    CustomFragmentSwipeBackAnimator.getINSTANCE().addFragment(NewAlbumFrag.newInstance(id, data, name));
                }

                @Override
                public void onOptionClick(int position, View v) {

                }
            }));
        }
    }

    private class ArtistsPage extends SearchPageItem {

        private ArtistsSearchAdapter adapter;

        ArtistsPage(Context context, ViewPager viewPager, int position) {
            super(context, viewPager, position);
        }


        @Override
        public void update() {
            if (searchResult != null) {
                adapter.setArtists(searchResult.getArtists());
            }
        }

        @Override
        protected void prepare() {
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
            getRecyclerView().setLayoutManager(layoutManager);
            getRecyclerView().setAdapter(adapter = new ArtistsSearchAdapter(getContext()));
        }
    }

}
