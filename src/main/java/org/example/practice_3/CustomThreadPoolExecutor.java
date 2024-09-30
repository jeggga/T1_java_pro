package org.example.practice_3;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.Executor;

public class CustomThreadPoolExecutor implements Executor {
    private static final System.Logger LOG = System.getLogger("CustomThreadPoolExecutor");

    private final LinkedList<Runnable> threadsQueue = new LinkedList<>();

    private Boolean isActive = true;
    private Boolean isAwaitTermination = false;

    public CustomThreadPoolExecutor(Integer capacity) {
        for (int i = 0; i < capacity; i++) {
            new Thread(new Worker()).start();
        }
    }

    @Override
    public void execute(Runnable command) {
        if (isActive) {
            threadsQueue.addLast(command);
        } else {
            throw new IllegalStateException("Threads are shutdown");
        }
    }

    public void shutdown() {
        isActive = false;
    }

    public void awaitTermination() {
        isAwaitTermination = true;
    }

    private class Worker implements Runnable {

        @Override
        public void run() {
            LOG.log(System.Logger.Level.INFO, String.format("%s is started", Thread.currentThread().getName()));
            mainWorker();
            afterInterruptWorker();
            LOG.log(System.Logger.Level.INFO, String.format("%s is interrupt", Thread.currentThread().getName()));
        }

        private void mainWorker() {
            while (isActive) {
                try {
                    getNextTask().run();
                } catch (NoSuchElementException e) {
//                    LOG.log(System.Logger.Level.INFO, String.format("%s waiting new task", Thread.currentThread().getName()));
                }
            }
        }

        private void afterInterruptWorker() {
            while (isAwaitTermination) {
                try {
                    getNextTask().run();
                } catch (NoSuchElementException e) {
                    break;
                }
            }
        }
    }

    private synchronized Runnable getNextTask() {
        return threadsQueue.removeFirst();
    }
}
