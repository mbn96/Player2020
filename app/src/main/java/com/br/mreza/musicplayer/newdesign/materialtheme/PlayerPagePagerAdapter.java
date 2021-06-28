package com.br.mreza.musicplayer.newdesign.materialtheme;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.core.util.Pools;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.LongSparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.br.mreza.musicplayer.newmodel.database.DataBaseManager;
import com.br.mreza.musicplayer.newmodel.database.SelfShrinkMap;

import java.util.ArrayList;

import mbn.libs.backgroundtask.BaseTaskHolder;

public class PlayerPagePagerAdapter extends PagerAdapter {

    private String TAG = "adapter_pager";

    private int screenHeight;
    private Bitmap defaultBitmap;
    private ViewPager viewPager;
    private ArrayList<Long> queue = new ArrayList<>();
    private long currentId;
    private Pools.SimplePool<FrameLayout> items_pool = new Pools.SimplePool<>(10);
    private SelfShrinkMap<Bitmap> bitmaps = new SelfShrinkMap<>(10);
    private ArrayList<PlayerPageBackgroundMaterial> inUseImageViews = new ArrayList<>();
    private LongSparseArray<PlayerPageBackgroundMaterial> requestingImageViews = new LongSparseArray<>();

    private BaseTaskHolder.ResultReceiver bitmapReceiver = new BaseTaskHolder.ResultReceiver() {
        @Override
        public void onResult(Object result, Object info) {
            long id = (long) info;
            Bitmap bitmap;
            if (result != null) {
                bitmap = (Bitmap) result;
            } else bitmap = defaultBitmap;
            bitmaps.put(id, bitmap);
            if (requestingImageViews.get(id) != null && inUseImageViews.contains(requestingImageViews.get(id))) {
                requestingImageViews.get(id).setImgBitmap(bitmap);
                requestingImageViews.remove(id);
            }

        }
    };


    public void setQueue(ArrayList<Long> queue_new) {
        queue.clear();
        queue.addAll(queue_new);
        requestingImageViews.clear();
        inUseImageViews.clear();
        notifyDataSetChanged();
        viewPager.setAdapter(this);
    }

    public void setCurrentId(long currentId) {
        this.currentId = currentId;
        viewPager.post(changeRunnable);
    }

    private Runnable changeRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager.setCurrentItem(queue.indexOf(currentId), true);
        }
    };


    public PlayerPagePagerAdapter(Bitmap defaultBitmap, ViewPager viewPager) {
        this.defaultBitmap = defaultBitmap;
        this.viewPager = viewPager;

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            private boolean changed = false;
            private int pos;

            @Override
            public void onPageSelected(int position) {
                pos = position;
                changed = true;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE && changed) {
                    long id = queue.get(pos);
                    if (id != currentId) {
                        DataBaseManager.getManager().setCurrentTrack(id);
                    }
                    changed = false;
                }
            }
        });


        viewPager.setPageTransformer(false, pageTransformer);
    }

    void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
//        for (PlayerPageBackgroundMaterial img : inUseImageViews) {
//            img.setScreenHeight(screenHeight);
//        }
    }

    private void requestBitmap(long id) {
        DataBaseManager.getManager().getSongArtWork(id, 600, bitmapReceiver);
    }

    private void prepareItem(int pos, PlayerPageBackgroundMaterial imageView) {
        long id = queue.get(pos);
        Bitmap bitmap = bitmaps.get(id);
        if (bitmap == null) {
            requestingImageViews.put(id, imageView);
            requestBitmap(id);
            return;
        }
        imageView.setImgBitmap(bitmap);
    }

    private FrameLayout getItem(Context context) {
        FrameLayout view = items_pool.acquire();
//        FrameLayout view = null;
        if (view == null) {
            view = new FrameLayout(context);
            view.addView(new PlayerPageBackgroundMaterial(context));
        }
        return view;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        FrameLayout mainView = getItem(container.getContext());
        PlayerPageBackgroundMaterial imageView = (PlayerPageBackgroundMaterial) mainView.getChildAt(0);
        inUseImageViews.add(imageView);
        imageView.setScreenHeight(screenHeight);
        prepareItem(position, imageView);
        container.addView(mainView);
        return mainView;
    }

    @Override
    public int getCount() {
        return queue.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
        FrameLayout mainView = (FrameLayout) object;
        PlayerPageBackgroundMaterial imageView = (PlayerPageBackgroundMaterial) mainView.getChildAt(0);
        inUseImageViews.remove(imageView);
        items_pool.release((FrameLayout) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }

    private ViewPager.PageTransformer pageTransformer = new ViewPager.PageTransformer() {
        @SuppressLint("WrongConstant")
        @Override
        public void transformPage(@NonNull View page, float position) {
            if (position <= -1 || position >= 1) {
                page.setVisibility(View.INVISIBLE);
            } else {
                PlayerPageBackgroundMaterial image = (PlayerPageBackgroundMaterial) ((ViewGroup) page).getChildAt(0);
                page.setVisibility(View.VISIBLE);
                page.setTranslationX(10 * position);
                image.setTranslationX(image.getWidth() * -(position / 2));
            }
        }
    };
}
