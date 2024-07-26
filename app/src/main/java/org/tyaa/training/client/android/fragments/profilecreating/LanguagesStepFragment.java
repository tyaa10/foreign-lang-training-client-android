package org.tyaa.training.client.android.fragments.profilecreating;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.adapters.RoleAdapter;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.LanguageModel;
import org.tyaa.training.client.android.models.RoleModel;
import org.tyaa.training.client.android.services.HttpLanguageService;
import org.tyaa.training.client.android.services.interfaces.ILanguageService;
import org.tyaa.training.client.android.utils.UIActions;
import org.tyaa.training.client.android.utils.UIActionsRunner;

import java.util.ArrayList;
import java.util.List;

public class LanguagesStepFragment extends BaseStepFragment {

    private TextView mTitleTextView;
    private AutoCompleteTextView mNativeLanguageAutoCompleteTextView;
    private AutoCompleteTextView mLearningLanguageAutoCompleteTextView;
    private Button mBackButton;
    private Button mNextButton;

    private final ILanguageService mLanguageService;

    public LanguagesStepFragment() {
        super(R.layout.fragment_languages);
        mLanguageService = new HttpLanguageService();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // установка заголовка экрана
        mTitleTextView = view.findViewById(R.id.profile_creating_fragmentLanguages_title_TextView);
        mTitleTextView.setText(mTitleParam);
        // обработчик клика для перехода на следующий экран
        mNextButton = view.findViewById(R.id.profile_creating_fragmentLanguages_next_Button);
        mNextButton.setOnClickListener(v -> {
            FragmentManager fragmentManager =
                    LanguagesStepFragment.this.getActivity().getSupportFragmentManager();
            Fragment fragment =
                    LevelStepFragment.getInstance(LevelStepFragment.class, "Level");
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.activityProfileCreating_step_fragment, fragment)
                    .commit();
        });
        // подключение данных к выпадающим спискам выбора языков
        mNativeLanguageAutoCompleteTextView =
                view.findViewById(R.id.fragmentProfileCreating_nativeLanguage_autoCompleteTextView);
        mLearningLanguageAutoCompleteTextView =
                view.findViewById(R.id.fragmentProfileCreating_learningLanguage_autoCompleteTextView);
        final List<LanguageModel> languages = new ArrayList<>();
        final ArrayAdapter languageAdapter =
                new ArrayAdapter(LanguagesStepFragment.this.getActivity(), android.R.layout.simple_list_item_1, languages);
        mNativeLanguageAutoCompleteTextView.setAdapter(languageAdapter);
        mLearningLanguageAutoCompleteTextView.setAdapter(languageAdapter);
        mLanguageService.getLanguages(new IResultHandler<List<LanguageModel>>() {
            @Override
            public void onSuccess(List<LanguageModel> result) {
                if(languages.size() > 0) {
                    languages.clear();
                }
                languages.addAll(result);
                UIActionsRunner.run(languageAdapter::notifyDataSetChanged);
            }

            @Override
            public void onFailure(String errorMessage) {
                UIActions.showError(getActivity(), errorMessage);
            }
        });
        // обработчики заполнения полей ввода
        mNativeLanguageAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO исключать выбранный пункт из второго выпадающего списка
            }
        });
    }
}