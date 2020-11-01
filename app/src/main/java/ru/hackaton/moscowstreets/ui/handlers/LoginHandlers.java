package ru.hackaton.moscowstreets.ui.handlers;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.Nullable;

import ru.hackaton.moscowstreets.MainActivity;
import ru.hackaton.moscowstreets.api.ApiAggregator;
import ru.hackaton.moscowstreets.api.AuthenticationErrorException;
import ru.hackaton.moscowstreets.api.InvalidTokenException;
import ru.hackaton.moscowstreets.api.SessionOutdatedException;
import ru.hackaton.moscowstreets.app.UserService;

public class LoginHandlers implements ru.hackaton.moscowstreets.ui.login.LoginHandlers {
    private final UserService userService;
    private final ApiAggregator apiAggregator;

    private Activity activity;

    public LoginHandlers(UserService userService, ApiAggregator apiAggregator) {
        this.userService = userService;
        this.apiAggregator = apiAggregator;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean login(String userName, String password) {
        try {
            userService.login(userName, password);
            return true;
        }
        catch (AuthenticationErrorException e) {
            return false;
        }
    }

    @Override
    public void logout() {
        userService.logout();
    }

    @Override
    public boolean restoreSession() {
        try {
            userService.restoreSession();
            return true;
        }
        catch (SessionOutdatedException | InvalidTokenException e) {
            return false;
        }
    }

    @Nullable
    @Override
    public String restoreUserName() {
        userService.restoreUserName();
        return userService.getUserName();
    }

    @Override
    public void onLoggedIn() {
        apiAggregator.setToken(userService.getToken());
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }
}
