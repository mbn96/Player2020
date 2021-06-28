package mbn.libs.backgroundtask;

import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleThreadManager {
    private static final AtomicInteger MANAGER_ID = new AtomicInteger(1);

    private final AtomicInteger THREAD_ID = new AtomicInteger(1);
    private final ReentrantLock LOCK = new ReentrantLock();
    private final SparseArray<TaskHolder> taskHolders = new SparseArray<>();
    private final Handler callbackHandler;
    private volatile boolean callbackOnTaskThread;
    private volatile STMListener listener;
    private final int THIS_ID = MANAGER_ID.getAndIncrement();

    private SimpleThreadManager(Handler callbackHandler, boolean callbackOnTaskThread) {
        this.callbackHandler = callbackHandler;
        this.callbackOnTaskThread = callbackOnTaskThread;
    }

    public SimpleThreadManager() {
        this(new Handler(Looper.getMainLooper()), false);
    }

    public SimpleThreadManager(boolean callbackOnTaskThread) {
        this(callbackOnTaskThread ? null : new Handler(Looper.getMainLooper()), callbackOnTaskThread);
    }

    public SimpleThreadManager(Handler callbackHandler) {
        this(callbackHandler, false);
    }

    public int submit(Runnable task) {
        LOCK.lock();
        int id = THREAD_ID.getAndIncrement();
        TaskAdapter adapter = new TaskAdapter(task, id);
        Thread t = new Thread(adapter, "SimpleThreadManager-" + THIS_ID + "-Thread-" + id);
        TaskHolder holder = new TaskHolder(t/*, adapter*/);
        taskHolders.append(id, holder);
        holder.start();
        LOCK.unlock();
        return id;
    }

    public int[] submit(Runnable task, int count) {
        int[] ids = new int[count];
        for (int i = 0; i < count; i++) {
            ids[i] = submit(task);
        }
        return ids;
    }

    public void interrupt(int id) {
        LOCK.lock();
        TaskHolder holder = taskHolders.get(id);
        if (holder != null) {
            holder.thread.interrupt();
        }
        LOCK.unlock();
    }

    public void interruptAll() {
        LOCK.lock();
        for (int i = 0; i < taskHolders.size(); i++) {
            taskHolders.valueAt(i).thread.interrupt();
        }
        LOCK.unlock();
    }

    public void setListener(STMListener listener) {
        this.listener = listener;
    }

    private class TaskAdapter implements Runnable {
        private final Runnable task;
        private final int id;

        public TaskAdapter(Runnable task, int id) {
            this.task = task;
            this.id = id;
        }

        @Override
        public void run() {
            if (listener != null) {
                if (callbackOnTaskThread) {
                    listener.onTaskStarted(id, task);
                } else {
                    callbackHandler.post(() -> listener.onTaskStarted(id, task));
                }
            }
            task.run();
            LOCK.lock();
            taskHolders.remove(id);
            LOCK.unlock();
            if (listener != null) {
                if (callbackOnTaskThread) {
                    listener.onTaskFinished(id, task);
                } else {
                    callbackHandler.post(() -> listener.onTaskFinished(id, task));
                }
            }
        }
    }

    private static class TaskHolder {
        private final Thread thread;
//        private final TaskAdapter taskAdapter;

        private TaskHolder(Thread thread/*, TaskAdapter taskAdapter*/) {
            this.thread = thread;
//            this.taskAdapter = taskAdapter;
        }

        private void start() {
            thread.start();
        }

    }

    public interface STMListener {
        void onTaskStarted(int id, Runnable task);

        void onTaskFinished(int id, Runnable task);
    }
}
