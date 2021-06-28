package com.br.mreza.musicplayer.newmodel.database;


import androidx.collection.LongSparseArray;

import java.util.ArrayList;

public class SelfShrinkMap<T> extends LongSparseArray<T> {

    private final Object LOCK = new Object();

    private int MAX_SIZE = 150;

    private final ArrayList<Long> keys = new ArrayList<>();

    public SelfShrinkMap() {
    }

    public SelfShrinkMap(int maxSize) {
        this.MAX_SIZE = maxSize;
    }

    @Override
    public void put(long key, T value) {
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
    public void remove(long key) {
        synchronized (LOCK) {
            super.remove(key);
            keys.remove(keys.indexOf(key));
        }
    }

    @Override
    public T get(long key) {
        synchronized (LOCK) {
            T out = super.get(key);

            if (out != null) {
                keys.remove(keys.indexOf(key));
                keys.add(key);
            }

            return out;
        }
    }
}
