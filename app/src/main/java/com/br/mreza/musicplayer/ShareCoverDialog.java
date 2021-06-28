package com.br.mreza.musicplayer;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.br.mreza.musicplayer.newmodel.jobs.ContextJobs;

public class ShareCoverDialog extends DialogFragment {


    public static ShareCoverDialog newInstance(long id) {

        Bundle args = new Bundle();
        args.putLong("SONG_ID", id);
        ShareCoverDialog fragment = new ShareCoverDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_title_layout, null);
        TextView textView = view.findViewById(R.id.text_for_title);
        textView.setText("Please select");
        builder.setCustomTitle(view);


        builder.setItems(new CharSequence[]{"Save in device", "Share via..."}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                long id = getArguments().getLong("SONG_ID", 0);
                switch (which) {
                    case 0:
                        new ContextJobs.ShareCoverJob(getContext(), id, false);
                        break;
                    case 1:
                        new ContextJobs.ShareCoverJob(getContext(), id, true);
                        break;
                }
            }
        });

        return builder.create();
    }
}
