package com.br.mreza.musicplayer.newdesign.newmodelfrags;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.br.mreza.musicplayer.DataSong;
import com.br.mreza.musicplayer.MbnController;
import com.br.mreza.musicplayer.MbnNowPlayingDecor;
import com.br.mreza.musicplayer.NewService.A_B_Button;
import com.br.mreza.musicplayer.NewService.MusicInfoHolder;
import com.br.mreza.musicplayer.R;
import com.br.mreza.musicplayer.StorageUtils;
import com.br.mreza.musicplayer.newdesign.AlbumCoverShowDialog;
import com.br.mreza.musicplayer.newdesign.BackgroundFullScreenImageView;
import com.br.mreza.musicplayer.newdesign.MbnMusicPlayer;
import com.br.mreza.musicplayer.newdesign.NewSongOptionDialog_NewMenuApi;
import com.br.mreza.musicplayer.newdesign.PlayerPageDecoration;
import com.br.mreza.musicplayer.newdesign.customviews.AB_ProgressBar;
import com.br.mreza.musicplayer.newdesign.customviews.NewPlayButton;
import com.br.mreza.musicplayer.newdesign.customviews.StateFulImageButton;
import com.br.mreza.musicplayer.newdesign.materialtheme.PlayerPagePagerAdapter;
import com.br.mreza.musicplayer.newdesign.materialtheme.PlayerPageViewPager;
import com.br.mreza.musicplayer.newdesign.materialtheme.pageDecorationBlur;
import com.br.mreza.musicplayer.newdesign.materialtheme.pageDecorationMaterialTheme;
import com.br.mreza.musicplayer.newmodel.adapters.songs.NewModelCurrentQueueAdapter;
import com.br.mreza.musicplayer.newmodel.database.A_B_Manager;
import com.br.mreza.musicplayer.newmodel.database.DataBaseManager;
import com.br.mreza.musicplayer.newmodel.database.DataBaseUtils;
import com.br.mreza.musicplayer.newmodel.service.player.PlayerCommunicates;
import com.br.mreza.musicplayer.newmodel.theme.ThemeEngine;

import java.util.ArrayList;


import mbn.libs.UI.ThemeCoordinator;
import mbn.libs.backgroundtask.BaseTaskHolder;
import mbn.libs.backgroundtask.ThreadManager;
import mbn.libs.fragmanager.BaseFragment;
import mbn.libs.fragmanager.ResultReceivingManager;
import mbn.libs.fragmanager.TouchEaterRecyclerView;
import rm.com.audiowave.AudioWaveView;
import rm.com.audiowave.OnProgressListener;

import static mbn.libs.fragmanager.CustomFragmentSwipeBackAnimator.ANIM_BOTTOM_FANCY;


