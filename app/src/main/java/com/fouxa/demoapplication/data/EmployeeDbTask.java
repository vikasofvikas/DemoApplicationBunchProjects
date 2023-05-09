package com.fouxa.demoapplication.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class EmployeeDbTask extends DatabaseHelper {
    public EmployeeDbTask(Context context) {
        super(context);
    }

    // Insert Data on User Table
    public boolean insertData(String empId, String empName){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_EMP_ID, empId);
        contentValues.put(COL_EMP_NAME, empName);

        long res = database.insert(TABLE_NAME, null, contentValues);
        return res != -1;
    }

    // View data from user Table
    public Cursor viewData(){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor res = database.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

}
