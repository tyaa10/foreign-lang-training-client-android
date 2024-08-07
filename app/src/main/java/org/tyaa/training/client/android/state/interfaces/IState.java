package org.tyaa.training.client.android.state.interfaces;

import org.tyaa.training.client.android.models.UserProfileModel;

/**
 * Абстракция текущего состояния приложения
 * */
public interface IState {

    String getLogin();
    void setLogin(String login);

    UserProfileModel getProfile();
    void setProfile(UserProfileModel profile);
}
