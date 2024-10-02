package org.example.practice_3;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomThreadPoolExecutor implements Executor {
    private static final System.Logger LOG = System.getLogger("CustomThreadPoolExecutor");

    private final LinkedList<Runnable> threadsQueue = new LinkedList<>();

    private AtomicBoolean isActive = new AtomicBoolean(true);

    public CustomThreadPoolExecutor(Integer capacity) {
        for (int i = 0; i < capacity; i++) {
            new Thread(new Worker()).start();
        }
    }

    @Override
    public void execute(Runnable command) {
        if (isActive.get()) {
            threadsQueue.addLast(command);
        } else {
            throw new IllegalStateException("Threads are shutdown");
        }
    }

    public void shutdown() {
        isActive.set(false);
    }

    public void awaitTermination() {

    }

    private class Worker implements Runnable {

        @Override
        public void run() {
            LOG.log(System.Logger.Level.INFO, String.format("%s is started", Thread.currentThread().getName()));
            while (isActive.get()) {
                try {
                    getNextTask().run();
                } catch (NoSuchElementException e) {
                    LOG.log(System.Logger.Level.INFO, String.format("%s waiting new task", Thread.currentThread().getName()));
                }

            }
            LOG.log(System.Logger.Level.INFO, String.format("%s is interrupt", Thread.currentThread().getName()));
        }
    }

    private synchronized Runnable getNextTask() {
        return threadsQueue.removeFirst();
    }
}
