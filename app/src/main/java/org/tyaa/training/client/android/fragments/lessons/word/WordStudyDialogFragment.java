package org.tyaa.training.client.android.fragments.lessons.word;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import org.tyaa.training.client.android.R;

/**
 * Класс фрагмента диалога выбора режима урока (изучение материала или проверка знаний)
 * */
public class WordStudyDialogFragment extends Fragment {

    private Button mGoToTestButton;
    private Button mReturnToLessonsMenuButton;

    public WordStudyDialogFragment() {
        // подключение представления к объекту логики фрагмента
        super(R.layout.fragment_educational_process_word_study_dialog);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        // получение идентификатора текущего урока от предыдущего фрагмента (изучения слов)
        final Long lessonId = WordStudyDialogFragmentArgs.fromBundle(getArguments()).getLessonId();
        // Log.d("lessonId", String.valueOf(lessonId));
        // UIActions.showInfo(getActivity(), String.valueOf(lessonId));

        /* получение объектов доступа к кнопкам выбора типа урока */
        mGoToTestButton =
                view.findViewById(R.id.activityMain_fragmentEducationProcessWordStudyDialog_test_Button);
        mReturnToLessonsMenuButton =
                view.findViewById(R.id.activityMain_fragmentEducationProcessWordStudyDialog_return_Button);

        /* установка обработчиков событий клика по кнопкам */
        // обработчик клика по кнопке перехода к экрану проверки знания слов текущего урока
        mGoToTestButton.setOnClickListener(v -> {
            // подготовка действия перехода к фрагменту проверки знания слов текущего урока
            // с передачей ему идентификатора урока
            final WordStudyDialogFragmentDirections.NavigateToFragmentEducationProcessWordKnowledgeTest action =
                WordStudyDialogFragmentDirections.navigateToFragmentEducationProcessWordKnowledgeTest(lessonId);
            // выполнение подготовленного действия перехода
            Navigation.findNavController(v).navigate(action);
        });
        // обработчик клика по кнопке перехода к экрану списка уроков
        mReturnToLessonsMenuButton.setOnClickListener(v -> {
            // выполнение действия перехода к фрагменту списка уроков
            Navigation.findNavController(v).navigate(R.id.navigate_from_word_study_to_fragment_education_process_lesson_list);
        });
    }
}
