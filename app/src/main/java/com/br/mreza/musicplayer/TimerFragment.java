package com.br.mreza.musicplayer;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.br.mreza.musicplayer.newmodel.service.player.PlayerCommunicates;

public class TimerFragment extends BottomSheetDialogFragment implements View.OnClickListener {


    public final static String TIMER_ACTION = "timer_action";
    public final static String TIMER_ACTION_TIME = "timer_action_time";
    public final static String TIMER_ACTION_COMMAND = "timer_action_command";

    SeekBar seekBar;
    TextView timeView;
    ImageButton okButt;
    ImageButton closeButt;

    private int sleepTime = 30;

    private static final int MIN = 60 * 1000;

    Context cont;

    static public TimerFragment newInstance() {

        Bundle args = new Bundle();


        TimerFragment fragment = new TimerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        cont = inflater.getContext();


        return inflater.inflate(R.layout.timer_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        orange(view);


    }

    @SuppressLint("SetTextI18n")
    private void orange(View view) {


        seekBar = view.findViewById(R.id.timer_seek);
        timeView = view.findViewById(R.id.time_text);
        okButt = view.findViewById(R.id.timer_ok);
        closeButt = view.findViewById(R.id.timer_can);

        okButt.setOnClickListener(this);
        closeButt.setOnClickListener(this);

        seekWork();

        timeView.setText(Integer.toString(sleepTime) + " min");


    }

    private void seekWork() {


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                sleepTime = (seekBar.getProgress() + 1) * 15;

                timeView.setText(Integer.toString(sleepTime) + " min");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.timer_ok:

                if (PlayerCommunicates.getINSTANCE().isPlaying()) {

                    long timerRequest = MIN * sleepTime;

                    Intent intent = new Intent(TIMER_ACTION_COMMAND);
                    intent.putExtra(TIMER_ACTION, 1);
                    intent.putExtra(TIMER_ACTION_TIME, timerRequest);

                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
//                    cont.sendBroadcast(intent);

                    Toast.makeText(getContext(), "Timer activated", Toast.LENGTH_SHORT).show();

                    dismiss();

                } else {

                    Snackbar.make(view.getRootView(), "Player must be playing !", Snackbar.LENGTH_SHORT).show();

                }


                break;
            case R.id.timer_can:
                dismiss();
                break;


        }


    }
}
