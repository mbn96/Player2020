package com.br.mreza.musicplayer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.mreza.musicplayer.MBN.bitmapUtils.AdvancedMbnBlur;
import com.br.mreza.musicplayer.MBN.bitmapUtils.MbnBitmapBucket;
import com.br.mreza.musicplayer.MBN.customViews.MbnCircularSeekBar;
import com.br.mreza.musicplayer.MBN.customViews.MbnPlayButton;
import com.br.mreza.musicplayer.MBN.customViews.MbnScrollingTextView;
import com.br.mreza.musicplayer.MBN.customViews.MbnSeekBarHelper;

import java.io.IOException;
import java.util.Random;


public class FromChooserPlayer extends AppCompatActivity implements View.OnClickListener {


    Song chooserSong;

    ImageView backGround;

    int chooserSeekPosition;
    boolean chooserPlayerState = false;


    ConstraintLayout main;

    TextView time;
    MbnScrollingTextView titleText;
    TextView albumText;
    TextView artist;
    TextView bitrate;

    MbnPlayButton play_Button;

    int durationMain;

    MbnCircularSeekBar seekBar;
    private MediaPlayer player;
    private Handler handler;
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_layout_for_chooser_activity);

        main = (ConstraintLayout) findViewById(R.id.plater_layout_rootView);

        getWindow().setStatusBarColor(Color.argb(0, 0, 0, 0));


        main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        Intent intent = getIntent();

        Uri uri = intent.getData();

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.getPath());
        String titleName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String albumName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        String artistName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        int duration = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

        chooserSong = new Song(titleName, albumName, uri.getPath(), artistName, duration, 54);

        retriever.release();

        player = new MediaPlayer();

        try {
            player.setDataSource(chooserSong.getPath());
            player.prepare();
            player.start();
            chooserPlayerState = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        durationMain = player.getDuration();

        findUi();

        timeHandler();


    }

    private void timeHandler() {


        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {


                if (chooserPlayerState) {

                    try {
                        chooserSeekPosition = player.getCurrentPosition();

                        System.out.println("ruuuuuun");

                        tick();

                    } catch (Exception e) {
                        handler.removeCallbacks(this, 1000);
                        e.printStackTrace();
                    }

                }
                handler.postDelayed(this, 1000);


            }
        };

        handler.postDelayed(runnable, 10);


    }

    private void tick() {

//        System.out.println("tick " + chooserSeekPosition + "  " + durationMain);

        time.setText(MbnController.timeConverter(chooserSeekPosition, durationMain));

        seekBar.setCurrentValue(chooserSeekPosition);


    }

    private void findUi() {

        time = (TextView) findViewById(R.id.fragment_time_text);
        titleText = (MbnScrollingTextView) findViewById(R.id.frag_title_text);
        albumText = (TextView) findViewById(R.id.frag_album_text);
        artist = (TextView) findViewById(R.id.frag_artist_text);
        bitrate = (TextView) findViewById(R.id.fragment_player_bitrate);

        titleText.setText(chooserSong.getTitle());
        albumText.setText(chooserSong.getAlbumTitle());
        artist.setText(chooserSong.getArtist());
        bitrate.setText(MbnController.makeBitRate(chooserSong.getBitRate()));


        play_Button = (MbnPlayButton) findViewById(R.id.frag_play_button);
        play_Button.setOnClickListener(this);

        play_Button.setMode(true);

        backGround = (ImageView) findViewById(R.id.player_fragment_image);


        seekBar = (MbnCircularSeekBar) findViewById(R.id.fragment_seekBar);

        seekBar.setCenterImg(chooserSong.getCover());


        seekBar.setOnChangeListener(new MbnSeekBarHelper() {
            @Override
            public void onChange(float value) {


                chooserSeekPosition = (int) value;


                time.setText(MbnController.timeConverter(value, chooserSong.getDuration()));


            }

            @Override
            public void onTouchStart(float value) {

                chooserPlayerState = false;


//                MbnController.pause(context);

            }

            @Override
            public void onTouchEnd(float value) {

                chooserPlayerState = true;

                player.seekTo((int) value);


            }
        });

        seekBar.setMaxValue(durationMain);

//        decorate();


    }

    private void decorate() {

        AdvancedMbnBlur mbnBlur = new AdvancedMbnBlur();

        mbnBlur.setHelper(new AdvancedMbnBlur.MbnAdvancedBlurHelper() {
            @Override
            public void onFinish(Bitmap blurred) {

                backGround.setImageBitmap(blurred);

            }
        });

        mbnBlur.makeBlur(getApplicationContext(), chooserSong.getPath());

        int color = MbnBitmapBucket.makeColorBright(Palette.from(chooserSong.getCover()).generate().getDominantColor(Color.CYAN));

        titleText.setTextColor(color);
        seekBar.setColor(color);
        play_Button.setColor(color);


    }

    @Override
    protected void onStart() {
        super.onStart();
        decorate();
    }

    @Override
    public void onClick(View view) {

        if (chooserPlayerState) {

            player.pause();

            play_Button.setMode(false);

            chooserPlayerState = false;


        } else {

            player.start();
            play_Button.setMode(true);
            chooserPlayerState = true;
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
        player.reset();
        player.release();
        handler.removeCallbacks(runnable);
//        player.reset();
    }

    private class Song {

//        private class InformationCatcher extends AsyncTask<Void, Void, Void> {
//            @Override
//            protected Void doInBackground(Void... voids) {
//
//                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                retriever.setDataSource(path);
//                duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
//                retriever.release();
//
//                return null;
//            }
//        }

        public Song(String title, String albumTitle, String path, String artist, int duration, long dateAdded) {
            this.title = title;
            this.albumTitle = albumTitle;
            this.path = path;
            this.hashCode = title + albumTitle;
            this.artist = artist;
            this.duration = duration;
            this.dateAdded = dateAdded;

//            new InformationCatcher().execute();


        }

        private long dateAdded;

        @Override
        public String toString() {


            return title;
        }

        public String getHashCode() {
            return hashCode;
        }

        private String title;

        public int getDuration() {
            return duration;
        }

        private int duration;

        private String bitRate;

        private String hashCode;

        private String artist;

        private String albumTitle;
        private String path;


        private Bitmap cover;

        private void catchBitrate() {

            try {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(path);
                bitRate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
                retriever.release();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                bitRate = "0";
            }
        }

        public String getBitRate() {

            if (bitRate != null) {
                return bitRate;
            }
            catchBitrate();

            return bitRate;
        }

//        MbnSong(String song) {
////            retriever.setDataSource(Uri.parse(song).getPath());
////
////            cover = retriever.getEmbeddedPicture();
////
////            title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
////            albumTitle = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
//
//            if (title == null) {
//
//
//                title = "Unknown";
//
////                title = song.getName().substring(0, song.getName().length() - 4);
//            }
//            if (albumTitle == null) {
//
//                albumTitle = "Album Unknown";
//            }
//
//
//            this.path = Uri.parse(song).getPath();
//
////            if (cover == null) {
////                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.night_rain_1);
////
////                cover = bitmap.
////
////            }
//
////            System.out.println(title);
//        }

        public String getAlbumTitle() {
            return albumTitle;
        }

        String getTitle() {
            return title;
        }

        public String getArtist() {
            return artist;
        }

        private void makeCover() {


            try {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(path);
                byte[] coverArray = retriever.getEmbeddedPicture();
                retriever.release();
                cover = BitmapFactory.decodeByteArray(coverArray, 0, coverArray.length);
            } catch (Exception e) {

                Random random = new Random();

                int rand = random.nextInt(5);
                if (rand == 0) {
                    cover = BitmapFactory.decodeResource(getResources(), R.drawable.music_love5_ppcorn);
                } else if (rand == 1) {
                    cover = BitmapFactory.decodeResource(getResources(), R.drawable.night_rain_1);
                } else if (rand == 2) {
                    cover = BitmapFactory.decodeResource(getResources(), R.drawable.night_rain_2);
                } else if (rand == 3) {
                    cover = BitmapFactory.decodeResource(getResources(), R.drawable.night_rain_4);
                } else {
                    cover = BitmapFactory.decodeResource(getResources(), R.drawable.day_fog);
                }
            }


        }

        public Bitmap getCover() {

            if (cover != null) {

                return cover;
            }
            makeCover();

            return cover;
        }

        String getPath() {
            return path;
        }

    }


}
