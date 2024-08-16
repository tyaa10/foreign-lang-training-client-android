package org.tyaa.training.client.android.state.interfaces;

import org.tyaa.training.client.android.models.UserProfileModel;
import org.tyaa.training.client.android.models.WordModel;

import java.util.List;

/**
 * Абстракция текущего состояния приложения
 * */
public interface IState {

    String getLogin();
    void setLogin(String login);

    UserProfileModel getProfile();
    void setProfile(UserProfileModel profile);

    Integer getNativeLanguageFlag();
    // void setNativeLanguageFlag(String languageName);

    Integer getLearningLanguageFlag();
    // void setLearningLanguageFlag(String languageName);

    List<WordModel> getCurrentLessonWords();
    void addCurrentLessonWords(List<WordModel> words);
    void clearCurrentLessonWords();
}
