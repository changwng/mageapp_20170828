package com.example.foo.mageapp.helper;

import android.annotation.TargetApi;
import android.util.Log;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by foo on 3/11/17.
 */

public class Cookie {

    protected static final String TAG = "COOKIE";

    protected static CookieManager sCookieMgr = new CookieManager();
    protected String mDomain;
    protected String mHost;
    protected String mPath = "/";
    protected int mVersion = 0;
    protected long mMaxAge;
    protected boolean mSecure;
    protected boolean mHttpOnly;

    public Cookie(String domain) {
        mDomain = domain;
    }

    public CookieStore getStore() {
        CookieHandler.setDefault(sCookieMgr);
        CookieStore store = sCookieMgr.getCookieStore();
        return store;
    }

    public void add(String name, String value) {
        HttpCookie cookie = new HttpCookie(name, value);
        cookie.setDomain(mDomain);
        cookie.setPath(mPath);
        cookie.setVersion(mVersion);
        if (mMaxAge > 0) {
            cookie.setMaxAge(mMaxAge);
        }
        cookie.setSecure(mSecure);
//        cookie.setHttpOnly(mHttpOnly);

//        URI uri = getURI();
        getStore().add(null, cookie);
    }

    protected URI getURI() {
        URI uri = null;
        try {
            uri = new URI(mHost);
        } catch (URISyntaxException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return uri;
    }

    public HttpCookie get(String name) {
        List<HttpCookie> cookies = getStore().get(getURI());
        int numCookies = cookies.size();
        for (int i = 0; i < numCookies; i++) {
            HttpCookie cookie = cookies.get(i);
            cookie.getName();
            cookie.getValue();
        }
        return null;
    }

    public void addHeaderCookie(String headerCookie) {
        List<HttpCookie> cookies = HttpCookie.parse(headerCookie);
        int numCookies = cookies.size();
        for (int i = 0; i < numCookies; i++) {
            HttpCookie cookie = cookies.get(i);
            mDomain = cookie.getDomain();
            mPath = cookie.getPath();
            mVersion = cookie.getVersion();
            mMaxAge = cookie.getMaxAge();
            mSecure = cookie.getSecure();
            mHttpOnly = cookie.isHttpOnly();
            getStore().add(null, cookie);
        }
    }

    public List<HttpCookie> getCookies() {
        return getStore().getCookies();
    }
}