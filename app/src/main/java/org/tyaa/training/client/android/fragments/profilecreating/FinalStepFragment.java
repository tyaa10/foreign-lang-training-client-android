package org.tyaa.training.client.android.fragments.profilecreating;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.app.imagepickerlibrary.ImagePicker;
import com.app.imagepickerlibrary.listener.ImagePickerResultListener;
import com.app.imagepickerlibrary.model.PickExtension;
import com.app.imagepickerlibrary.model.PickerType;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.activities.ProfileCreatingActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FinalStepFragment extends BaseStepFragment implements ImagePickerResultListener {

    private TextView mTitleTextView;
    private CircleImageView mAvatarCircleImageView;
    private Button mBackButton;
    private Button mNextButton;

    private ImagePicker mAvatarImagePicker;

    public FinalStepFragment() {
        super(R.layout.fragment_profile_creating_name_and_avatar);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        /* Установка заголовка экрана */
        mTitleTextView = view.findViewById(R.id.activityProfileCreating_fragmentNameAndAvatar_title_TextView);
        mTitleTextView.setText(mTitleParam);
        /* Обработчик клика для перехода на предыдущий экран */
        mBackButton = view.findViewById(R.id.activityProfileCreating_fragmentNameAndAvatar_back_Button);
        mBackButton.setOnClickListener(v -> {
            // получение объекта управления фрагментами у текущей Activity
            FragmentManager fragmentManager =
                    FinalStepFragment.this.getActivity().getSupportFragmentManager();
            // немедленно заменить экземпляр текущего фрагмента экземпляром предыдущего
            // (вернуться на предыдущий экран)
            fragmentManager.popBackStackImmediate();
        });
        /* Обработчик клика для перехода на следующий экран */
        mNextButton = view.findViewById(R.id.activityProfileCreating_fragmentNameAndAvatar_next_Button);
        mNextButton.setOnClickListener(v -> {
            // получение объекта управления фрагментами у текущей Activity
            FragmentManager fragmentManager =
                    FinalStepFragment.this.getActivity().getSupportFragmentManager();
            // создание экземпляра фрагмента необязательного задания имени и аватара для профиля
            // с передачей ему заголовка для отображения - "Name and Avatar"
            Fragment fragment =
                    LevelStepFragment.getInstance(FinalStepFragment.class, "Name and Avatar");
            // замена текущего фрагмента следующим
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.activityProfileCreating_step_fragment, fragment)
                    .commit();
            // увеличение номера шага заполнения профиля на единицу
            ((ProfileCreatingActivity) getActivity()).increaseStepNumber();
        });
        /*  */

        mAvatarCircleImageView = view.findViewById(R.id.activityProfileCreating_fragmentNameAndAvatar_avatar_CircleImageView);
        mAvatarCircleImageView.setOnClickListener(v -> {
            mAvatarImagePicker
                    .title("Choose Your Avatar")
                    .multipleSelection(false)
                    .showCountInToolBar(false)
                    .showFolder(true)
                    .cameraIcon(true)
                    .doneIcon(true)
                    .allowCropping(true)
                    .compressImage(false, 100)
                    .maxImageSize(1)
                    .extension(PickExtension.PNG);
            mAvatarImagePicker.open(PickerType.GALLERY);
        });
    }

    @Override
    public void onImagePick(@Nullable Uri uri) {

        mAvatarCircleImageView.setImageURI(uri);
    }

    @Override
    public void onMultiImagePick(@Nullable List<? extends Uri> list) {

    }
}