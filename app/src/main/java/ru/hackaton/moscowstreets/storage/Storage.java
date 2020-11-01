package ru.hackaton.moscowstreets.storage;

import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Storage<TValue> {
    public Storage() {
    }

    public interface ErrorHandler {
        void handleError(Exception exception);
    }

    public interface ResultHandler<TValue> {
        void handleResult(@Nullable TValue value);
    }

    public interface Subscriber {
        void unsubscribe();
    }

    public interface UnsubscribableErrorHandler {
        /**
         * @param exception Exception
         * @return The handler should be unsubscribed.
         */
        boolean handleError(Exception exception);
    }

    public interface UnsubscribableResultHandler<TValue> {
        /**
         * @param value Value
         * @return The handler should be unsubscribed.
         */
        boolean handleResult(@Nullable TValue value);
    }

    public interface ValueModifier<TValue> {
        TValue createNewValue(@Nullable TValue value);
    }

    private static void callErrorHandlers(Collection<UnsubscribableErrorHandler> errorHandlers,
                                          Exception exception) {
        for (Iterator<UnsubscribableErrorHandler> i = errorHandlers.iterator(); i.hasNext(); ) {
            UnsubscribableErrorHandler errorHandler = i.next();
            boolean shouldUnsubscribe = errorHandler.handleError(exception);
            if (shouldUnsubscribe) {
                i.remove();
            }
        }
    }

    private static <TValue> void callUpdateHandlers(
        Collection<UnsubscribableResultHandler<TValue>> updateHandlers, TValue value) {
        for (Iterator<UnsubscribableResultHandler<TValue>> i = updateHandlers.iterator();
             i.hasNext(); ) {
            UnsubscribableResultHandler<TValue> updateHandler = i.next();
            boolean shouldUnsubscribe = updateHandler.handleResult(value);
            if (shouldUnsubscribe) {
                i.remove();
            }
        }
    }

    @Nullable
    protected volatile TValue localValue;

    private Collection<UnsubscribableErrorHandler>
        errorHandlers = Collections.newSetFromMap(new ConcurrentHashMap<>());
    @Nullable
    private Exception refreshError;
    private volatile boolean ignoreSetValue = false;
    private volatile boolean isFirstFetchDone = false;
    private volatile boolean isUpdateHandlersCalling = false;
    private volatile boolean isValueFetching = false;
    private volatile boolean shouldUpdateHandlersBeenCalledAgain = false;
    private Collection<UnsubscribableResultHandler<TValue>>
        updateHandlers = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public boolean getIsFirstFetchDone() {
        return isFirstFetchDone;
    }

    @Nullable
    public Exception getRefreshError() {
        return refreshError;
    }

    @Nullable
    public TValue getValue() {
        return localValue;
    }

    public void modifyLocalValue(ValueModifier<TValue> updater) {
        setValue(updater.createNewValue(localValue));
        if (isValueFetching) {
            subscribeSingle(
                newValue -> setValue(updater.createNewValue(newValue)),
                exception -> setValue(updater.createNewValue(localValue))
            );
        }
    }

    public void refresh() {
        try {
            isValueFetching = true;
            TValue fetchedValue = fetchValue();
            if (!isFirstFetchDone) {
                isFirstFetchDone = true;
            }
            setValue(fetchedValue);
            refreshError = null;
        }
        catch (Exception e) {
            refreshError = e;
            callErrorHandlers(errorHandlers, e);
        }
        finally {
            isValueFetching = false;
        }
    }

    public Subscriber subscribe(ResultHandler<TValue> updateHandler, ErrorHandler errorHandler) {
        return subscribe(updateHandler, errorHandler, false);
    }

    public Subscriber subscribeSingle(ResultHandler<TValue> updateHandler,
                                      ErrorHandler errorHandler) {
        return subscribe(updateHandler, errorHandler, true);
    }

    private Subscriber subscribe(ResultHandler<TValue> updateHandler, ErrorHandler errorHandler,
                                 boolean single) {
        UnsubscribableResultHandler<TValue> updateHandlerWrapper = (@Nullable TValue value) -> {
            updateHandler.handleResult(value);

            return single;
        };
        updateHandlers.add(updateHandlerWrapper);

        UnsubscribableErrorHandler errorHandlerWrapper = (Exception exception) -> {
            errorHandler.handleError(exception);

            return single;
        };
        errorHandlers.add(errorHandlerWrapper);

        return () -> {
            updateHandlers.remove(updateHandlerWrapper);
            errorHandlers.remove(errorHandlerWrapper);
        };
    }

    protected abstract TValue fetchValue() throws Exception;

    protected void setValue(TValue value) {
        if (!ignoreSetValue) {
            if (value != localValue) {
                localValue = value;
                if (isUpdateHandlersCalling) {
                    shouldUpdateHandlersBeenCalledAgain = true;
                }
                else {
                    isUpdateHandlersCalling = true;
                    callUpdateHandlers(updateHandlers, localValue);
                    isUpdateHandlersCalling = false;

                    if (shouldUpdateHandlersBeenCalledAgain) {
                        shouldUpdateHandlersBeenCalledAgain = false;

                        ignoreSetValue = true;
                        callUpdateHandlers(updateHandlers, localValue);
                        ignoreSetValue = false;
                    }
                }
            }
        }
    }
}
