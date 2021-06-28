//package com.br.mreza.musicplayer;
//
//
//import android.annotation.SuppressLint;
//import android.app.ActivityOptions;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.res.ColorStateList;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.ResultReceiver;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.LocalBroadcastManager;
//import android.support.v4.media.session.MediaControllerCompat;
//import android.support.v4.view.ViewPager;
//import android.support.v7.graphics.Palette;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.helper.ItemTouchHelper;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.SeekBar;
//import android.widget.TextView;
//
//import com.br.mreza.musicplayer.MBN.bitmapUtils.MbnUtils;
//import com.br.mreza.musicplayer.MBN.customViews.MbnImageButtonChangeIcon;
//import com.br.mreza.musicplayer.MBN.customViews.MbnNewDesignLinearSeekBar;
//import com.br.mreza.musicplayer.MBN.customViews.MbnTouchForceRecyclerView;
//import com.br.mreza.musicplayer.MBN.customViews.VisualizerNewDesign;
//import com.br.mreza.musicplayer.MBN.customViews.VisualizerView;
//import com.br.mreza.musicplayer.UI.RecyclerViews.ItemDecors.SpringOnScrollEnding;
//
//import java.util.Collections;
//
//import static com.br.mreza.musicplayer.ListMaker.currentQueue;
//import static com.br.mreza.musicplayer.ListMaker.currentTrack;
//import static com.br.mreza.musicplayer.ListMaker.currentTrackNumberInQueue;
//import static com.br.mreza.musicplayer.MbnController.ON_TICK;
//import static com.br.mreza.musicplayer.MbnController.To_FRAGMENT;
//import static com.br.mreza.musicplayer.MbnController.playerFragmentIsOn;
//import static com.br.mreza.musicplayer.MbnController.playerState;
//import static com.br.mreza.musicplayer.MbnController.seekPosition;
//import static com.br.mreza.musicplayer.MbnController.unShuffledQueue;
//
//public class PlayerFragmentNewDesign extends Fragment implements View.OnClickListener, View.OnLongClickListener {
//
//    TextView time;
//    TextView title;
//    //    TextView album;
//    TextView artist;
//    TextView bitrate;
//    TextView numberInQU;
//    ImageView albumArtImage;
//
//    VisualizerView visualizerView;
//    VisualizerNewDesign visualizerNewDesign;
//
//    MbnTouchForceRecyclerView currentQueueList;
//
////    Bitmap likeEmpty;
////    Bitmap likeFull;
////    Bitmap play;
////    Bitmap pause;
//
//
//    MediaControllerCompat mediaController;
//    MediaControllerCompat.TransportControls transportControls;
//
//
//    private String bitrateString;
//
//    int activeColor = Color.parseColor("#9bfff3");
//
////    ImageView backGround;
//
////    AdvancedMbnBlur mbnBlur = new AdvancedMbnBlur();
//
//    ImageButton previous_Button;
//    //    MbnPlayButton play_Button;
//    MbnImageButtonChangeIcon play_Button;
//    MbnImageButtonChangeIcon top_play_Button;
//    ImageButton next_Button;
//    ImageButton repeat_Button;
//    ImageView like_Button;
//    ImageButton shuffle_Button;
//    ViewPager swipeViewPager;
////    ImageButton showQueueButton;
//
//
//    //    MbnCircularSeekBar seekBar;
//    MbnNewDesignLinearSeekBar seekBar;
//
//    int durationThis;
//    String formattedTime = "";
//
//    String trackCode = "";
//
//
//    Context context;
//    //    private LocalBroadcastManager localBroadcastManager;
//    private Bitmap cover;
//    private CurrentQueuePageAdapter adapter;
//    private SwipeAdapter swipeAdapter;
//
//
//    public static PlayerFragmentNewDesign newInstance() {
//
//
//        return new PlayerFragmentNewDesign();
//    }
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        context = inflater.getContext();
//
//        try {
//            mediaController = new MediaControllerCompat(inflater.getContext(), MediaSessionHolder.getMediaSession(getContext()).getSessionToken());
//            transportControls = mediaController.getTransportControls();
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return inflater.inflate(R.layout.player_fragment_new_design, container, false);
//    }
//
//
//    private void orangeTexts(View view) {
//
//        visualizerView = view.findViewById(R.id.mbn_visy);
//        visualizerNewDesign = view.findViewById(R.id.circular_visualizer);
//
////        MediaSessionHolder.visyHelper.setVisualizerView(visualizerView);
//        MediaSessionHolder.visyHelper.setVisualizerNewDesign(visualizerNewDesign);
//
//        time = view.findViewById(R.id.player_frag_time_text);
//        title = view.findViewById(R.id.player_frag_title_text);
////        album = view.findViewById(R.id.frag_album_text);
//        artist = view.findViewById(R.id.player_frag_artist_text);
//        bitrate = view.findViewById(R.id.player_frag_bitrate_text);
//        numberInQU = view.findViewById(R.id.player_frag_number_text);
//        albumArtImage = view.findViewById(R.id.player_frag_album_art);
//
//        albumArtImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent showCovIntent = new Intent(getContext(), CoverShowActivity.class);
//
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//////                    ActivityOptions options = ActivityOptions.makeScaleUpAnimation(view, 0, 0, 0, 0);
//////                    startActivity(showCovIntent);
////                    startActivity(showCovIntent, options.toBundle());
////
////                } else {
//
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "img_art_trans");
////                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity());
//
//
//                startActivity(showCovIntent, options.toBundle());
////                }
//
//            }
//        });
//
//
//        artist.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//
//                SongOptionDialog.newInstance().show(getFragmentManager(), "options");
//
//                return true;
//            }
//        });
//
////        title.setHelper(new MbnScrollingTextView.TextViewSwipeHelper() {
////            @Override
////            public void onSwipe(boolean direction) {
////
////                if (direction) {
////                    MbnController.previous(getContext());
////                } else {
////                    MbnController.next(getContext());
////                }
////
////            }
////        });
//
//
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    private void orangeButtons(View view) {
//
//        previous_Button = view.findViewById(R.id.player_frag_pervious);
//        previous_Button.setOnClickListener(this);
//        previous_Button.setOnLongClickListener(this);
//        previous_Button.setOnTouchListener(touchListener);
//        play_Button = view.findViewById(R.id.player_frag_play_pause);
//        top_play_Button = view.findViewById(R.id.player_frag_top_play_button);
//        play_Button.setOnClickListener(this);
//        top_play_Button.setOnClickListener(this);
//        next_Button = view.findViewById(R.id.player_frag_skip_next);
//        next_Button.setOnClickListener(this);
//        next_Button.setOnLongClickListener(this);
//        next_Button.setOnTouchListener(touchListener);
//        repeat_Button = view.findViewById(R.id.player_frag_repeat);
//        repeat_Button.setOnClickListener(this);
////        like_Button = view.findViewById(R.id.frag_like_button);
////        like_Button.setOnClickListener(this);
//        shuffle_Button = view.findViewById(R.id.player_farg_shuffle);
//        shuffle_Button.setOnClickListener(this);
////        showQueueButton = view.findViewById(R.id.frag_showQueue_button);
////        showQueueButton.setOnClickListener(this);
//
//        makeTheList(view);
//    }
//
//    private void makeTheList(View view) {
//        currentQueueList = view.findViewById(R.id.player_frag_queue_recycler);
//
//        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        currentQueueList.setLayoutManager(manager);
//        currentQueueList.setHasFixedSize(true);
//
//
//        adapter = new CurrentQueuePageAdapter(getContext());
//        currentQueueList.setAdapter(adapter);
//        currentQueueList.setSpringOnScrollEnding(new SpringOnScrollEnding(currentQueueList));
////        currentQueueList.addItemDecoration(new SpringOnScrollEnding(currentQueueList));
//        currentQueueList.addItemDecoration(new MbnNowPlayingDecor());
////        currentQueueList.addItemDecoration(new MbnRecyclerDecor(getContext()));
//
//
//        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.START) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//
//                Collections.swap(currentQueue, viewHolder.getAdapterPosition(), target.getAdapterPosition());
//
//                recyclerView.getAdapter().notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
//
//                StorageUtils.setCurrent(getContext());
//
//                return true;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//
////                viewHolder.itemView.animate().translationX(0f).setDuration(400);
//
//                if (currentQueue.indexOf(currentTrack) == viewHolder.getAdapterPosition()) {
//                    MbnController.next(getContext());
//                }
//
//                currentQueue.remove(viewHolder.getAdapterPosition());
//                currentQueueList.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
//                MbnController.makeUnshuffledList();
//                StorageUtils.setCurrent(getContext());
//
//            }
//        };
//
//
//        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleCallback);
//
//        touchHelper.attachToRecyclerView(currentQueueList);
//
//
//    }
//
//    private void orangeSeekBar(final View view) {
//
//        seekBar = view.findViewById(R.id.player_frag_seekbar);
//
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
//
//                seekPosition = value;
//
//
//                time.setText(MbnController.timeConverter(value, durationThis));
//
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//                transportControls.seekTo(seekPosition);
//
//
//            }
//        });
//
////        seekBar.setOnChangeListener(new MbnSeekBarHelper() {
////            @Override
////            public void onChange(float value) {
////
////
////                seekPosition = (int) value;
////
////
////                time.setText(MbnController.timeConverter(value, durationThis));
////
////
////            }
////
////            @Override
////            public void onTouchStart(float value) {
////
//////                playerState = false;
////
////
//////                MbnController.pause(context);
////
////            }
////
////            @Override
////            public void onTouchEnd(float value) {
////
//////                MbnController.play(context);
////                transportControls.seekTo(seekPosition);
////
////            }
////        });
//
//    }
//
//    private void everyTick(String formattedTime, int position) {
//
//        this.formattedTime = formattedTime;
//
//
//        if (seekBar.getMax() != durationThis) {
//            seekBar.setMax(durationThis);
//        }
//
//        seekBar.setProgress(position);
//        time.setText(formattedTime);
//
//
//    }
//
//    @SuppressLint("StaticFieldLeak")
//    private void onChange() throws Exception {
//
//
////        Log.i("MBN", "onChange   " + playerState);
//
//        play_Button.setMode(playerState);
//        top_play_Button.setMode(playerState);
//
////        visualizerView.mode(playerState);
//
////        if (playerState) {
////
////            play_Button.setImageBitmap(pause);
////
////        } else {
////            play_Button.setImageBitmap(play);
////
////        }
//
//
//        if (StorageUtils.isShuffle(context)) {
//
//            shuffle_Button.setImageTintList(ColorStateList.valueOf(activeColor));
//        } else {
//
//            shuffle_Button.setImageTintList(ColorStateList.valueOf(Color.BLACK));
//        }
//        if (StorageUtils.isRepeat(context)) {
//
//            repeat_Button.setImageTintList(ColorStateList.valueOf(activeColor));
//        } else {
//
//            repeat_Button.setImageTintList(ColorStateList.valueOf(Color.BLACK));
//        }
//
////        try {
////            if (ListMaker.getPlaylist("Favorite").contains(currentTrack.getPath())) {
////
////
////                like_Button.setImageBitmap(likeFull);
////
////            } else {
////
////                like_Button.setImageBitmap(likeEmpty);
////
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//
//        if (!trackCode.equals(currentTrack.getPath())) {
//
//            durationThis = currentTrack.getDuration();
//
//            time.setText(MbnController.timeConverter(0, durationThis));
//
//            trackCode = "";
//            trackCode = trackCode.concat(currentTrack.getPath());
//
////            backGround.animate().alpha(0f).setDuration(100).start();
//
//            new AlbumArtTaskForSongs(getContext(), currentTrack.getPath(), (int) currentTrack.getId()) {
//                @Override
//                public void onFinish(Bitmap bitmap, String data) {
//
//                    cover = bitmap;
//
//                    try {
//                        albumArtImage.setImageBitmap(MbnUtils.roundedBitmap(cover));
//                        Palette.from(cover).generate(new Palette.PaletteAsyncListener() {
//                            @Override
//                            public void onGenerated(Palette palette) {
//
//                                seekBar.setColor(palette.getLightVibrantColor(Color.CYAN));
//                                visualizerNewDesign.setColor(palette.getLightVibrantColor(Color.CYAN));
//
//
//                            }
//                        });
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//
//                }
//            };
//
//            bitrateString = MbnController.makeBitRate(currentTrack.getBitRate());
//            bitrate.setText(bitrateString);
//
////            new AsyncTask<Void, Void, Void>() {
////                @Override
////                protected Void doInBackground(Void... voids) {
////
////
////                    cover = MbnController.getCover(getContext(), currentTrack.getPath(), 1);
////
////                    bitrateString = MbnController.makeBitRate(MbnController.getBitrate(currentTrack.getPath()));
////
////                    return null;
////                }
////
////                @Override
////                protected void onPostExecute(Void aVoid) {
////                    super.onPostExecute(aVoid);
//////                    seekBar.setCenterImg(cover);
////                    bitrate.setText(bitrateString);
////
////                    try {
////                        albumArtImage.setImageBitmap(MbnUtils.roundedBitmap(cover));
////                        Palette.from(cover).generate(new Palette.PaletteAsyncListener() {
////                            @Override
////                            public void onGenerated(Palette palette) {
////
////                                seekBar.setColor(palette.getLightVibrantColor(Color.CYAN));
////
////
////                            }
////                        });
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                    }
////
////
////                }
////
////            }.execute();
//
//
//            title.setText(currentTrack.getTitle());
//            artist.setText(currentTrack.getArtistTitle());
////            album.setText(currentTrack.getAlbumTitle());
//            numberInQU.setText((unShuffledQueue.indexOf(currentTrack) + 1) + " of " + currentQueue.size());
//
//
////            seekBar.setColor(colorToBe);
//
//
////            Color.
//
////            play_Button.setColor(colorToBe);
//
////            System.out.println("passed COLOR !!!!!!!!!!");
//
////            title.setTextColor(colorToBe);
//
////            Palette.from(cover).generate(new Palette.PaletteAsyncListener() {
////                @Override
////                public void onGenerated(Palette palette) {
////
////                    int colorToBe = MbnBitmapBucket.makeColorBright(palette.getDominantColor(activeColor));
////
////                    seekBar.setColor(colorToBe);
////
////                    play_Button.setColor(colorToBe);
////
////                    title.setTextColor(colorToBe);
////
////
////                }
////            });
//
////            mbnBlur.makeBlur(cover);
//        }
//
//
//    }
//
//    private void makeSwipeViewPager(View view) {
//
//        swipeViewPager = view.findViewById(R.id.swipe_view_pager);
//
//        swipeAdapter = new SwipeAdapter(getFragmentManager());
//        swipeViewPager.setOffscreenPageLimit(0);
//
//
//        swipeViewPager.setAdapter(swipeAdapter);
//
//        swipeViewPager.setCurrentItem(currentTrackNumberInQueue);
//
//        swipeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//
//                MbnController.change(context, currentQueue, currentQueue.get(position));
//
////                swipeAdapter.notifyDataSetChanged();
//
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//
//
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//
////        backGround = view.findViewById(R.id.player_fragment_image);
//
//
//        orangeTexts(view);
//        orangeButtons(view);
//        orangeSeekBar(view);
//        makeSwipeViewPager(view);
////        mbnBlur.setHelper(new AdvancedMbnBlur.MbnAdvancedBlurHelper() {
////            @Override
////            public void onFinish(Bitmap blurred) {
////
////
////                backGround.setImageBitmap(blurred);
////
////                backGround.animate().alpha(1f).setDuration(800).start();
////            }
////        });
////        bindService();
//
//
//        try {
//            onChange();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
////        new ImageLoader().execute();
//        try {
//            onChange();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////        reg();
//
//
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
////        reg();
//        playerFragmentIsOn = true;
//        try {
//            onChange();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        timeHandler.postDelayed(timeRunnable, 500);
//
//    }
//
//    Handler timeHandler = new Handler();
//
//    Runnable timeRunnable = new Runnable() {
//        @Override
//        public void run() {
//
//
//            if (playerState) {
//
//                mediaController.sendCommand("getTime", null, resultReceiver);
//
//            }
//
//
//            timeHandler.postDelayed(this, 1000);
//
//        }
//    };
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        currentQueueList.scrollToPosition(currentTrackNumberInQueue);
//        reg();
//        playerFragmentIsOn = true;
//        try {
//            onChange();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//
////        Log.i("Pause", "yeah it did");
//
//        unReg();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        playerFragmentIsOn = false;
//        timeHandler.removeCallbacks(timeRunnable);
////        unReg();
//
//    }
//
//
//    private int currentPosition;
//
//    BroadcastReceiver tickReceive = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            durationThis = intent.getIntExtra("duration", 0);
//
//            currentPosition = intent.getIntExtra("seek", 0);
//
//            everyTick(MbnController.timeConverter(currentPosition, durationThis), currentPosition);
//
//        }
//    };
//
//    BroadcastReceiver fragmentReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//
//            try {
//                onChange();
//                Log.i("InRec", intent.getAction());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            adapter.update();
//            swipeAdapter.notifyDataSetChanged();
//            swipeViewPager.setCurrentItem(currentTrackNumberInQueue, true);
//            currentQueueList.scrollToPosition(currentTrackNumberInQueue);
//
//
//        }
//    };
//
//    private void reg() {
//
//        IntentFilter filter = new IntentFilter(To_FRAGMENT);
//        IntentFilter filter2 = new IntentFilter(ON_TICK);
//
//        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
//
//        localBroadcastManager.registerReceiver(fragmentReceiver, filter);
//        localBroadcastManager.registerReceiver(tickReceive, filter2);
//
//
//    }
//
//    private void unReg() {
//
//        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
//
//        localBroadcastManager.unregisterReceiver(fragmentReceiver);
//        localBroadcastManager.unregisterReceiver(tickReceive);
//
//    }
//
//
////    Handler handler = new Handler(new Handler.Callback() {
////        @Override
////        public boolean handleMessage(Message message) {
////
////            return false;
////        }
////    });
//
//    Handler handlerForResultReceiver = new Handler(Looper.getMainLooper());
//
//    MyReceiver resultReceiver = new MyReceiver(handlerForResultReceiver);
//
//
//    class MyReceiver extends ResultReceiver {
//
//        public MyReceiver(Handler handler) {
//            super(handler);
//        }
//
//
//        @Override
//        protected void onReceiveResult(int resultCode, Bundle resultData) {
//            super.onReceiveResult(resultCode, resultData);
//
//
////            resultData.getInt("seekPosition");
//
//            everyTick(MbnController.timeConverter(seekPosition, currentTrack.getDuration()), seekPosition);
//
//
////            System.out.println(resultCode);
////
////            System.out.println(Thread.currentThread().getName());
//
//        }
//    }
//
//    @Override
//    public void onClick(View view) {
//
//
//        int id = view.getId();
//
//
//        switch (id) {
//            case R.id.player_frag_skip_next:
////                bindService();
//                MbnController.next(this.getContext());
//
////                MediaSessionHolder.getControls(getContext()).skipToNext();
////                transportControls.skipToNext();
//
//
////                mediaController.sendCommand("mbn", null, resultReceiver);
//
//
////                System.out.println("heeerrrrr before");
////                System.out.println(Thread.currentThread().getName());
//
//
//                break;
//            case R.id.player_frag_play_pause:
//                if (playerState) {
//                    MbnController.pause(this.getContext());
////                    transportControls.pause();
////                    MediaSessionHolder.getControls(getContext()).pause();
//
//                } else {
//                    MbnController.play(this.getContext());
////                    transportControls.play();
////                    MediaSessionHolder.getControls(getContext()).play();
//
//                }
//                break;
//            case R.id.player_frag_top_play_button:
//                if (playerState) {
//                    MbnController.pause(this.getContext());
////                    transportControls.pause();
////                    MediaSessionHolder.getControls(getContext()).pause();
//
//                } else {
//                    MbnController.play(this.getContext());
////                    transportControls.play();
////                    MediaSessionHolder.getControls(getContext()).play();
//
//                }
//                break;
//            case R.id.player_frag_pervious:
//                MbnController.previous(this.getContext());
////                transportControls.skipToPrevious();
////                MediaSessionHolder.getControls(getContext()).skipToPrevious();
//
//                break;
//            case R.id.frag_like_button:
////                ArrayList<String> a = new ArrayList<>();
////                a.add(currentTrack.getPath());
////                if (ListMaker.getPlaylist("Favorite").contains(currentTrack.getPath())) {
////                    ListMaker.removeFromPlaylist(context, "Favorite", a);
////                } else {
////                    ListMaker.addToPlayList(context, "Favorite", a);
////                }
////                a.clear();
//                try {
//                    onChange();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            case R.id.player_frag_repeat:
//                if (StorageUtils.isRepeat(context)) {
//
//                    StorageUtils.setRepeat(context, false);
//
//                } else {
//                    StorageUtils.setRepeat(context, true);
//                }
//
//                try {
//                    onChange();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            case R.id.player_farg_shuffle:
//                if (StorageUtils.isShuffle(context)) {
//
//                    StorageUtils.setShuffle(context, false);
//                    MbnController.unShuffle(getContext());
//
//                } else {
//                    StorageUtils.setShuffle(context, true);
//                    MbnController.makeShuffle(getContext());
//                }
////                onChange();
//
//                break;
//
//            case R.id.frag_showQueue_button:
//                CurrentQueueFragment_bottomFragment.newInstance().show(getFragmentManager(), "queue");
//                break;
//        }
//
//
//    }
//
//
//    @Override
//    public boolean onLongClick(View v) {
//        holdHandler.removeCallbacks(holdRunnable_forward);
//        holdHandler.removeCallbacks(holdRunnable_rewind);
//
//        if (v.getId() == R.id.player_frag_skip_next) {
//            holdHandler.post(holdRunnable_forward);
//        } else {
//            holdHandler.post(holdRunnable_rewind);
//        }
//
//        return true;
//    }
//
//    View.OnTouchListener touchListener = new View.OnTouchListener() {
//        @SuppressLint("ClickableViewAccessibility")
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//
//            if (event.getActionMasked() == MotionEvent.ACTION_UP || event.getActionMasked() == MotionEvent.ACTION_CANCEL) {
//                holdHandler.removeCallbacks(holdRunnable_forward);
//                holdHandler.removeCallbacks(holdRunnable_rewind);
//            }
//
//            return false;
//        }
//    };
//
//    Handler holdHandler = new Handler();
//
//    Runnable holdRunnable_forward = new Runnable() {
//        @Override
//        public void run() {
//            transportControls.fastForward();
//            mediaController.sendCommand("getTime", null, resultReceiver);
//            holdHandler.postDelayed(this, 300);
//
//        }
//    };
//    Runnable holdRunnable_rewind = new Runnable() {
//        @Override
//        public void run() {
//            transportControls.rewind();
//            mediaController.sendCommand("getTime", null, resultReceiver);
//            holdHandler.postDelayed(this, 300);
//        }
//    };
//
//
//    private class ImageLoader extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//
////            likeEmpty = BitmapFactory.decodeResource(getResources(), R.drawable.like_empty_png);
////            likeFull = BitmapFactory.decodeResource(getResources(), R.drawable.like_png);
////            play = BitmapFactory.decodeResource(getResources(), R.drawable.play_button_png);
////            pause = BitmapFactory.decodeResource(getResources(), R.drawable.pause_png);
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
//            try {
//                onChange();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            reg();
//
//        }
//    }
//}
