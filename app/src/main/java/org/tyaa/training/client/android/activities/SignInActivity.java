package org.tyaa.training.client.android.activities;

import static org.tyaa.training.client.android.utils.UIDataExtractor.*;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.UserProfileModel;
import org.tyaa.training.client.android.services.HttpAuthService;
import org.tyaa.training.client.android.services.HttpProfileService;
import org.tyaa.training.client.android.services.interfaces.IAuthService;
import org.tyaa.training.client.android.services.interfaces.IProfileService;
import org.tyaa.training.client.android.utils.UIActions;

/**
 * Логика экрана формы входа в учётную запись
 * или перехода к форме регистрации
 * */
public class SignInActivity extends AppCompatActivity {

    private final IAuthService mAuthService = new HttpAuthService();
    private final IProfileService mProfileService = new HttpProfileService();
    private TextInputEditText mLoginTextInputEditText;
    private TextInputEditText mPasswordTextInputEditText;
    private Button mSignInButton;
    private Button mGoToSignUpButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // подключение представления входа в учётную запись
        setContentView(R.layout.activity_sign_in);

        // инициализация объектов доступа к постоянным элементам представления
        mLoginTextInputEditText =
                findViewById(R.id.activitySignIn_login_TextInputEditText);
        mPasswordTextInputEditText =
                findViewById(R.id.activitySignIn_password_TextInputEditText);
        mSignInButton = findViewById(R.id.activitySignIn_signIn_Button);
        mGoToSignUpButton = findViewById(R.id.activitySignIn_goToSignUp_Button);

        // установка обработчиков событий для постоянных элементов представления
        mSignInButton.setOnClickListener(v -> {
            if (validateInputs(mLoginTextInputEditText, mPasswordTextInputEditText)) {
                mAuthService.signIn(
                        getEditTextString(mLoginTextInputEditText),
                        getEditTextString(mPasswordTextInputEditText),
                        new IResponseHandler() {
                            // если вход в учётную запись выполнен успешно
                            @Override
                            public void onSuccess() {
                                // попытка получить данные профиля
                                mProfileService.getCurrentUserProfile(new IResultHandler<>() {
                                    // если запрос был выполнен успешно
                                    @Override
                                    public void onSuccess(UserProfileModel profile) {
                                        Intent intent = null;
                                        if (profile != null) {
                                            // если данные профиля получены - подготовить переход на главную Activity
                                            intent = new Intent(SignInActivity.this, MainActivity.class);
                                        } else {
                                            // иначе - подготовить переход на Activity начального заполнения профиля
                                            intent = new Intent(SignInActivity.this, ProfileCreatingActivity.class);
                                        }
                                        // выполнить переход на Activity, определённую выше
                                        startActivity(intent);
                                    }
                                    // если выполнение запроса провалилось
                                    @Override
                                    public void onFailure(String errorMessage) {
                                        UIActions.showError(SignInActivity.this, errorMessage);
                                    }
                                });
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                UIActions.showError(SignInActivity.this, errorMessage);
                            }
                        }
                );
            }
        });
        mGoToSignUpButton.setOnClickListener(v -> {
            // перейти на Activity регистрации
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private boolean validateInputs(TextInputEditText loginInput, TextInputEditText passwordInput) {
        if (loginInput.length() == 0) {
            loginInput.setError(getString(R.string.message_error_validation_login));
            return false;
        }
        if (passwordInput.length() == 0) {
            passwordInput.setError(getString(R.string.message_error_validation_password));
            return false;
        }
        return true;
    }
}
