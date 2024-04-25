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
import org.tyaa.training.client.android.services.HttpAuthService;
import org.tyaa.training.client.android.services.interfaces.IAuthService;
import org.tyaa.training.client.android.utils.UIActionsRunner;
/**
 * Логика экрана дополнительной заставки приложения,
 * во время отображения которой на сервер отправляется http-запрос
 * для выяснения состояния аутентификации пользователя
 * */
public class SplashScreenActivity extends AppCompatActivity {
    private final IAuthService mAuthService = new HttpAuthService();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuthService.checkUser(new IResultHandler<>() {
            @Override
            public void onSuccess(UserModel result) {
                Intent intent;
                if (result != null) {
                    // если получено описание пользователя, значит, вход в учётную запись был выполнен ранее,
                    // и следует перейти на главную Activity
                    intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                } else {
                    // иначе - перейти на Activity с формой входа в учётную запись
                    intent = new Intent(SplashScreenActivity.this, SignInActivity.class);
                }
                startActivity(intent);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.println(Log.ERROR, getString(R.string.message_error), errorMessage);
                UIActionsRunner.run(() -> Toast.makeText(SplashScreenActivity.this, errorMessage, Toast.LENGTH_LONG).show());
            }
        });
    }
}
