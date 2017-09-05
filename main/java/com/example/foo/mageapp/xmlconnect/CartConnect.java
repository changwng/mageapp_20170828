package com.example.foo.mageapp.xmlconnect;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.foo.mageapp.checkout.CartData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by foo on 3/18/17.
 */

public class CartConnect extends DefaultConnect {

    public CartConnect(Context context) {
        super(context);
        mPath = "xmlconnect/cart/index";
    }

    public ResponseMessage addItemToCart(CartData data) {
        mPath = "xmlconnect/cart/add";
        setPostData("qty", "1");
        setPostData("product", data.getProductId());
        for (String key : data.getOptions().keySet()) {
            String val = data.getOptions().get(key);
            setPostData(key, val);
        }
        String url = getRequestUrl();
        String xml = getContentByUrl(url);
        return parseAddToCartXml(xml);
    }

    protected ResponseMessage parseAddToCartXml(String xml) {
        ResponseMessage msg = new ResponseMessage();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));
            int eventType = parser.getEventType();
            while (eventType == XmlPullParser.START_TAG) {
                String name = parser.getName();
                if (name.equals("status")) {
                    msg.setStatus(parser.nextText());
                } else if (name.equals("text")) {
                    msg.setText(parser.nextText());
                }
                eventType = parser.nextTag();
            }
        } catch (XmlPullParserException xppe) {
            Log.e(TAG, xppe.getMessage(), xppe);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage(), ioe);
        }
        return msg;
    }

    public void fetchCart() {
        String url = getRequestUrl();
        String xml = getContentByUrl(url);
        parseCartXml(xml);
    }

    protected void parseCartXml(String xml) {

    }
}
