package org.tyaa.training.client.android.activities;

import static org.tyaa.training.client.android.utils.UIDataExtractor.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.interfaces.IShadowable;
import org.tyaa.training.client.android.models.UserProfileModel;
import org.tyaa.training.client.android.services.HttpAuthService;
import org.tyaa.training.client.android.services.HttpProfileService;
import org.tyaa.training.client.android.services.interfaces.IAuthService;
import org.tyaa.training.client.android.services.interfaces.IProfileService;
import org.tyaa.training.client.android.state.InMemoryLocalState;
import org.tyaa.training.client.android.state.interfaces.IState;
import org.tyaa.training.client.android.utils.UIActions;
import org.tyaa.training.client.android.utils.UIActionsRunner;

/**
 * Логика экрана формы входа в учётную запись
 * или перехода к форме регистрации
 * */
public class SignInActivity extends AppCompatActivity implements IShadowable {

    private final IAuthService mAuthService = new HttpAuthService();
    private final IProfileService mProfileService = new HttpProfileService();

    private final IState mState = new InMemoryLocalState();

    private View mShadowView;
    private TextInputEditText mLoginTextInputEditText;
    private TextInputEditText mPasswordTextInputEditText;
    private Button mSignInButton;
    private Button mGoToSignUpButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // подключение представления входа в учётную запись
        setContentView(R.layout.activity_sign_in);

        /* инициализация объектов доступа к постоянным элементам представления */
        // инициализация объекта доступа к виджету затенения экрана
        mShadowView = findViewById(R.id.activitySignIn_shadow_View);
        mLoginTextInputEditText =
                findViewById(R.id.activitySignIn_login_TextInputEditText);
        mPasswordTextInputEditText =
                findViewById(R.id.activitySignIn_password_TextInputEditText);
        mSignInButton = findViewById(R.id.activitySignIn_signIn_Button);
        mGoToSignUpButton = findViewById(R.id.activitySignIn_goToSignUp_Button);

        // установка обработчиков событий для постоянных элементов представления
        mSignInButton.setOnClickListener(v -> {
            if (validateInputs(mLoginTextInputEditText, mPasswordTextInputEditText)) {
                // затенить и сделать неинтерактивным представление
                shade();
                // отобразить бесконечный прогресс
                UIActions.showInfinityProgressToast(this);
                // отправить серверу запрос входа в учётную запись
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
                                            // Если данные профиля получены -
                                            // 1. установить объект модели профиля в объект текущего состояния приложения
                                            mState.setProfile(profile);
                                            // 2. подготовить переход на главную Activity
                                            intent = new Intent(SignInActivity.this, MainActivity.class);
                                        } else {
                                            // иначе - подготовить переход на Activity начального заполнения профиля
                                            intent = new Intent(SignInActivity.this, ProfileCreatingActivity.class);
                                        }
                                        // снять с представления тень и вернуть интерактивность
                                        unshade();
                                        // скрыть бесконечный прогресс
                                        UIActions.closeInfinityProgressToast();
                                        // выполнить переход на Activity, определённую выше
                                        startActivity(intent);
                                    }
                                    // если выполнение запроса провалилось
                                    @Override
                                    public void onFailure(String errorMessage) {
                                        // снять с представления тень и вернуть интерактивность
                                        unshade();
                                        // скрыть бесконечный прогресс
                                        UIActions.closeInfinityProgressToast();
                                        // отобразить сообщение об ошибке во всплывающем окне
                                        UIActions.showError(SignInActivity.this, errorMessage);
                                    }
                                });
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                // снять с представления тень и вернуть интерактивность
                                unshade();
                                // скрыть бесконечный прогресс
                                UIActions.closeInfinityProgressToast();
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

    @Override
    public void shade() {
        UIActionsRunner.run(() -> mShadowView.setVisibility(View.VISIBLE));
    }

    @Override
    public void unshade() {
        UIActionsRunner.run(() -> mShadowView.setVisibility(View.GONE));
    }
}
