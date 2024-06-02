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

import org.tyaa.training.client.android.R;

public class LevelStepFragment extends BaseStepFragment {

    private TextView mTitleTextView;
    private Button mBackButton;
    private Button mNextButton;

    public LevelStepFragment() {
        super(R.layout.fragment_levels);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleTextView = view.findViewById(R.id.profile_creating_fragmentLevels_title_TextView);
        mTitleTextView.setText(mTitleParam);
    }
}