package com.example.foo.mageapp.helper;

import android.content.Context;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by foo on 3/18/17.
 */

public class Helper {

    protected static final String TAG = "Helper";
    public static final String UTF_8 = "UTF-8";
    protected static final String LOG_FILE = "debug.log";
    protected static Helper sInstance;
    protected Context mContext;

    protected Helper(Context context) {
        mContext = context;
    }

    public static Helper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Helper(context);
        }
        return sInstance;
    }

    public void fileLog(String msg) {
        File file = new File(mContext.getFilesDir(), LOG_FILE);
        String filePath = file.getPath();
        Log.d(TAG, "filePath: " + filePath);
        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(mContext.openFileOutput(LOG_FILE, Context.MODE_APPEND));
            byte[] bytes = msg.getBytes();
            out.write(bytes);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Failed to find the file: " + LOG_FILE, e);
        } catch (IOException e) {
            Log.e(TAG, msg, e);
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
    }

    public long dbLog(String msg) {
        return Db.getInstance(mContext).addLog(msg);
    }

    public String getLogs() {
        String s = null;
        List<String> logs =  Db.getInstance(mContext).getLogs();
        int numLogs = logs.size();
        for (int i = 0; i < numLogs; i++) {
            s += logs.get(i);
        }
        return s;
    }
}
