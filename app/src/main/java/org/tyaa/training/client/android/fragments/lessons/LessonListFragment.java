package org.tyaa.training.client.android.fragments.lessons;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.adapters.LessonAdapter;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.LessonListItemModel;
import org.tyaa.training.client.android.services.HttpLessonService;
import org.tyaa.training.client.android.services.interfaces.ILessonService;
import org.tyaa.training.client.android.utils.UIActions;
import org.tyaa.training.client.android.utils.UIActionsRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс фрагмента списка уроков
 * */
public class LessonListFragment extends Fragment {

    private final ILessonService mLessonService = new HttpLessonService();

    private RecyclerView mLessonListRecyclerView;

    public LessonListFragment() {
        // подключение представления к объекту логики фрагмента
        super(R.layout.fragment_education_process_lesson_list);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_education_process_lesson_list, container, false);
        mLessonListRecyclerView = view.findViewById(R.id.activityMain_fragmentEducationProcessLessonList_list_RecyclerView);
        /* view.setOnClickListener(
                v -> {
                    Navigation.findNavController(view).navigate(R.id.navigate_to_fragment_education_process_word_study);
                }
        ); */
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        /* Заполнение списочного виджета описаниями уроков */
        // создание пустого списка для моделей описаний уроков
        final List<LessonListItemModel> lessonListItems = new ArrayList<>();
        // создание переходника с подключением на его вход списка моделей описаний уроков
        final LessonAdapter lessonAdapter = new LessonAdapter(getActivity(), lessonListItems);
        // установка списочному виджету линейного типа макета для отображения пунктов
        mLessonListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // установка списочному виджету переходника для получения данных из списка моделей
        mLessonListRecyclerView.setAdapter(lessonAdapter);
        // попытка получить с сервера список моделей описаний уроков
        mLessonService.getLessonListItems(
                new IResultHandler<>() {
                    // если получен положительный ответ на запрос
                    @Override
                    public void onSuccess(List<LessonListItemModel> result) {
                        // очистить список моделей, если он не пуст
                        if(lessonListItems.size() > 0) {
                            lessonListItems.clear();
                        }
                        // все полученные модели добавить в список
                        lessonListItems.addAll(result);
                        // сообщить переходнику о необходимоти переристовки
                        UIActionsRunner.run(lessonAdapter::notifyDataSetChanged);
                    }
                    // если попытка запроса провалилась, или получен ответ об ошибке
                    @Override
                    public void onFailure(String errorMessage) {
                        // отобразить сообщение об ошибке
                        UIActions.showError(getActivity(), errorMessage);
                    }
                }
        );
    }
}