public class NewModelTelegramStyleFrag extends BaseFragment.ReverseFragment implements View.OnClickListener, View.OnLongClickListener,
        PlayerCommunicates.PlayerCallbacks, MusicInfoHolder.CallBack, A_B_Manager.A_B_Interface/*,
        ThemeCoordinator.ProfileListener<ThemeCoordinator.DefaultThemeTypes, ThemeCoordinator.ColorItem>*/ {

    private final int STATE_SHUFFLE_ON = 1;

    private final int STATE_REPEAT_OFF = 1;
    private final int STATE_REPEAT_ALL = 2;
    private final int STATE_REPEAT_ONE = 3;

    private BackgroundFullScreenImageView album_big;
    private PlayerPageDecoration pageDecoration;
    private MbnNowPlayingDecor mbnNowPlayingDecor;

    private DataSong currentSong;
    private long currentID;

    private int themeType;
    private TextView time;
    private TextView title;
    private TextView artist;
    private TextView bitrate;
    private TextView numberInQU;
    private ImageView albumArtImage;
    //    private VisualizerView visualizerView;
//    private VisualizerNewDesign visualizerNewDesign;
    private TouchEaterRecyclerView currentQueueList;
    private int activeColor = Color.parseColor("#9bfff3");
    private A_B_Button a_b_button;
    private ImageButton previous_Button;
    private NewPlayButton play_Button;
    private ImageButton next_Button;
    private StateFulImageButton repeat_Button;
    private ImageView like_Button;
    private StateFulImageButton shuffle_Button;
    private ImageButton option_button;
    //    private MbnNewDesignLinearSeekBar seekBar;
    private AudioWaveView seekBar;
    private int durationThis;
    private boolean seekBarInTouch = false;
    private NewModelCurrentQueueAdapter adapter;
    private A_B_Manager.A_B_Object a_b_object;
    private TextView aButton, bButton, abOffButton;
    private AB_ProgressBar ab_progressBar;
    private View abLayout;

    //----------------------- ImageViewPager -------------------//

    private PlayerPageViewPager playerPageViewPager;
    private PlayerPagePagerAdapter playerPagePagerAdapter;


//    @Override
//    public boolean canInterceptTouches() {
//        return !currentQueueList.canScrollVertically(-1) || pageDecoration.canIntercept();
//
//    }

    @Override
    public boolean canPopThisFragment() {
        if (adapter.isInSelectionMode()) {
            adapter.cancelSelect();
            return false;
        }
        return true;
    }

    @Override
    public View onCreateViewForChild(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        themeType = StorageUtils.getThemeType();
        return inflater.inflate(R.layout.new_telegram_style_player, container, false);
    }


    @Override
    public int getAnimationMode() {
        return ANIM_BOTTOM_FANCY;
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


    public void onProfileItemChanged(ThemeCoordinator.DefaultThemeTypes type, ThemeCoordinator.ColorItem changed) {
        int back = 0;
        int accent = 0;
        switch (type) {
            case BackgroundColor:
                if (pageDecoration instanceof pageDecorationBlur) {
                    ((pageDecorationBlur) pageDecoration).setAccent(accent, back = changed.getValue());
                }
                break;
            case AccentColor:
                if (pageDecoration instanceof pageDecorationBlur) {
                    ((pageDecorationBlur) pageDecoration).setAccent(accent = changed.getValue(), back);
                }
                break;
            case TitleTextColor:
                break;
            case ContextTextColor:
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playerPageViewPager = view.findViewById(R.id.player_view_pager);
        playerPagePagerAdapter = new PlayerPagePagerAdapter(DataBaseUtils.defaultArt, playerPageViewPager);
        playerPageViewPager.setAdapter(playerPagePagerAdapter);

        LinearLayout mainTop = view.findViewById(R.id.player_frag_linear_top);
        mainTop.setLayoutTransition(new LayoutTransition());

        album_big = view.findViewById(R.id.player_back_cover);
        if (themeType != 0) album_big.setMaterial(true);

        arrangeTexts(view);
        arrangeButtons(view);
        arrangeSeekBar(view);
        manageAB();

        registerResultReceiver(new ResultReceivingManager.ResultReceiver() {
            @SuppressLint("Range")
            @Override
            public void onReceiveResultFromFragment(Object resultObject) {
                AlbumCoverShowDialog.InfoGetter infoGetter = (AlbumCoverShowDialog.InfoGetter) resultObject;
                View v = albumArtImage;
                int[] info = new int[4];
                v.getLocationInWindow(info);
                info[2] = v.getWidth();
                info[3] = v.getHeight();
                infoGetter.setInfo(info);
            }
        });

    }

    private void setCurrentSong(long id, DataSong song) {
        playerPagePagerAdapter.setCurrentId(id);
        currentID = id;
        currentSong = song;
        onChange();
        mbnNowPlayingDecor.setId(currentID);
        ThreadManager.getAppGlobalTask_MultiThread().StartTask(new MusicFileToByte(song.getPath(), song.getId()), fileBytesReceiver);
    }

    private BaseTaskHolder.ResultReceiver fileBytesReceiver = new BaseTaskHolder.ResultReceiver() {
        @Override
        public void onResult(Object result, Object info) {
//            Log.i("RAW", "onResult: yes");
            if ((long) info == currentID) {
                seekBar.setRawData((byte[]) result);
            }
        }
    };

    private DataBaseManager.DefaultCallback databaseCallback = new DataBaseManager.DefaultCallback(true, true) {
        @Override
        public void onTrackChange(long id, DataSong song) {
            setCurrentSong(id, song);
        }

        @Override
        public void onQueueChange(ArrayList<Long> ids) {
            adapter.setSongsIDs(ids);
            playerPagePagerAdapter.setQueue(ids);
        }
    };

    private ThemeEngine.DefaultThemeCallback themeCallback = new ThemeEngine.DefaultThemeCallback(true, true, true) {
        @Override
        public void onProcessFinished() {
            currentQueueList.invalidateItemDecorations();

        }

        @Override
        public void onMainBitmap(Bitmap main) {
//            album_big.setImgBitmap(main);
            albumArtImage.setImageBitmap(main);

        }

        @Override
        public void onBlurBitmap(Bitmap blur) {
            pageDecoration.setImageBitmap(blur);
            album_big.setImgBitmap(blur);
        }

        @Override
        public void onAccentColor(int color) {
            seekBar.setWaveColor(color);
            play_Button.setAccentColor(color);
            shuffle_Button.setAccentColor(color);
            repeat_Button.setAccentColor(color);
//            visualizerNewDesign.setColor(color);
            mbnNowPlayingDecor.setColor(color);
//            if (pageDecoration instanceof pageDecorationMaterialTheme) {
//                ((pageDecorationMaterialTheme) pageDecoration).setAccent(color);
//            }
            currentQueueList.invalidate();
            bitrate.setTextColor(color);
            numberInQU.setTextColor(color);
            title.setTextColor(color);
        }
    };

    private void arrangeTexts(View view) {


//        visualizerView = view.findViewById(R.id.mbn_visy);
//        visualizerNewDesign = view.findViewById(R.id.circular_visualizer);
        time = view.findViewById(R.id.player_frag_time_text);
        title = view.findViewById(R.id.player_frag_title_text);
        artist = view.findViewById(R.id.player_frag_artist_text);
        bitrate = view.findViewById(R.id.player_frag_bitrate_text);
        numberInQU = view.findViewById(R.id.player_frag_number_text);
        albumArtImage = view.findViewById(R.id.player_frag_album_art);
        albumArtImage.setClipToOutline(true);

        albumArtImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentSwipeBackManager().addFragment(AlbumCoverShowDialog.newInstance(getResultID()));
            }
        });

        option_button = view.findViewById(R.id.player_frag_option_button);
        option_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentSwipeBackManager().addFragment(NewSongOptionDialog_NewMenuApi.newInstance(currentID, true));
            }
        });

    }

    private Handler holdHandler = new Handler(Looper.getMainLooper());

    private Runnable holdRunnable_forward = new Runnable() {
        @Override
        public void run() {
            PlayerCommunicates.getINSTANCE().fastForward();
            holdHandler.postDelayed(this, 300);

        }
    };
    private Runnable holdRunnable_rewind = new Runnable() {
        @Override
        public void run() {
            PlayerCommunicates.getINSTANCE().rewind();
            holdHandler.postDelayed(this, 300);
        }
    };

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getActionMasked() == MotionEvent.ACTION_UP || event.getActionMasked() == MotionEvent.ACTION_CANCEL) {
                holdHandler.removeCallbacks(holdRunnable_forward);
                holdHandler.removeCallbacks(holdRunnable_rewind);
            }

            return false;
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    private void arrangeButtons(View view) {

        a_b_button = view.findViewById(R.id.a_b_button);
        previous_Button = view.findViewById(R.id.player_frag_pervious);
        previous_Button.setOnClickListener(this);
        previous_Button.setOnLongClickListener(this);
        previous_Button.setOnTouchListener(touchListener);
        play_Button = view.findViewById(R.id.player_frag_play_pause);
        play_Button.setOnClickListener(this);
        next_Button = view.findViewById(R.id.player_frag_skip_next);
        next_Button.setOnClickListener(this);
        next_Button.setOnLongClickListener(this);
        next_Button.setOnTouchListener(touchListener);
        repeat_Button = view.findViewById(R.id.player_frag_repeat);
        repeat_Button.setOnClickListener(this);
        repeat_Button.addToStates(STATE_REPEAT_ONE, R.drawable.ic_repeat_one_black_24dp);
        repeat_Button.addToStates(STATE_REPEAT_ALL, R.drawable.ic_repeat_black_24dp);
        repeat_Button.addToStates(STATE_REPEAT_OFF, R.drawable.ic_repeat_black_24dp);
        shuffle_Button = view.findViewById(R.id.player_farg_shuffle);
        shuffle_Button.setOnClickListener(this);
        shuffle_Button.addToStates(STATE_SHUFFLE_ON, R.drawable.ic_suffle_option);
        ImageButton findNowPlaying = view.findViewById(R.id.player_frag_find_button);
        ImageButton goToTop = view.findViewById(R.id.player_frag_go_top_button);

        View.OnClickListener find_top = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.player_frag_find_button:
                        currentQueueList.scrollToPosition(adapter.getPositionForID(currentID));
                        break;
                    case R.id.player_frag_go_top_button:
                        currentQueueList.smoothScrollToPosition(0);
                        break;
                }
            }
        };

        findNowPlaying.setOnClickListener(find_top);
        goToTop.setOnClickListener(find_top);

        makeTheList(view);
    }

    private float percentGetter(float percent, int number) {
        return number * (percent / 100);
    }

    private void arrangeSeekBar(final View view) {

        seekBar = view.findViewById(R.id.player_frag_seekbar);
//        seekBar.setPopTimeText((TextView) view.findViewById(R.id.touch_time_show));

        seekBar.setOnProgressListener(new OnProgressListener() {
            @Override
            public void onStartTracking(float v) {
                seekBarInTouch = true;
            }

            @Override
            public void onStopTracking(float v) {
                PlayerCommunicates.getINSTANCE().seek((long) percentGetter(v, durationThis));
                seekBarInTouch = false;
            }

            @Override
            public void onProgressChanged(float v, boolean b) {
                if (b) {
                    time.setText(MbnController.timeConverter(percentGetter(v, durationThis), durationThis));
                }
            }
        });

//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
//                if (b) {
//                    time.setText(MbnController.timeConverter(value, durationThis));
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                seekBarInTouch = true;
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                PlayerCommunicates.getINSTANCE().seek(seekBar.getProgress());
//                seekBarInTouch = false;
//            }
//        });

    }


    private void makeTheList(View view) {
        currentQueueList = view.findViewById(R.id.player_frag_queue_recycler);
        @SuppressLint("WrongConstant") LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        currentQueueList.setLayoutManager(manager);
        currentQueueList.setHasFixedSize(true);
        switch (themeType) {
            case 0:
                pageDecoration = new PlayerPageDecoration(getContext(), view.findViewById(R.id.player_frag_linear_top), currentQueueList);
                break;
            case 1:
                pageDecoration = new pageDecorationBlur(getContext(), view.findViewById(R.id.frame_layout_top), currentQueueList, playerPageViewPager);
//                pageDecoration = new pageDecorationMaterialTheme(getContext(), view.findViewById(R.id.frame_layout_top), currentQueueList, playerPageViewPager);
                break;
        }
        currentQueueList.addItemDecoration(pageDecoration);
        mbnNowPlayingDecor = new MbnNowPlayingDecor();
        currentQueueList.addItemDecoration(mbnNowPlayingDecor);
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.START) {

            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

//                Collections.swap(currentQueue, viewHolder.getAdapterPosition(), target.getAdapterPosition());
//
//                recyclerView.getAdapter().notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
//
//                StorageUtils.setCurrent(getContext());

                //TODO: implement

                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

//                viewHolder.itemView.animate().translationX(0f).setDuration(400);

//                if (currentQueue.indexOf(getCurrentTrack()) == viewHolder.getAdapterPosition()) {
//                    MbnController.next(getContext());
//                }
//
//                currentQueue.remove(viewHolder.getAdapterPosition());
//                currentQueueList.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
//                MbnController.makeUnshuffledList();
//                StorageUtils.setCurrent(getContext());

                //TODO: implement

            }
        };
        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleCallback);
        adapter = new NewModelCurrentQueueAdapter(getContext(), touchHelper, view.findViewById(R.id.selection_options), getFragmentManager());
        currentQueueList.setAdapter(adapter);
        touchHelper.attachToRecyclerView(currentQueueList);


    }

    @SuppressLint("SetTextI18n")
    private void onChange() {
        NewPlayButton.PlayingState state = PlayerCommunicates.getINSTANCE().isPlaying() ? NewPlayButton.PlayingState.STATE_PLAYING : NewPlayButton.PlayingState.STATE_PAUSED;
        play_Button.setState(state);

        if (StorageUtils.isShuffle(getContext())) {
            shuffle_Button.setCurrentState(STATE_SHUFFLE_ON);
            shuffle_Button.setActive(true);
        } else {
            shuffle_Button.setCurrentState(STATE_SHUFFLE_ON);
            shuffle_Button.setActive(false);
        }
        if (StorageUtils.isRepeat(getContext())) {
            repeat_Button.setCurrentState(STATE_REPEAT_ONE);
            repeat_Button.setActive(true);
        } else {
            repeat_Button.setCurrentState(STATE_REPEAT_OFF);
            repeat_Button.setActive(false);
        }

        durationThis = currentSong.getDuration();
        if (a_b_object != null) {
            ab_progressBar.setPoints(a_b_object, durationThis);
        }
//        seekBar.setMax(durationThis);
        time.setText(MbnController.timeConverter(0, durationThis));
        String bitrateString = MbnController.makeBitRate(currentSong.getBitRate());
        bitrate.setText(bitrateString);

        title.setText(currentSong.getTitle());
        artist.setText(currentSong.getArtistTitle());

        numberInQU.setText(adapter.getPositionForID(currentID) + 1 + " of " + adapter.getItemCount());

    }

    private void everyTick(String formattedTime, int position) {
        float sent = ((float) position / durationThis) * 100;
        if (sent > 100) {
            sent = 100;
        }
        seekBar.setProgress(sent);
        time.setText(formattedTime);
    }

    @Override
    public void onStart() {
        super.onStart();
        DataBaseManager.getManager().registerCallback(databaseCallback);
        DataBaseManager.getManager().getCurrentQueueAndTrack(new BaseTaskHolder.ResultReceiver() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResult(Object result, Object info) {
                ArrayList<Long> ids = (ArrayList<Long>) ((Object[]) result)[0];
                adapter.setSongsIDs(ids);
                playerPagePagerAdapter.setQueue(ids);
                DataSong song = (DataSong) ((Object[]) result)[1];
                setCurrentSong(song.getId(), song);
            }
        });
        A_B_Manager.INSTANCE.request(A_B_Manager.SET_AGENT, 0, this);
        MbnMusicPlayer.THEME_COORDINATOR.registerListener(this::onProfileItemChanged);
    }

    @Override
    public void onStop() {
        super.onStop();
        DataBaseManager.getManager().unRegisterCallback(databaseCallback);
        A_B_Manager.INSTANCE.unregister(this);
        MbnMusicPlayer.THEME_COORDINATOR.unregisterListener(this::onProfileItemChanged);
    }

    @Override
    public void onResume() {
        super.onResume();
        ThemeEngine.getINSTANCE().registerCallback(themeCallback);
        ThemeEngine.getINSTANCE().getResult(themeCallback);
        PlayerCommunicates.getINSTANCE().registerCallback(this);
        MusicInfoHolder.registerCallback(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ThemeEngine.getINSTANCE().unRegisterCallback(themeCallback);
        PlayerCommunicates.getINSTANCE().unRegisterCallback(this);
        MusicInfoHolder.unRegisterCllBack(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.player_frag_skip_next:
                DataBaseManager.getManager().performNext();
                break;
            case R.id.player_frag_play_pause:
                if (PlayerCommunicates.getINSTANCE().isPlaying()) {
                    PlayerCommunicates.getINSTANCE().pause();
                } else {
                    PlayerCommunicates.getINSTANCE().play();
                }
                break;
            case R.id.player_frag_top_play_button:
                if (PlayerCommunicates.getINSTANCE().isPlaying()) {
                    PlayerCommunicates.getINSTANCE().pause();
                } else {
                    PlayerCommunicates.getINSTANCE().play();
                }
                break;
            case R.id.player_frag_pervious:
                PlayerCommunicates.getINSTANCE().checkForPrevious();
                break;
            case R.id.frag_like_button:
                break;
            case R.id.player_frag_repeat:
                if (StorageUtils.isRepeat(getContext())) {
                    StorageUtils.setRepeat(getContext(), false);
                } else {
                    StorageUtils.setRepeat(getContext(), true);
                }
                onChange();
                break;
            case R.id.player_farg_shuffle:
                if (StorageUtils.isShuffle(getContext())) {
                    StorageUtils.setShuffle(getContext(), false);
                } else {
                    StorageUtils.setShuffle(getContext(), true);
                }
                onChange();
                break;

            case R.id.frag_showQueue_button:
                break;
        }


    }


    @Override
    public boolean onLongClick(View v) {
        holdHandler.removeCallbacks(holdRunnable_forward);
        holdHandler.removeCallbacks(holdRunnable_rewind);

        if (v.getId() == R.id.player_frag_skip_next) {
            holdHandler.post(holdRunnable_forward);
        } else {
            holdHandler.post(holdRunnable_rewind);
        }

        return true;
    }

    @Override
    public void onCurrentPosChange(long pos) {
        if (!seekBarInTouch)
            everyTick(MbnController.timeConverter(pos, durationThis), (int) pos);
    }

    @Override
    public void onPlayerStateChange(boolean state) {
        NewPlayButton.PlayingState useState = state ? NewPlayButton.PlayingState.STATE_PLAYING : NewPlayButton.PlayingState.STATE_PAUSED;
        play_Button.setState(useState);
    }

    @Override
    public void onA_B_Change() {

    }

    private void manageAB() {
        View view = getUserView();
        abLayout = view.findViewById(R.id.ab_layout);
        aButton = view.findViewById(R.id.set_a_button);
        bButton = view.findViewById(R.id.set_b_button);
        abOffButton = view.findViewById(R.id.ab_off_button);
        ab_progressBar = view.findViewById(R.id.ab_progressbar);
        abOffButton.setOnClickListener(v -> A_B_Manager.INSTANCE.request(A_B_Manager.SET_STATE_OFF, 0, null));

        View.OnClickListener abListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.set_a_button:
                        PlayerCommunicates.getINSTANCE().setA();
                        break;
                    case R.id.set_b_button:
                        PlayerCommunicates.getINSTANCE().setB();
                        break;
                }
            }
        };

        aButton.setOnClickListener(abListener);
        bButton.setOnClickListener(abListener);

    }

    @Override
    public void stateChanged(A_B_Manager.A_B_Object a_b_object) {
        this.a_b_object = a_b_object;
        if (a_b_object.isState()) {
            abLayout.setVisibility(View.VISIBLE);
        } else {
            abLayout.setVisibility(View.GONE);
        }
        currentQueueList.invalidate();
        posChanged(a_b_object);
    }

    @Override
    public void posChanged(A_B_Manager.A_B_Object a_b_object) {
        this.a_b_object = a_b_object;
        ab_progressBar.setPoints(a_b_object, durationThis);
        if (a_b_object.isaState()) {
            aButton.setTextColor(Color.GREEN);
        } else {
            aButton.setTextColor(Color.BLACK);
        }
        if (a_b_object.isbState()) {
            bButton.setTextColor(Color.GREEN);
        } else {
            bButton.setTextColor(Color.BLACK);
        }

    }
}
