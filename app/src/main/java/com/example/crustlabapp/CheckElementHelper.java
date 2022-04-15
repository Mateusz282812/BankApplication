package com.example.crustlabapp;

import android.widget.EditText;

import java.util.ArrayList;

public class CheckElementHelper {


    public static boolean checkIsEmpty(EditText editText) {
        if (editText.getText().toString().equals("")) {
            editText.setError("The field cannot be empty");
            return true;
        } else {
            return false;
        }

    }

    public static boolean checkPassword(EditText editText) {
        if (editText.getText().toString().equals("")) {
            editText.setError("The field cannot be empty");
            return false;
        } else if (editText.getText().toString().length() < 8) {
            editText.setError("Password must be at least 8 characters long");
            return false;
        } else
            return true;
    }

}
