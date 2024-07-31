package org.tyaa.training.client.android.fragments.profilecreating;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.activities.ProfileCreatingActivity;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.LanguageLevelModel;
import org.tyaa.training.client.android.models.LevelModel;
import org.tyaa.training.client.android.models.UserProfileModel;
import org.tyaa.training.client.android.services.HttpLanguageLevelService;
import org.tyaa.training.client.android.services.interfaces.ILanguageLevelService;
import org.tyaa.training.client.android.utils.UIActions;
import org.tyaa.training.client.android.utils.UIActionsRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LevelStepFragment extends BaseStepFragment {

    private TextView mTitleTextView;
    private AutoCompleteTextView mTargerLevelAutoCompleteTextView;
    private Button mBackButton;
    private Button mNextButton;

    private final ILanguageLevelService mLanguageLevelService;

    private final List<LevelModel> mFilteredLevels;

    public LevelStepFragment() {
        super(R.layout.fragment_levels);
        mLanguageLevelService = new HttpLanguageLevelService();
        mFilteredLevels = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        /* Установка заголовка экрана */
        mTitleTextView = view.findViewById(R.id.profile_creating_fragmentLevel_title_TextView);
        mTitleTextView.setText(mTitleParam);
        /* Обработчик клика для перехода на предыдущий экран */
        mBackButton = view.findViewById(R.id.profile_creating_fragmentLevel_back_Button);
        mBackButton.setOnClickListener(v -> {
            // получение объекта управления фрагментами у текущей Activity
            FragmentManager fragmentManager =
                    LevelStepFragment.this.getActivity().getSupportFragmentManager();
            // немедленно заменить экземпляр текущего фрагмента экземпляром предыдущего
            // (вернуться на предыдущий экран)
            fragmentManager.popBackStackImmediate();
        });
        /* Обработчик клика для перехода на следующий экран */
        mNextButton = view.findViewById(R.id.profile_creating_fragmentLevel_next_Button);
        mNextButton.setOnClickListener(v -> {
            // получение объекта управления фрагментами у текущей Activity
            FragmentManager fragmentManager =
                    LevelStepFragment.this.getActivity().getSupportFragmentManager();
            // создание экземпляра фрагмента необязательного задания имени и аватара для профиля
            // с передачей ему заголовка для отображения - "Name and Avatar"
            Fragment fragment =
                    LevelStepFragment.getInstance(NameAndAvatarStepFragment.class, "Name and Avatar");
            // замена текущего фрагмента следующим
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.activityProfileCreating_step_fragment, fragment)
                    .commit();
            // увеличение номера шага заполнения профиля на единицу
            ((ProfileCreatingActivity) getActivity()).increaseStepNumber();
        });
        /* Подключение фильтруемого источника данных к выпадающему списку выбора уровня */
        mTargerLevelAutoCompleteTextView =
                view.findViewById(R.id.fragmentProfileCreating_targetLevel_autoCompleteTextView);
        final ArrayAdapter levelAdapter =
                new ArrayAdapter(
                        LevelStepFragment.this.getActivity(),
                        android.R.layout.simple_list_item_1,
                        mFilteredLevels
                );
        mTargerLevelAutoCompleteTextView.setAdapter(levelAdapter);
        mLanguageLevelService.getLanguageLevels(new IResultHandler<List<LanguageLevelModel>>() {

            @Override
            public void onFailure(String errorMessage) {
                UIActions.showError(getActivity(), errorMessage);
            }

            @Override
            public void onSuccess(List<LanguageLevelModel> languageLevels) {
                // получение ссылки на заполняемую модель профиля
                UserProfileModel profileModel =
                        ((ProfileCreatingActivity) getActivity()).getProfileModel();
                // заполнить список-источник данных моделями уровней,
                // доступных при выбранной ранее комбинации "родной язык - изучаемый язык"
                fillLevelList(
                        languageLevels,
                        mFilteredLevels,
                        profileModel.getNativeLanguageName(),
                        profileModel.getLearningLanguageName()
                );
            }
        });
        /* Обработчик события выбора уровня в выпадающем списке */
        mTargerLevelAutoCompleteTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // получить текст выбранного уровня
                String selectedLangugeName = s.toString();
                // установить текст выбранного родного языка в модель профиля
                ((ProfileCreatingActivity) getActivity())
                        .getProfileModel().setNativeLanguageName(selectedLangugeName);
                // через переходник уведомить выпадающий список уровней,
                // что данные в источнике изменились, и требуется перерисовка
                UIActionsRunner.run(levelAdapter::notifyDataSetChanged);
                // сделать активной кнопку перехода на следующий экран
                mNextButton.setEnabled(true);
            }
        });

    }

    /**
     * Заполнение списка моделей для выбора уровня данными из списка комбинаций языков и уровней изучения
     * @param languageLevelModels список доступных комбинаций языков и уровней изучения
     * @param levelModels заполняемый список уровней
     * @param nativeLanguageName родной язык для отбора комбинаций
     * @param learningLanguageName изучаемый язык для отбора комбинаций
     * */
    private static void fillLevelList(
            List<LanguageLevelModel> languageLevelModels,
            List<LevelModel> levelModels,
            String nativeLanguageName,
            String learningLanguageName
    ) {
        // очистить список моделей уровней, если он не пуст
        if(levelModels.size() > 0) {
            levelModels.clear();
        }
        // последовательно перебрать все доступные комбинации языков и уровней изучения
        languageLevelModels.forEach(languageLevelModel -> {
            // заполнить список уровнями, встречающимися в комбинациях,
            // и соответствующими заданным родному и изучаемому языкам
            if(languageLevelModel.getNativeLanguage().getName().equals(nativeLanguageName)
                && languageLevelModel.getLearningLanguage().getName().equals(learningLanguageName)) {
                levelModels.add(languageLevelModel.getLevel());
            }
        });
    }
}