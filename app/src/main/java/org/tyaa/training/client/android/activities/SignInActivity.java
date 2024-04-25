package org.tyaa.training.client.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.services.HttpAuthService;
import org.tyaa.training.client.android.services.interfaces.IAuthService;
import org.tyaa.training.client.android.utils.UIActionsRunner;

import java.util.Objects;

/**
 * Логика формы входа в учётную запись
 * или перехода к форме регистрации
 * */
public class SignInActivity extends AppCompatActivity {
    private final IAuthService mAuthService = new HttpAuthService();
    private TextInputEditText mLoginTextInputEditText;
    private TextInputEditText mPasswordTextInputEditText;
    private Button mSignInButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mSignInButton = findViewById(R.id.activitySignIn_signIn_Button);
        mLoginTextInputEditText =
                SignInActivity.this.findViewById(R.id.activitySignIn_login_TextInputEditText);
        mPasswordTextInputEditText =
                SignInActivity.this.findViewById(R.id.activitySignIn_password_TextInputEditText);
        mSignInButton.setOnClickListener(v -> {
            if (validateInputs(mLoginTextInputEditText, mPasswordTextInputEditText)) {
                mAuthService.signIn(
                        Objects.requireNonNull(mLoginTextInputEditText.getText()).toString(),
                        Objects.requireNonNull(mPasswordTextInputEditText.getText()).toString(),
                        new IResponseHandler() {
                            @Override
                            public void onSuccess() {
                                // если вход в учётную запись выполнен - перейти на главную Activity
                                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                Log.println(Log.ERROR, getString(R.string.message_error), errorMessage);
                                UIActionsRunner.run(() -> Toast.makeText(SignInActivity.this, errorMessage, Toast.LENGTH_LONG).show());
                            }
                        }
                );
            }

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
