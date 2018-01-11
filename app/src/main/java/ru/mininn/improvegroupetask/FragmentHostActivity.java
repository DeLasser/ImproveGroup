package ru.mininn.improvegroupetask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ru.mininn.improvegroupetask.fragments.EmailFragment;
import ru.mininn.improvegroupetask.fragments.SignInFragment;

public class FragmentHostActivity extends AppCompatActivity {
    private SharedPreferencesHelper mSharedPreferencesHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_host);
        if (savedInstanceState == null) {
            setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(R.id.toolbar));
            initFragment();
        }
    }

    private void initFragment() {
        mSharedPreferencesHelper = new SharedPreferencesHelper(this);
        if (mSharedPreferencesHelper.isUserExist(this)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new EmailFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new SignInFragment()).commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        mSharedPreferencesHelper.clearData(getApplicationContext());
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SignInFragment()).commit();
        return super.onSupportNavigateUp();
    }
}
