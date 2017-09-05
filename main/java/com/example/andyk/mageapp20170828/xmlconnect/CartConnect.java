package com.example.andyk.mageapp20170828.xmlconnect;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by foo on 3/18/17.
 */

public class CartConnect extends DefaultConnect {

    public CartConnect(Context context) {
        super(context);
        mPath = "xmlconnect/cart/add";
    }

    public void addItemToCart() {

    }

    protected void setPostData() {

    }

    private class CartTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            new CartConnect(mContext).addItemToCart();
            return null;
        }
    }
}
