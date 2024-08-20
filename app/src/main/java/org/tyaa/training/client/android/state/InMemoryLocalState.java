package org.tyaa.training.client.android.state;

import com.blongho.country_data.World;

import org.tyaa.training.client.android.models.WordModel;
import org.tyaa.training.client.android.state.interfaces.IState;
import org.tyaa.training.client.android.models.UserProfileModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Java-объекты текущего состояния приложения, хранимые в оперативной памяти клиента
 * */
public class InMemoryLocalState implements IState {

    private static String login = null;
    private static UserProfileModel profile = null;
    private static Integer nativeLanguageFlag = null;
    private static Integer learningLanguageFlag = null;
    private static List<WordModel> currentLessonWords = null;

    private static Map<String, String> languageFlags =
            Map.of(
                    "English", "gb",
                    "Русский", "ru"
            );

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
        setNativeLanguageFlag(this.profile.getNativeLanguageName());
        setLearningLanguageFlag(this.profile.getLearningLanguageName());
    }

    @Override
    public Integer getNativeLanguageFlag() {
        return nativeLanguageFlag;
    }

    // @Override
    private void setNativeLanguageFlag(String languageName) {
        nativeLanguageFlag =
                World.getFlagOf(languageFlags.get(languageName));
    }

    @Override
    public Integer getLearningLanguageFlag() {
        return learningLanguageFlag;
    }

    // @Override
    private void setLearningLanguageFlag(String languageName) {
        learningLanguageFlag =
                World.getFlagOf(languageFlags.get(languageName));
    }

    @Override
    public List<WordModel> getCurrentLessonWords() {
        return currentLessonWords;
    }

    @Override
    public void addCurrentLessonWords(List<WordModel> words) {
        // создание пустого списка для моделей при первом вызове данного метода
        if (currentLessonWords == null) {
            currentLessonWords = new ArrayList<>();
        }
        // упорядочение моделей слов по их номерам последовательности
        Collections.sort(words, new Comparator<WordModel>() {
            @Override
            public int compare(WordModel o1, WordModel o2) {
                return o1.getSequenceNumber().compareTo(o2.getSequenceNumber());
            }
        });
        // копирование ссылок на объекты моделей слов в список слов текущего состояния приложения
        currentLessonWords.addAll(words);
    }

    @Override
    public void clearCurrentLessonWords() {
        // если список моделей слов для изучения существует и не пуст
        if(currentLessonWords != null && currentLessonWords.size() > 0) {
            // удалить все модели из списка
            currentLessonWords.clear();
        }
    }

    @Override
    public String toString() {
        return String.format("InMemoryLocalState{login: %s, profile: %s}", login, profile);
    }
}
