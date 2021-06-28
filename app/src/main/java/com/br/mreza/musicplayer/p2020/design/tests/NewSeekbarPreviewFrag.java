package com.br.mreza.musicplayer.p2020.design.tests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.br.mreza.musicplayer.newdesign.newmodelfrags.MusicFileToByte;
import com.br.mreza.musicplayer.p2020.views.NewWaveFormSeekBar;

import mbn.libs.backgroundtask.ThreadManager;
import mbn.libs.fragmanager.BaseFragment;

public class NewSeekbarPreviewFrag extends BaseFragment {


    public static NewSeekbarPreviewFrag newInstance(String fPath, long sId) {

        Bundle args = new Bundle();
        args.putString("F_PATH", fPath);
        args.putLong("S_ID", sId);
        NewSeekbarPreviewFrag fragment = new NewSeekbarPreviewFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateViewForChild(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return new FrameLayout(inflater.getContext());
    }

    @Override
    public void makeState(Bundle state) {

    }

    @Override
    public void checkForRestoreState() {

    }

    private long getSID() {
        return getArguments().getLong("S_ID");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NewWaveFormSeekBar seekBar = new NewWaveFormSeekBar(getContext());
        seekBar.setSongID(getSID());

        ((ViewGroup) view).addView(seekBar);

        ThreadManager.getAppGlobalTask_MultiThread().StartTask(new MusicFileToByte_new(getArguments().getString("F_PATH"), getSID(), seekBar), null);

    }
}
