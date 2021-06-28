//package com.br.mreza.musicplayer;
//
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.res.ColorStateList;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.design.widget.BottomSheetDialogFragment;
//import android.support.v7.graphics.Palette;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.br.mreza.musicplayer.MBN.bitmapUtils.AdvancedMbnBlur;
//import com.br.mreza.musicplayer.MBN.bitmapUtils.MbnBitmapBucket;
//import com.br.mreza.musicplayer.MBN.customViews.MbnCircularSeekBar;
//import com.br.mreza.musicplayer.MBN.customViews.MbnPlayButton;
//import com.br.mreza.musicplayer.MBN.customViews.MbnSeekBarHelper;
//
//import java.util.ArrayList;
//
//import static com.br.mreza.musicplayer.ListMaker.currentQueue;
//import static com.br.mreza.musicplayer.ListMaker.currentTrack;
//import static com.br.mreza.musicplayer.ListMaker.currentTrackCode;
//import static com.br.mreza.musicplayer.ListMaker.currentTrackNumberInQueue;
//import static com.br.mreza.musicplayer.MbnController.ON_TICK;
//import static com.br.mreza.musicplayer.MbnController.To_FRAGMENT;
//import static com.br.mreza.musicplayer.MbnController.playerFragmentIsOn;
//import static com.br.mreza.musicplayer.MbnController.playerState;
//import static com.br.mreza.musicplayer.MbnController.seekPosition;
//
//public class PlayerFragment_backup extends BottomSheetDialogFragment implements View.OnClickListener {
//
//    TextView time;
//    TextView title;
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
//    int activeColor = Color.parseColor("#9bfff3");
//
//    ImageView backGround;
//
//    AdvancedMbnBlur mbnBlur = new AdvancedMbnBlur();
//
//    ImageButton previous_Button;
//    MbnPlayButton play_Button;
//    ImageButton next_Button;
//    ImageButton repeat_Button;
//    ImageView like_Button;
//    ImageButton shuffle_Button;
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
//
//
//    public static PlayerFragment_backup newInstance() {
//
//
//        return new PlayerFragment_backup();
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        context = inflater.getContext();
//
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
//
//    }
//
//    private void orangeSeekBar(final View view) {
//        seekBar = view.findViewById(R.id.fragment_seekBar);
//
//
//        seekBar.setOnChangeListener(new MbnSeekBarHelper() {
//            @Override
//            public void onChange(float value) {
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
//                MbnController.pause(context);
//
//            }
//
//            @Override
//            public void onTouchEnd(float value) {
//
//                MbnController.play(context);
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
//    private void onChange() {
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
//        if (ListMaker.getPlaylist("Favorite").contains(currentTrackCode)) {
//
//
//            like_Button.setImageBitmap(likeFull);
//
//        } else {
//
//            like_Button.setImageBitmap(likeEmpty);
//
//        }
//
//        if (!trackCode.equals(currentTrackCode)) {
//
//            durationThis = currentTrack.getDuration();
//
//            time.setText(MbnController.timeConverter(0, durationThis));
//
//            trackCode = "";
//            trackCode = trackCode.concat(currentTrackCode);
//
//            backGround.animate().alpha(0f).setDuration(100).start();
//
//            Bitmap cover = null;
//
//            try {
//                cover = currentTrack.getCover(getContext());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            if (cover != null) {
//                seekBar.setCenterImg(cover);
//            } else {
//                seekBar.setCenterImg(BitmapFactory.decodeResource(getResources(), R.drawable.night_rain_1));
//            }
//            title.setText(currentTrack.getTitle());
//            artist.setText(currentTrack.getArtistTitle());
//            album.setText(currentTrack.getAlbumTitle());
//            numberInQU.setText((currentTrackNumberInQueue + 1) + " of " + currentQueue.size());
//            bitrate.setText(MbnController.makeBitRate(currentTrack.getBitRate()));
//
//            assert cover != null;
//            Palette.from(cover).generate(new Palette.PaletteAsyncListener() {
//                @Override
//                public void onGenerated(Palette palette) {
//
//                    int colorToBe = MbnBitmapBucket.makeColorBright(palette.getDominantColor(activeColor));
//
//                    seekBar.setColor(colorToBe);
//
//                    play_Button.setColor(colorToBe);
//
//                    title.setTextColor(colorToBe);
//
//
//                }
//            });
//
//            mbnBlur.makeBlur(cover);
//        }
//
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//
//        backGround = view.findViewById(R.id.player_fragment_image);
//
//
//        orangeTexts(view);
//        orangeButtons(view);
//        orangeSeekBar(view);
//        mbnBlur.setHelper(new AdvancedMbnBlur.MbnAdvancedBlurHelper() {
//            @Override
//            public void onFinish(Bitmap blurred) {
//
//
//                backGround.setImageBitmap(blurred);
//
//                backGround.animate().alpha(1f).setDuration(800).start();
//            }
//        });
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
//
//
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        playerFragmentIsOn = true;
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        playerFragmentIsOn = false;
//    }
//
//
//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        super.onDismiss(dialog);
//        unReg();
//
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
//            onChange();
//
//        }
//    };
//
//    private void reg() {
//
//        IntentFilter filter = new IntentFilter(To_FRAGMENT);
//        IntentFilter filter2 = new IntentFilter(ON_TICK);
//
//        context.registerReceiver(fragmentReceiver, filter);
//        context.registerReceiver(tickReceive, filter2);
//
//    }
//
//    private void unReg() {
//
//        context.unregisterReceiver(fragmentReceiver);
//        context.unregisterReceiver(tickReceive);
//
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
//                break;
//            case R.id.frag_play_button:
//                if (playerState) {
//                    MbnController.pause(this.getContext());
//                } else {
//                    MbnController.play(this.getContext());
//                }
//                break;
//            case R.id.frag_previous_button:
//                MbnController.previous(this.getContext());
//                break;
//            case R.id.frag_like_button:
//                ArrayList<String> a = new ArrayList<>();
//                a.add(currentTrackCode);
//                if (ListMaker.getPlaylist("Favorite").contains(currentTrackCode)) {
//                    ListMaker.removeFromPlaylist(context, "Favorite", a);
//                } else {
//                    ListMaker.addToPlayList(context, "Favorite", a);
//                }
//                a.clear();
//                onChange();
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
//                onChange();
//                break;
//            case R.id.frag_shuffle_button:
//                if (StorageUtils.isShuffle(context)) {
//
//                    StorageUtils.setShuffle(context, false);
//
//                } else {
//                    StorageUtils.setShuffle(context, true);
//                }
//
//
//                onChange();
//
//                break;
//
//
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
//            onChange();
//            reg();
//
//        }
//    }
//}
