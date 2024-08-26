package org.tyaa.training.client.android.fragments.lessons.word;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.interfaces.IShadowable;
import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.models.WordModel;
import org.tyaa.training.client.android.services.HttpWordService;
import org.tyaa.training.client.android.services.HttpWordTestService;
import org.tyaa.training.client.android.services.interfaces.IWordService;
import org.tyaa.training.client.android.services.interfaces.IWordTestService;
import org.tyaa.training.client.android.state.InMemoryLocalState;
import org.tyaa.training.client.android.state.interfaces.IState;
import org.tyaa.training.client.android.utils.ImageConverter;
import org.tyaa.training.client.android.utils.UIActions;
import org.tyaa.training.client.android.utils.UIActionsRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Класс фрагмента проверки знания слова
 * */
public class WordKnowledgeTestFragment extends Fragment implements IShadowable {

    private final IWordService mWordService = new HttpWordService();
    private final IWordTestService mWordTestService = new HttpWordTestService();
    private final IState mState = new InMemoryLocalState();

    private Long mLessonId;
    private Integer mCurrentWordIndex = 0;

    private View mWordTestFragmentView;

    private ImageView mNativeLanguageFlagImageView;
    private ImageView mLearningLanguageFlagImageView;
    private TextView mWordTextView;
    private TextView mTranslationTextView;
    private List<ImageView> mAnswerOptionsImageViewList;
    private ImageView mResultImageView;

