package com.example.crustlabapp.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.crustlabapp.DBHandler;
import com.example.crustlabapp.Interfaces.IOnBackPressed;
import com.example.crustlabapp.OpenFragment;
import com.example.crustlabapp.R;
import com.example.crustlabapp.SharedPreferencesHelper;
import com.example.crustlabapp.ui.account.LogInFragment;
import com.example.crustlabapp.ui.home.HomeFragment;
import com.example.crustlabapp.ui.payments.PaymentsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final Integer ID_CONTAINER = R.id.nav_host_fragment_activity_main;
    public static Double Provision;
    public static Double EURToPLNExchangeRate;
    public static Double PLNToEURExchangeRate;
    public static Double USDToPLNExchangeRate;
    public static Double PLNToUSDExchangeRate;
    public static Double USDToEURExchangeRate;
    public static Double EURToUSDExchangeRate;
    private DBHandler dbHandler;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        SharedPreferences sharedPreferences = SharedPreferencesHelper.getSharedPreferences(this);
        setExchangeRate();
        sharedPreferences.edit().putString("provision", String.valueOf(Provision)).apply();
        if (sharedPreferences.getBoolean("isLogged", false)) {
            OpenFragment.openFragment(this, HomeFragment.newInstance(), ID_CONTAINER);
        } else {
            OpenFragment.openFragment(this, LogInFragment.newInstance(), ID_CONTAINER);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(ID_CONTAINER);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

    private void setExchangeRate() {
        //TODO: W tym miejscu należy pobierać aktualny kurs poszczególnych walut
        //TODO : (PROVISION, EURToPLNExchangeRate, USDToPLNExchangeRate, USDToEURExchangeRate, ...) ustalone sztywno
        // nie byłem pewien na jakiej zasadzie miałaby być ustawiana prowizja,
        // dlatego ustawiona w momencie startu aplikacji stała wartość
        Provision = 0.02;
        EURToPLNExchangeRate = 4.63;
        PLNToEURExchangeRate = 0.22;
        USDToPLNExchangeRate = 4.26;
        PLNToUSDExchangeRate = 0.23;
        USDToEURExchangeRate = 0.92;
        EURToUSDExchangeRate = 1.09;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeItemBottomNavigation:
                OpenFragment.openFragment(this, HomeFragment.newInstance(), ID_CONTAINER);
                break;
            case R.id.transferItemBottomNavigation:
                OpenFragment.openFragment(this, PaymentsFragment.newInstance(), ID_CONTAINER);
                break;
        }
        return false;
    }
}