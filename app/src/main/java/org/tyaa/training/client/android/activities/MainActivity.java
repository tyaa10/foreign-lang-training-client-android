package org.tyaa.training.client.android.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.RoleModel;
import org.tyaa.training.client.android.repositories.RoleRepository;
import org.tyaa.training.client.android.repositories.interfaces.IRoleRepository;
import org.tyaa.training.client.android.utils.UIActionsRunner;

import java.util.List;

/**
 * Логика главного экрана приложения
 * */
public class MainActivity extends AppCompatActivity {

    private final IRoleRepository roleRepository = new RoleRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // подключение представления главного экрана приложения
        setContentView(R.layout.activity_main);
        // получение управления текстовым элементом представления по его идентификатору
        final TextView rolesTextView = findViewById(R.id.rolesJson);
        // вызов метода получения всех возможных ролей пользователей
        // с дальнейшим их выводом в консоль
        roleRepository.getRoles(new IResultHandler<>() {
            @Override
            public void onSuccess(List<RoleModel> result) {
                // вставка текста заголовка в текстовый элемент представления
                UIActionsRunner.run(() -> rolesTextView.setText("Роли:\n"));
                for (int i = 0; i < result.size(); i++) {
                    final int index = i;
                    Log.println(Log.DEBUG, String.format("Роль #%s", result.get(index).id), result.get(index).name);
                    // добавление текста названия роли в конец содержимого текстового элемента представления
                    UIActionsRunner.run(() -> rolesTextView.append(
                            String.format("\nРоль #%s: %s", result.get(index).id, result.get(index).name.replace("ROLE_", "")))
                    );
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.println(Log.ERROR, "Ошибка", errorMessage);
                UIActionsRunner.run(() -> Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show());
            }
        });
    }
}