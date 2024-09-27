package org.tyaa.training.client.android.fragments.lessons.word;

import static org.tyaa.training.client.android.utils.UIDataExtractor.getEditTextString;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.interfaces.IShadowable;
import org.tyaa.training.client.android.models.WordModel;
import org.tyaa.training.client.android.models.WordTestModel;
import org.tyaa.training.client.android.state.InMemoryLocalState;
import org.tyaa.training.client.android.state.interfaces.IState;
import org.tyaa.training.client.android.utils.TypeConverters;
import org.tyaa.training.client.android.utils.UIActions;
import org.tyaa.training.client.android.utils.UIActionsRunner;

/**
 * Класс фрагмента проверки умения переводить слова с родного на изучаемый язык
 * */
public class WordTranslationTestFragment extends Fragment implements IShadowable {

    // Доступ к Java-объектам текущего состояния приложения, хранимым в оперативной памяти клиента
    private final IState mState = new InMemoryLocalState();

    // Модель для накопления суммы результатов текущего сеанса проверки знаний слов урока
    private WordTestModel mCurrentWordTestModel;

    // Поле для объекта управления представлением фрагмента
    private View mWordTranslationTestView;

    // Поля для объектов управления виджетами, расположенными на представлении фрагмента
    private ImageView mNativeLanguageFlagImageView;
    private ImageView mLearningLanguageFlagImageView;
    private TextView mWordTextView;
    private EditText mTranslationEditText;
    private ImageView mResultImageView;
    private ImageView mWordImageView;
    private ImageView mNextImageView;

    public WordTranslationTestFragment() {
        // Подключение представления к объекту логики фрагмента
        super(R.layout.fragment_education_process_word_translation_test);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Инициализация поля объекта доступа к представлению фрагмента
        mWordTranslationTestView =
                inflater.inflate(R.layout.fragment_education_process_word_translation_test, container, false);

        // Инициализация полей доступа к виджетам на представлении фрагмента
        mNativeLanguageFlagImageView =
                mWordTranslationTestView.findViewById(R.id.activityMain_fragmentEducationProcessKnowledgeTest_nativeLanguageFlag_ImageView);
        mLearningLanguageFlagImageView =
                mWordTranslationTestView.findViewById(R.id.activityMain_fragmentEducationProcessKnowledgeTest_learningLanguageFlag_ImageView);
        mWordTextView =
                mWordTranslationTestView.findViewById(R.id.activityMain_fragmentEducationProcessWordTranslationTest_word_TextView);
        mTranslationEditText =
                mWordTranslationTestView.findViewById(R.id.activityMain_fragmentEducationProcessWordTranslationTest_translation_EditText);
        mResultImageView =
                mWordTranslationTestView.findViewById(R.id.activityMain_fragmentEducationProcessWordTranslationTest_result_ImageView);
        mWordImageView =
                mWordTranslationTestView.findViewById(R.id.activityMain_fragmentEducationProcessWordTranslationTest_wordImage_ImageView);
        mNextImageView =
                mWordTranslationTestView.findViewById(R.id.activityMain_fragmentEducationProcessWordTranslationTest_next_ImageView);

        // Обработка касания кнопки "далее"
        mNextImageView.setOnClickListener(v -> {
            // Проверка правильности перевода
            checkTranslation(v);
        });

        // Возвращение объекта доступа к создаваемому представлению фрагмента
        return mWordTranslationTestView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Установка флагов языков на представление
        mNativeLanguageFlagImageView.setImageResource(mState.getNativeLanguageFlag());
        mLearningLanguageFlagImageView.setImageResource(mState.getLearningLanguageFlag());
    }

    /**
     * Действия при попытке перехода к проверке знания следующего изучаемого слова
     * */
    private void showNextWord(Integer index) {
        // Если модели слов в списке ещё не закончились
        if (index < mState.getCurrentLessonWords().size()) {
            // получение данных очередного слова для проверки знания
            WordModel wordModel = mState.getCurrentLessonWords().get(index);
            UIActionsRunner.run(() -> {
                // вывод слова на виджет
                mWordTextView.setText(wordModel.getWord());
                // сохранение правильного перевода слова в метаданные виджета ввода перевода
                mTranslationEditText.setTag(wordModel.getTranslation());
            });
        } else {
            // Иначе - вызов перехода на экран отображения результатов проверки знания слов урока
            goToWordTestFinalFragment();
        }
    }

    /**
     * Проверка правильности перевода слова с родного языка на иностранный
     * @param v объект доступа к виджету кнопки "далее"
     * */
    private void checkTranslation(View v) {
        // затенить и сделать неинтерактивным представление
        shade();
        // отобразить бесконечный прогресс
        UIActions.showInfinityProgressToast(getActivity());
        addCurrentTestResult(getEditTextString(mTranslationEditText).equals(mTranslationEditText.getTag()));
    }

    /**
     * Приращение значений результата текущего сеанса проверки знаний слов урока
     * @param success успешной ли была попытка перевода слова
     * */
    private void addCurrentTestResult(Boolean success) {
        mCurrentWordTestModel.attemptsNumber++;
        mCurrentWordTestModel.setSuccessNumber(
                mCurrentWordTestModel.getSuccessNumber() + TypeConverters.booleanToInteger(success)
        );
    }

    @Override
    public void shade() {
        ((IShadowable) getActivity()).shade();
    }

    @Override
    public void unshade() {
        ((IShadowable) getActivity()).unshade();
    }
}
