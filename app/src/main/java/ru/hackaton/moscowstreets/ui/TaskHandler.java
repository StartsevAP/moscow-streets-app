package ru.hackaton.moscowstreets.ui;

import ru.hackaton.moscowstreets.data.models.Task;

public interface TaskHandler {
    void handle(Task task);
}
