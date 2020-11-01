package ru.hackaton.moscowstreets.app;

import android.content.SharedPreferences;

import ru.hackaton.moscowstreets.api.AuthenticationErrorException;
import ru.hackaton.moscowstreets.api.InvalidTokenException;
import ru.hackaton.moscowstreets.api.SessionOutdatedException;
import ru.hackaton.moscowstreets.api.AuthApi;

public class UserService {
    private final SharedPreferences preferences;
    private final AuthApi authApi;
    private String token;
    private String userName;

    public UserService(SharedPreferences preferences, AuthApi authApi) {
        this.preferences = preferences;
        this.authApi = authApi;
    }

    public String getToken() {
        return token;
    }

    public String getUserName() {
        return userName;
    }

    public void restoreUserName() {
        this.userName = preferences.getString(PreferencesKeys.USER_NAME, null);
    }

    public void login(String userName, String password) throws AuthenticationErrorException {
        this.token = authApi.login(userName, password);
        this.userName = userName;
        preferences
                .edit()
                .putString(PreferencesKeys.TOKEN, token)
                .putString(PreferencesKeys.USER_NAME, userName)
                .apply();
    }

    public void restoreSession() throws SessionOutdatedException, InvalidTokenException {
        String token = preferences.getString(PreferencesKeys.TOKEN, null);
        String userName = preferences.getString(PreferencesKeys.USER_NAME, null);
        if (token == null || userName == null) {
            throw new InvalidTokenException();
        }
        try {
            authApi.checkToken(token);
            this.token = token;
            this.userName = userName;
        } catch (AuthenticationErrorException e) {
            throw new SessionOutdatedException();
        }
    }

    public void logout() {
        this.token = null;
        this.userName = null;
        preferences
                .edit()
                .remove(PreferencesKeys.TOKEN)
                .remove(PreferencesKeys.USER_NAME)
                .apply();
    }

    private static class PreferencesKeys {
        static final String TOKEN = "token";
        static final String USER_NAME = "userName";
    }
}
