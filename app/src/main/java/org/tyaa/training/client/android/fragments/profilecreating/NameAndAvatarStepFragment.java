package org.tyaa.training.client.android.fragments.profilecreating;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.tyaa.training.client.android.R;

public class NameAndAvatarStepFragment extends BaseStepFragment {

    private TextView mTitleTextView;

    public NameAndAvatarStepFragment() {
        super(R.layout.fragment_name_and_avatar);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        /* Установка заголовка экрана */
        mTitleTextView = view.findViewById(R.id.profile_creating_fragmentNameAndAvatar_title_TextView);
        mTitleTextView.setText(mTitleParam);
    }
}