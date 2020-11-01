package ru.hackaton.moscowstreets.api;

public class ApiAggregator {
    private final AuthApi authApi;
    private final TasksApi tasksApi;

    public ApiAggregator() {
        authApi = new AuthApi();
        tasksApi = new TasksApi();
    }

    public AuthApi getAuthApi() {
        return authApi;
    }

    public TasksApi getTasksApi() {
        return tasksApi;
    }

    public void setToken(String token) {

    }
}
