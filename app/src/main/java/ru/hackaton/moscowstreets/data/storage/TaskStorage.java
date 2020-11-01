package ru.hackaton.moscowstreets.data.storage;

import androidx.annotation.Nullable;

import java.util.List;

import ru.hackaton.moscowstreets.api.TasksApi;
import ru.hackaton.moscowstreets.data.models.Task;
import ru.hackaton.moscowstreets.storage.SynchronizedStorage;

public class TaskStorage extends SynchronizedStorage<List<Task>> {
    private final TasksApi tasksApi;

    public TaskStorage(long period, long maxPauseInterval, TasksApi tasksApi) {
        super(period, maxPauseInterval);
        getRefreshRunner().setThreadName("TaskStorageThread");
        this.tasksApi = tasksApi;
    }

    @Nullable
    public Task getTask(@Nullable Integer taskId) {
        if (taskId != null) {
            List<Task> taskList = getValue();
            if (taskList != null) {
                for (Task task : taskList) {
                    if (task.getId() == taskId) {
                        return task;
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected List<Task> fetchValue() throws Exception {
        List<Task> taskList = tasksApi.getTasks();
        if (taskList != null) {
            return taskList;
        }
        return localValue;
    }
}
