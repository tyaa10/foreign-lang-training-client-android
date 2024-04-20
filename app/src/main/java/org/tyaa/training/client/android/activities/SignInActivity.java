package org.tyaa.training.client.android.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.tyaa.training.client.android.R;

/**
 * Activity формы входа в учётную запись
 * или перехода к форме регистрации
 * */
public class SignInActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }
}
