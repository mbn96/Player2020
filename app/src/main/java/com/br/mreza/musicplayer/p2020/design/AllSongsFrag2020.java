package com.br.mreza.musicplayer.p2020.design;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.br.mreza.musicplayer.DataSong;
import com.br.mreza.musicplayer.MbnController;
import com.br.mreza.musicplayer.MbnNowPlayingDecor;
import com.br.mreza.musicplayer.MySongDAO;
import com.br.mreza.musicplayer.R;
import com.br.mreza.musicplayer.newmodel.database.DataBaseManager;
import com.br.mreza.musicplayer.newmodel.service.player.PlayerCommunicates;
import com.br.mreza.musicplayer.newmodel.theme.ThemeEngine;
import com.br.mreza.musicplayer.p2020.adapters.AllSongsAdapter2020;

import java.util.ArrayList;
import java.util.List;

import mbn.libs.backgroundtask.BaseTaskHolder;
import mbn.libs.fragmanager.BaseFragment;
import mbn.libs.fragmanager.CustomAppBar;
import mbn.libs.utils.JavaUtils;

public class AllSongsFrag2020 extends BaseFragment {

    public static final String SONGS_ORDER_NAME = "loadAllSongsIdsOrderedByName";
    public static final String SONGS_ORDER_DATE = "loadAllSongsIdsOrderedByTime";
    public static String[] ORDERS;

    static {
        ORDERS = new String[]{SONGS_ORDER_NAME, SONGS_ORDER_DATE};
    }


    private RecyclerView songsRecyclerView;
    private AllSongsAdapter2020 allSongsAdapter;
    private MbnNowPlayingDecor mbnNowPlayingDecor;

    private DataSong currentSong;

    private ImageButton backButton;
    private ImageButton sortButton;

    private View selectLayout;
    private View normalLayout;


    private DataBaseManager.DefaultCallback dataBaseCallback = new DataBaseManager.DefaultCallback(true, false) {
        @Override
        public void onTrackChange(long id, DataSong song) {
            setCurrentSong(song);
        }
    };

//    private PlayerCommunicates.PlayerCallbacks playerCallbacks = new PlayerCommunicates.PlayerCallbacks() {
//        @Override
//        public void onCurrentPosChange(long pos) {
//            if (currentSong != null) {
//                bottomProgressBar.setProgress((float) pos / currentSong.getDuration());
//                bottomTime.setText(MbnController.timeConverter(pos, currentSong.getDuration()));
//            }
//        }
//
//        @Override
//        public void onPlayerStateChange(boolean state) {
//            bottomPlayButton.setMode(state);
//        }
//    };

    private ThemeEngine.DefaultThemeCallback themeCallback = new ThemeEngine.DefaultThemeCallback(false, false, true) {
        @Override
        public void onProcessFinished() {
            songsRecyclerView.invalidateItemDecorations();
        }

        @Override
        public void onAccentColor(int color) {
            mbnNowPlayingDecor.setColor(color);
//            setToolBarBackgroundColor(color);
        }

    };

    private void setCurrentSong(DataSong currentSong) {
        this.currentSong = currentSong;
        mbnNowPlayingDecor.setId(currentSong.getId());
    }

    @Override
    public View onCreateViewForChild(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.all_songs_layout2020, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCanStartAnim(false);
        setToolbar(getLayoutInflater().inflate(R.layout.all_songs_appbar2020, getToolbarContainer(), false));
        setToolBarBackgroundColor(Color.WHITE);
        setToolbarElevation(1 * getDisplayDensity());
        setToolbarSize(1, CustomAppBar.UNIT_VIEW_SIZE);

        manageAppBar();
        manageRecyclerView(view);
    }

    private void manageAppBar() {
        selectLayout = getToolbar().findViewById(R.id.select_layout);
        normalLayout = getToolbar().findViewById(R.id.normal_layout);

        backButton = getToolbar().findViewById(R.id.back_button);
        sortButton = getToolbar().findViewById(R.id.option_button);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.back_button:
                        getFragmentSwipeBackManager().popFragment();
                        break;
                    case R.id.option_button:
                        // TODO: 2/9/2020 implement...
                        break;
                }
            }
        };
        backButton.setOnClickListener(onClickListener);
        sortButton.setOnClickListener(onClickListener);
    }

    private void manageRecyclerView(View view) {
        songsRecyclerView = view.findViewById(R.id.recycler_view_songs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        songsRecyclerView.setLayoutManager(layoutManager);
        songsRecyclerView.setHasFixedSize(true);
        songsRecyclerView.setAdapter(allSongsAdapter = new AllSongsAdapter2020(getContext(), null, selectLayout, normalLayout, getFragmentManager()));
        allSongsAdapter.setContextColor(Color.DKGRAY);
        allSongsAdapter.setTitleColor(Color.BLACK);
        songsRecyclerView.addItemDecoration(mbnNowPlayingDecor = new MbnNowPlayingDecor());
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
    }

    @Override
    public void onStop() {
        super.onStop();
        DataBaseManager.getManager().unRegisterCallback(dataBaseCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        ThemeEngine.getINSTANCE().registerCallback(themeCallback);
        ThemeEngine.getINSTANCE().getResult(themeCallback);

        DataBaseManager.getManager().executeCustomTask(JavaUtils.getMethod(MySongDAO.class, ORDERS[0]), null, true, new BaseTaskHolder.ResultReceiver() {
            @Override
            public void onResult(Object result, Object info) {
                if (result instanceof List) {
                    if (((List) result).size() > 0) {
                        //noinspection unchecked
                        allSongsAdapter.setSongsIDs((ArrayList<Long>) result);
                    }
                }
                getUserView().postDelayed(() -> setCanStartAnim(true), 100);
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        ThemeEngine.getINSTANCE().unRegisterCallback(themeCallback);
    }

    @Override
    public boolean canPopThisFragment() {
        if (allSongsAdapter.isInSelectionMode()) {
            allSongsAdapter.cancelSelect();
            return false;
        }
        return true;
    }

    @Override
    public void makeState(Bundle state) {

    }

    @Override
    public void checkForRestoreState() {

    }
}
