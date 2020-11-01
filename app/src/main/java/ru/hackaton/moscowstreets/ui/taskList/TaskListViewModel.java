package ru.hackaton.moscowstreets.ui.taskList;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ru.hackaton.moscowstreets.data.models.Task;

public class TaskListViewModel extends ViewModel {
    private List<TaskItemViewModel> taskItemViewModelList = new ArrayList<>();
    private TaskListHandlers taskListHandlers;

    public void setTaskListHandlers(TaskListHandlers taskListHandlers) {
        this.taskListHandlers = taskListHandlers;
    }

    public void setTaskList(@NonNull List<Task> taskList) {
        taskItemViewModelList.clear();
        for (Task task : taskList) {
            TaskItemViewModel taskItemViewModel = new TaskItemViewModel(task);
            taskItemViewModel.setOnSelectHandler(taskListHandlers::selectTask);
            taskItemViewModelList.add(taskItemViewModel);
        }
    }

    public List<TaskItemViewModel> getTaskItemViewModelList() {
        return taskItemViewModelList;
    }
}
