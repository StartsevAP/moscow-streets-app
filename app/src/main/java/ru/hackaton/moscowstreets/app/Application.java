package ru.hackaton.moscowstreets.app;

import ru.hackaton.moscowstreets.api.ApiAggregator;
import ru.hackaton.moscowstreets.data.storage.TaskStorage;

public class Application extends android.app.Application {
    private final UserService userService;
    private final ApiAggregator apiAggregator;
    private final TaskStorage taskStorage;

    public Application() {
        apiAggregator = new ApiAggregator();
        userService = new UserService(getSharedPreferences("user", MODE_PRIVATE),
                                      apiAggregator.getAuthApi());
        taskStorage = new TaskStorage(30000, 30000, apiAggregator.getTasksApi());
    }

    public UserService getUserService() {
        return userService;
    }

    public ApiAggregator getApiAggregator() {
        return apiAggregator;
    }

    public TaskStorage getTaskStorage() {
        return taskStorage;
    }
}
