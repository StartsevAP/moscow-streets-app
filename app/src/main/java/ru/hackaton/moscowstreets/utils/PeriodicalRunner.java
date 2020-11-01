package ru.hackaton.moscowstreets.utils;

public class PeriodicalRunner {
    private final Runnable task;
    private final long periodicalTimeout;
    private final long maxPauseInterval;

    private final Object monitor = new Object();
    private Thread runnerThread;
    private int pauseCounter = 0;
    private long pauseExpireTime = 0;
    private String name;

    public PeriodicalRunner(Runnable task, long period, long maxPauseInterval) {
        this.task = task;
        this.periodicalTimeout = period;
        this.maxPauseInterval = maxPauseInterval;
    }

    private void mainLoop() {
        try {
            while (runnerThread != null) {
                if (waitPause()) {
                    break;
                }
                task.run();
                if (waitPeriodicalTimeout()) {
                    break;
                }
            }
        }
        catch (InterruptedException e) {
        }
    }

    private boolean waitPause() throws InterruptedException {
        synchronized (monitor) {
            if (runnerThread == null) {
                return true;
            }
            long pauseMills = getPauseMills();
            if (pauseMills > 0) {
                monitor.wait(pauseMills);
                if (runnerThread == null) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean waitPeriodicalTimeout() throws InterruptedException {
        synchronized (monitor) {
            if (runnerThread == null) {
                return true;
            }
            long pauseMills = getPauseMills();
            if (pauseMills == 0) {
                monitor.wait(periodicalTimeout);
            }
        }
        return false;
    }

    private long getPauseMills() {
        synchronized (monitor) {
            if (pauseCounter == 0) {
                return 0;
            }
            long currentTimeMillis = System.currentTimeMillis();
            if (pauseExpireTime < currentTimeMillis) {
                pauseCounter = 0;
                pauseExpireTime = 0;
                return 0;
            }
            return pauseExpireTime - currentTimeMillis;
        }
    }

    public void setThreadName(String name) {
        this.name = name;
    }

    private boolean isActive() {
        synchronized (monitor) {
            return (runnerThread != null);
        }
    }

    public void start() {
        synchronized (monitor) {
            if (isActive()) {
                throw new IllegalStateException("Periodical Runner is already started.");
            }
            pauseCounter = 0;
            pauseExpireTime = 0;
            runnerThread = new Thread(this::mainLoop);
            runnerThread.setName(name);
            runnerThread.start();
        }
    }

    public void startIfNeed() {
        synchronized (monitor) {
            if (!isActive()) {
                start();
            }
        }
    }

    public void stop() {
        synchronized (monitor) {
            if (!isActive()) {
                throw new IllegalStateException("Periodical Runner is not started yet.");
            }
            runnerThread.interrupt();
            runnerThread = null;
        }
    }

    public void pause() {
        synchronized (monitor) {
            if (!isActive()) {
                throw new IllegalStateException("Periodical Runner is not started yet.");
            }
            pauseCounter++;
            pauseExpireTime = System.currentTimeMillis() + maxPauseInterval;
            monitor.notifyAll();
        }
    }

    public void resume() {
        synchronized (monitor) {
            if (!isActive()) {
                throw new IllegalStateException("Periodical Runner is not started yet.");
            }
            if (pauseCounter == 0) {
                throw new IllegalStateException("Periodical Runner is not paused.");
            }
            pauseCounter--;
            if (pauseCounter == 0) {
                pauseExpireTime = 0;
            }
            monitor.notifyAll();
        }
    }
}