    public WordKnowledgeTestFragment() {
        // подключение представления к объекту логики фрагмента
        super(R.layout.fragment_education_process_word_knowledge_test);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mWordTestFragmentView =
                inflater.inflate(R.layout.fragment_education_process_word_knowledge_test, container, false);

        mNativeLanguageFlagImageView =
                mWordTestFragmentView.findViewById(R.id.activityMain_fragmentEducationProcessKnowledgeTest_nativeLanguageFlag_ImageView);
        mLearningLanguageFlagImageView =
                mWordTestFragmentView.findViewById(R.id.activityMain_fragmentEducationProcessKnowledgeTest_learningLanguageFlag_ImageView);
        mTranslationTextView =
                mWordTestFragmentView.findViewById(R.id.activityMain_fragmentEducationProcessWordKnowledgeTest_translation_TextView);
        mWordTextView =
                mWordTestFragmentView.findViewById(R.id.activityMain_fragmentEducationProcessWordKnowledgeTest_word_TextView);
        mAnswerOptionsImageViewList = new ArrayList<>();
        mAnswerOptionsImageViewList.add(mWordTestFragmentView.findViewById(R.id.activityMain_fragmentEducationProcessKnowledgeTest_wordImageOptionOne_ImageView));
        mAnswerOptionsImageViewList.add(mWordTestFragmentView.findViewById(R.id.activityMain_fragmentEducationProcessKnowledgeTest_wordImageOptionTwo_ImageView));
        mAnswerOptionsImageViewList.add(mWordTestFragmentView.findViewById(R.id.activityMain_fragmentEducationProcessKnowledgeTest_wordImageOptionThree_ImageView));
        mAnswerOptionsImageViewList.add(mWordTestFragmentView.findViewById(R.id.activityMain_fragmentEducationProcessKnowledgeTest_wordImageOptionFour_ImageView));
        mResultImageView =
                mWordTestFragmentView.findViewById(R.id.activityMain_fragmentEducationProcessWordKnowledgeTest_result_ImageView);
        return mWordTestFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // получение идентификатора выбранного урока от фрагмента списка уроков,
        // логикой которого был вызван переход на текущий фрагмент
        mLessonId = WordKnowledgeTestFragmentArgs.fromBundle(getArguments()).getLessonId();
        // Log.d("lessonId", String.valueOf(lessonId));
        // UIActions.showInfo(getActivity(), String.valueOf(lessonId));

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
     * Действия при попытке перехода к проверке знания следующего изучаемого слова
     * */
    private void showNextWord(Integer index) {
        // если модели слов в списке ещё не закончились
        if (index < mState.getCurrentLessonWords().size()) {
            // получение данных очередного слова для проверки знания
            WordModel wordModel = mState.getCurrentLessonWords().get(index);
            UIActionsRunner.run(() -> {
                // вывод на виджет перевода слова
                mTranslationTextView.setText(wordModel.getTranslation());
                // подключение слова к виджету отображения слова, в то время, когда виджет скрыт
                mWordTextView.setText(wordModel.getWord());
            });
            // получение данных ещё трёх слов, не совпадающих с текущим
            List<WordModel> wrongChoiceWordModels =
                    getRandomWordModels(mState.getCurrentLessonWords(), wordModel.getId(), 3);

            // случайный выбор одной из четырёх ячеек,
            // в которую будет выведенео изображение текущего слова
            int correctChoiceImageNumber = getRandomInt(1, 4);

            /* вывод изображений всех четырёх слов */
            // счётчик моделей слов неправильного выбора
            int wrongChoiceWordModelIdx = 1;
            // цикл вывода изображений на четыре виджета вариантов для выбора
            for(int optionImageIdx = 1; optionImageIdx <= 4; optionImageIdx++) {
                // ссылка на текущий виджет изображения варианта выбора
                final ImageView answerOptionsImageView =
                        mAnswerOptionsImageViewList.get(optionImageIdx - 1);
                // когда обрабатывается виджет с индексом правильного выбра
                if (optionImageIdx == correctChoiceImageNumber) {
                    UIActionsRunner.run(() -> {
                        // установить виджету изображение правильного выбора
                        answerOptionsImageView
                                .setImageBitmap(
                                        ImageConverter.base64ToBitmap(
                                                getActivity(),
                                                wordModel.getImage()
                                        )
                                );
                        // установить виджету значение тега "истина"
                        answerOptionsImageView.setTag(true);
                    });
                } else {
                    // иначе, когда обрабатывается виджет с индексом неправильного выбра
                    final int idx = wrongChoiceWordModelIdx - 1;
                    UIActionsRunner.run(() -> {
                        // установить виджету очередное изображение неправильного выбора
                        answerOptionsImageView
                                .setImageBitmap(
                                        ImageConverter.base64ToBitmap(
                                                getActivity(),
                                                wrongChoiceWordModels.get(idx).getImage()
                                        )
                                );
                        // установить виджету значение тега "ложь"
                        answerOptionsImageView.setTag(false);
                    });
                    // увеличить значение счётчика выводимых на представление неправильных вариантов
                    wrongChoiceWordModelIdx++;
                }
                answerOptionsImageView.setOnClickListener(
                        v -> {
                            final Boolean success = (Boolean) v.getTag();
                            // затенить и сделать неинтерактивным представление
                            shade();
                            // отобразить бесконечный прогресс
                            UIActions.showInfinityProgressToast(getActivity());
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
                                            // включение видимости виджета слова
                                            mWordTextView.setVisibility(VISIBLE);
                                        });
                                        try {
                                            Thread.sleep(3000);
                                        } catch (InterruptedException e) {
                                            throw new RuntimeException(e);
                                        } finally {
                                            UIActionsRunner.run(() -> {
                                                // скрытие слова и результата проверки
                                                mResultImageView.setVisibility(GONE);
                                                mWordTextView.setVisibility(GONE);
                                            });
                                            // снять с представления тень и вернуть интерактивность
                                            unshade();
                                            // отображение данных проверки знания следующего слова
                                            showNextWord(mCurrentWordIndex++);
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
                );
            }
        } else {
            // TODO переход на экран отображения общего результата проверки знания слов урока
            UIActions.showInfo(getActivity(), "Проверка знания слов завершена");
        }
    }

    /**
     * Получение заданного числа моделей слов, не совпадающих с текущим
     * @param currentLessonWords исходный список
     * @param excludedWordModelId идентификатор исключаемой модели слова
     * @param resultWordModelQuantity количество моделей слов в результирующем списке
     * */
    private List<WordModel> getRandomWordModels(List<WordModel> currentLessonWords, Long excludedWordModelId, int resultWordModelQuantity) {
        currentLessonWords =
                currentLessonWords.stream()
                        .filter(w -> !Objects.equals(w.getId(), excludedWordModelId))
                        .collect(Collectors.toList());
        Collections.shuffle(currentLessonWords);
        return currentLessonWords.stream()
                .limit(resultWordModelQuantity)
                .collect(Collectors.toList());
    }

    /**
     * Получение случайного целого числа в заданном диапазоне
     * @param min минимальное значение, включительно
     * @param max максимальное значение, включительно
     * */
    private int getRandomInt(int min, int max) {
        Random random = new Random();
        return random.ints(min, ++max)
                .findFirst()
                .getAsInt();
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
