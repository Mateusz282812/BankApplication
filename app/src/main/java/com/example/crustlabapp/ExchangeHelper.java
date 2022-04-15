package com.example.crustlabapp;

import com.example.crustlabapp.ui.MainActivity;

public class ExchangeHelper {

    public static Double exchangePLNToEUR(Double actualBalance){
        return  actualBalance * MainActivity.PLNToEURExchangeRate;
    }

    public static Double exchangeUSDToEUR(Double actualBalance){
        return  actualBalance * MainActivity.USDToEURExchangeRate;
    }

    public static Double exchangePLNToUSD(Double actualBalance){
        return  actualBalance * MainActivity.PLNToUSDExchangeRate;
    }

    public static Double exchangeUSDToPLN(Double actualBalance){
        return  actualBalance * MainActivity.USDToPLNExchangeRate;
    }

    public static Double exchangeEURToUSD(Double actualBalance){
        return  actualBalance * MainActivity.EURToUSDExchangeRate;
    }

    public static Double exchangeEURToPLN(Double actualBalance){
        return  actualBalance * MainActivity.EURToPLNExchangeRate;
    }
}
