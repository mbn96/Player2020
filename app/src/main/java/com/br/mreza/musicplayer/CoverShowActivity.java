//package com.br.mreza.musicplayer;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.constraint.ConstraintLayout;
//import android.support.v4.content.LocalBroadcastManager;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.ImageView;
//
//import com.br.mreza.musicplayer.MBN.bitmapUtils.AdvancedMbnBlur;
//
//import static com.br.mreza.musicplayer.ListMaker.getCurrentTrack;
//import static com.br.mreza.musicplayer.MbnController.To_FRAGMENT;
//
//public class CoverShowActivity extends AppCompatActivity {
//
//    private ConstraintLayout root;
//    ImageView coverBlur;
//    ImageView coverMain;
//    AdvancedMbnBlur mbnBlur;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
////        getWindow().setEnterTransition(new Fade(Fade.IN));
////        getWindow().setEnterTransition(new Slide(GravityCompat.END));
//
//        setContentView(R.layout.activity_cover_show);
//
//
//        try {
//            getSupportActionBar().hide();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        root = findViewById(R.id.cover_root);
//
//        root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//
//        getWindow().setStatusBarColor(Color.argb(0, 0, 0, 0));
//
//
//        coverBlur = findViewById(R.id.cover_blur);
//        coverMain = findViewById(R.id.cover_main);
//
//        mbnBlur = new AdvancedMbnBlur();
//
//        mbnBlur.setHelper(new AdvancedMbnBlur.MbnAdvancedBlurHelper() {
//            @Override
//            public void onFinish(Bitmap blurred) {
//                coverBlur.animate().alpha(1f).setDuration(250);
//                coverBlur.setImageBitmap(blurred);
//            }
//        });
//
//
//        coverMain.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                ShareCoverDialog.newInstance().show(getSupportFragmentManager(), "cover");
//                return true;
//            }
//        });
//
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        try {
////            mbnBlur.makeBlur(getApplicationContext(), currentTrack.getBlurredCover());
//
//            coverMain.setImageBitmap(MbnController.getCover(getApplicationContext(), getCurrentTrack().getPath(), 1));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        reg();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        unReg();
//    }
//
//    BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
////            mbnBlur.makeBlur(getApplicationContext(), currentTrack.getBlurredCover());
//
//            coverMain.setImageBitmap(MbnController.getCover(getApplicationContext(), getCurrentTrack().getPath(), 1));
//
//        }
//    };
//
//
//    private void reg() {
//
//
//        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, new IntentFilter(To_FRAGMENT));
//
//
//    }
//
//    private void unReg() {
//
//        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(receiver);
//
//    }
//
//}
