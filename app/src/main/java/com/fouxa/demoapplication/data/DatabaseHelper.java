package com.fouxa.demoapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // User Info table and column
    public final String TABLE_NAME = "employee_info";
    public static final String COL_EMP_ID = "emp_id";
    public static final String COL_EMP_NAME = "emp_name";

    public DatabaseHelper(Context context) {
        super(context, "employee_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + " (" + COL_EMP_ID + " text, "
                + COL_EMP_NAME + " text" + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop table if exists " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
