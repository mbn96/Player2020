//package com.br.mreza.musicplayer;
//
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.br.mreza.musicplayer.MBN.customViews.MbnTabLayout;
//import com.br.mreza.musicplayer.MBN.customViews.MbnVerticalViewPager;
//
//public class PlayAndQueueFragment extends Fragment {
//
//
//    public static PlayAndQueueFragment newInstance() {
//
//        Bundle args = new Bundle();
//
//        PlayAndQueueFragment fragment = new PlayAndQueueFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    private MbnVerticalViewPager mViewPager;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//
//        try {
//            return inflater.inflate(R.layout.main_page_two_for_play_and_queue, container, false);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        mViewPager = view.findViewById(R.id.second_pager);
//
//        mViewPager.setAdapter(new Adapter(getActivity().getSupportFragmentManager()));
//
//        MbnTabLayout tabLayout = view.findViewById(R.id.tabs_lay);
//        tabLayout.setmViewPager(mViewPager);
//
//
//    }
//
//
//    public class Adapter extends FragmentPagerAdapter {
//
//
//        public Adapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//
//
//            if (position == 0) {
//                return PlayerFragment.newInstance();
//            } else {
////                return CurrentQueueFragment.newInstance();
//
//            }
//            return null;
//        }
//
//        @Override
//        public int getCount() {
//            return 2;
//        }
//
//        public Bitmap getTabImage(int position) {
//
//            if (position == 0) {
//                return BitmapFactory.decodeResource(getResources(), R.drawable.ic_now_playing);
//            } else {
//                return BitmapFactory.decodeResource(getResources(), R.drawable.ic_play_list);
//
//            }
//
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//
//            String title = null;
//            switch (position) {
//
//                case 0:
//                    title = "Player";
//                    break;
//                case 1:
//                    title = "Queue";
//                    break;
//
//            }
//
//            return title;
//        }
//    }
//
//}
