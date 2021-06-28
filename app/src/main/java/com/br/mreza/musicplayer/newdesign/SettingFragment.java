package com.br.mreza.musicplayer.newdesign;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.mreza.musicplayer.mbnviewpicker.MbnViewPicker;
import com.br.mreza.musicplayer.newdesign.setting.TestSetting;

import mbn.libs.fragmanager.BaseFragment;


public class SettingFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private MbnViewPicker viewPicker;

    @Override
    public boolean hasAppBar() {
        return true;
    }

//    @Override
//    public void onScreen() {
//
//    }
//
//    @Override
//    public void offScreen() {
//
//    }

    @Override
    public View onCreateViewForChild(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = new RecyclerView(getContext());
        return recyclerView;
//        viewPicker = new MbnViewPicker(getContext());
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
//        viewPicker.setLayoutParams(layoutParams);
//        return viewPicker;
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
    public void onResume() {
        super.onResume();
        getActivity().lightUI(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().lightUI(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(0xff203040);
        setToolBarBackgroundColor(0xff203040);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new TestSetting(getContext()));

//        viewPicker.setAdapter(new Adapter() {
//            @Override
//            public int getItemCount() {
//                return 20;
//            }
//
//            @Override
//            public View getView(int pos) {
//                TextView textView = new TextView(getContext());
//                textView.setText(String.valueOf(pos));
//                textView.setGravity(Gravity.CENTER);
////                textView.setBackgroundColor(Color.LTGRAY);
//                return textView;
//            }
//
//            @Override
//            public void prepareViewForPosition(View view, int pos) {
//                ((TextView) view).setText(String.valueOf(pos));
//            }
//
//            @Override
//            public void viewOutOfSight(View view, int pos) {
//
//            }
//
//            @Override
//            public int getViewCountInSight() {
//                return 5;
//            }
//        });
//
//        viewPicker.setOrientation(MbnViewPicker.ORI_VERTICAL);

    }
}
