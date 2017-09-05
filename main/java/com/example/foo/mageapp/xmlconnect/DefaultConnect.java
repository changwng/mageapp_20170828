package com.example.foo.mageapp.xmlconnect;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.foo.mageapp.catalog.Category;
import com.example.foo.mageapp.helper.Cookie;
import com.example.foo.mageapp.helper.Helper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by foo on 3/11/17.
 */
public class DefaultConnect {

    protected static final String TAG = "DefaultConnect";
    protected static final String DOMAIN = "mage.testing.acacloud.com";
    protected static final String HOST = "http://mage.testing.acacloud.com";
    //    protected static final String DOMAIN = "magento.dev.com";
//    protected static final String HOST = "http://magento.dev.com";
    protected static final String APP_CODE_NAME = "app_code";
    protected static final String APP_CODE_VALUE = "defand1";
    protected static final String APP_SCREEN_SIZE_NAME = "screen_size";
//    protected static final String APP_SCREEN_SIZE_VALUE = "2400x1600"; // "320x480";

    protected String mAppScreenSizeValue;
    protected Context mContext;
    protected String mPath;
    protected Uri mUri;
    protected Map<String, String> mPostData = new HashMap<>();
    protected Cookie mCookie;
    protected String mHeaderCookie;

    public DefaultConnect(Context context) {
        mContext = context;
        mUri = Uri.parse(HOST);
        mPath = "xmlconnect/index/index";

        // set screen size
        DisplayMetrics outMetrics = Helper.getScreenSize(mContext);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        String size = String.format("%dx%d", width, height);
        Log.d(TAG, "device size: " + size);
        mAppScreenSizeValue = size;
    }

    public String getContentByUrl(String url) {
        byte[] bytes = getUrlBytes(url);
        if (bytes == null) {
            Log.d(TAG, "Failed to receive response!");
        }
        String resp = new String(bytes);
        Log.d(TAG, "response: " + resp);
        /*long rowId = Helper.getInstance(mContext).dbLog(resp);
        if (rowId > 0) {
            Log.d(TAG, "row: " + rowId);
            String s = Helper.getInstance(mContext).getLogs();
            Log.d(TAG, "db result: " + s);
        }*/
        return resp;
    }

    public byte[] getUrlBytes(String spec) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HttpURLConnection conn = null;
        InputStream in = null;
        try {
            URL url = new URL(spec);
            conn = (HttpURLConnection) url.openConnection();
            prepareRequest(conn);
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
            readHeader(conn);
            in = new BufferedInputStream(conn.getInputStream());
            byte[] b = new byte[1024];
            int len;
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
            return out.toByteArray();
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage(), ioe);
        } finally {
            if (conn != null) conn.disconnect();
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException ioe) {
                Log.e(TAG, ioe.getMessage(), ioe);
            }
        }
        return null;
    }

    protected String getCookieFromHeader(HttpURLConnection conn) {
        Map<String, List<String>> headers = conn.getHeaderFields();
        for (String key : headers.keySet()) {
            if ((key != null) && key.toLowerCase().contains("cookie")) {
                List<String> header = headers.get(key);
                int numHeaders = header.size();
                String val = "";
                for (int i = 0; i < numHeaders; i++) {
                    val += header.get(i);
                }
                return val;
            }
        }
        return null;
    }

//    protected void prepareRequest(HttpURLConnection conn) throws IOException {
//        Cookie cookie = new Cookie(DOMAIN);
//
//        /*String headerCookie = getCookieFromHeader(conn);
//        if (headerCookie != null) {
//            cookie.addHeaderCookie(headerCookie);
//        }*/
//
//        cookie.add(APP_CODE_NAME, APP_CODE_VALUE);
//        cookie.add(APP_SCREEN_SIZE_NAME, mAppScreenSizeValue);
//
//        List<HttpCookie> cookies = cookie.getCookies();
//        String strCookies = TextUtils.join(";", cookies);
//
//        conn.setRequestProperty("Cookie", strCookies);
//        prepareRequestParams(conn);
//    }

    protected void prepareRequest(HttpURLConnection conn) throws IOException {
        mCookie = new Cookie(DOMAIN);
        mCookie.add(APP_CODE_NAME, APP_CODE_VALUE);
        mCookie.add(APP_SCREEN_SIZE_NAME, mAppScreenSizeValue);

        // check saved header cookie from SharedPreferences and set it if there is one..




        List<HttpCookie> cookies = mCookie.getCookies();
        String strCookies = TextUtils.join(";", cookies);
        conn.setRequestProperty("Cookie", strCookies);

        prepareRequestParams(conn);
    }

    public String getRequestUrl() {
        Uri uri = Uri.parse(HOST).buildUpon()
                .path(mPath)
                .build();
        String url = uri.toString();
        Log.d(TAG, "request: " + url);
        return url;
    }

    public void setPostData(String key, String value) {
        if (key.isEmpty() || value.isEmpty()) return;
        mPostData.put(key, value);
    }

    protected void prepareRequestParams(HttpURLConnection conn) throws IOException {
        conn.setDoOutput(true);
        conn.setChunkedStreamingMode(0);
        if (mPostData.size() < 1) return;
        Uri.Builder builder = new Uri.Builder();
        /*for (String key : mPostData.keySet()) {
            String val = mPostData.get(key);
            builder.appendQueryParameter(key, val);
        }*/
        for (Map.Entry<String, String> entry : mPostData.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            builder.appendQueryParameter(key, val);
        }
        Uri uri = builder.build();
        String query = uri.getEncodedQuery();
        byte[] bytes = query.getBytes();
        OutputStream out = conn.getOutputStream();
        out.write(bytes);
        out.close();
    }

    protected void readHeader(HttpURLConnection conn) {
        mHeaderCookie = getCookieFromHeader(conn);
        if (mHeaderCookie != null) {
            mCookie.addHeaderCookie(mHeaderCookie);

            // save into SharedPreferences..



        }
    }

    public List<Category> fetchCategoryItems() {
        List<Category> items = new ArrayList<>();
        String spec = getRequestUrl();
        String xml = getContentByUrl(spec);
        if (xml == null) return items;
        try {
            items = getCateogryItemsByXml(xml, items);
        } catch (XmlPullParserException xppe) {
            Log.e(TAG, xppe.getMessage(), xppe);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage(), ioe);
        }
        return items;
    }

    protected List<Category> getCateogryItemsByXml(String xml, List<Category> items)
            throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(xml));
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if ((eventType == XmlPullParser.START_TAG) && (parser.getName().equals("item"))) {
                Category category = getCategoryByXml(xml, parser, eventType);
                items.add(category);
            }
            eventType = parser.next();
        }
        return items;
    }

    protected Category getCategoryByXml(String xml, XmlPullParser parser, int eventType)
            throws IOException, XmlPullParserException {
        Category cat = new Category();
        while (!((eventType == XmlPullParser.END_TAG) && parser.getName().equals("item"))) {
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("label")) {
                    String label = parser.nextText();
                    cat.setLabel(label);
                } else if (parser.getName().equals("entity_id")) {
                    String id = parser.nextText();
                    cat.setId(id);
                } else if (parser.getName().equals("icon")) {
                    String icon = parser.nextText();
                    cat.setIcon(icon);
                }
            }
            eventType = parser.next();
        }
        return cat;
    }
}