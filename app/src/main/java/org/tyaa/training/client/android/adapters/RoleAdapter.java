package org.tyaa.training.client.android.adapters;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.models.RoleModel;

import java.util.List;

/**
 * Переходник от списка моделей ролей к списочному элементу представления
 * */
public class RoleAdapter extends ArrayAdapter<RoleModel> {

    private final Context mContext;
    private final int mResource;

    public RoleAdapter(@NonNull Context context, int resource, List<RoleModel> _roles) {
        super(context, resource, _roles);
        mContext = context;
        mResource = resource;
    }

    /**
     * Модель объекта доступа к элементам представления пункта списка
     * */
    private static class ViewHolder {
        TextView roleIdTextView;
        TextView roleNameTextView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Переменная для объекта доступа к элементам представления текущего пункта списка
        ViewHolder holder;
        // Преобразователь из xml-разметки в объект представления
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // Если ранее объект представления пункта списка не создавался
        if (convertView == null) {
            // создать пустой объект представления пункта списка
            convertView = mInflater.inflate(mResource, parent, false);
            // создать и проинициализировать объект доступа к элементам представления текущего пункта списка
            holder = new ViewHolder();
            holder.roleIdTextView = convertView.findViewById(R.id.activityMain_listItem_roleId_TextView);
            holder.roleNameTextView = convertView.findViewById(R.id.activityMain_listItem_name_TextView);
            // сохранить ссылку на объект доступа к элементам представления текущего пункта списка
            // как тэг объекта представления этого пункта
            convertView.setTag(holder);
        } else {
            // иначе - использовать ранее созданный объект доступа
            holder = (ViewHolder) convertView.getTag();
        }
        // получить объект модели данных для текущего пункта списка
        RoleModel roleModel = getItem(position);
        // установить в объект представления текущего пункта списка
        // значения из объекта модели
        holder.roleIdTextView.setText(String.format("%s", roleModel.id));
        holder.roleNameTextView.setText(String.format("%s", roleModel.name));
        return convertView;
    }
}
