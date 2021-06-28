package com.br.mreza.musicplayer.p2020.visualizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import mbn.libs.fragmanager.BaseFragment;

public class VisualizerFrag_1 extends BaseFragment {
    @Override
    public View onCreateViewForChild(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //noinspection ConstantConditions
//        return new VisualizerView(getContext());
        return new VisualizerView_Bars(getContext());
//        return new VisualizerViewSimple(container.getContext());
    }

    @Override
    public void makeState(Bundle state) {

    }

    @Override
    public boolean hasAppBar() {
        return false;
    }

    @Override
    public void checkForRestoreState() {

    }

}
