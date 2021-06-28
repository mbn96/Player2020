package com.br.mreza.musicplayer.BackgroundForUI;


import android.graphics.Bitmap;
import android.util.SparseArray;

import java.util.ArrayList;

public class BitmapHolderMap extends SparseArray<Bitmap> {

    private final static Object LOCK = new Object();

    private final static int MAX_SIZE = 50;

    private final ArrayList<Integer> keys = new ArrayList<>();

    public BitmapHolderMap() {
        super(MAX_SIZE);
    }


    @Override
    public void put(int key, Bitmap value) {
        synchronized (LOCK) {
            super.put(key, value);
            keys.add(key);
            checkForSize();
        }
    }

    private void checkForSize() {
        while (size() > MAX_SIZE - 1) {
            remove(keys.get(0));
        }
    }

    @SuppressWarnings("RedundantCollectionOperation")
    @Override
    public void remove(int key) {
        synchronized (LOCK) {
            if (keys.contains(key)) {
                super.remove(key);
                keys.remove(keys.indexOf(key));
            }
        }
    }

    @Override
    public Bitmap get(int key) {
        synchronized (LOCK) {
            Bitmap out = super.get(key);

            if (out != null) {
                keys.remove(keys.indexOf(key));
                keys.add(key);
            }

            return out;
        }
    }
}
