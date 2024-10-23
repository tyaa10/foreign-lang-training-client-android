package org.tyaa.training.client.android.activities;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.interfaces.IShadowable;
import org.tyaa.training.client.android.models.UserModel;
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
 * Логика экрана дополнительной заставки приложения,
 * во время отображения которой на сервер отправляется http-запрос
 * для выяснения состояния аутентификации пользователя
 * */
public class SplashScreenActivity extends AppCompatActivity implements IShadowable {

    private final IAuthService mAuthService = new HttpAuthService();
    private final IProfileService mProfileService = new HttpProfileService();

    private final IState mState = new InMemoryLocalState();

    private View mShadowView;
    private Button mReloadButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // подключение представления экрана дополнительной заставки приложения
        setContentView(R.layout.activity_splash_screen);

        // инициализация объекта доступа к виджету затенения экрана
        mShadowView = findViewById(R.id.activitySplashScreen_shadow_View);

        // получение объекта управления кнопкой вызова повторной попытки загрузки приложения
        mReloadButton = findViewById(R.id.activitySplashScreen_reload_Button);

        // установка обработчика клика по кнопке вызова повторной попытки загрузки приложения
        mReloadButton.setOnClickListener(v -> {
            // сделать кнопку снова невидимой (это её состояние по умолчанию)
            mReloadButton.setVisibility(INVISIBLE);
            // принудительный повторный вызов проверки наличия входа в учётную запись пользователя
            checkAuth();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // безусловный первый вызов проверки наличия входа в учётную запись пользователя
        checkAuth();
    }

    /**
     * Проверка наличия входа в учётную запись пользователя
     * */
    private void checkAuth() {
        // затенить и сделать неинтерактивным представление
        shade();
        // отобразить бесконечный прогресс
        UIActions.showInfinityProgressToast(this);
        // Запрос к серверу: выполнен ли вход в учётную запись?
        mAuthService.checkUser(new IResultHandler<>() {
            // если запрос к конечной точке "проверить пользователя" на сервере успешно выполнен
            @Override
            public void onSuccess(UserModel result) {
                // безусловная выдержка времени 3 секунды,
                // чтобы пользователь гарантированно успел увидеть экран заставки
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
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
                                // если данные профиля получены:
                                // 1. установить объект модели профиля в объект текущего состояния приложения
                                mState.setProfile(profile);
                                // 2. подготовить переход на главную Activity
                                intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            } else {
                                // иначе - подготовить переход на Activity начального заполнения профиля
                                intent = new Intent(SplashScreenActivity.this, ProfileCreatingActivity.class);
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
                            // отобразить всплывающее окно с сообщением об ошибке
                            UIActions.showError(SplashScreenActivity.this, errorMessage);
                        }
                    });
                } else {
                    // снять с представления тень и вернуть интерактивность
                    unshade();
                    // скрыть бесконечный прогресс
                    UIActions.closeInfinityProgressToast();
                    // если вход в учётную запись ранее не был выполнен -
                    // перейти на Activity с формой входа в учётную запись
                    Intent intent = new Intent(SplashScreenActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
            }
            // если запрос к конечной точке "проверить пользователя" на сервере не смог выполниться,
            // или выполнился, но был получен ответ об ошибке
            @Override
            public void onFailure(String errorMessage) {
                // снять с представления тень и вернуть интерактивность
                unshade();
                // скрыть бесконечный прогресс
                UIActions.closeInfinityProgressToast();
                // сделать видимой кнопку попытки принудительной повторной проверки пользователя
                UIActionsRunner.run(() -> mReloadButton.setVisibility(VISIBLE));
                // вывести сообщение об ошибке в отладочную консоль
                Log.println(Log.ERROR, getString(R.string.message_error), errorMessage);
                // отобразить сообщение об ошибке во всплывающем окне
                UIActionsRunner.run(() -> Toast.makeText(SplashScreenActivity.this, errorMessage, Toast.LENGTH_LONG).show());
            }
        });
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
