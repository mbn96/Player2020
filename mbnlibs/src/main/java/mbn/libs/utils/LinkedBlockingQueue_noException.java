package mbn.libs.utils;

import androidx.annotation.Nullable;

import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingQueue_noException<E> extends LinkedBlockingQueue<E> {

    @Nullable
    @Override
    public E take() {
        try {
            return super.take();
        } catch (InterruptedException e) {
            return null;
        }
    }


    public boolean tryPut(E e) {
        try {
            super.put(e);
            return true;
        } catch (InterruptedException ex) {
            return false;
        }
    }
}
