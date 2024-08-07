package org.tyaa.training.client.android.fragments.profilecreating;

import static org.tyaa.training.client.android.utils.UIDataExtractor.getEditTextString;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputEditText;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.activities.ProfileCreatingActivity;
import org.tyaa.training.client.android.state.InMemoryLocalState;
import org.tyaa.training.client.android.state.interfaces.IState;
import org.tyaa.training.client.android.utils.ImageConverter;
import org.tyaa.training.client.android.utils.UIActions;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NameAndAvatarStepFragment extends BaseStepFragment implements ImagePickerResultListener {

    private TextView mTitleTextView;
    private CircleImageView mAvatarCircleImageView;
    private TextInputEditText mNameEditText;
    private Button mBackButton;
    private Button mNextButton;

    private ImagePicker mAvatarImagePicker;

    private final IState mState;

    public NameAndAvatarStepFragment() {
        super(R.layout.fragment_profile_creating_name_and_avatar);
        mState = new InMemoryLocalState();
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
            // уменьшение номера шага заполнения профиля на единицу
            ((ProfileCreatingActivity) getActivity()).decreaseStepNumber();
            // получение объекта управления фрагментами у текущей Activity
            FragmentManager fragmentManager =
                    NameAndAvatarStepFragment.this.getActivity().getSupportFragmentManager();
            // немедленно заменить экземпляр текущего фрагмента экземпляром предыдущего
            // (вернуться на предыдущий экран)
            fragmentManager.popBackStackImmediate();
        });

        /* Обработчик клика для перехода на следующий экран */
        mNextButton = view.findViewById(R.id.activityProfileCreating_fragmentNameAndAvatar_next_Button);
        mNextButton.setOnClickListener(v -> {
            // получение объекта управления фрагментами у текущей Activity
            FragmentManager fragmentManager =
                    NameAndAvatarStepFragment.this.getActivity().getSupportFragmentManager();
            // создание экземпляра фрагмента завершения первоначального заполнения профиля
            // с передачей ему заголовка для отображения - "Your profile info"
            Fragment fragment =
                    FinalStepFragment.getInstance(FinalStepFragment.class, "Your profile info");
            // замена текущего фрагмента следующим
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.activityProfileCreating_step_fragment, fragment)
                    .addToBackStack(null)
                    .commit();
            // увеличение номера шага заполнения профиля на единицу
            ((ProfileCreatingActivity) getActivity()).increaseStepNumber();
        });

        // По умолчанию установить логин пользователя в качестве отображаемого имени пользователя
        // в модели профиля
        ((ProfileCreatingActivity) getActivity())
                .getProfileModel().setName(mState.getLogin());

        /* Обработчик изменения текста в элементе ввода имени пользователя */
        mNameEditText =
                view.findViewById(R.id.activityProfileCreating_fragmentNameAndAvatar_name_TextInputEditText);
        mNameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // получить текстовое значение из элемента ввода отображаемого имени пользователя
                String name = s.toString();
                // если поле ввода пусто
                if(name.isBlank()) {
                    // установить логин пользователя в качестве отображаемого имени пользователя
                    // в модели профиля
                    ((ProfileCreatingActivity) getActivity())
                            .getProfileModel().setName(mState.getLogin());
                    // разблокировать кнопку перехода на следующий экран
                    // после возможной предшествующей блокировки
                    mNextButton.setEnabled(true);
                } else {
                    // проверить значение на соответствие правилам
                    if(validateNameInput(mNameEditText)) {
                        // если значение соответствует правилам - установить его
                        // в качестве отображаемого имени пользователя в модели профиля
                        ((ProfileCreatingActivity) getActivity()).getProfileModel().setName(name);
                        // разблокировать кнопку перехода на следующий экран
                        // после возможной предшествующей блокировки
                        mNextButton.setEnabled(true);
                    } else {
                        // иначе - заблокировать кнопку перехода на следующий экран
                        mNextButton.setEnabled(false);
                    }
                }
            }
        });

        /* Настройка функциональности выбора аватара */
        // создание объекта выбора изображения из галлереи / получения фотографии с камеры
        mAvatarImagePicker =
                ImagePicker.Companion.registerImagePicker(this, this);
        // получение объекта управления элементом выводв изображения в круглой рамке
        mAvatarCircleImageView = view.findViewById(R.id.activityProfileCreating_fragmentNameAndAvatar_avatar_CircleImageView);
        // установка обработчика клика по элементу выводв изображения в круглой рамке
        mAvatarCircleImageView.setOnClickListener(v -> {
            // настройка объекта выбора изображения из галлереи / получения фотографии с камеры
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
            // запуск выбора изображения из галлереи / получения фотографии с камеры
            mAvatarImagePicker.open(PickerType.GALLERY);
        });
    }

    @Override
    public void onImagePick(@Nullable Uri uri) {
        /* обработка события завершения выбора изображения из галлереи / получения фотографии с камеры */
        // установка полученного изображения в элемент вывода изображения в круглой рамке
        mAvatarCircleImageView.setImageURI(uri);
        // создание base64-строки на основе uri изображения
        try {
            String avatarBase64 = ImageConverter.uriToBase64(this.getActivity(), uri);
            // если преобразование удалось -
            // установить полученную строку в качестве значения поля аватара в модели профиля
            ((ProfileCreatingActivity) getActivity()).getProfileModel().setAvatar(avatarBase64);
        } catch (IOException e) {
            // иначе - сообщить об ошибке установки аватара и предложить выбрать другое изображение
            UIActions.showError(this.getActivity(), "Ошибка установки аватара. Попробуйте выбрать другое изображение.");
        }
    }

    @Override
    public void onMultiImagePick(@Nullable List<? extends Uri> list) {
        // обработка множественного выбора изображений не предусмотрена,
        // так как выбирается всегда одно изображение для аватара
    }

    /**
     * Проверить строку из поля ввода имени пользователя на соответствие правилам
     * */
    private boolean validateNameInput(TextInputEditText nameInput) {
        String name = getEditTextString(nameInput);
        if (name.length() <= 100) {
            return true;
        } else {
            nameInput.setError(getString(R.string.message_error_validation_name));
            return false;
        }
    }
}