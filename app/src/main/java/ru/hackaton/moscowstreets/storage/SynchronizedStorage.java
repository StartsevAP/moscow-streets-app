package ru.hackaton.moscowstreets.storage;


import ru.hackaton.moscowstreets.utils.PeriodicalRunner;

public abstract class SynchronizedStorage<TValue> extends Storage<TValue> {
    private final PeriodicalRunner refreshRunner;

    protected SynchronizedStorage(long period, long maxPauseInterval) {
        refreshRunner = new PeriodicalRunner(this::refresh, period, maxPauseInterval);
    }

    public PeriodicalRunner getRefreshRunner() {
        return refreshRunner;
    }
}