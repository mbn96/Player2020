//package com.br.mreza.musicplayer;
//
//
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.DialogFragment;
//import android.support.v7.app.AlertDialog;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//public class SongOptionDialog extends DialogFragment {
//
//
//    public static SongOptionDialog newInstance() {
//
//        Bundle args = new Bundle();
//
//        SongOptionDialog fragment = new SongOptionDialog();
//        fragment.setArguments(args);
////        fragment.setStyle(STYLE_NO_FRAME, 0);
//        return fragment;
//    }
//
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//
////        builder.setTitle("Options");
//
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//
//        View view = inflater.inflate(R.layout.custom_title_layout, null);
//
//        TextView textView = view.findViewById(R.id.text_for_title);
//
//
//        builder.setCustomTitle(view);
//
//        builder.setItems(R.array.onPlayerPageDialogList, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int which) {
//
//                switch (which) {
//
//
//                    case 0:
//
//
//                        ShareDialog.newInstance().show(getFragmentManager(), "shareWith");
//
//                        break;
//
//                    case 1:
//
//                        ArrayList<DataSong> song = new ArrayList<>();
////                        song.add(getCurrentTrack());
//                        CreateNew_AddTo_RemoveFrom_Playlist_Fragment.newInstance(song, true).show(getFragmentManager(), "addToList");
//                        break;
//
//                }
//
//            }
//        });
//
//
//        return builder.create();
//    }
//}
