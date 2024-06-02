package org.tyaa.training.client.android.fragments.profilecreating;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.tyaa.training.client.android.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NameAndAvatarStepFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NameAndAvatarStepFragment extends BaseStepFragment {

    public NameAndAvatarStepFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_languages, container, false);
    }
}