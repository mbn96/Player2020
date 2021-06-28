//package com.br.mreza.musicplayer.newdesign;
//
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.view.Gravity;
//
//import com.br.mreza.musicplayer.CreateNew_AddTo_RemoveFrom_Playlist_Fragment;
//import com.br.mreza.musicplayer.R;
//import com.br.mreza.musicplayer.fragmanager.BasicMbnPopupMenu;
//import com.br.mreza.musicplayer.fragmanager.CustomFragmentSwipeBackAnimator;
//
//import java.util.ArrayList;
//
//
//public class NewSongOptionDialog extends BasicMbnPopupMenu {
//
//    @Override
//    public int getAnimationMode() {
//        return CustomFragmentSwipeBackAnimator.ANIM_DIALOG_TTB;
//    }
//
////    @Override
////    public Point getCirclePoint() {
////        return new Point(getDialogBase().getWidth() / 2, getDialogBase().getHeight());
////    }
//
//
//    public static NewSongOptionDialog newInstance(long id) {
//        Bundle args = new Bundle();
//        args.putLong("SONG_ID", id);
//        NewSongOptionDialog fragment = new NewSongOptionDialog();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    private NewSongOptionDialog() {
//    }
//
//    @Override
//    public void makeState(Bundle state) {
//
//    }
//
//    @Override
//    public void checkForRestoreState() {
//
//    }
//
////    @Override
////    public void setMargins(FrameLayout.LayoutParams layoutParams) {
////        layoutParams.setMargins((int) (8 * getDensity()), (int) (8 * getDensity()), (int) (8 * getDensity()), (int) (16 * getDensity()));
////    }
//
//    @Override
//    public int getGravity() {
//        return Gravity.CENTER;
//    }
//
//    @Override
//    public PopupItem[] getItems() {
//        PopupItem[] items = new PopupItem[3];
////        items[0] = new PopupItem(BitmapFactory.decodeResource(getResources(), R.drawable.ic_share_png), "Share");
////        items[1] = new PopupItem(BitmapFactory.decodeResource(getResources(), R.drawable.ic_add_playlist_png), "Add to a playlist");
////        items[2] = new PopupItem(BitmapFactory.decodeResource(getResources(), R.drawable.ic_search_png), "Search Google...");
//        return items;
//    }
//
//    @Override
//    public void onItemClicked(PopupItem[] items, int position) {
//        long id = getArguments().getLong("SONG_ID", 0);
//        switch (position) {
//            case 0:
//                CustomFragmentSwipeBackAnimator.getINSTANCE().addFragment(NewShareDialog.newInstance(id));
//                break;
//            case 1:
//                ArrayList<Long> song = new ArrayList<>();
//                song.add(id);
//                CreateNew_AddTo_RemoveFrom_Playlist_Fragment.newInstance(song, true).show(getFragmentManager(), "addToList");
//                break;
//        }
//    }
//}
