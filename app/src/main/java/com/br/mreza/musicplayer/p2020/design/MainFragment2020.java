package com.br.mreza.musicplayer.p2020.design;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.br.mreza.musicplayer.DataSong;
import com.br.mreza.musicplayer.MBN.customViews.MbnPlayButton;
import com.br.mreza.musicplayer.MBN.customViews.MbnScrollingTextView;
import com.br.mreza.musicplayer.MbnController;
import com.br.mreza.musicplayer.MySongDAO;
import com.br.mreza.musicplayer.PlayListsBottomFragment;
import com.br.mreza.musicplayer.R;
import com.br.mreza.musicplayer.newdesign.PermissionActivity;
import com.br.mreza.musicplayer.newdesign.SettingFragment;
import com.br.mreza.musicplayer.newdesign.customviews.DotProgressBar;
import com.br.mreza.musicplayer.newdesign.newmodelfrags.PlayListShowFragmentNew;
import com.br.mreza.musicplayer.newmodel.database.DataBaseManager;
import com.br.mreza.musicplayer.newmodel.search.MainSearchFrag;
import com.br.mreza.musicplayer.newmodel.service.player.PlayerCommunicates;
import com.br.mreza.musicplayer.newmodel.theme.ThemeEngine;
import com.br.mreza.musicplayer.p2020.adapters.MainFragAlbumAdapter;
import com.br.mreza.musicplayer.p2020.adapters.MainFragSongsAdapter;
import com.br.mreza.musicplayer.p2020.management.MostPlayedItem;
import com.br.mreza.musicplayer.p2020.views.VisualizerProgressBar;

import java.util.ArrayList;
import java.util.List;

import mbn.libs.backgroundtask.BaseTaskHolder;
import mbn.libs.fragmanager.BaseFragment;
import mbn.libs.fragmanager.CustomAppBar;
import mbn.libs.fragmanager.PermissionRequestFrag;
import mbn.libs.imagelibs.imageworks.ShadowBackground;
import mbn.libs.imagelibs.imageworks.ShadowBackground_2;
import mbn.libs.utils.JavaUtils;

public class MainFragment2020 extends BaseFragment {

    private static final long RECENTLY_ADDED = 0;
    private static final long MOST_PLAYED = 1;

    private int maxItems = 10;

    private DataSong currentSong;

    private RecyclerView albumsRecyclerView;
    private RecyclerView recentlyAddedRecyclerView;
    private RecyclerView mostPlayedRecyclerView;

    private MainFragAlbumAdapter albumAdapter;
    private MainFragSongsAdapter recentlyAddedAdapter;
    private MainFragSongsAdapter mostPlayedAdapter;

    private Button allSongsButton;
    private Button recentlyPlayedSongsButton;
    private Button artistsButton;
    private Button playListsButton;

    private LinearLayout bottomPanel;
    private MbnScrollingTextView bottomSongTitle;
    private TextView bottomTime;
    private MbnPlayButton bottomPlayButton;
    private VisualizerProgressBar bottomProgressBar;

    private BaseTaskHolder.ResultReceiver songIDsReceiver = new BaseTaskHolder.ResultReceiver() {
        @SuppressWarnings("unchecked")
        @Override
        public void onResult(Object result, Object info) {
            if (result instanceof List) {
                if (((List) result).size() > 0) {
                    long[] inf = (long[]) info;
                    if (inf[0] == RECENTLY_ADDED) {
                        DataBaseManager.getManager().changeTrackAndQueue((ArrayList<Long>) result, inf[1]);
                    } else {
                        ArrayList<Long> ids = new ArrayList<>();
                        for (int i = 0; i < ((List) result).size(); i++) {
                            ids.add(((MostPlayedItem) ((List) result).get(i)).getId());
                        }
                        DataBaseManager.getManager().changeTrackAndQueue(ids, inf[1]);
                    }
                }
            }
        }
    };


    private DataBaseManager.DefaultCallback dataBaseCallback = new DataBaseManager.DefaultCallback(true, false) {
        @Override
        public void onTrackChange(long id, DataSong song) {
            setCurrentSong(song);
        }
    };

    private PlayerCommunicates.PlayerCallbacks playerCallbacks = new PlayerCommunicates.PlayerCallbacks() {
        @Override
        public void onCurrentPosChange(long pos) {
            if (currentSong != null) {
                bottomProgressBar.setProgress((float) pos / currentSong.getDuration());
                bottomTime.setText(MbnController.timeConverter(pos, currentSong.getDuration()));
            }
        }

        @Override
        public void onPlayerStateChange(boolean state) {
            bottomPlayButton.setMode(state);
        }
    };

    private ThemeEngine.DefaultThemeCallback themeCallback = new ThemeEngine.DefaultThemeCallback(false, false, true) {
        @Override
        public void onProcessFinished() {

        }

        @Override
        public void onAccentColor(int color) {
//            bottomPlayButton.setColor(color);
//            bottomProgressBar.setAccentColor(color);
            bottomPanel.setBackgroundColor(color);
        }

    };

