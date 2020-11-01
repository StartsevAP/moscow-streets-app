package ru.hackaton.moscowstreets.ui.taskList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ru.hackaton.moscowstreets.R;
import ru.hackaton.moscowstreets.databinding.FragmentTaskListBinding;

public class TaskListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentTaskListBinding binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_task_list, container, false);
        binding.setLifecycleOwner(this);
        TaskAdapter taskAdapter = new TaskAdapter();
        TaskListViewModel taskListViewModel = new ViewModelProvider(getActivity()).get(
            TaskListViewModel.class);
        taskAdapter.setTasks(taskListViewModel.getTaskItemViewModelList());
        binding.setAdapter(taskAdapter);
        return binding.getRoot();
    }
}
