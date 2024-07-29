package org.tyaa.training.client.android.activities;

import static org.tyaa.training.client.android.utils.UIDataExtractor.getEditTextString;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.services.HttpAuthService;
import org.tyaa.training.client.android.services.interfaces.IAuthService;
import org.tyaa.training.client.android.utils.UIActions;
import org.tyaa.training.client.android.utils.UIActionsRunner;

import java.util.List;
import java.util.Locale;

/**
 * Логика экрана формы регистрации
 * */
public class SignUpActivity extends AppCompatActivity {

    private final IAuthService mAuthService = new HttpAuthService();
    private TextInputEditText mLoginTextInputEditText;
    private TextInputEditText mPasswordTextInputEditText;
    private TextInputEditText mConfirmPasswordTextInputEditText;
    private Button mSignUpButton;
    private Button mGoToSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // подключение представления регистрации
        setContentView(R.layout.activity_sign_up);
        // инициализация объектов доступа к постоянным элементам представления
        mLoginTextInputEditText =
                findViewById(R.id.activitySignUp_login_TextInputEditText);
        mPasswordTextInputEditText =
                findViewById(R.id.activitySignUp_password_TextInputEditText);
        mConfirmPasswordTextInputEditText =
                findViewById(R.id.activitySignUp_confirmPassword_TextInputEditText);
        mSignUpButton = findViewById(R.id.activitySignUp_signUp_Button);
        mGoToSignInButton = findViewById(R.id.activitySignUp_goToSignIn_Button);
        // установка обработчиков событий для постоянных элементов представления
        mSignUpButton.setOnClickListener(v -> {
            if (validateInputs(
                    mLoginTextInputEditText,
                    mPasswordTextInputEditText,
                    mConfirmPasswordTextInputEditText)) {
                mAuthService.signUp(
                        getEditTextString(mLoginTextInputEditText),
                        getEditTextString(mPasswordTextInputEditText),
                        new IResponseHandler() {
                            @Override
                            public void onSuccess() {
                                // UIActions.showInfo(SignUpActivity.this, "A new user registered");
                                // перейти на Activity начального заполнения профиля
                                Intent intent = new Intent(SignUpActivity.this, ProfileCreatingActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                UIActions.showError(SignUpActivity.this, errorMessage);
                            }

                            @Override
                            public void onValidationErrors(List<String> validationErrors) {
                                UIActionsRunner.run(() -> processValidationErrors(
                                        mLoginTextInputEditText,
                                        mPasswordTextInputEditText,
                                        validationErrors
                                ));
                            }
                        }
                );
            }
        });
        mGoToSignInButton.setOnClickListener(v -> {
            // перейти на Activity входа
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Проверить данные, введенные пользователем в поля ввода, на соответствие правилам
     * */
    private boolean validateInputs(
            TextInputEditText loginInput,
            TextInputEditText passwordInput,
            TextInputEditText confirmPasswordInput) {

        if (loginInput.length() == 0) {
            loginInput.setError(getString(R.string.message_error_validation_login));
            return false;
        }
        if (passwordInput.length() == 0) {
            passwordInput.setError(getString(R.string.message_error_validation_password));
            return false;
        }
        final StringBuilder confirmPasswordInputErrors = new StringBuilder();
        if (confirmPasswordInput.length() == 0) {
            confirmPasswordInputErrors.append(getString(R.string.message_error_validation_confirm_password));
        }
        if (!getEditTextString(passwordInput).equals(getEditTextString(confirmPasswordInput))) {
            if (confirmPasswordInputErrors.length() > 0) {
                confirmPasswordInputErrors.append(". ");
            }
            confirmPasswordInputErrors
                    .append(getString(R.string.message_error_validation_confirm_password_matching));
        }
        if (confirmPasswordInputErrors.length() > 0) {
            confirmPasswordInput.setError(confirmPasswordInputErrors.toString());
            return false;
        }
        return true;
    }

    /**
     * При наличии текстов ошибок валидации отобразить их в соответствующих полях ввода
     * */
    private void processValidationErrors(
            TextInputEditText loginInput,
            TextInputEditText passwordInput,
            List<String> validationErrors) {

        final StringBuilder loginErrors = new StringBuilder();
        final StringBuilder passwordErrors = new StringBuilder();

        for (String validationError : validationErrors) {
            if (validationError.toLowerCase(Locale.ROOT).contains("username")
            || validationError.toLowerCase(Locale.ROOT).contains("name is already taken")) {
                loginErrors.append(validationError).append(" ");
            }
            if (validationError.toLowerCase(Locale.ROOT).contains("password")) {
                passwordErrors.append(validationError).append(" ");
            }
        }

        if (loginErrors.length() > 0) {
            loginInput.setError(loginErrors.toString());
        }
        if (passwordErrors.length() > 0) {
            passwordInput.setError(passwordErrors.toString());
        }
    }
}