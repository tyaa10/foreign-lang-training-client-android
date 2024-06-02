package org.tyaa.training.client.android.fragments.profilecreating;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.tyaa.training.client.android.R;

public class LanguagesStepFragment extends BaseStepFragment {

    private TextView mTitleTextView;
    private Button mBackButton;
    private Button mNextButton;

    public LanguagesStepFragment() {
        super(R.layout.fragment_languages);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleTextView = view.findViewById(R.id.profile_creating_fragmentLanguages_title_TextView);
        mTitleTextView.setText(mTitleParam);
        mNextButton = view.findViewById(R.id.profile_creating_fragmentLanguages_next_Button);
        mNextButton.setOnClickListener(v -> {
            FragmentManager fragmentManager =
                    LanguagesStepFragment.this.getActivity().getSupportFragmentManager();
            Fragment fragment =
                    LevelStepFragment.getInstance(LevelStepFragment.class, "Level");
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.activityProfileCreating_step_fragment, fragment)
                    .commit();
        });
    }
}