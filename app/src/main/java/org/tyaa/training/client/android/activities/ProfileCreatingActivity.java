package org.tyaa.training.client.android.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.fragments.profilecreating.LanguagesStepFragment;
import org.tyaa.training.client.android.models.UserProfileModel;

public class ProfileCreatingActivity extends AppCompatActivity {

    private TextView mStepNumberTextView;

    private final UserProfileModel mProfileModel;

    public ProfileCreatingActivity () {
        super(R.layout.activity_profile_creating);
        mProfileModel = new UserProfileModel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStepNumberTextView = findViewById(R.id.activityProfileCreating_step_number_TextView);
        mStepNumberTextView.setText("1");
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
}