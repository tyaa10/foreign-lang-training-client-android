package org.tyaa.training.client.android.fragments.lessons.word;

import android.os.Bundle;
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
import org.tyaa.training.client.android.utils.Player;
import org.tyaa.training.client.android.utils.UIActions;
import org.tyaa.training.client.android.utils.UIActionsRunner;

import java.io.IOException;

/**
 * Класс фрагмента изучения слова
 * */
public class WordStudyFragment extends Fragment {

    // Экземпляр службы изучения слов
    private final IWordService mWordService = new HttpWordService();
    // Доступ к Java-объектам текущего состояния приложения, хранимым в оперативной памяти клиента
    private final IState mState = new InMemoryLocalState();

    // Поле идентификатора текущего урока
    private Long mLessonId;
    // Индекс текущего слова
    private Integer mCurrentWordIndex = 0;

    // Поле для объекта управления представлением фрагмента
    private View mWordStudyFragmentView;

    // Поля для объектов управления виджетами, расположенными на представлении фрагмента
    private ImageView mNativeLanguageFlagImageView;
    private ImageView mLearningLanguageFlagImageView;
    private ImageView mWordImageView;
    private TextView mWordTextView;
    private TextView mTranslationTextView;
    private ImageView mTranslationPronunciationImageView;
    private ImageView mNextImageView;

    public WordStudyFragment() {
        // Подключение представления к объекту логики фрагмента
        super(R.layout.fragment_education_process_word_study);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Инициализация объекта управления представлением фрагмента
        mWordStudyFragmentView =
                inflater.inflate(R.layout.fragment_education_process_word_study, container, false);

        // Инициализация объектов управления виджетами, расположенными на представлении фрагмента
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
        mTranslationPronunciationImageView =
                mWordStudyFragmentView.findViewById(R.id.activityMain_fragmentEducationProcessWordStudy_translation_pronunciation_ImageView);
        mNextImageView =
                mWordStudyFragmentView.findViewById(R.id.activityMain_fragmentEducationProcessWordStudy_next_ImageView);

        /* установка обработчиков событий виджетов, расположенных на представлении фрагмента */
        // воспроизведение произношения перевода слова
        mTranslationPronunciationImageView.setOnClickListener(v -> {
            try {
                Player.playAudio(mTranslationPronunciationImageView.getTag().toString());
            } catch (IOException e) {
                UIActions.showError(getActivity(), getString(R.string.message_error_failed_to_play_audio));
            }

        });
        // вывод данных следующего изучаемого слова в текущий фрагмент
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

        // установка флагов языков на представление
        mNativeLanguageFlagImageView.setImageResource(mState.getNativeLanguageFlag());
        mLearningLanguageFlagImageView.setImageResource(mState.getLearningLanguageFlag());

        // очистка списка моделей слов для текущего урока в объекте состояния приложения
        mState.clearCurrentLessonWords();

        // отобразить бесконечный прогресс
        UIActions.showInfinityProgressToast(getActivity());

        // попытка получить с сервера список моделей слов для текущего урока
        mWordService.getWords(
                mLessonId,
                new IResponseHandler() {
                    // если получен положительный ответ от сервера
                    @Override
                    public void onSuccess() {
                        // скрыть бесконечный прогресс
                        UIActions.closeInfinityProgressToast();
                        // установить на представление данные очередного слова
                        // и затем увеличить счётчик отображённых слов на единицу
                        UIActionsRunner.run(() -> showNextWord(mCurrentWordIndex++));
                    }
                    // если запрос не выполнен или получен отрицательный ответ от сервера
                    @Override
                    public void onFailure(String errorMessage) {
                        // скрыть бесконечный прогресс
                        UIActions.closeInfinityProgressToast();
                        // отобразить сообщение об ошибке
                        UIActions.showError(getActivity(), errorMessage);
                    }
                }
        );
    }

    /**
     * Действия при попытке перехода к следующему изучаемому слову
     * */
    private void showNextWord(Integer index) {
        // если модели изучаемых слов в списке ещё не закончились
        if (index < mState.getCurrentLessonWords().size()) {
            // получение данных очередного слова для отображения
            WordModel wordModel = mState.getCurrentLessonWords().get(index);
            // выод данных текущего слова на виджеты представления
            mWordImageView.setImageBitmap(ImageConverter.base64ToBitmap(getActivity(), wordModel.getImage()));
            mWordTextView.setText(wordModel.getWord());
            mTranslationTextView.setText(wordModel.getTranslation());
            mTranslationPronunciationImageView.setTag(wordModel.getPronunciationAudio());
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
