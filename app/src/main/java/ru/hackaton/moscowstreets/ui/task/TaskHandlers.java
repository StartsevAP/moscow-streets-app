package ru.hackaton.moscowstreets.ui.task;

import android.net.Uri;

import ru.hackaton.moscowstreets.data.models.Task;

public interface TaskHandlers {
    void updateTask(Task task);
    void openGallery();
    void addFiles(Uri uri);
}
