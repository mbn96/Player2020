package com.br.mreza.musicplayer.cutter.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.br.mreza.musicplayer.DataSong;
import com.br.mreza.musicplayer.MbnController;
import com.br.mreza.musicplayer.R;
import com.br.mreza.musicplayer.cutter.audiotrimmer.CheapSoundFile;
import com.br.mreza.musicplayer.cutter.audiotrimmer.Util;
import com.br.mreza.musicplayer.newdesign.customviews.DotProgressBar;
import com.br.mreza.musicplayer.newmodel.database.DataBaseManager;
import com.br.mreza.musicplayer.newmodel.service.player.PlayerCommunicates;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import mbn.libs.backgroundtask.BaseTaskHolder;
import mbn.libs.backgroundtask.BaseTaskHolder.ResultReceiver;
import mbn.libs.backgroundtask.ThreadManager;
import mbn.libs.fragmanager.BaseFragment;


public class CutterFragment extends BaseFragment {

    private static final String SONG_ID = "Song_id";

    private ResultReceiver songReceiver = new ResultReceiver() {
        @Override
        public void onResult(Object result, Object info) {
            song = (DataSong) result;
            getUserView().animate().alpha(1).setDuration(400).start();
            startText.setText(MbnController.makeMillisToTime(getStartTime()));
            endText.setText(MbnController.makeMillisToTime(getEndTime()));
            title.setText(song.getTitle());
        }
    };

    private ResultReceiver jobDone = new ResultReceiver() {
        @Override
        public void onResult(Object result, Object info) {
            Toast.makeText(getContext(), "Clip saved in : \n" + (String) result, Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
            progText.setVisibility(View.INVISIBLE);
//            getFragmentSwipeBackManager().popFragment();
        }
    };

    private DataSong song;
    private long songID;
    private MediaPlayer mediaPlayer;
    private DoubleThumbSeekBar doubleThumbSeekBar;
    private Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @SuppressLint("SetTextI18n")
        @Override
        public boolean handleMessage(Message msg) {
            progText.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress((Integer) msg.obj);
            progText.setText(msg.obj + "%");
            return true;
        }
    });
    private ImageButton cutButt;
    private ImageButton playButt;
    private ImageButton stopButt;
    private TextView title;
    private TextView startText;
    private TextView currentText;
    private TextView endText;
    private TextView progText;
    private DotProgressBar progressBar;


    private Runnable checkRunnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                try {
                    if (mediaPlayer.getCurrentPosition() > getEndTime()) {
                        stopPlayer();
                    }
                    float pos = (((float) mediaPlayer.getCurrentPosition()) / song.getDuration()) * 100;
                    doubleThumbSeekBar.setPlayerPos(pos);
                    currentText.setText(MbnController.makeMillisToTime(mediaPlayer.getCurrentPosition()));
                } catch (Exception ignored) {
                }
            }
            handler.postDelayed(this, 100);
        }
    };

    private int getStartTime() {
        float seekBarV = doubleThumbSeekBar.getStartValue() / 100;
        return (int) (song.getDuration() * seekBarV);
    }

    private int getEndTime() {
        float seekBarV = doubleThumbSeekBar.getEndValue() / 100;
        return (int) (song.getDuration() * seekBarV);
    }

    private void startPlayer() {
        PlayerCommunicates.getINSTANCE().pause();
        try {
            mediaPlayer.release();
        } catch (Exception ignored) {
        }
        mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(song.getPath()));
        mediaPlayer.seekTo(getStartTime());
        mediaPlayer.start();
        handler.post(checkRunnable);
        doubleThumbSeekBar.setPlaying(true);
    }

    private void stopPlayer() {
        try {
            mediaPlayer.release();
        } catch (Exception ignored) {
        }
        handler.removeCallbacks(checkRunnable);
        doubleThumbSeekBar.setPlaying(false);
        currentText.setText("");
    }

    @Override
    public void onStop() {
        super.onStop();
        stopPlayer();
        handler.removeCallbacks(checkRunnable);
    }

    public static CutterFragment newInstance(long songId) {
        Bundle args = new Bundle();
        args.putLong(SONG_ID, songId);
        CutterFragment fragment = new CutterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateViewForChild(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cutter_frag_layout, container, false);
    }

    @Override
    public boolean hasAppBar() {
        return false;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUserView().setAlpha(0);

        progressBar = view.findViewById(R.id.cutter_progress);
        progressBar.setMax(100);
        progressBar.setAccentColor(Color.parseColor("#00b4ff"));
        progText = view.findViewById(R.id.cutter_prog_text);

        songID = getArguments().getLong(SONG_ID);
        DataBaseManager.getManager().getSong(songID, songReceiver);

        title = view.findViewById(R.id.cutter_title);
        startText = view.findViewById(R.id.cutter_start);
        currentText = view.findViewById(R.id.cutter_current);
        currentText.setText("");
        endText = view.findViewById(R.id.cutter_end);

        stopButt = view.findViewById(R.id.cutter_stop);
        playButt = view.findViewById(R.id.cutter_play);
        cutButt = view.findViewById(R.id.cutter_cut);

        doubleThumbSeekBar = view.findViewById(R.id.cutter_seek);
        doubleThumbSeekBar.setListener(new DoubleThumbSeekBar.Listener() {
            @Override
            public void onChange() {
                startText.setText(MbnController.makeMillisToTime(getStartTime()));
                endText.setText(MbnController.makeMillisToTime(getEndTime()));
            }
        });

        playButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlayer();
            }
        });
        stopButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlayer();
            }
        });
        cutButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadManager.getAppGlobalTask().StartTask(new CutterTask(song.getPath(), getStartTime(), getEndTime(), getContext()), jobDone);
            }
        });
    }


    private class CutterTask implements BaseTaskHolder.BaseTask {

        private String path;
        private float start, end;
        private Context context;

        CutterTask(String path, int start, int end, Context context) {
            this.path = path;
            this.start = start;
            this.end = end;
            this.context = context;
        }

        @Override
        public Object onRun() {
            final CheapSoundFile.ProgressListener listener = new CheapSoundFile.ProgressListener() {
                int percent = 0;

                @Override
                public boolean reportProgress(double frac) {
                    if (percent != Math.floor(frac * 100)) {
                        percent = (int) Math.floor(frac * 100);
                        Message message = Message.obtain(handler);
                        message.obj = percent;
                        message.sendToTarget();
                    }
                    return true;
                }
            };
            File dir = new File(Environment.getExternalStorageDirectory(), "DrMusic");
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    context = null;
                    return null;
                }
            }
            File outFile = new File(dir, "Clip_" + new SimpleDateFormat("yyyy.MM.dd_HHmmss").format(new Date()) + ".mp3");
            try {
                CheapSoundFile cheapSoundFile = CheapSoundFile.create(path, listener);


                int mSampleRate = cheapSoundFile.getSampleRate();

                int mSamplesPerFrame = cheapSoundFile.getSamplesPerFrame();

//                Log.i(TAG, "onRun: " + start / 1000 + "  " + end / 1000);

                int startFrame = Util.secondsToFrames(start / 1000, mSampleRate, mSamplesPerFrame);

                int endFrame = Util.secondsToFrames(end / 1000, mSampleRate, mSamplesPerFrame);

                cheapSoundFile.WriteFile(outFile, startFrame, endFrame - startFrame);

                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(outFile)));
            } catch (IOException ignored) {
            } finally {
                context = null;
            }
            return outFile.getPath();
        }

        @Override
        public Object getInfo() {
            return null;
        }
    }
}
