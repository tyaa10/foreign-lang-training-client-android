package org.tyaa.training.client.android.fragments.lessons.word;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.models.WordModel;
import org.tyaa.training.client.android.services.HttpWordService;
import org.tyaa.training.client.android.services.interfaces.IWordService;
import org.tyaa.training.client.android.state.InMemoryLocalState;
import org.tyaa.training.client.android.state.interfaces.IState;
import org.tyaa.training.client.android.utils.ImageConverter;
import org.tyaa.training.client.android.utils.UIActions;
import org.tyaa.training.client.android.utils.UIActionsRunner;

/**
 * Класс фрагмента изучения слова
 * */
public class WordStudyFragment extends Fragment {

    private final IWordService mWordService = new HttpWordService();
    private final IState mState = new InMemoryLocalState();

    private Long mLessonId;
    private Integer mCurrentWordIndex = 0;

    private View mWordStudyFragmentView;

    private ImageView mNativeLanguageFlagImageView;
    private ImageView mLearningLanguageFlagImageView;
    private ImageView mWordImageView;
    private TextView mWordTextView;
    private TextView mTranslationTextView;
    private ImageView mNextImageView;

    public WordStudyFragment() {
        // подключение представления к объекту логики фрагмента
        super(R.layout.fragment_education_process_word_study);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // инициализация объекта управления представлением фрагмента
        mWordStudyFragmentView =
                inflater.inflate(R.layout.fragment_education_process_word_study, container, false);

        /* инициализация объектов управления виджетами, расположенными на представлении фрагмента */
        mNativeLanguageFlagImageView =
                mWordStudyFragmentView.findViewById(R.id.activityMain_fragmentEducationProcessWordStudy_nativeLanguageFlag_ImageView);
        mLearningLanguageFlagImageView =
                mWordStudyFragmentView.findViewById(R.id.activityMain_fragmentEducationProcessWordStudy_learningLanguageFlag_ImageView);
        mWordImageView =
                mWordStudyFragmentView.findViewById(R.id.activityMain_fragmentEducationProcessWordStudy_wordImage_ImageView);
        mWordTextView =
                mWordStudyFragmentView.findViewById(R.id.activityMain_fragmentEducationProcessWordStudy_word_TextView);
        mTranslationTextView =
                mWordStudyFragmentView.findViewById(R.id.activityMain_fragmentEducationProcessWordStudy_translation_TextView);
        mNextImageView =
                mWordStudyFragmentView.findViewById(R.id.activityMain_fragmentEducationProcessWordStudy_next_ImageView);

        /* установка обработчиков событий виджетов, расположенных на представлении фрагмента */
        mNextImageView.setOnClickListener(v -> {
            showNextWord(mCurrentWordIndex++);
        });

        return mWordStudyFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // получение идентификатора выбранного урока от фрагмента списка уроков,
        // логикой которого был вызван переход на текущий фрагмент
        mLessonId = WordStudyFragmentArgs.fromBundle(getArguments()).getLessonId();
        // Log.d("lessonId", String.valueOf(lessonId));
        // UIActions.showInfo(getActivity(), String.valueOf(lessonId));
        mNativeLanguageFlagImageView.setImageResource(mState.getNativeLanguageFlag());
        mLearningLanguageFlagImageView.setImageResource(mState.getLearningLanguageFlag());

        // очистка списка моделей слов для текущего урока в объекте состояния приложения
        mState.clearCurrentLessonWords();
        // попытка получить с сервера список моделей слов для текущего урока
        mWordService.getWords(
                mLessonId,
                new IResponseHandler() {

                    @Override
                    public void onSuccess() {
                        UIActionsRunner.run(() -> showNextWord(mCurrentWordIndex++));
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // отобразить сообщение об ошибке
                        UIActions.showError(getActivity(), errorMessage);
                    }
                }
        );
    }

    private void showNextWord(Integer index) {
        if (index < mState.getCurrentLessonWords().size()) {
            WordModel wordModel = mState.getCurrentLessonWords().get(index);
            mWordImageView.setImageBitmap(ImageConverter.base64ToBitmap(getActivity(), wordModel.getImage()));
            mWordTextView.setText(wordModel.getWord());
            mTranslationTextView.setText(wordModel.getTranslation());
        } else {
            // подготовка действия перехода к фрагменту диалога выбора:
            // начать проверку знаний слов или вернуться в меню выбора урока
            final WordStudyFragmentDirections.NavigateToFragmentEducationalProcessWordStudyDialog action =
                WordStudyFragmentDirections.navigateToFragmentEducationalProcessWordStudyDialog(mLessonId);
            // выполнение подготовленного действия перехода
            Navigation.findNavController(mWordStudyFragmentView).navigate(action);
        }
    }
}
