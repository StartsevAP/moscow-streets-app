package ru.hackaton.moscowstreets.ui.taskList;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.hackaton.moscowstreets.R;
import ru.hackaton.moscowstreets.databinding.FragmentTaskItemBinding;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private final List<TaskItemViewModel> tasks = new ArrayList<>();

    public void setTasks(List<TaskItemViewModel> tasks) {
        this.tasks.clear();
        this.tasks.addAll(tasks);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        FragmentTaskItemBinding binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_task_item, parent, false);
        binding.setLifecycleOwner((LifecycleOwner) parent.getContext());
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final FragmentTaskItemBinding binding;

        public ViewHolder(FragmentTaskItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(TaskItemViewModel taskItemViewModel) {
            binding.setViewModel(taskItemViewModel);
        }
    }
}
