package org.tyaa.training.client.android.fragments.profilecreating;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.activities.ProfileCreatingActivity;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.LanguageLevelModel;
import org.tyaa.training.client.android.models.LanguageModel;
import org.tyaa.training.client.android.models.UserProfileModel;
import org.tyaa.training.client.android.services.HttpLanguageLevelService;
import org.tyaa.training.client.android.services.interfaces.ILanguageLevelService;
import org.tyaa.training.client.android.utils.UIActions;
import org.tyaa.training.client.android.utils.UIActionsRunner;

import java.util.ArrayList;
import java.util.List;

public class LanguagesStepFragment extends BaseStepFragment {

    private TextView mTitleTextView;
    private AutoCompleteTextView mNativeLanguageAutoCompleteTextView;
    private AutoCompleteTextView mLearningLanguageAutoCompleteTextView;
    private Button mNextButton;

    private final ILanguageLevelService mLanguageLevelService;

    private final List<LanguageModel> mNativeLanguages;
    private final List<LanguageModel> mLearningLanguages;
    private final List<LanguageModel> mFilteredNativeLanguages;
    private final List<LanguageModel> mFilteredLearningLanguages;

    public LanguagesStepFragment() {
        super(R.layout.fragment_profile_creating_languages);
        mLanguageLevelService = new HttpLanguageLevelService();
        // создание пустых списков моделей языков - источников данных
        mNativeLanguages = new ArrayList<>();
        mLearningLanguages = new ArrayList<>();
        mFilteredNativeLanguages = new ArrayList<>();
        mFilteredLearningLanguages = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /* установка заголовка экрана */
        mTitleTextView = view.findViewById(R.id.activityProfileCreating_fragmentLanguages_title_TextView);
        mTitleTextView.setText(mTitleParam);
        /* обработчик клика для перехода на следующий экран */
        mNextButton = view.findViewById(R.id.activityProfileCreating_fragmentLanguages_next_Button);
        mNextButton.setOnClickListener(v -> {
            // получение объекта управления фрагментами у текущей Activity
            FragmentManager fragmentManager =
                    LanguagesStepFragment.this.getActivity().getSupportFragmentManager();
            // создание экземпляра фрагмента выбра уровня изучения языка для профиля
            // с передачей ему заголовка для отображения - "Level"
            Fragment fragment =
                    LevelStepFragment.getInstance(LevelStepFragment.class, "Level");
            // замена текущего фрагмента следующим
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.activityProfileCreating_step_fragment, fragment)
                    .addToBackStack(null)
                    .commit();
            // увеличение номера шага заполнения профиля на единицу
            ((ProfileCreatingActivity) getActivity()).increaseStepNumber();
        });
        /* подключение данных к выпадающим спискам выбора языков */
        // 1. получение объектов доступа к выпадающим спискам выбора языков
        mNativeLanguageAutoCompleteTextView =
                view.findViewById(R.id.activityProfileCreating_fragmentLanguages_nativeLanguage_autoCompleteTextView);
        mLearningLanguageAutoCompleteTextView =
                view.findViewById(R.id.activityProfileCreating_fragmentLanguages_learningLanguage_autoCompleteTextView);
        // 2. создание переходников для подключения списков моделей данных к элементам управления UI
        final ArrayAdapter nativeLanguageAdapter =
                new ArrayAdapter(
                        LanguagesStepFragment.this.getActivity(),
                        android.R.layout.simple_list_item_1,
                        mFilteredNativeLanguages
                );
        final ArrayAdapter learningLanguageAdapter =
                new ArrayAdapter(
                        LanguagesStepFragment.this.getActivity(),
                        android.R.layout.simple_list_item_1,
                        mFilteredLearningLanguages
                );
        // 3. подключение списков моделей языков к выпадающим спискам выбора языков через переходники
        mNativeLanguageAutoCompleteTextView.setAdapter(nativeLanguageAdapter);
        mLearningLanguageAutoCompleteTextView.setAdapter(learningLanguageAdapter);
        // 4. получение списка комбинаций языков и уровней с сервера и заполнение списков языков
        // полученными данными
        mLanguageLevelService.getLanguageLevels(new IResultHandler<List<LanguageLevelModel>>() {

            @Override
            public void onSuccess(List<LanguageLevelModel> languageLevels) {
                // заполнить полные списки-источники данных моделями языков
                fillLanguageLists(languageLevels, mNativeLanguages, mLearningLanguages);
                // скопировать ссылки на все модели из полных списков в фильтрованные,
                // которые через переходники подключены к выпадающим спискам выбора
                mFilteredNativeLanguages.addAll(mNativeLanguages);
                mFilteredLearningLanguages.addAll(mLearningLanguages);
                // через переходники уведомить выпадающие списки выбора языков о том,
                // что наборы данных в списках-источниках изменились
                UIActionsRunner.run(nativeLanguageAdapter::notifyDataSetChanged);
                UIActionsRunner.run(learningLanguageAdapter::notifyDataSetChanged);
            }

            @Override
            public void onFailure(String errorMessage) {
                UIActions.showError(getActivity(), errorMessage);
            }
        });
        /* обработчики событий выбора языков в выпадающих списках */
        mNativeLanguageAutoCompleteTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // получить текст выбранного родного языка
                String selectedLangugeName = s.toString();
                // установить текст выбранного родного языка в модель профиля
                ((ProfileCreatingActivity) getActivity())
                        .getProfileModel().setNativeLanguageName(selectedLangugeName);
                // исключить выбранный пункт из фильтруемого списка изучаемых языков
                excludeSelectedLanguage(mLearningLanguages, mFilteredLearningLanguages, selectedLangugeName);
                // через переходник уведомить выпадающий список изучаемых языков,
                // что данные в источнике изменились, и требуется перерисовка
                UIActionsRunner.run(learningLanguageAdapter::notifyDataSetChanged);
                // пересмотреть состояние кнопки перехода на следующий экран
                // (сделать активной или нет)
                reconsiderNextButtonState();
            }
        });
        mLearningLanguageAutoCompleteTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // получить текст выбранного родного языка
                String selectedLangugeName = s.toString();
                // установить текст выбранного родного языка в модель профиля
                ((ProfileCreatingActivity) getActivity())
                        .getProfileModel().setLearningLanguageName(selectedLangugeName);
                // исключить выбранный пункт из фильтруемого списка изучаемых языков
                excludeSelectedLanguage(mNativeLanguages, mFilteredNativeLanguages, selectedLangugeName);
                // через переходник уведомить выпадающий список выбора родного языка,
                // что данные в источнике изменились, и требуется перерисовка
                UIActionsRunner.run(nativeLanguageAdapter::notifyDataSetChanged);
                // пересмотреть состояние кнопки перехода на следующий экран
                // (сделать активной или нет)
                reconsiderNextButtonState();
            }
        });
    }

    /**
     * Заполнение полных списков выбора языков данными из списка комбинаций языков и уровней изучения
     * @param languageLevelModels список доступных комбинаций языков и уровней изучения
     * @param nativeLanguages заполняемый полный список для выбора родного языка
     * @param learningLanguages заполняемый полный список для выбора изучаемого языка
     * */
    private static void fillLanguageLists(
            List<LanguageLevelModel> languageLevelModels,
            List<LanguageModel> nativeLanguages,
            List<LanguageModel> learningLanguages
    ) {
        // очистить списки моделей, если они не пусты
        if(nativeLanguages.size() > 0) {
            nativeLanguages.clear();
        }
        if(learningLanguages.size() > 0) {
            learningLanguages.clear();
        }
        // последовательно перебрать все доступные комбинации языков и уровней изучения
        languageLevelModels.forEach(languageLevelModel -> {
            // заполнить список выбора родного языка всеми языками, входящими в комбинации
            // в качестве родного языка
            nativeLanguages.add(languageLevelModel.getNativeLanguage());
            // заполнить список выбора изучаемого языка всеми языками, входящими в комбинации
            // в качестве изучаемого языка
            learningLanguages.add(languageLevelModel.getLearningLanguage());
        });
    }

    /**
     * Исключение из фильтруемого списка одной модели
     * @param languages полный список
     * @param filteredLanguages фильтруемый список
     * @param excludedLanguageName название исключаемого языка
     * */
    private static void excludeSelectedLanguage(
            List<LanguageModel> languages,
            List<LanguageModel> filteredLanguages,
            String excludedLanguageName
    ) {
        // очистка фильтруемого списка от всех ссылок
        filteredLanguages.clear();
        // перебор полного списка моделей языков
        languages.forEach(language -> {
            // если текущая модель не содержит названия исключаемого языка
            if (!language.getName().equals(excludedLanguageName)) {
                // ссылка на модель копируется из полного списка в фильтруемый
                filteredLanguages.add(language);
            }
        });
    }

    /**
     * Пересмотр состояния кнопки перехода на следующий экран (сделать активной или нет)
     * */
    private void reconsiderNextButtonState() {
        // получение ссылки на заполняемую модель профиля
        UserProfileModel profileModel =
            ((ProfileCreatingActivity) getActivity()).getProfileModel();
        // если заполнены поля родного и изучаемого языков
        if (profileModel.getNativeLanguageName() != null && profileModel.getLearningLanguageName() != null) {
            // сделать кнопку перехода на следующий экран активной
            mNextButton.setEnabled(true);
        }
    }
}