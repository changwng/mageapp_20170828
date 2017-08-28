package com.example.andyk.mageapp20170828;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class MainActivity extends DefaultActivity {

    protected static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
