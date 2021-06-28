package mbn.packfragmentmanager.fragmanager.backgroundtask;


import androidx.annotation.NonNull;
import androidx.core.util.Pools;

public abstract class Disposal<T> extends Pools.SimplePool<T> {

    /**
     * Creates a new instance.
     *
     * @param maxPoolSize The max pool size.
     * @throws IllegalArgumentException If the max pool size is less than zero.
     */
    public Disposal(int maxPoolSize) {
        super(maxPoolSize);
    }

    public abstract boolean isOK(T object);

    public abstract T createNew();

    public abstract void recycleObject(T object);

    @Override
    public T acquire() {
        T object;
        while ((object = super.acquire()) != null) {
            if (isOK(object)) return object;
            else recycleObject(object);
        }
        object = createNew();
        return object;
    }


    public static abstract class SynchronizedDisposal<E> extends Disposal<E> {
        /**
         * Creates a new instance.
         *
         * @param maxPoolSize The max pool size.
         * @throws IllegalArgumentException If the max pool size is less than zero.
         */
        public SynchronizedDisposal(int maxPoolSize) {
            super(maxPoolSize);
        }

        private final Object LOCK = new Object();

        @Override
        public E acquire() {
            synchronized (LOCK) {
                return super.acquire();
            }
        }

        @Override
        public boolean release(@NonNull E instance) {
            synchronized (LOCK) {
                return super.release(instance);
            }
        }
    }
}
