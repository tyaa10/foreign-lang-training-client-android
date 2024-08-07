package org.tyaa.training.client.android.state;

import org.tyaa.training.client.android.state.interfaces.IState;
import org.tyaa.training.client.android.models.UserProfileModel;

/**
 * Java-объекты текущего состояния приложения, хранимые в оперативной памяти клиента
 * */
public class InMemoryLocalState implements IState {

    private static String login = null;
    private static UserProfileModel profile = null;

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public UserProfileModel getProfile() {
        return profile;
    }

    @Override
    public void setProfile(UserProfileModel profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return String.format("InMemoryLocalState{login: %s, profile: %s}", login, profile);
    }
}
