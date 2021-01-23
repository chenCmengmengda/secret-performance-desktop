package cn.chenc.performs.task;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2021/1/23 14:23
 *
 */
public abstract class NanoTimer extends ScheduledService<Void> {

    private final long ONE_NANO = 1000000000L;

    private final double ONE_NANO_INV = 1f / 1000000000L;

    private long startTime;

    private long previousTime;

    private double frameRate;

    private double deltaTime;

    public NanoTimer(double period) {
        super();
        this.setPeriod(Duration.millis(period));
        this.setExecutor(Executors.newCachedThreadPool(new NanoThreadFactory()));
    }

    public final long getTime() {
        return System.nanoTime() - startTime;
    }

    public final double getTimeAsSeconds() {
        return getTime() * ONE_NANO_INV;
    }

    public final double getDeltaTime() {
        return deltaTime;
    }

    public final double getFrameRate() {
        return frameRate;
    }

    @Override
    public final void start() {
        super.start();
        if (startTime <= 0) {
            startTime = System.nanoTime();
        }
    }

    @Override
    public final void reset() {
        super.reset();
        startTime = System.nanoTime();
        previousTime = getTime();
    }

    @Override
    protected final Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateTimer();
                return null;
            }
        };
    }

    private void updateTimer() {
        deltaTime = (getTime() - previousTime) * (1.0f / ONE_NANO);
        frameRate = 1.0f / deltaTime;
        previousTime = getTime();
    }

    @Override
    protected final void succeeded() {
        super.succeeded();
        onSucceeded();
    }

    @Override
    protected final void failed() {
        getException().printStackTrace(System.err);
        onFailed();
    }

    private class NanoThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "NanoTimerThread");
            thread.setPriority(Thread.NORM_PRIORITY + 1);
            thread.setDaemon(true);
            return thread;
        }
    }

    /**
     *
     */
    protected abstract void onSucceeded();

    /**
     *
     */
    protected void onFailed() {
    }
}

