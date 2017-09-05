package com.example.andyk.mageapp20170828.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.andyk.mageapp20170828.db.LogDbContract;
import com.example.andyk.mageapp20170828.db.LogDbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 5/29/17.
 */

public class Db {

    protected static Db sInstance;
    protected Context mContext;

    private Db(Context context) {
        mContext = context;
    }

    public static Db getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Db(context);
        }
        return sInstance;
    }

    public long addLog(String msg) {
        LogDbHelper logDb = new LogDbHelper(mContext);
        SQLiteDatabase db = logDb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("data", msg);
        long newRowId = db.insert(LogDbContract.LogEntry.TABLE_NAME, null, values);
        return newRowId;
    }

    public List<String> getLogs() {
        List<String> data = new ArrayList<>();
        LogDbHelper logDb = new LogDbHelper(mContext);
        SQLiteDatabase db = logDb.getReadableDatabase();
        String[] cols = {LogDbContract.LogEntry.COLUMN_NAME_DATA};
        Cursor cursor = db.query(LogDbContract.LogEntry.TABLE_NAME, cols, null, null, null, null, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            int colIdx = cursor.getColumnIndex(LogDbContract.LogEntry.COLUMN_NAME_DATA);
            data.add(cursor.getString(colIdx));
        }
        cursor.close();
        return data;
    }
}
