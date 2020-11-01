package ru.hackaton.moscowstreets.ui.login;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import ru.hackaton.moscowstreets.R;
import ru.hackaton.moscowstreets.app.Application;
import ru.hackaton.moscowstreets.databinding.ActivityLoginBinding;
import ru.hackaton.moscowstreets.ui.handlers.LoginHandlers;

public class LoginActivity extends FragmentActivity {
    private LoginHandlers loginHandlers;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this,
                                                                      R.layout.activity_login);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        loginHandlers = new LoginHandlers(
            ((Application) getApplication()).getUserService(),
            ((Application) getApplication()).getApiAggregator());
        viewModel.setLoginHandlers(loginHandlers);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loginHandlers.setActivity(this);
        viewModel.startRestore();
    }
}
