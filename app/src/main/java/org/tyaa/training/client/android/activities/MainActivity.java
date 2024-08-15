package org.tyaa.training.client.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.adapters.LessonAdapter;
import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.LessonListItemModel;
import org.tyaa.training.client.android.services.HttpAuthService;
import org.tyaa.training.client.android.services.HttpLessonService;
import org.tyaa.training.client.android.services.interfaces.IAuthService;
import org.tyaa.training.client.android.services.interfaces.ILessonService;
import org.tyaa.training.client.android.utils.UIActions;
import org.tyaa.training.client.android.utils.UIActionsRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Логика главного экрана приложения
 * (хост для фрагментов основной функциональности приложения)
 * */
public class MainActivity extends AppCompatActivity {

    private final IAuthService mAuthService = new HttpAuthService();

    // private final IState mState = new InMemoryLocalState();

    private Button mSignOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // подключение представления главного экрана приложения
        setContentView(R.layout.activity_main);

        // инициализация объектов доступа к постоянным элементам представления
        mSignOutButton = findViewById(R.id.activityMain_signOut_Button);

        // установка обработчиков событий для постоянных элементов представления
        mSignOutButton.setOnClickListener(v -> {
            mAuthService.signOut(new IResponseHandler() {
                @Override
                public void onSuccess() {
                    // после успешного выхода из учётной записи
                    // перейти на Activity с формой входа в учётную запись
                    Intent intent =
                            new Intent(MainActivity.this, SignInActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(String errorMessage) {
                    UIActions.showError(MainActivity.this, errorMessage);
                }
            });
        });

        // Отладочный вывод в консоль данных текущего состояния приложения
        // Log.d(MainActivity.class.getName(), mState.toString());

        /* Демонстрационное получение списка ролей и их вывод в списочный элемент на экране */
        // соединение списочного элемента на представлении со списком моделей ролей через адаптер
        /* final List<RoleModel> roles = new ArrayList<>();
        final RoleAdapter roleAdapter = new RoleAdapter(this, R.layout.activity_main_list_item, roles);
        this.setListAdapter(roleAdapter);
        // вызов метода получения всех возможных ролей пользователей
        // с дальнейшим их выводом на экран в виде списка
        mAuthService.getRoles(new IResultHandler<>() {
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
                UIActions.showError(MainActivity.this, errorMessage);
            }
        }); */
    }
}