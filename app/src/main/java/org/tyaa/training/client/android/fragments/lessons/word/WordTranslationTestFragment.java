package org.tyaa.training.client.android.fragments.lessons.word;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
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
import androidx.navigation.Navigation;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.interfaces.IShadowable;
import org.tyaa.training.client.android.models.WordModel;
import org.tyaa.training.client.android.models.WordTestModel;
import org.tyaa.training.client.android.services.HttpWordTestService;
import org.tyaa.training.client.android.services.interfaces.IWordTestService;
import org.tyaa.training.client.android.state.InMemoryLocalState;
import org.tyaa.training.client.android.state.interfaces.IState;
import org.tyaa.training.client.android.utils.ImageConverter;
import org.tyaa.training.client.android.utils.TypeConverters;
import org.tyaa.training.client.android.utils.UIActions;
import org.tyaa.training.client.android.utils.UIActionsRunner;

/**
 * Класс фрагмента проверки умения переводить слова с родного на изучаемый язык
 * */
public class WordTranslationTestFragment extends Fragment implements IShadowable {

    // Доступ к Java-объектам текущего состояния приложения, хранимым в оперативной памяти клиента
    private final IState mState = new InMemoryLocalState();
    // Экземпляр службы проверки знания слов и умения переводить слова
    private final IWordTestService mWordTestService = new HttpWordTestService();

    // Идентификатор урока по изучению слов
    private Long mLessonId;
    // Индекс текущего слова
    private Integer mCurrentWordIndex = 0;

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
            checkTranslation();
        });

        // Возвращение объекта доступа к создаваемому представлению фрагмента
        return mWordTranslationTestView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // получение идентификатора выбранного урока от фрагмента списка уроков,
        // логикой которого был вызван переход на текущий фрагмент
        mLessonId = WordTranslationTestFragmentArgs.fromBundle(getArguments()).getLessonId();
        mCurrentWordTestModel =
                WordTranslationTestFragmentArgs.fromBundle(getArguments()).getLastTestResults();

        // Установка флагов языков на представление
        mNativeLanguageFlagImageView.setImageResource(mState.getNativeLanguageFlag());
        mLearningLanguageFlagImageView.setImageResource(mState.getLearningLanguageFlag());

        // установить на представление данные очередного слова
        // и затем увеличить счётчик отображённых слов на единицу
        UIActionsRunner.run(() -> showNextWord(mCurrentWordIndex));
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
                // стирание содержимого поля ввода перевода слова
                mTranslationEditText.setText("");
                // выод изображения текущего слова на виджет представления
                mWordImageView.setImageBitmap(ImageConverter.base64ToBitmap(getActivity(), wordModel.getImage()));
            });
        } else {
            // Иначе - вызов перехода на экран отображения результатов проверки знания слов урока
            goToWordTestFinalFragment();
        }
    }

    /**
     * Проверка правильности перевода слова с родного языка на иностранный
     * */
    private void checkTranslation() {
        // Сравнить правильный перевод с введенным пользователем
        final Boolean success =
                getEditTextString(mTranslationEditText).equals(mTranslationEditText.getTag());
        // затенить и сделать неинтерактивным представление
        shade();
        // отобразить бесконечный прогресс
        UIActions.showInfinityProgressToast(getActivity());
        // Прибавить значение результата проверки слова к объекту модели
        // результатов текущего сеанса проверки слов урока
        // (локально)
        addCurrentTestResult(success);
        // Прибавить значение результата проверки слова к объекту модели
        // суммарных результатов всех сеансов проверки слов урока
        // (отправить на сервер)
        // получение данных очередного слова для проверки знания
        WordModel wordModel = mState.getCurrentLessonWords().get(mCurrentWordIndex);
        mWordTestService.addWordTestResult(wordModel.getId(), success, new IResponseHandler() {
            @Override
            public void onSuccess() {
                // скрыть бесконечный прогресс
                UIActions.closeInfinityProgressToast();
                if (success) {
                    UIActionsRunner.run(() -> {
                        // установка изображения "правильно" в виджет результата
                        mResultImageView.setImageResource(R.drawable.correct);
                        // включение видимости виджета результата
                        mResultImageView.setVisibility(VISIBLE);
                        // включение видимости виджета изображения слова
                        mWordImageView.setVisibility(VISIBLE);
                    });
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        UIActionsRunner.run(() -> {
                            // скрытие изображения слова и результата проверки
                            mResultImageView.setVisibility(GONE);
                            mWordImageView.setVisibility(GONE);
                        });
                        // снять с представления тень и вернуть интерактивность
                        unshade();
                        // отображение данных проверки знания следующего слова
                        showNextWord(++mCurrentWordIndex);
                    }
                } else {
                    UIActionsRunner.run(() -> {
                        // установить и отобразить изображение результата "неправильно"
                        mResultImageView.setImageResource(R.drawable.wrong);
                        mResultImageView.setVisibility(VISIBLE);
                    });
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        // скрыть изображение результата
                        UIActionsRunner.run(() -> mResultImageView.setVisibility(GONE));
                        // снять с представления тень и вернуть интерактивность
                        unshade();
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                // скрыть бесконечный прогресс
                UIActions.closeInfinityProgressToast();
                // снять с представления тень и вернуть интерактивность
                unshade();
                // отобразить сообщение об ошибке
                UIActions.showError(getActivity(), errorMessage);
            }
        });
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

    private void goToWordTestFinalFragment() {
        UIActionsRunner.run(() -> {
            // подготовка действия перехода к фрагменту результатов проверки знания слов текущего урока
            // с передачей ему идентификатора урока и модели результатов
            final WordTranslationTestFragmentDirections.NavigateToFragmentEducationalProcessWordTestFinal action =
                    WordTranslationTestFragmentDirections.navigateToFragmentEducationalProcessWordTestFinal(mLessonId, mCurrentWordTestModel);
            // выполнение подготовленного выше действия перехода
            Navigation.findNavController(mWordTranslationTestView).navigate(action);
        });
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
