package org.tyaa.training.client.android.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.fragments.profilecreating.LanguagesStepFragment;
import org.tyaa.training.client.android.interfaces.IShadowable;
import org.tyaa.training.client.android.models.UserProfileModel;
import org.tyaa.training.client.android.utils.UIActionsRunner;

public class ProfileCreatingActivity extends AppCompatActivity implements IShadowable {

    private View mShadowView;
    private TextView mStepNumberTextView;

    private final UserProfileModel mProfileModel;

    public ProfileCreatingActivity () {
        // подключение представления Activity
        super(R.layout.activity_profile_creating);
        // инициализация временной модели профиля,
        // в которой будут накапливаться данные по мере прохождения пользователем
        // последовательности экранов создания профиля
        mProfileModel = new UserProfileModel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // инициализация объекта доступа к виджету затенения экрана
        mShadowView = findViewById(R.id.activityProfileCreating_shadow_View);

        // установка номера первого шага создания профиля в виджет вывода текста
        mStepNumberTextView = findViewById(R.id.activityProfileCreating_step_number_TextView);
        mStepNumberTextView.setText("1");

        // установка фрагмента первого экрана в цепочке экранов создания профиля
        if (savedInstanceState == null) {
            setInitialFragment();
        }
    }

    /**
     * Создание и подключение фрагмента первого экрана из последовательности заполнения профиля
     * */
    private void setInitialFragment() {
        // получение объекта управления фрагментами у текущей Activity
        FragmentManager fragmentManager = getSupportFragmentManager();
        // создание экземпляра фрагмента выбра родного и изучаемого языков для профиля
        // с передачей ему заголовка для отображения - "Languages"
        Fragment fragment =
                LanguagesStepFragment.getInstance(LanguagesStepFragment.class, "Languages");
        // замена фрагмента-заглушки первым фрагментом последовательности заполнения профиля
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.activityProfileCreating_step_fragment, fragment)
                .commit();
    }

    public UserProfileModel getProfileModel() {
        return mProfileModel;
    }

    public void increaseStepNumber() {
        mStepNumberTextView.setText(String.valueOf(Integer.valueOf(mStepNumberTextView.getText().toString()) + 1));
    }

    public void decreaseStepNumber() {
        mStepNumberTextView.setText(String.valueOf(Integer.valueOf(mStepNumberTextView.getText().toString()) - 1));
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