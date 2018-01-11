package ru.mininn.improvegroupetask.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.ChangeBounds;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.drawable.RoundedBitmapDrawable;
import com.facebook.drawee.view.SimpleDraweeView;

import ru.mininn.improvegroupetask.R;
import ru.mininn.improvegroupetask.SharedPreferencesHelper;
import ru.mininn.improvegroupetask.Validator;

public class SignInFragment extends Fragment {
    private final int CAMERA_RESULT = 0;

    private SimpleDraweeView mSimpleDraweeView;
    private EditText mEmail;
    private EditText mPhone;
    private EditText mPassword;
    private TextView mTextCounter;
    private TextView mErrorText;
    private Button mConfirmButton;
    private SharedPreferencesHelper mSharedPreferencesHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity());
        initActionBar();
        initView(view);
        return view;
    }

    private void initView(View view) {
        mEmail = view.findViewById(R.id.email_text_view);
        mPhone = view.findViewById(R.id.phone_text_view);
        mPassword = view.findViewById(R.id.password_text_view);
        mTextCounter = view.findViewById(R.id.text_counter);
        mConfirmButton = view.findViewById(R.id.confirm_button);
        mErrorText = view.findViewById(R.id.error_text);
        mSimpleDraweeView = view.findViewById(R.id.simple_drawee_view);
        mTextCounter.setText(String.format(getResources().getString(R.string.counter), 0));
        mSimpleDraweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_RESULT);
                mErrorText.setVisibility(View.GONE);
            }
        });
        mPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTextCounter.setText(String.format(getResources().getString(R.string.counter), s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            Validator validator = new Validator();

            @Override
            public void onClick(View v) {
                if (validator.isEmailFieldValid(mEmail) && validator.isPhoneFieldValid(mPhone) && validator.isPasswordFieldValid(mPassword)) {
                    if (mSharedPreferencesHelper.getPhotoBitmap(getActivity()) == null) {
                        mErrorText.setVisibility(View.VISIBLE);
                        return;
                    }
                    finishSignIn();
                }
            }
        });
    }

    private void finishSignIn() {
        mSharedPreferencesHelper.saveUserInfo(mEmail.getText().toString()
                , mPhone.getText().toString(), mPassword.getText().toString());
        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        EmailFragment emailFragment = new EmailFragment();
        //here i should add transition manager but i think it's not required for so small app. Hardcoded names is mad manner but without over engineering in so small case.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewCompat.setTransitionName(mSimpleDraweeView, "simpleDraweeView");
            ViewCompat.setTransitionName(mConfirmButton, "button");
            fragmentTransaction.addSharedElement(mSimpleDraweeView, "simpleDraweeView");
            fragmentTransaction.addSharedElement(mConfirmButton, "confirmButton");
            emailFragment.setEnterTransition(new ChangeBounds());
            emailFragment.setSharedElementEnterTransition(new ChangeBounds());
        }
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, emailFragment).commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_RESULT && resultCode == Activity.RESULT_OK) {
            if (data.getExtras().get("data") != null) {
                mSharedPreferencesHelper.cachePhoto(getActivity(), (Bitmap) data.getExtras().get("data"));
                RoundedBitmapDrawable bitmapDrawable = new RoundedBitmapDrawable(getResources(), mSharedPreferencesHelper.getPhotoBitmap(getActivity()));
                bitmapDrawable.setCircle(true);
                bitmapDrawable.setBorder(getResources().getColor(R.color.imageBorder), getResources().getDimension(R.dimen.image_border));
                mSimpleDraweeView.setImageDrawable(bitmapDrawable);
                mSimpleDraweeView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
    }

    private void initActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }
}
