package org.tyaa.training.client.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.UserModel;
import org.tyaa.training.client.android.models.UserProfileModel;
import org.tyaa.training.client.android.services.HttpAuthService;
import org.tyaa.training.client.android.services.HttpProfileService;
import org.tyaa.training.client.android.services.interfaces.IAuthService;
import org.tyaa.training.client.android.services.interfaces.IProfileService;
import org.tyaa.training.client.android.utils.UIActions;
import org.tyaa.training.client.android.utils.UIActionsRunner;
/**
 * Логика экрана дополнительной заставки приложения,
 * во время отображения которой на сервер отправляется http-запрос
 * для выяснения состояния аутентификации пользователя
 * */
public class SplashScreenActivity extends AppCompatActivity {

    private final IAuthService mAuthService = new HttpAuthService();
    private final IProfileService mProfileService = new HttpProfileService();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuthService.checkUser(new IResultHandler<>() {
            @Override
            public void onSuccess(UserModel result) {
                // если получено описание пользователя,
                // значит вход в учётную запись ранее был выполнен
                if (result != null) {
                    // попытка получить данные профиля пользователя
                    mProfileService.getCurrentUserProfile(new IResultHandler<>() {
                        // если запрос был выполнен успешно
                        @Override
                        public void onSuccess(UserProfileModel profile) {
                            Intent intent = null;
                            if (profile != null) {
                                // если данные профиля получены - подготовить переход на главную Activity
                                intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            } else {
                                // иначе - подготовить переход на Activity начального заполнения профиля
                                intent = new Intent(SplashScreenActivity.this, ProfileCreatingActivity.class);
                            }
                            // выполнить переход на Activity, определённую выше
                            startActivity(intent);
                        }
                        // если выполнение запроса провалилось
                        @Override
                        public void onFailure(String errorMessage) {
                            UIActions.showError(SplashScreenActivity.this, errorMessage);
                        }
                    });
                } else {
                    // если вход в учётную запись ранее не был выполнен -
                    // перейти на Activity с формой входа в учётную запись
                    Intent intent = new Intent(SplashScreenActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.println(Log.ERROR, getString(R.string.message_error), errorMessage);
                UIActionsRunner.run(() -> Toast.makeText(SplashScreenActivity.this, errorMessage, Toast.LENGTH_LONG).show());
            }
        });
    }
}
