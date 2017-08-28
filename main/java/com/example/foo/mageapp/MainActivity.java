package com.example.foo.mageapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.foo.mageapp.helper.Helper;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    protected static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Calendar cal = Calendar.getInstance();
        int min = (cal.get(Calendar.MINUTE) - 5);
        cal.set(Calendar.MINUTE, min);
        Date date = cal.getTime();

        String gmt = Helper.getGMT(date);
        Log.d(TAG, "GMT: " + gmt);*/

        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);
        if (f == null) {
            f = MainFragment.getFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, f)
                    .commit();
        }
    }
}
