package com.example.andyk.mageapp20170828.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.example.andyk.mageapp20170828.xmlconnect.DefaultConnect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by foo on 7/15/17.
 */

public class ImgDownloader<T> extends HandlerThread {

    protected static final String TAG = ImgDownloader.class.getName();
    protected static final int MSG_DOWNLOAD = 0;
    protected Context mContext;
    protected Handler mResponseHandler;
    protected Map<T, String> mRequestMap = new ConcurrentHashMap<>();
    protected Handler mRequestHandler;
    protected OnDownloadListener mDownloadListener;

    public interface OnDownloadListener<T> {
        void onDownloaded(T target, Bitmap bitmap);
    }

    public void setOnDownloadListener(OnDownloadListener listener) {
        mDownloadListener = listener;
    }

    public ImgDownloader(Context context, Handler handler) {
        super(TAG);
        mContext = context;
        mResponseHandler = handler;
    }

    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MSG_DOWNLOAD) {
                    T target = (T) msg.obj;
                    handleRequest(target);
                }
            }
        };
    }

    public void queueImg(T target, String url) {
        if (url == null) {
            mRequestMap.remove(target);
        } else {
            mRequestMap.put(target, url);
            Message msg = mRequestHandler.obtainMessage(MSG_DOWNLOAD, target);
            msg.sendToTarget();
        }
    }

    protected void handleRequest(final T target) {
        final String url = mRequestMap.get(target);
        if (url == null) {
            return;
        }

        byte[] data = new DefaultConnect(mContext).getUrlBytes(url);
        final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        if (bitmap == null) return;

//        Log.d(TAG, "bitmap: " + bitmap);
        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                if (url != mRequestMap.get(target)) {
                    return;
                }
                mRequestMap.remove(target);
                mDownloadListener.onDownloaded(target, bitmap);
            }
        });
    }

    public void clearQueue() {
        mRequestHandler.removeMessages(MSG_DOWNLOAD);
    }
}
