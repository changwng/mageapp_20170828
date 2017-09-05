package com.example.foo.mageapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.foo.mageapp.checkout.CartData;

public class CartActivity extends AppCompatActivity {

    protected static final String TAG = "CartActivity";
//    protected static final String INTENT_EXTRA_CART_DATA = "intent.extra.CART_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);
        if (f == null) {
            f = CartFragment.getFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, f)
                    .commit();
        }
    }
}