package org.tyaa.training.client.android.fragments.lessons.word;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.interfaces.IShadowable;
import org.tyaa.training.client.android.models.WordTestModel;

/**
 * Класс фрагмента проверки умения переводить слова с родного на изучаемый язык
 * */
public class WordTranslationTestFragment extends Fragment implements IShadowable {

    // Модель для накопления суммы результатов текущего сеанса проверки знаний слов урока
    private WordTestModel mCurrentWordTestModel;

    private View mWordTranslationTestView;

    private TextView mWordTextView;
    private EditText mTranslationEditText;
    private ImageView mResultImageView;
    private ImageView mWordImageView;
    private ImageView mNextImageView;

    public WordTranslationTestFragment() {
        super(R.layout.fragment_education_process_word_translation_test);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mWordTranslationTestView =
                inflater.inflate(R.layout.fragment_education_process_word_translation_test, container, false);
        mWordTextView =
                mWordTranslationTestView.findViewById(R.id.activityMain_fragmentEducationProcessWordTranslationTest_word_TextView);
        mTranslationEditText =
                mWordTranslationTestView.findViewById(R.id.activityMain_fragmentEducationProcessWordTranslationTest_translation_EditText);
        mResultImageView =
                mWordTranslationTestView.findViewById(R.id.activityMain_fragmentEducationProcessWordTranslationTest_result_ImageView);
        mWordImageView =
                mWordTranslationTestView.findViewById(R.id.activityMain_fragmentEducationProcessWordTranslationTest_wordImage_ImageView);
        return mWordTranslationTestView;
    }

    @Override
    public void shade() {

    }

    @Override
    public void unshade() {

    }
}
