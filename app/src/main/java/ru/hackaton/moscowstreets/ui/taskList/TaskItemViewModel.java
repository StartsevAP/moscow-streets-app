package ru.hackaton.moscowstreets.ui.taskList;

import ru.hackaton.moscowstreets.data.models.Task;
import ru.hackaton.moscowstreets.ui.BaseTaskViewModel;
import ru.hackaton.moscowstreets.ui.TaskHandler;

public class TaskItemViewModel extends BaseTaskViewModel {
    private TaskHandler onSelectHandler;

    public TaskItemViewModel(Task task) {
        super(task);
    }

    public void setOnSelectHandler(TaskHandler onSelectHandler) {
        this.onSelectHandler = onSelectHandler;
    }

    public void selectItem() {
        handle(onSelectHandler);
    }
}
