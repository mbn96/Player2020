package com.br.mreza.musicplayer.p2020.design;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.br.mreza.musicplayer.MBN.customViews.MbnCircularSeekBar;
import com.br.mreza.musicplayer.MBN.customViews.MbnSeekBarHelper;
import com.br.mreza.musicplayer.R;
import com.br.mreza.musicplayer.StorageUtils;

import mbn.libs.fragmanager.BaseMbnDialog;
import mbn.libs.fragmanager.CustomFragmentSwipeBackAnimator;


public class TimerChooseDialog2020 extends BaseMbnDialog implements View.OnClickListener {
    public static final long ONE_MIN = 60 * 1000L;

    private TextView timeText;
    private MbnCircularSeekBar seekBar;
    private Button cancelButt;
    private Button deleteButt;
    private Button okButt;

    private long currentChosenTime = 0;

    private long currentChosenTimeWithSeekBar = 0;


    @Override
    public int getAnimationMode() {
        return CustomFragmentSwipeBackAnimator.ANIM_DIALOG_TTB;
    }

    @Override
    public void makeState(Bundle state) {

    }

    @Override
    public void checkForRestoreState() {

    }


    @Override
    public void onPrepareDialog() {
        View view = getLayoutInflater().inflate(R.layout.timer_choose_new_design_2020, getDialogBase(), false);
        addLayout(view);
        timeText = view.findViewById(R.id.time_text);
        seekBar = view.findViewById(R.id.time_seekbar);
        seekBar.setOnChangeListener(seekBarListener);
        seekBar.setMaxValue(120);
        seekBar.setInterval(1);
        seekBar.setColor(Color.WHITE);
        cancelButt = view.findViewById(R.id.timer_frag_cancel_butt);
        deleteButt = view.findViewById(R.id.timer_frag_delete_butt);
        okButt = view.findViewById(R.id.timer_frag_ok_butt);
        deleteButt.setVisibility(View.GONE);

        cancelButt.setOnClickListener(this);
        deleteButt.setOnClickListener(this);
        okButt.setOnClickListener(this);

        findValues();

        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidateBackBlur();
            }
        }, 500);
    }

    private void findValues() {

        currentChosenTime = StorageUtils.getTimer(getContext());
        long diff = currentChosenTime - System.currentTimeMillis();
        if (diff > ONE_MIN) {
            deleteButt.setVisibility(View.VISIBLE);
            seekBar.setCurrentValue(diff / ONE_MIN);
            String time = (int) (diff / ONE_MIN) + " min";
            timeText.setText(time);
            currentChosenTimeWithSeekBar = diff;
            return;
        }

        deleteButt.setVisibility(View.GONE);
        seekBar.setCurrentValue(0);
        timeText.setText("0 min");

    }


    private MbnSeekBarHelper seekBarListener = new MbnSeekBarHelper() {
        @Override
        public void onChange(float value) {
            currentChosenTimeWithSeekBar = (long) (value * ONE_MIN);
            String time = (int) (currentChosenTimeWithSeekBar / ONE_MIN) + " min";
            timeText.setText(time);
        }

        @Override
        public void onTouchStart(float value) {

        }

        @Override
        public void onTouchEnd(float value) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.timer_frag_cancel_butt:
                getFragmentSwipeBackManager().popFragment();
                break;
            case R.id.timer_frag_delete_butt:
                StorageUtils.setTimer(getContext(), 0);
                findValues();
                break;
            case R.id.timer_frag_ok_butt:
                StorageUtils.setTimer(getContext(), System.currentTimeMillis() + currentChosenTimeWithSeekBar);
                getFragmentSwipeBackManager().popFragment();
                break;
        }
    }
}
