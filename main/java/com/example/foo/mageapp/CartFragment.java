package com.example.foo.mageapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foo.mageapp.checkout.CartData;
import com.example.foo.mageapp.xmlconnect.CartConnect;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    protected static final String TAG = "CartFragment";
    protected CartData mCartData;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CartTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_blank, container, false);

        return v;
    }

    public static Fragment getFragment() {
        return new CartFragment();
    }

    private class CartTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            new CartConnect(getContext()).fetchCart();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {

        }
    }
}