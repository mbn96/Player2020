//package com.br.mreza.musicplayer;
//
//
//import android.annotation.SuppressLint;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.res.ColorStateList;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.ResultReceiver;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.LocalBroadcastManager;
//import android.support.v4.media.session.MediaControllerCompat;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.br.mreza.musicplayer.MBN.customViews.MbnCircularSeekBar;
//import com.br.mreza.musicplayer.MBN.customViews.MbnPlayButton;
//import com.br.mreza.musicplayer.MBN.customViews.MbnScrollingTextView;
//import com.br.mreza.musicplayer.MBN.customViews.MbnSeekBarHelper;
//
//import static com.br.mreza.musicplayer.ListMaker.currentQueue;
//import static com.br.mreza.musicplayer.ListMaker.getCurrentTrack;
//import static com.br.mreza.musicplayer.MbnController.ON_TICK;
//import static com.br.mreza.musicplayer.MbnController.To_FRAGMENT;
//import static com.br.mreza.musicplayer.MbnController.colorToBe;
//import static com.br.mreza.musicplayer.MbnController.playerFragmentIsOn;
//import static com.br.mreza.musicplayer.MbnController.playerState;
//import static com.br.mreza.musicplayer.MbnController.seekPosition;
//import static com.br.mreza.musicplayer.MbnController.unShuffledQueue;
//
//public class PlayerFragment extends Fragment implements View.OnClickListener {
//
//    TextView time;
//    MbnScrollingTextView title;
//    TextView album;
//    TextView artist;
//    TextView bitrate;
//    TextView numberInQU;
//
//    Bitmap likeEmpty;
//    Bitmap likeFull;
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
//    MbnPlayButton play_Button;
//    ImageButton next_Button;
//    ImageButton repeat_Button;
//    ImageView like_Button;
//    ImageButton shuffle_Button;
//    ImageButton showQueueButton;
//
//
//    MbnCircularSeekBar seekBar;
//
//    int durationThis;
//    String formattedTime = "";
//
//    String trackCode = "";
//
//
//    Context context;
//    private LocalBroadcastManager localBroadcastManager;
//    private Bitmap cover;
//
//
//    public static PlayerFragment newInstance() {
//
//
//        return new PlayerFragment();
//    }
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        context = inflater.getContext();
//
//
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
//        return inflater.inflate(R.layout.player_fragment_layout, container, false);
//    }
//
//
//    private void orangeTexts(View view) {
//
//        time = view.findViewById(R.id.fragment_time_text);
//        title = view.findViewById(R.id.frag_title_text);
//        album = view.findViewById(R.id.frag_album_text);
//        artist = view.findViewById(R.id.frag_artist_text);
//        bitrate = view.findViewById(R.id.fragment_player_bitrate);
//        numberInQU = view.findViewById(R.id.fragment_player_number_in_qu);
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
//        title.setHelper(new MbnScrollingTextView.TextViewSwipeHelper() {
//            @Override
//            public void onSwipe(boolean direction) {
//
//                if (direction) {
//                    MbnController.previous(getContext());
//                } else {
//                    MbnController.next(getContext());
//                }
//
//            }
//        });
//
//
//    }
//
//    private void orangeButtons(View view) {
//
//        previous_Button = view.findViewById(R.id.frag_previous_button);
//        previous_Button.setOnClickListener(this);
//        play_Button = view.findViewById(R.id.frag_play_button);
//        play_Button.setOnClickListener(this);
//        next_Button = view.findViewById(R.id.frag_next_button);
//        next_Button.setOnClickListener(this);
//        repeat_Button = view.findViewById(R.id.frag_repeat_button);
//        repeat_Button.setOnClickListener(this);
//        like_Button = view.findViewById(R.id.frag_like_button);
//        like_Button.setOnClickListener(this);
//        shuffle_Button = view.findViewById(R.id.frag_shuffle_button);
//        shuffle_Button.setOnClickListener(this);
//        showQueueButton = view.findViewById(R.id.frag_showQueue_button);
//        showQueueButton.setOnClickListener(this);
//
//    }
//
//    private void orangeSeekBar(final View view) {
//
//        seekBar = view.findViewById(R.id.fragment_seekBar);
//
//
//        seekBar.setOnChangeListener(new MbnSeekBarHelper() {
//            @Override
//            public void onChange(float value) {
//
//
//                seekPosition = (int) value;
//
//
//                time.setText(MbnController.timeConverter(value, durationThis));
//
//
//            }
//
//            @Override
//            public void onTouchStart(float value) {
//
////                playerState = false;
//
//
////                MbnController.pause(context);
//
//            }
//
//            @Override
//            public void onTouchEnd(float value) {
//
////                MbnController.play(context);
//                transportControls.seekTo(seekPosition);
//
//            }
//        });
//
//    }
//
//    private void everyTick(String formattedTime, int position) {
//
//        this.formattedTime = formattedTime;
//
//
//        if (seekBar.getMaxValue() != durationThis) {
//            seekBar.setMaxValue(durationThis);
//        }
//
//        seekBar.setCurrentValue(position);
//        time.setText(formattedTime);
//
//
//    }
//
//    @SuppressLint("StaticFieldLeak")
//    private void onChange() throws Exception {
//
//
//        play_Button.setMode(playerState);
//
////        if (playerState) {
////
////            play_Button.setImageBitmap(pause);
////
////        } else {
////            play_Button.setImageBitmap(play);
////
////        }
//        if (StorageUtils.isShuffle(context)) {
//
//            shuffle_Button.setImageTintList(ColorStateList.valueOf(activeColor));
//        } else {
//
//            shuffle_Button.setImageTintList(ColorStateList.valueOf(Color.GRAY));
//        }
//        if (StorageUtils.isRepeat(context)) {
//
//            repeat_Button.setImageTintList(ColorStateList.valueOf(activeColor));
//        } else {
//
//            repeat_Button.setImageTintList(ColorStateList.valueOf(Color.GRAY));
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
//        if (!trackCode.equals(getCurrentTrack().getPath())) {
//
//            durationThis = getCurrentTrack().getDuration();
//
//            time.setText(MbnController.timeConverter(0, durationThis));
//
//            trackCode = "";
//            trackCode = trackCode.concat(getCurrentTrack().getPath());
//
////            backGround.animate().alpha(0f).setDuration(100).start();
//
//            new AsyncTask<Void, Void, Void>() {
//                @Override
//                protected Void doInBackground(Void... voids) {
//
//
//                    cover = MbnController.getCover(getContext(), getCurrentTrack().getPath(), 1);
//
//                    bitrateString = MbnController.makeBitRate(MbnController.getBitrate(getCurrentTrack().getPath()));
//
//                    return null;
//                }
//
//                @Override
//                protected void onPostExecute(Void aVoid) {
//                    super.onPostExecute(aVoid);
//                    seekBar.setCenterImg(cover);
//                    bitrate.setText(bitrateString);
//
//
//                }
//
//            }.execute();
//
//
//            title.setText(getCurrentTrack().getTitle());
//            artist.setText(getCurrentTrack().getArtistTitle());
//            album.setText(getCurrentTrack().getAlbumTitle());
//            numberInQU.setText((unShuffledQueue.indexOf(getCurrentTrack()) + 1) + " of " + currentQueue.size());
//
//
//            seekBar.setColor(colorToBe);
//
//            play_Button.setColor(colorToBe);
//
////            System.out.println("passed COLOR !!!!!!!!!!");
//
//            title.setTextColor(colorToBe);
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
//        new ImageLoader().execute();
//        try {
//            onChange();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        reg();
//
//
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        reg();
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
//    @Override
//    public void onResume() {
//        super.onResume();
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
//    public void onStop() {
//        super.onStop();
//        playerFragmentIsOn = false;
//        unReg();
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
//            try {
//                onChange();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//    };
//
//    private void reg() {
//
//        IntentFilter filter = new IntentFilter(To_FRAGMENT);
//        IntentFilter filter2 = new IntentFilter(ON_TICK);
//
//        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
//
//        localBroadcastManager.registerReceiver(fragmentReceiver, filter);
//        localBroadcastManager.registerReceiver(tickReceive, filter2);
//
//    }
//
//    private void unReg() {
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
//    MyReceiver resultReceiver = new MyReceiver(null);
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
//            everyTick(MbnController.timeConverter(seekPosition, getCurrentTrack().getDuration()), seekPosition);
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
//            case R.id.frag_next_button:
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
//            case R.id.frag_play_button:
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
//            case R.id.frag_previous_button:
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
//            case R.id.frag_repeat_button:
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
//            case R.id.frag_shuffle_button:
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
////                CurrentQueueFragment_bottomFragment.newInstance().show(getFragmentManager(), "queue");
//                break;
//        }
//
//
//    }
//
//    private class ImageLoader extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//
//            likeEmpty = BitmapFactory.decodeResource(getResources(), R.drawable.like_empty_png);
//            likeFull = BitmapFactory.decodeResource(getResources(), R.drawable.like_png);
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
