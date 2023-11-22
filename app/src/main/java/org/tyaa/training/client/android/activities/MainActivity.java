package org.tyaa.training.client.android.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.adapters.RoleAdapter;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.RoleModel;
import org.tyaa.training.client.android.repositories.RoleRepository;
import org.tyaa.training.client.android.repositories.interfaces.IRoleRepository;
import org.tyaa.training.client.android.utils.UIActionsRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Логика главного экрана приложения
 * */
public class MainActivity extends ListActivity {

    private final IRoleRepository roleRepository = new RoleRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // подключение представления главного экрана приложения
        setContentView(R.layout.activity_main);
        final List<RoleModel> roles = new ArrayList<>();
        final RoleAdapter roleAdapter = new RoleAdapter(this, R.layout.activity_main_list_item, roles);
        this.setListAdapter(roleAdapter);
        // вызов метода получения всех возможных ролей пользователей
        // с дальнейшим их выводом на экран в виде списка
        roleRepository.getRoles(new IResultHandler<>() {
            @Override
            public void onSuccess(List<RoleModel> result) {
                if(roles.size() > 0) {
                    roles.clear();
                }
                roles.addAll(result);
                UIActionsRunner.run(roleAdapter::notifyDataSetChanged);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.println(Log.ERROR, "Ошибка", errorMessage);
                UIActionsRunner.run(() -> Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show());
            }
        });
    }
}