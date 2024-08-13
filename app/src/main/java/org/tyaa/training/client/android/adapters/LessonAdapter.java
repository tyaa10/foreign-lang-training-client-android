package org.tyaa.training.client.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.tyaa.training.client.android.models.LessonListItemModel;
import org.tyaa.training.client.android.utils.UIActions;

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
            titleTextView.setOnClickListener(v -> {
                UIActions.showInfo(context, itemView.getTag().toString());
            });
        }

        public TextView getTextView() {
            return titleTextView;
        }
    }
}
