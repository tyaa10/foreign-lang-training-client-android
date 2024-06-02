package org.tyaa.training.client.android.fragments.profilecreating;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tyaa.training.client.android.R;

import java.lang.reflect.InvocationTargetException;

public class BaseStepFragment extends Fragment {

    private static final String TITLE_PARAM = "TITLE_PARAM";

    protected String mTitleParam;

    public BaseStepFragment() {
        super();
    }

    public BaseStepFragment(int fragmentLanguages) {
        super(fragmentLanguages);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param titleParam Parameter 1.
     * @return A new instance of fragment LanguagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BaseStepFragment getInstance(Class<? extends BaseStepFragment> clazz, String titleParam) {
        BaseStepFragment fragment = null;
        try {
            fragment = clazz.getConstructor().newInstance();
        } catch (IllegalAccessException | java.lang.InstantiationException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        Bundle args = new Bundle();
        args.putString(TITLE_PARAM, titleParam);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitleParam = getArguments().getString(TITLE_PARAM);
        }
    }
}