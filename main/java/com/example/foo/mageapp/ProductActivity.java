package com.example.foo.mageapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ProductActivity extends AppCompatActivity {

    protected static final String TAG = "ProductActivity";
    protected static final String INTENT_EXTRA_PRODCT_ID = "intent.extra.PRODUCT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);
        if (f == null) {
            String productId = getIntent().getStringExtra(INTENT_EXTRA_PRODCT_ID);
            f = ProductFragment.getFragment(productId);
            fm.beginTransaction()
                    .add(R.id.fragment_container, f)
                    .commit();
        }
    }
}