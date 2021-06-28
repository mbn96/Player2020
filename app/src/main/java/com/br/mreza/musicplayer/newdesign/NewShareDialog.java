package com.br.mreza.musicplayer.newdesign;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.br.mreza.musicplayer.ShareCoverDialog;
import com.br.mreza.musicplayer.newmodel.jobs.ContextJobs;

import mbn.libs.fragmanager.BasicMbnPopupMenu;
import mbn.libs.fragmanager.CustomFragmentSwipeBackAnimator;


public class NewShareDialog extends BasicMbnPopupMenu {

    public static NewShareDialog newInstance(long id) {

        Bundle args = new Bundle();
        args.putLong("SONG_ID", id);
        NewShareDialog fragment = new NewShareDialog();
        fragment.setArguments(args);
        return fragment;
    }


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

//    @Override
//    public void setMargins(FrameLayout.LayoutParams layoutParams) {
//        layoutParams.setMargins((int) (8 * getDensity()), (int) (8 * getDensity()), (int) (8 * getDensity()), (int) (16 * getDensity()));
//    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM | Gravity.CENTER;
    }

    @Override
    public PopupItem[] getItems() {
        PopupItem[] items = new PopupItem[3];
        items[0] = new PopupItem(null, "Song");
        items[1] = new PopupItem(null, "Album art");
        items[2] = new PopupItem(null, "Title and artist");
        return items;
    }

    @Override
    public void onItemClicked(PopupItem[] items, int position) {
        long id = getArguments().getLong("SONG_ID", 0);
        switch (position) {
            case 0:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Long.toString(id)));
                intent.setType("audio/*");
                startActivity(Intent.createChooser(intent, "Share with..."));
                break;
            case 1:
                ShareCoverDialog.newInstance(id).show(getFragmentManager(), "coverShare");
                break;
            case 2:
                new ContextJobs.ShareSongTitleJob(getContext(), id);
                break;
        }
    }
}
