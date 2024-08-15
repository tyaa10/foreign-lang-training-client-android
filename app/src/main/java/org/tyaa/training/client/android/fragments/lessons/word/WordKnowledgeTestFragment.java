package org.tyaa.training.client.android.fragments.lessons.word;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.utils.UIActions;

/**
 * Класс фрагмента проверки знания слова
 * */
public class WordKnowledgeTestFragment extends Fragment {

    public WordKnowledgeTestFragment() {
        // подключение представления к объекту логики фрагмента
        super(R.layout.fragment_education_process_word_knowledge_test);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // получение идентификатора выбранного урока от фрагмента списка уроков,
        // логикой которого был вызван переход на текущий фрагмент
        final Long lessonId = WordKnowledgeTestFragmentArgs.fromBundle(getArguments()).getLessonId();
        Log.d("lessonId", String.valueOf(lessonId));
        UIActions.showInfo(getActivity(), String.valueOf(lessonId));
    }
}
