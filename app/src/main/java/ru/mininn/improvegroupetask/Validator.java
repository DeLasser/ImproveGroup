package ru.mininn.improvegroupetask;

import android.util.Patterns;
import android.widget.EditText;

import java.util.regex.Pattern;

public class Validator {

    public boolean isEmailFieldValid(EditText editText) {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(editText.getText()).matches()) {
            return true;
        } else {
            setError(editText, R.string.invalid_mail);
            return false;
        }
    }

    public boolean isPhoneFieldValid(EditText editText) {
        if (Patterns.PHONE.matcher(editText.getText()).matches() && editText.getText().length() == 12) {
            return true;
        } else {
            setError(editText, R.string.invalid_phone);
            return false;
        }
    }

    public boolean isPasswordFieldValid(EditText editText) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]");
        Pattern patternNumbers = Pattern.compile("[0-9]");
        if (pattern.matcher(editText.getText()).find() && patternNumbers.matcher(editText.getText()).find()) {
            return true;
        } else {
            editText.setError(editText.getContext().getResources().getString(R.string.invalid_password), null);
            return false;
        }
    }

    private void setError(EditText editText, int errorResId) {
        editText.setError(editText.getContext().getResources().getString(errorResId));
    }
}
