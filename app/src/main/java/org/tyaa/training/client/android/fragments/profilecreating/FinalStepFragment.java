package org.tyaa.training.client.android.fragments.profilecreating;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.activities.MainActivity;
import org.tyaa.training.client.android.activities.ProfileCreatingActivity;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.UserProfileModel;
import org.tyaa.training.client.android.services.HttpProfileService;
import org.tyaa.training.client.android.services.interfaces.IProfileService;
import org.tyaa.training.client.android.state.InMemoryLocalState;
import org.tyaa.training.client.android.state.interfaces.IState;
import org.tyaa.training.client.android.utils.ImageConverter;
import org.tyaa.training.client.android.utils.UIActions;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Класс фрагмента завершающего экрана начального заполнения профиля,
 * на котором отображаются все ранее введенные данные
 * */
public class FinalStepFragment extends BaseStepFragment {

    private TextView mTitleTextView;
    private CircleImageView mAvatarCircleImageView;
    private TextView mNameTextView;
    private TextView mNativeLanguageTextView;
    private TextView mLearningLanguageTextView;
    private TextView mLevelTextView;
    private Button mBackButton;
    private Button mSaveButton;

    private final IProfileService mProfileService;
    private final IState mState;

    public FinalStepFragment() {
        super(R.layout.fragment_profile_creating_final);
        mProfileService = new HttpProfileService();
        mState = new InMemoryLocalState();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Ссылка на объект модели профиля, в котором накапливались данные, введенные пользователем
        UserProfileModel completedProfileModel =
                ((ProfileCreatingActivity) getActivity()).getProfileModel();

        /* Установка заголовка экрана */
        mTitleTextView = view.findViewById(R.id.activityProfileCreating_fragmentFinal_title_TextView);
        mTitleTextView.setText(mTitleParam);

        /* Отображение всех текстовых данных из модели профиля */
        // вывод отображаемого имени пользователя
        mNameTextView = view.findViewById(R.id.activityProfileCreating_fragmentFinal_name_TextView);
        mNameTextView.setText(completedProfileModel.getName());
        // вывод названия родного языка пользователя
        mNativeLanguageTextView = view.findViewById(R.id.activityProfileCreating_fragmentFinal_nativeLanguage_TextView);
        mNativeLanguageTextView.setText(completedProfileModel.getNativeLanguageName());
        // вывод названия изучаемого языка
        mLearningLanguageTextView = view.findViewById(R.id.activityProfileCreating_fragmentFinal_learningLanguage_TextView);
        mLearningLanguageTextView.setText(completedProfileModel.getLearningLanguageName());
        // вывод названия уровня изучения языка
        mLevelTextView = view.findViewById(R.id.activityProfileCreating_fragmentFinal_level_TextView);
        mLevelTextView.setText(completedProfileModel.getLevelName());

        /* Отображение аватара, если ранее он был установлен пользователем */
        if(completedProfileModel.getAvatar() != null && !completedProfileModel.getAvatar().isBlank()) {
            // получение Java-объекта доступа к элементу вывода изображения в круглой рамке
            mAvatarCircleImageView =
                    view.findViewById(R.id.activityProfileCreating_fragmentFinal_avatar_CircleImageView);
            // преобразование base64-строки из поля аватара модели профиля в данные растрового изображения
            // и подключение этих данных к элементу вывода изображения в круглой рамке
            mAvatarCircleImageView.setImageBitmap(
                    ImageConverter.base64ToBitmap(this.getActivity(), completedProfileModel.getAvatar())
            );
        }

        /* Обработчик клика для перехода на предыдущий экран */
        mBackButton = view.findViewById(R.id.activityProfileCreating_fragmentFinal_back_Button);
        mBackButton.setOnClickListener(v -> {
            // уменьшение номера шага заполнения профиля на единицу
            ((ProfileCreatingActivity) getActivity()).decreaseStepNumber();
            // получение объекта управления фрагментами у текущей Activity
            FragmentManager fragmentManager =
                    FinalStepFragment.this.getActivity().getSupportFragmentManager();
            // немедленно заменить экземпляр текущего фрагмента экземпляром предыдущего
            // (вернуться на предыдущий экран)
            fragmentManager.popBackStackImmediate();
        });

        /* Обработчик клика для завершения создания профиля с сохранением всех введенных данных*/
        mSaveButton = view.findViewById(R.id.activityProfileCreating_fragmentFinal_save_Button);
        mSaveButton.setOnClickListener(v -> {
            // отправка данных профиля на сервер для сохранения
            mProfileService.createProfile(completedProfileModel, new IResultHandler<>() {

                @Override
                public void onSuccess(UserProfileModel profile) {
                    // Если профиль создан успешно -
                    // 1. установить объект модели профиля в объект текущего состояния приложения
                    mState.setProfile(profile);
                    // 2. выполнить переход на главную Activity
                    Intent intent =
                            new Intent(FinalStepFragment.this.getActivity(), MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(String errorMessage) {
                    UIActions.showError(FinalStepFragment.this.getActivity(), errorMessage);
                }
            });
        });
    }
}