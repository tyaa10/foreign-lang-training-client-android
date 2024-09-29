package org.tyaa.training.client.android.fragments.lessons.word;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.interfaces.IShadowable;
import org.tyaa.training.client.android.models.WordTestModel;
import org.tyaa.training.client.android.services.HttpWordTestService;
import org.tyaa.training.client.android.services.interfaces.IWordTestService;
import org.tyaa.training.client.android.state.InMemoryLocalState;
import org.tyaa.training.client.android.state.interfaces.IState;
import org.tyaa.training.client.android.utils.Calc;
import org.tyaa.training.client.android.utils.UIActions;
import org.tyaa.training.client.android.utils.UIActionsRunner;

/**
 * Логика завершающего фрагмента проверки знаний слов урока по изучению слов
 * с итогами проверки и диалогом выбора: повторная проверка знаний или возврат в меню уроков
 * */
public class WordTestFinalFragment extends Fragment implements IShadowable {

    private final IState mState = new InMemoryLocalState();
    private final IWordTestService mWordTestService = new HttpWordTestService();

    private Long mLessonId;

    // Поле для модели суммы результатов текущего сеанса проверки знаний слов урока
    private WordTestModel mCurrentWordTestModel;

    private TextView mLastTestResultsTextView;
    private TextView mTotalTestResultsTextView;
    private Button mTestAgainButton;
    private Button mReturnToLessonsMenuButton;

    public WordTestFinalFragment() {
        // подключение представления к объекту логики фрагмента
        super(R.layout.fragment_educational_process_word_test_final);

        // очистка списка моделей слов для текущего урока в объекте состояния приложения
        mState.clearCurrentLessonWords();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // получение идентификатора выбранного урока от фрагмента списка уроков,
        // логикой которого был вызван переход на текущий фрагмент
        mLessonId = WordTestFinalFragmentArgs.fromBundle(getArguments()).getLessonId();
        mCurrentWordTestModel =
                WordTestFinalFragmentArgs.fromBundle(getArguments()).getLastTestResults();

        /* получение объектов доступа к виджетам представления */
        mTestAgainButton =
                view.findViewById(R.id.activityMain_fragmentEducationProcessWordTestFinal_testAgain_Button);
        mReturnToLessonsMenuButton =
                view.findViewById(R.id.activityMain_fragmentEducationProcessWordTestFinal_return_Button);
        mLastTestResultsTextView =
                view.findViewById(R.id.activityMain_fragmentEducationProcessWordTestFinal_lastResults_TextView);
        mTotalTestResultsTextView =
                view.findViewById(R.id.activityMain_fragmentEducationProcessWordTestFinal_totalResults_TextView);

        /* *** Получение и вывод на экран результатов проверки знаний *** */
        /* 1. результаты последнего сеанса проверки, накопленные в локальной модели */
        showResults(mLastTestResultsTextView, getString(R.string.word_test_final_last_results_template), mCurrentWordTestModel);
        /* 2. суммарные результаты всех сеансов проверки, получаемые от сервера */
        // затенить и сделать неинтерактивным представление
        shade();
        // отобразить бесконечный прогресс
        UIActions.showInfinityProgressToast(getActivity());
        // попытаться получить суммарные результаты всех сеансов проверки знаний по данному уроку с сервера
        mWordTestService.getWordStudyLessonTestResults(
                mLessonId,
                new IResultHandler<>() {

                    @Override
                    public void onSuccess(WordTestModel results) {
                        showResults(mTotalTestResultsTextView, getString(R.string.word_test_final_total_results_template), results);
                        // скрыть бесконечный прогресс
                        UIActions.closeInfinityProgressToast();
                        // снять с представления тень и вернуть интерактивность
                        unshade();
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
                }
        );

        /* Установка обработчиков событий виджетов */
        // обработчик клика по кнопке перехода к экрану проверки знания слов текущего урока
        mTestAgainButton.setOnClickListener(v -> {
            // подготовка действия перехода к фрагменту проверки знания слов текущего урока
            // с передачей ему идентификатора урока
            final WordTestFinalFragmentDirections.NavigateToFragmentEducationProcessWordKnowledgeTest action =
                    WordTestFinalFragmentDirections.navigateToFragmentEducationProcessWordKnowledgeTest(mLessonId);
            // выполнение подготовленного действия перехода
            Navigation.findNavController(v).navigate(action);
        });
        // обработчик клика по кнопке перехода к экрану списка уроков
        mReturnToLessonsMenuButton.setOnClickListener(v -> {
            // выполнение действия перехода к фрагменту списка уроков
            Navigation.findNavController(v).navigate(R.id.navigate_from_word_test_final_to_fragment_education_process_lesson_list);
        });
    }

    /**
     * Вывод на экран результатов проверки знаний
     * @param testResultsTextView виджет, на которые выводятся данные
     * @param template шаблон текста резульатов, который должен содержать три места подстановки: 1) процент успешных переводов ; 2) число успешных переводов; 3) число попыток
     * @param results модель, из которой выводятся данные
     * */
    private void showResults(TextView testResultsTextView, String template, WordTestModel results) {
        final int attemptsNumber = results.getAttemptsNumber();
        final int successNumber = results.getSuccessNumber();
        UIActionsRunner.run(() -> {
            testResultsTextView.setText(
                    String.format(
                            template,
                            Calc.percent(successNumber, attemptsNumber),
                            successNumber,
                            attemptsNumber
                    )
            );
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
