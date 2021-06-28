//package com.br.mreza.musicplayer;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.br.mreza.musicplayer.MBN.bitmapUtils.MbnBitmapBucket;
//import com.br.mreza.musicplayer.MBN.customViews.MbnCircularSeekBar;
//import com.br.mreza.musicplayer.MBN.customViews.MbnSeekBarHelper;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//import static com.br.mreza.musicplayer.ListMaker.allSongsMap;
////import static com.br.mreza.musicplayer.ListMaker.current;
//import static com.br.mreza.musicplayer.ListMaker.currentQueue;
//
//public class PlayerActivity extends AppCompatActivity {
//
//
//    private class SyncJob extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            seekPOS = PlayerService.seekPosition;
//
//            seekPosSeconds = seekPOS / 1000;
//
//            seekTmeStr = seekPosSeconds / 60 + ":" + seekPosSeconds % 60;
//            String[] a = seekTmeStr.split(":");
//            if (a[0].length() < 2) {
//
//                a[0] = "0" + a[0];
//
//            }
//            if (a[1].length() < 2) {
//
//                a[1] = "0" + a[1];
//
//            }
//            seekTmeStr = a[0] + ":" + a[1];
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
//            tv.setText(seekTmeStr);
//
////            seeky.setProgress(seekPOS);
//            seekBar.setCurrentValue(seekPOS);
//        }
//    }
//
//
//    int seekPOS = PlayerService.seekPosition;
//    int seekPosSeconds;
//
//    String seekTmeStr;
//
//    //    SeekBar seeky;
//    TextView tv;
//    TextView title;
//    TextView albumTitle;
//    ImageView albumArt;
//    MbnCircularSeekBar seekBar;
//    Bitmap albumArtBitmap;
//
//    Window window;
//
//
//    @Override
//    protected void onPause() {
//
//
//        super.onPause();
//
//        finish();
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_player);
//        syncer();
//
//
////        getSupportActionBar().hide();
//
//        window = this.getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
////        window.setStatusBarColor(Color.MAGENTA);
//
//
////        seeky = (SeekBar) findViewById(R.id.seekBarOne);
//        tv = (TextView) findViewById(R.id.timeTextView);
//        title = (TextView) findViewById(R.id.songTitle);
//        albumTitle = (TextView) findViewById(R.id.albumTitle);
//        albumArt = (ImageView) findViewById(R.id.albumArt);
//        seekBar = (MbnCircularSeekBar) findViewById(R.id.seekBarCircule);
//
//        try {
//
//            albumArtBitmap = BitmapFactory.decodeByteArray(currentQueue.getCover(), 0, currentQueue.getCover().length);
//
//            int color = MbnBitmapBucket.averageFinder(albumArtBitmap);
////            window.setStatusBarColor(color);
//
////            title.setTextColor(color);
//            seekBar.setColor(color);
//
//        } catch (Exception ignored) {
//
//        }
//
//        if (albumArtBitmap != null) {
//
////            albumArt.setImageBitmap(MbnBitmapBucket.toneChanger(MbnBitmapBucket.imageMaster(albumArtBitmap, MbnBitmapBucket.MBN_BLUR, MbnBitmapBucket.MBN_WEAK), Color.BLACK));
//            albumArt.setImageBitmap(MbnBitmapBucket.imageMaster(albumArtBitmap, MbnBitmapBucket.MBN_BLUR, MbnBitmapBucket.MBN_MID));
//
//            seekBar.setCenterImg(albumArtBitmap);
//        } else {
//
//            albumArt.setImageBitmap(MbnBitmapBucket.toneChanger(MbnBitmapBucket.imageMaster(BitmapFactory.decodeResource(getResources(), R.drawable.night_rain_1), MbnBitmapBucket.MBN_BLUR, MbnBitmapBucket.MBN_WEAK), Color.GRAY));
//
//            seekBar.setCenterImg(BitmapFactory.decodeResource(getResources(), R.drawable.night_rain_1));
//        }
//
//
//
//
//        title.setText(allSongsMap.get(PlayerService.currentTrackCode).getTitle());
//
//        albumTitle.setText(allSongsMap.get(PlayerService.currentTrackCode).getAlbumTitle());
//
//
////        seeky.setMax(PlayerService.seekMax);
//
//        seekBar.setMaxValue(PlayerService.seekMax);
//
//        seekBar.setOnChangeListener(new MbnSeekBarHelper() {
//            @Override
//            public void onChange(float value) {
//                if (!PlayerService.player.isPlaying()) {
//
//
//                    PlayerService.player.seekTo((int) value);
//
//                    seekPosSeconds = (int) value / 1000;
//
//                    String a = Integer.toString(seekPosSeconds / 60);
//                    String b = Integer.toString(seekPosSeconds % 60);
//
//                    if (a.length() < 2) {
//
//                        a = "0" + a;
//                    }
//                    if (b.length() < 2) {
//
//                        b = "0" + b;
//                    }
//
//                    tv.setText(a + ":" + b);
//
//                }
//            }
//
//            @Override
//            public void onTouchStart(float value) {
//                PlayerService.player.pause();
//            }
//
//            @Override
//            public void onTouchEnd(float value) {
//                PlayerService.player.start();
//            }
//        });
//
////        seeky.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//////            PlayerService playerService = new PlayerService();
////
////            @Override
////            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
////
//////                playerService.controller(2, progress);
////
////                if (!PlayerService.player.isPlaying()) {
////
////
////                    PlayerService.player.seekTo(progress);
////
////                    seekPosSeconds = progress / 1000;
////
////                    tv.setText(seekPosSeconds / 60 + ":" + progress % 60);
////
////                }
////
////            }
////
////            @Override
////            public void onStartTrackingTouch(SeekBar seekBar) {
////
//////                playerService.controller(0, 0);
////
////                PlayerService.player.pause();
////
////
////            }
////
////            @Override
////            public void onStopTrackingTouch(SeekBar seekBar) {
////
//////                playerService.controller(1, 0);
////                PlayerService.player.start();
////
////
////            }
////        });
//
//
//    }
//
//    private void syncer() {
//
//        new Timer().scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                SyncJob syncJob = new SyncJob();
//
//                syncJob.execute();
//
////                System.out.println(Thread.currentThread().getName());
//
//            }
//        }, 0, 1000);
//
//
//    }
//
//
//    public void btonFuncs(View view) {
//
//        String tag = view.getTag().toString();
//
//
//        switch (tag) {
//
//            case "pre":
//
//                try {
//
//
//                    Intent intentOne = new Intent(getApplicationContext(), PlayerService.class);
//                    intentOne.putExtra("alPos", PlayerService.positionAlbum);
//                    intentOne.putExtra("sPos", PlayerService.position - 1);
//
//                    startService(intentOne);
//
//
//                    titleChanger();
//
//
//                } catch (Exception ignored) {
//                }
//
//
//                break;
//
//            case "pause":
//
//                PlayerService.player.pause();
//                break;
//
//            case "play":
//
//                if (!PlayerService.player.isPlaying()) {
//
//                    PlayerService.player.start();
//                }
//                break;
//
//            case "next":
//
//                try {
//
//
//                    Intent intentOne = new Intent(getApplicationContext(), PlayerService.class);
//                    intentOne.putExtra("alPos", PlayerService.positionAlbum);
//                    intentOne.putExtra("sPos", PlayerService.position + 1);
//
//                    startService(intentOne);
//
//
//                    titleChanger();
//
//
//                } catch (Exception ignored) {
//                }
//
////                PlayerService.position++;
//
//                break;
//
//
//        }
//
//    }
//
//
//    private void titleChanger() {
//
//        title.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                try {
//
//                    int color = MbnBitmapBucket.averageFinder(albumArtBitmap);
//
////                    title.setText(PlayerService.current.get(PlayerService.position).getTitle());
//
////                    title.setTextColor(color);
//
//                    seekBar.setColor(color);
//
//                    seekBar.setMaxValue(PlayerService.seekMax);
//
//
//                } catch (Exception ignored) {
//                }
//                title.setText(allSongsMap.get(PlayerService.currentTrackCode).getTitle());
//                albumTitle.setText(allSongsMap.get(PlayerService.currentTrackCode).getAlbumTitle());
//
//
//            }
//        }, 600);
//
//
//    }
//
//
//}
