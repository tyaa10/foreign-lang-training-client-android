package org.tyaa.training.client.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import org.tyaa.training.client.android.fragments.lessons.LessonListFragmentDirections;
import org.tyaa.training.client.android.models.LessonListItemModel;

import java.util.List;

/**
 * Переходник от списка моделей названий уроков к списочному элементу представления
 * */
public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {

    private final Context mContext;
    private final List<LessonListItemModel> mLessons;

    public LessonAdapter(Context context, List<LessonListItemModel> lessons) {
        this.mContext = context;
        this.mLessons = lessons;
    }

    @NonNull
    @Override
    public LessonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, parent, false);
        return new ViewHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonAdapter.ViewHolder holder, int position) {
        holder.getTextView().setText(mLessons.get(position).getName());
        holder.getTextView().setTag(mLessons.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return mLessons.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleTextView;

        public ViewHolder(@NonNull Context context, @NonNull View itemView) {

            super(itemView);

            titleTextView = (TextView) itemView.findViewById(android.R.id.text1);
            // обработчик клика по заголовку пункта списка
            titleTextView.setOnClickListener(v -> {
                // подготовка действия перехода к фрагменту диалога выбора типа урока
                // с передачей ему идентификатора урока, извлечённого из атрибута "тег"
                // виджета пункта списка
                LessonListFragmentDirections.NavigateToFragmentEducationProcessLessonDialog action =
                    LessonListFragmentDirections.navigateToFragmentEducationProcessLessonDialog((Long) itemView.getTag());
                // выполнение подготовленного действия перехода
                Navigation.findNavController(itemView).navigate(action);
            });
        }

        public TextView getTextView() {
            return titleTextView;
        }
    }
}
