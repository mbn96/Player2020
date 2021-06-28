//package com.br.mreza.musicplayer;
//
//
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.DialogFragment;
//import android.support.v7.app.AlertDialog;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.TextView;
//
//public class ShareDialog extends DialogFragment {
//
//
//    public static ShareDialog newInstance() {
//
//        Bundle args = new Bundle();
//
//        ShareDialog fragment = new ShareDialog();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//
//
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.custom_title_layout, null);
//        TextView textView = view.findViewById(R.id.text_for_title);
//        textView.setText("Please select");
//        builder.setCustomTitle(view);
//
//        builder.setItems(R.array.shareMenuListArray, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int which) {
//
//
//                Intent intent = new Intent(Intent.ACTION_SEND);
//
//
//                switch (which) {
//                    case 0:
////                        intent.putExtra(Intent.EXTRA_STREAM, Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Long.toString(getCurrentTrack().getId())));
//                        intent.setType("audio/*");
//                        startActivity(Intent.createChooser(intent, "Share with..."));
//                        break;
//                    case 1:
//
//                        ShareCoverDialog.newInstance().show(getFragmentManager(), "coverShare");
//
//                        break;
//                    case 2:
//
////                        intent.putExtra(Intent.EXTRA_TEXT, getCurrentTrack().getTitle() + " - " + getCurrentTrack().getArtistTitle());
//                        intent.setType("text/plain");
//                        startActivity(Intent.createChooser(intent, "Share with..."));
//                        break;
//
//
//                }
//
//
//            }
//        });
//
//
//        return builder.create();
//    }
//}
