package com.example.crustlabapp;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class OpenFragment {

    public static void openFragment(Activity activity, Fragment fragment, Integer containerViewId) {
        FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(containerViewId, fragment, null)
                .commit();
    }
}
