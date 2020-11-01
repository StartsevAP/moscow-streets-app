package ru.hackaton.moscowstreets;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import ru.hackaton.moscowstreets.app.Application;
import ru.hackaton.moscowstreets.ui.handlers.TaskHandlers;
import ru.hackaton.moscowstreets.ui.taskList.TaskListViewModel;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private ViewModelProvider viewModelProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModelProvider = new ViewModelProvider(this);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    }

    private void createHandlers() {
        TaskHandlers taskHandlers = new TaskHandlers(
            ((Application) getApplication()).getApiAggregator().getTasksApi(), viewModelProvider);
        viewModelProvider.get(TaskListViewModel.class).setTaskListHandlers(taskHandlers);
    }
}
