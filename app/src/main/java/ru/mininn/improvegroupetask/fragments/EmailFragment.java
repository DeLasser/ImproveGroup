package ru.mininn.improvegroupetask.fragments;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import ru.mininn.improvegroupetask.R;
import ru.mininn.improvegroupetask.SharedPreferencesHelper;

public class EmailFragment extends Fragment {
    private SimpleDraweeView mSimpleDraweeView;
    private TextView mEmail;
    private TextView mPhone;
    private TextView mPassword;
    private Button mEmailButton;
    private SharedPreferencesHelper mSharedPreferencesHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email, container, false);
        mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity());
        initActionBar();
        initView(view);
        return view;
    }

    private void initView(View view) {
        mSimpleDraweeView = view.findViewById(R.id.simple_drawee_view);
        mEmail = view.findViewById(R.id.email_text);
        mPhone = view.findViewById(R.id.phone_text);
        mPassword = view.findViewById(R.id.password_text);
        mEmailButton = view.findViewById(R.id.button);
        mEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewCompat.setTransitionName(mSimpleDraweeView, "simpleDraweeView");
            ViewCompat.setTransitionName(mEmailButton, "button");
        }
        mSimpleDraweeView.setImageDrawable(new BitmapDrawable(getActivity().getResources(), mSharedPreferencesHelper.getPhotoBitmap(getActivity())));
        mEmail.setText(mSharedPreferencesHelper.getEmail());
        mPhone.setText(mSharedPreferencesHelper.getPhone());
        mPassword.setText(mSharedPreferencesHelper.getPassword());
    }

    private void initActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
    }

    private void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_SUBJECT, getActivity().getApplicationInfo().loadLabel(getActivity().getPackageManager()) + ", "+ mSharedPreferencesHelper.getEmail() + ", "
                + mSharedPreferencesHelper.getPhone() + ", " + mSharedPreferencesHelper.getPassword());
        intent.putExtra(Intent.EXTRA_TEXT, "Email: \n\r" + mSharedPreferencesHelper.getEmail()
                + "\nPhone: \n\r" + mSharedPreferencesHelper.getPhone()
                + "\nPassword: \n" + "" + mSharedPreferencesHelper.getPassword());
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(mSharedPreferencesHelper.getPhotoFile(getActivity())));
        startActivityForResult(intent, 0);
    }
}
