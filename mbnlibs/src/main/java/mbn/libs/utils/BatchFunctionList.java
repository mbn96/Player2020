package mbn.libs.utils;

import java.util.ArrayList;

public class BatchFunctionList<T> extends ArrayList<T> {

    @Override
    public boolean add(T t) {
        if (!contains(t))
            return super.add(t);
        return false;
    }

    public void batch(BatchFunction<? super T> batchFunction) {
        internalBatch(batchFunction, null, new int[]{0, size()});
    }

    public void batchSelective(BatchFunction<? super T> batchFunction, ItemSelector<? super T> itemSelector) {
        internalBatch(batchFunction, itemSelector, new int[]{0, size()});
    }

    public void batch(BatchFunction<? super T> batchFunction, int[] range) {
        internalBatch(batchFunction, null, range);
    }

    public void batchSelective(BatchFunction<? super T> batchFunction, ItemSelector<? super T> itemSelector, int[] range) {
        internalBatch(batchFunction, itemSelector, range);
    }

    private void internalBatch(BatchFunction<? super T> function, ItemSelector<? super T> selector, int[] range) {
        if (function == null) {
            throw new RuntimeException("Batch function can't be null.");
        }
        if (range[0] < 0 || range[1] > size()) {
            throw new RuntimeException("Range must be within list boundaries.");
        }

        boolean hasSelector = selector != null;
        for (int i = range[0]; i < range[1]; i++) {
            T item = get(i);
            if (!hasSelector) {
                function.function(item);
            } else if (selector.select(item)) {
                function.function(item);
            }
        }
    }

    public interface BatchFunction<E> {
        void function(E element);
    }

    public interface ItemSelector<E> {
        boolean select(E item);
    }
}
