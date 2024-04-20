package org.tyaa.training.client.android.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.UserModel;
import org.tyaa.training.client.android.services.HttpAuthService;
import org.tyaa.training.client.android.services.interfaces.IAuthService;

public class SplashScreenActivity extends AppCompatActivity {
    private final IAuthService authService = new HttpAuthService();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        authService.checkUser(new IResultHandler<>() {
            @Override
            public void onSuccess(UserModel result) {
                Intent intent = null;
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

            }
        });
    }
}
