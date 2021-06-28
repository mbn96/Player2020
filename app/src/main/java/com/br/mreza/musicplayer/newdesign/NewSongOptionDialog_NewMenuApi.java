package com.br.mreza.musicplayer.newdesign;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;

import com.br.mreza.musicplayer.CreateNew_AddTo_RemoveFrom_Playlist_Fragment;
import com.br.mreza.musicplayer.R;
import com.br.mreza.musicplayer.cutter.ui.CutterFragment;
import com.br.mreza.musicplayer.newmodel.database.A_B_Manager;
import com.br.mreza.musicplayer.newmodel.database.DataBaseManager;
import com.br.mreza.musicplayer.newmodel.theme.ThemeEngine;

import java.util.ArrayList;

import mbn.libs.fragmanager.CustomFragmentSwipeBackAnimator;
import mbn.libs.fragmanager.popmenu.MbnPopupMenuWithMenuApi;


public class NewSongOptionDialog_NewMenuApi extends MbnPopupMenuWithMenuApi {

    @Override
    public int getAnimationMode() {
        return CustomFragmentSwipeBackAnimator.ANIM_DIALOG_TTB;
    }

//    @Override
//    public Point getCirclePoint() {
//        return new Point(getDialogBase().getWidth() / 2, getDialogBase().getHeight());
//    }


    public static NewSongOptionDialog_NewMenuApi newInstance(long id, boolean isCurrent) {
        Bundle args = new Bundle();
        args.putLong("SONG_ID", id);
        args.putBoolean("IS_CUR", isCurrent);
        NewSongOptionDialog_NewMenuApi fragment = new NewSongOptionDialog_NewMenuApi();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void makeState(Bundle state) {

    }

    @Override
    public void checkForRestoreState() {

    }

    @Override
    protected void onPrepareMenu(Menu menu) {
        if (getArguments().getBoolean("IS_CUR", false)) {
            menu.setGroupVisible(R.id.pl_next_gr, false);
        } else {
            menu.setGroupVisible(R.id.a_b_gr, false);
        }
    }

    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    private ThemeEngine.DefaultThemeCallback themeCallback = new ThemeEngine.DefaultThemeCallback(false, false, true) {
        @Override
        public void onProcessFinished() {

        }

        @Override
        public void onAccentColor(int color) {
            iconColor = color;
            resetAdapter();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        ThemeEngine.getINSTANCE().getResult(themeCallback);
    }

    @Override
    public int onGetMenuId() {
        return R.menu.song_option_menu;
    }

    @Override
    public void onItemClicked(int id) {
        long song_id = getArguments().getLong("SONG_ID", 0);
        switch (id) {
            case R.id.song_share:
                CustomFragmentSwipeBackAnimator.getINSTANCE().addFragment(NewShareDialog.newInstance(song_id));
                break;
            case R.id.add_to_playlist:
                ArrayList<Long> song = new ArrayList<>();
                song.add(song_id);
                CreateNew_AddTo_RemoveFrom_Playlist_Fragment.newInstance(song, true).show(getFragmentManager(), "addToList");
                break;
            case R.id.play_next:
                DataBaseManager.getManager().playAfterCurrent(song_id);
                break;
            case R.id.cutter_song_option:
                getFragmentSwipeBackManager().addFragment(CutterFragment.newInstance(song_id));
//                Context context = getContext();
//                final CheapSoundFile.ProgressListener listener = frac -> true;
//                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Covers");
//                File coverFile = new File(dir, new SimpleDateFormat("yyyy.MM.dd_HHmmss").format(new Date()) + ".mp3");
//                DataSong songFile = DataBaseUtils.getSong(getContext(), song_id);
//                try {
//                    CheapSoundFile cheapSoundFile = CheapSoundFile.create(songFile.getPath(), listener);
//
//                    int mSampleRate = cheapSoundFile.getSampleRate();
//
//                    int mSamplesPerFrame = cheapSoundFile.getSamplesPerFrame();
//
//                    int startFrame = Util.secondsToFrames(5.0, mSampleRate, mSamplesPerFrame);
//
//                    int endFrame = Util.secondsToFrames(120.0, mSampleRate, mSamplesPerFrame);
//
//                    cheapSoundFile.WriteFile(coverFile, startFrame, endFrame - startFrame);
//
//                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(coverFile)));
//
//                } catch (IOException ignored) {
//                }
                break;
            case R.id.a_b_active:
                A_B_Manager.INSTANCE.request(A_B_Manager.SET_STATE_ON, 0, null);
                break;
        }
    }

}
