package com.example.andyk.mageapp20170828;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.andyk.mageapp20170828.helper.SharedPref;

public class BootCompletedReceiver extends BroadcastReceiver {

    protected static final String TAG = "BootCompletedReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");

        Log.d(TAG, "BootCompletedReceiver.onReceive() method called..");

        boolean isOn = SharedPref.getIsPurchaseServiceOn(context);
        if (isOn) {
            PurchaseService.setPurchaseService(context, false);
        }
    }
}
