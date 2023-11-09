package org.tyaa.training.client.android.activities;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.repositories.RoleRepository;
import org.tyaa.training.client.android.repositories.interfaces.IRoleRepository;
import org.tyaa.training.client.android.utils.UIRunner;

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
        // вызов метода получения всех возможных ролей пользователей
        // с дальнейшим их выводом в консоль
        roleRepository.getRoles(new IResultHandler<>() {
            @Override
            public void onSuccess(String result) {
                Log.println(Log.DEBUG, "Роли", result);
                UIRunner.run(() -> Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show());
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.println(Log.ERROR, "Ошибка", errorMessage);
                UIRunner.run(() -> Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show());
            }
        });
    }
}