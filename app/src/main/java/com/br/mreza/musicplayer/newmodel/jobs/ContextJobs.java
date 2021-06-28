package com.br.mreza.musicplayer.newmodel.jobs;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.br.mreza.musicplayer.DataSong;
import com.br.mreza.musicplayer.MbnController;
import com.br.mreza.musicplayer.newmodel.database.DataBaseManager;

import mbn.libs.backgroundtask.BaseTaskHolder;

public abstract class ContextJobs {

    private Context context;

    ContextJobs(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void finish() {
        context = null;
    }

    public abstract void start();


    //--------------------- JOBS --------------------//

    public static class ShareSongTitleJob extends ContextJobs {

        private long songId;

        public ShareSongTitleJob(Context context, long songId) {
            super(context);
            this.songId = songId;
            start();
        }

        BaseTaskHolder.ResultReceiver resultReceiver = new BaseTaskHolder.ResultReceiver() {
            @Override
            public void onResult(Object result, Object info) {
                DataSong song = (DataSong) result;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, song.getTitle() + " - " + song.getArtistTitle() + "\n Dr.Music");
                intent.setType("text/plain");
                getContext().startActivity(Intent.createChooser(intent, "Share with..."));
                finish();
            }
        };

        @Override
        public void start() {
            DataBaseManager.getManager().getSong(songId, resultReceiver);
        }
    }

    public static class ShareCoverJob extends ContextJobs {

        private long songId;
        private boolean share;

        public ShareCoverJob(Context context, long songId, boolean shareOrSave) {
            super(context);
            this.songId = songId;
            share = shareOrSave;
            start();
        }

        BaseTaskHolder.ResultReceiver resultReceiver = new BaseTaskHolder.ResultReceiver() {
            @Override
            public void onResult(Object result, Object info) {
                DataSong song = (DataSong) result;

                if (share) {
                    Uri uri = MbnController.saveCoverForShare(song.getPath(), getContext(), true);
                    if (uri != null) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_STREAM, uri);
                        intent.setType("image/*");
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        getContext().startActivity(Intent.createChooser(intent, "Share with..."));
                    }
                } else {
                    MbnController.saveCoverForShare(song.getPath(), getContext(), false);
                }
                finish();
            }
        };

        @Override
        public void start() {
            DataBaseManager.getManager().getSong(songId, resultReceiver);
        }
    }

}
