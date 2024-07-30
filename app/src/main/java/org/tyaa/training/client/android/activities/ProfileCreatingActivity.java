package org.tyaa.training.client.android.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.fragments.profilecreating.LanguagesStepFragment;
import org.tyaa.training.client.android.fragments.profilecreating.LevelStepFragment;
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

    private void setInitialFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment =
                LanguagesStepFragment.getInstance(LanguagesStepFragment.class, "Languages");
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
}