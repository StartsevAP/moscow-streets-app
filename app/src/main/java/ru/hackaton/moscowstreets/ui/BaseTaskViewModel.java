package ru.hackaton.moscowstreets.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.hackaton.moscowstreets.data.models.Task;

public class BaseTaskViewModel extends ViewModel {
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<Integer> category = new MutableLiveData<>();
    public MutableLiveData<Task.Status> status = new MutableLiveData<>();
    public MutableLiveData<Integer> rating = new MutableLiveData<>();
    public MutableLiveData<String> commentary = new MutableLiveData<>();

    protected Task task;

    public BaseTaskViewModel(Task task) {
        setTask(task);
    }

    public BaseTaskViewModel() {
    }

    public void setTask(Task task) {
        this.task = task;
        name.postValue(task.getName());
        category.postValue(task.getCategory());
        status.postValue(task.getStatus());
        rating.postValue(task.getRating());
        commentary.postValue(task.getCommentary());
    }

    protected void handle(TaskHandler taskHandler) {
        if (taskHandler != null && task != null) {
            taskHandler.handle(task);
        }
    }

    public Task getTask() {
        return task;
    }
}
