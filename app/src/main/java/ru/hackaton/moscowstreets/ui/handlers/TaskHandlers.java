package ru.hackaton.moscowstreets.ui.handlers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import ru.hackaton.moscowstreets.api.TasksApi;
import ru.hackaton.moscowstreets.data.models.Task;
import ru.hackaton.moscowstreets.ui.task.TaskViewModel;
import ru.hackaton.moscowstreets.ui.taskList.TaskListHandlers;

public class TaskHandlers implements TaskListHandlers,
                                     ru.hackaton.moscowstreets.ui.task.TaskHandlers {
    private final TasksApi tasksApi;
    private final ViewModelProvider viewModelProvider;

    private Activity activity;
    private NavController navController;

    private static final int GET_PHOTOS = 100;

    public TaskHandlers(TasksApi tasksApi, ViewModelProvider viewModelProvider) {
        this.tasksApi = tasksApi;
        this.viewModelProvider = viewModelProvider;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setNavController(NavController navController) {
        this.navController = navController;
    }

    @Override
    public void updateTask(Task task) {
        tasksApi.updateTask(task);
    }

    @Override
    public void openGallery() {
        Intent getIntent = new Intent();
        getIntent.setType("image/*");
        getIntent.setAction(Intent.ACTION_GET_CONTENT);
        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                  "image/*");
        Intent chooserIntent = Intent.createChooser(getIntent, "Выбрать изображение");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        activity.startActivityForResult(chooserIntent, GET_PHOTOS);
    }

    @Override
    public void addFiles(Uri uri) {
        TaskViewModel taskViewModel = viewModelProvider.get(TaskViewModel.class);
        Task task = taskViewModel.getTask();
        task.addPhotoUri(uri);
    }

    @Override
    public void selectTask(Task task) {
        TaskViewModel taskViewModel = viewModelProvider.get(TaskViewModel.class);
        taskViewModel.setTask(task);
        navController.navigate();
    }
}