    private void setCurrentSong(DataSong currentSong) {
        this.currentSong = currentSong;
        bottomSongTitle.setText(currentSong.getTitle());
//        bottomProgressBar.setMax(currentSong.getDuration());
    }

    @Override
    public boolean hasAppBar() {
        return true;
    }

    @Override
    public View onCreateViewForChild(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_frag_layout_2020_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        manageAppBar();
        manageRecyclers();
        manageButtons();
        manageBottomPanel();
    }

    @Override
    public void onStart() {
        super.onStart();
        DataBaseManager.getManager().registerCallback(dataBaseCallback);
        DataBaseManager.getManager().getCurrentQueueAndTrack(new BaseTaskHolder.ResultReceiver() {
            @Override
            public void onResult(Object result, Object info) {
                Object[] objects = (Object[]) result;
                setCurrentSong((DataSong) objects[1]);
            }
        });

        getView().postDelayed(() -> {
            if (getFragmentSwipeBackManager() != null && getActivity().checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                getFragmentSwipeBackManager().addFragment(PermissionRequestFrag.newInstance("We need to access microphone in order for the visualizer to work.",
                        new String[]{Manifest.permission.RECORD_AUDIO}, getResultID(), -1, Color.BLACK));
            }
        }, 1500);

    }

    @Override
    public void onStop() {
        super.onStop();
        DataBaseManager.getManager().unRegisterCallback(dataBaseCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
//        getActivity().setUI_style(true, true);
        getActivity().lightUI(true);
        bottomPlayButton.setMode(PlayerCommunicates.getINSTANCE().isPlaying());
        PlayerCommunicates.getINSTANCE().registerCallback(playerCallbacks);
        ThemeEngine.getINSTANCE().registerCallback(themeCallback);
        ThemeEngine.getINSTANCE().getResult(themeCallback);

        albumAdapter.update();
        DataBaseManager.getManager().executeCustomTask(JavaUtils.getMethod(MySongDAO.class, "loadAllSongsIdsOrderedByTime"), null, false, new BaseTaskHolder.ResultReceiver() {
            @Override
            public void onResult(Object result, Object info) {
                if (result instanceof List) {
                    if (((List) result).size() > 0) {
                        ArrayList<Long> ids = new ArrayList<>();
                        for (int i = 0; i < ((List) result).size() && ids.size() < maxItems; i++) {
                            ids.add((Long) ((List) result).get(i));
                        }
                        recentlyAddedAdapter.setIds(ids);
                    }
                }
            }
        });
        DataBaseManager.getManager().executeCustomTask(JavaUtils.getMethod(MySongDAO.class, "getMostPlayed"), null, false, new BaseTaskHolder.ResultReceiver() {
            @Override
            public void onResult(Object result, Object info) {
                if (result instanceof List) {
                    if (((List) result).size() > 0) {
                        ArrayList<Long> ids = new ArrayList<>();
                        for (int i = 0; i < ((List) result).size() && ids.size() < maxItems; i++) {
                            ids.add(((MostPlayedItem) ((List) result).get(i)).getId());
                        }
                        mostPlayedAdapter.setIds(ids);
                    }
                }
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        PlayerCommunicates.getINSTANCE().unRegisterCallback(playerCallbacks);
        ThemeEngine.getINSTANCE().unRegisterCallback(themeCallback);
    }

    private void manageRecyclers() {
        albumsRecyclerView = getUserView().findViewById(R.id.albums_recyclr_v);
        recentlyAddedRecyclerView = getUserView().findViewById(R.id.recnt_add_recyclr_v);
        mostPlayedRecyclerView = getUserView().findViewById(R.id.most_ply_recyclr_v);

        /*---------------- Albums----------------*/

        albumAdapter = new MainFragAlbumAdapter(maxItems);
        LinearLayoutManager linearLayoutManager_albums = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        albumsRecyclerView.setHasFixedSize(true);
        albumsRecyclerView.setLayoutManager(linearLayoutManager_albums);
        albumsRecyclerView.setAdapter(albumAdapter);

        /*----------------- Recently Added ------------------*/

        LinearLayoutManager linearLayoutManager_recentlyAdded = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recentlyAddedRecyclerView.setLayoutManager(linearLayoutManager_recentlyAdded);
        recentlyAddedRecyclerView.setHasFixedSize(true);
        recentlyAddedAdapter = new MainFragSongsAdapter("Recently added") {
            @Override
            public void onSongClick(long songId) {
                DataBaseManager.getManager().executeCustomTask(JavaUtils.getMethod(MySongDAO.class, "loadAllSongsIdsOrderedByTime"),
                        new long[]{RECENTLY_ADDED, songId}, false, songIDsReceiver);
            }

            @Override
            protected void onFootClick() {
                getFragmentSwipeBackManager().addFragment(BottomSheetPlaylistFrag2020.newInstance(0, 0));
            }
        };
        recentlyAddedRecyclerView.setAdapter(recentlyAddedAdapter);

        /*---------------------- Most Played -----------------------*/

        LinearLayoutManager linearLayoutManager_mostPlayed = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        mostPlayedRecyclerView.setLayoutManager(linearLayoutManager_mostPlayed);
        mostPlayedRecyclerView.setHasFixedSize(true);
        mostPlayedAdapter = new MainFragSongsAdapter("Most played") {
            @Override
            public void onSongClick(long songId) {
                DataBaseManager.getManager().executeCustomTask(JavaUtils.getMethod(MySongDAO.class, "getMostPlayed"),
                        new long[]{MOST_PLAYED, songId}, false, songIDsReceiver);
            }

            @Override
            protected void onFootClick() {
                getFragmentSwipeBackManager().addFragment(BottomSheetPlaylistFrag2020.newInstance(3, 0));
            }
        };
        mostPlayedRecyclerView.setAdapter(mostPlayedAdapter);
    }

    private void manageButtons() {
        allSongsButton = getUserView().findViewById(R.id.all_sng_b);
        recentlyPlayedSongsButton = getUserView().findViewById(R.id.recnt_ply_b);
        artistsButton = getUserView().findViewById(R.id.artst_b);
        playListsButton = getUserView().findViewById(R.id.ply_list_b);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.all_sng_b:
                        getFragmentSwipeBackManager().addFragment(new AllSongsFrag2020());
                        break;
                    case R.id.recnt_ply_b:
                        getFragmentSwipeBackManager().addFragment(BottomSheetPlaylistFrag2020.newInstance(1, 0));
                        break;
                    case R.id.artst_b:
                        break;
                    case R.id.ply_list_b:
                        PlayListsBottomFragment.newInstance().show(getFragmentManager(), "playLists");
                        break;
                }
            }
        };

        allSongsButton.setOnClickListener(onClickListener);
        recentlyPlayedSongsButton.setOnClickListener(onClickListener);
        artistsButton.setOnClickListener(onClickListener);
        playListsButton.setOnClickListener(onClickListener);

    }

    private void manageAppBar() {
        ViewGroup appbar = (ViewGroup) getLayoutInflater().inflate(R.layout.mainfrag_appbar_layout_2020, getToolbarContainer(), false);
        setToolbar(appbar);
        setToolbarSize(2.5f, CustomAppBar.UNIT_VIEW_SIZE);
//        setToolbarScrollingSize(50);

//        TextView appTitle = appbar.findViewById(R.id.app_title_text);
//        ShadowBackground_2 appTitleBack = new ShadowBackground_2(getContext(), appTitle, 45, (int) (15 * getDisplayDensity()), Color.LTGRAY);
//        appTitle.setBackground(appTitleBack);
//        appTitle.setTextColor(Color.BLACK);

        TextView searchButton = appbar.findViewById(R.id.searche_button);
        ImageButton timerButton = appbar.findViewById(R.id.timer_button);
        ImageButton settingButton = appbar.findViewById(R.id.setting_button);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.searche_button:
                        getFragmentSwipeBackManager().addFragment(new MainSearchFrag());
                        break;
                    case R.id.timer_button:
                        getFragmentSwipeBackManager().addFragment(new TimerChooseDialog2020_BottomSheetStyle());
                        break;
                    case R.id.setting_button:
                        getFragmentSwipeBackManager().addFragment(new SettingFragment());
                        break;
                }
            }
        };

        searchButton.setOnClickListener(onClickListener);
        timerButton.setOnClickListener(onClickListener);
        settingButton.setOnClickListener(onClickListener);

    }

    private void manageBottomPanel() {
        bottomPanel = getUserView().findViewById(R.id.now_playing_new);
        bottomSongTitle = bottomPanel.findViewById(R.id.now_playing_new_title);
        bottomPlayButton = bottomPanel.findViewById(R.id.main_frag_play_button);
        bottomProgressBar = getUserView().findViewById(R.id.main_frag_prog_bar);
        bottomTime = getUserView().findViewById(R.id.main_frag_time_text);

//        ShadowBackground playButtonBack = new ShadowBackground(getContext(), bottomPlayButton, 180, (int) (15 * getDisplayDensity()), Color.LTGRAY);
//        bottomPlayButton.setBackground(playButtonBack);

//        ShadowBackground shadowBackground = new ShadowBackground(getContext(), bottomSongTitle, 45, (int) (10 * getDisplayDensity()), Color.WHITE);
//        bottomSongTitle.setBackground(shadowBackground);

        bottomPanel.setOnClickListener(v -> getFragmentSwipeBackManager().addFragment(new PlayerFrag2020_notReverse()));
        bottomPlayButton.setColor(Color.WHITE);
        bottomProgressBar.setAccentColor(Color.WHITE);

        bottomPlayButton.setOnClickListener(v -> {
            if (PlayerCommunicates.getINSTANCE().isPlaying()) {
                PlayerCommunicates.getINSTANCE().pause();
            } else {
                PlayerCommunicates.getINSTANCE().play();
            }
        });

    }

    @Override
    public void makeState(Bundle state) {

    }

    @Override
    public void checkForRestoreState() {

    }
}
