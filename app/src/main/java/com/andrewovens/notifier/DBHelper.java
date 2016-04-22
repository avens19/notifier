package com.andrewovens.notifier;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DBHelper {

    public static final String DBNAME = "NDB";
    public static final String MESSAGESTABLENAME = "Messages";
    private static SQLiteDatabase myDB;

    public static void CreateMessagesTable() {
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS " +
                        MESSAGESTABLENAME +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "message text," +
                        "created_at integer)"
        );
    }

    public static void OpenDB(Context c) {
        if (myDB == null || !myDB.isOpen())
            myDB = c.openOrCreateDatabase(DBNAME, SQLiteDatabase.OPEN_READWRITE, null);

        CreateMessagesTable();
    }

    public static void AddMessage(String message) {
        ContentValues cv = new ContentValues();

        cv.put("message", message);
        cv.put("created_at", Calendar.getInstance().getTime().getTime());

        myDB.insertWithOnConflict(MESSAGESTABLENAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static List<String> GetMessages()
    {
        Cursor c = myDB.query(MESSAGESTABLENAME, new String[]{"message"} , null, null, null , null, "created_at DESC", null);

        List<String> list = new ArrayList<String>();

        c.moveToFirst();

        while(!c.isAfterLast())
        {
            list.add(c.getString(0));

            c.moveToNext();
        }

        c.close();

        return list;
    }
}