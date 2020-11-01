package ru.hackaton.moscowstreets.ui.login;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {
    public MutableLiveData<State> state = new MutableLiveData<>();
    public MutableLiveData<String> userName = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<Boolean> hasError = new MutableLiveData<>();
    public MediatorLiveData<Boolean> allFieldsFilled = new MediatorLiveData<>();

    private LoginHandlers loginHandlers;

    public LoginViewModel() {
        userName.setValue("");
        password.setValue("");
        allFieldsFilled.addSource(userName, s -> allFieldsFilled.postValue(checkFieldsAreFilled()));
        allFieldsFilled.addSource(password, s -> allFieldsFilled.postValue(checkFieldsAreFilled()));
        hasError.setValue(false);
        state.setValue(State.WAITING);
    }

    public void setLoginHandlers(LoginHandlers loginHandlers) {
        this.loginHandlers = loginHandlers;
    }

    public void startRestore() {
        if (loginHandlers != null) {
            state.postValue(State.RESTORING);
            if (loginHandlers.restoreSession()) {
                state.postValue(State.LOGGED_IN);
                loginHandlers.onLoggedIn();
            }
            else {
                state.postValue(State.WAITING);
                this.userName.postValue(loginHandlers.restoreUserName());
            }
        }
    }

    public void login() {
        if (loginHandlers != null && checkFieldsAreFilled()) {
            state.postValue(State.LOGGING);
            if (loginHandlers.login(userName.getValue(), password.getValue())) {
                state.postValue(State.LOGGED_IN);
                loginHandlers.onLoggedIn();
            }
            else {
                state.postValue(State.WAITING);
            }
        }
    }

    private boolean checkFieldsAreFilled() {
        return userName.getValue() != null && !userName.getValue().isEmpty() &&
            password.getValue() != null && !password.getValue().isEmpty();
    }

    public enum State {
        LOGGING,
        RESTORING,
        WAITING,
        LOGGED_IN
    }
}
