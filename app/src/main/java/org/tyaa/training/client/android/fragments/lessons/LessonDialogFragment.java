package org.tyaa.training.client.android.fragments.lessons;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.utils.UIActions;

/**
 * Класс фрагмента диалога выбора режима урока (изучение материала или проверка знаний)
 * */
public class LessonDialogFragment extends Fragment {

    private Button mStudyButton;
    private Button mTestButton;

    public LessonDialogFragment() {
        // подключение представления к объекту логики фрагмента
        super(R.layout.fragment_education_process_lesson_dialog);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        // получение идентификатора выбранного урока от фрагмента списка уроков,
        // логикой которого был вызван переход на текущий фрагмент
        final Long lessonId = LessonDialogFragmentArgs.fromBundle(getArguments()).getLessonId();
        // Log.d("lessonId", String.valueOf(lessonId));
        // UIActions.showInfo(getActivity(), String.valueOf(lessonId));

        /* получение объектов доступа к кнопкам выбора типа урока */
        mStudyButton =
                view.findViewById(R.id.activityMain_fragmentEducationProcessLessonDialog_study_Button);
        mTestButton =
                view.findViewById(R.id.activityMain_fragmentEducationProcessLessonDialog_test_Button);

        /* установка обработчиков событий клика по кнопкам */
        // обработчик клика по кнопке перехода к экрану изучения слов
        mStudyButton.setOnClickListener(v -> {
            // подготовка действия перехода к фрагменту изучения слов
            // с передачей ему идентификатора урока
            final LessonDialogFragmentDirections.NavigateToFragmentEducationProcessWordStudy action =
                LessonDialogFragmentDirections.navigateToFragmentEducationProcessWordStudy(lessonId);
            // выполнение подготовленного действия перехода
            Navigation.findNavController(view).navigate(action);
        });
        // обработчик клика по кнопке перехода к экрану проверки знания слов
        mTestButton.setOnClickListener(v -> {
            // подготовка действия перехода к фрагменту проверки знания слов
            // с передачей ему идентификатора урока
            final LessonDialogFragmentDirections.NavigateToFragmentEducationProcessWordKnowledgeTest action =
                    LessonDialogFragmentDirections.navigateToFragmentEducationProcessWordKnowledgeTest(lessonId);
            // выполнение подготовленного действия перехода
            Navigation.findNavController(view).navigate(action);
        });
    }
}
