package ru.hackaton.moscowstreets.ui.login;

import androidx.annotation.Nullable;

public interface LoginHandlers {
    boolean login(String userName, String password);

    void logout();

    boolean restoreSession();

    @Nullable
    String restoreUserName();

    void onLoggedIn();
}
