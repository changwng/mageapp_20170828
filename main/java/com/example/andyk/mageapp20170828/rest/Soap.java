package com.example.andyk.mageapp20170828.rest;

import android.net.Uri;
import android.util.Log;

import com.example.andyk.mageapp20170828.helper.Helper;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by foo on 8/26/17.
 */

public class Soap {
    protected static final String TAG = "Soap";
    protected static final String ENDPOINT = "http://mage.testing.acacloud.com";
    protected static final String REQUEST_PATH = "index.php/api/v2_soap";
    protected static final String REQUEST_PARAM_KEY = "wsdl";
    protected static final String REQUEST_PARAM_VALUE = "1";
    protected static final String API_USER = "apiUser";
    protected static final String API_KEY = "apiKey";
    protected static final String NAME_SPACE = "urn:Magento";
    protected static final String SOAP_ACTION = "urn:Action";
    protected static final int ORDER_OFFSET_HR = 240000;

    protected Uri mUri;
    protected String mUrl; // http://mage.testing.acacloud.com/api/soap/?wsdl
    protected SoapSerializationEnvelope mEnv;
    protected HttpTransportSE mTrans;
    protected String mSessId;

    public Soap() {
        init();
    }

    protected void init() {
        mUri = Uri.parse(ENDPOINT).buildUpon()
                .appendEncodedPath(REQUEST_PATH)
//                .appendQueryParameter(REQUEST_PARAM_KEY, REQUEST_PARAM_VALUE)
                .build();
        mUrl = mUri.toString();
    }

    public String fetchLastOrderNumber() {
        String id = null;
        try {
            id = getLastOrderNumber();
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage(), ioe);
        } catch (XmlPullParserException xppe) {
            Log.e(TAG, xppe.getMessage(), xppe);
        }
        return id;
    }

    protected String getLastOrderNumber() throws IOException, XmlPullParserException {
        String id = null;

        mEnv = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        mEnv.dotNet = false;
        mEnv.xsd = SoapSerializationEnvelope.XSD;
        mEnv.enc = SoapSerializationEnvelope.ENC;

        mSessId = login();

        Map<String, String> info = getLastOrder();
        String orderNum = info.get("increment_id");

        return orderNum;
    }

    protected String login() throws IOException, XmlPullParserException {
        SoapObject req = new SoapObject(NAME_SPACE, "login");
        req.addProperty("username", API_USER);
        req.addProperty("apiKey", API_KEY);

        mEnv.setOutputSoapObject(req);
        mTrans = new HttpTransportSE(mUrl);

        mTrans.call(SOAP_ACTION, mEnv);
        Object resp = mEnv.getResponse();
        return resp.toString();
    }

    /**
     * gets last order from an hour interval
     *
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    protected Map<String, String> getLastOrder() throws IOException, XmlPullParserException {
        Map<String, String> data = new HashMap();
        String dateOffset = getOrderDateOffset();

        SoapObject value = new SoapObject();
        value.addProperty("key", "gt");
        value.addProperty("value", dateOffset);

        SoapObject filter = new SoapObject();
        filter.addProperty("key", "created_at");
        filter.addProperty("value", value);

        SoapObject filterArr = new SoapObject();
        filterArr.addProperty("complexFilterArray", filter);

        SoapObject filters = new SoapObject();
        filters.addProperty("complex_filter", filterArr);

        SoapObject req = new SoapObject(NAME_SPACE, "salesOrderList");
        req.addProperty("sessionId", mSessId);
        req.addProperty("filters", filters);

        mEnv.setOutputSoapObject(req);
        mTrans.call(SOAP_ACTION, mEnv);
        SoapObject resp = (SoapObject) mEnv.getResponse();

        int cnt = resp.getPropertyCount();
        if (cnt > 0) {
            int lastIndex = (cnt - 1); // we only care the last order info
            SoapObject val = (SoapObject) resp.getProperty(lastIndex);
            int len = val.getPropertyCount();
            for (int i = 0; i < len; i++) {
                PropertyInfo info = val.getPropertyInfo(i);
                String name = info.getName();
                String v = (String) info.getValue();
                data.put(name, v);
            }
        }
        return data;
    }

    protected String getOrderDateOffset() {
        // subtract 24 hrs from now
        Calendar cal = Calendar.getInstance();
        int hr = (cal.get(Calendar.HOUR) - ORDER_OFFSET_HR);
        cal.set(Calendar.HOUR, hr);
        Date date = cal.getTime();
        return Helper.getGMT(date);
    }
}
