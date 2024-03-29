package com.example.contactformq2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class databasehelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="Contact.db";
    public static final String TABLE_NAME="Contact_table";
    public static final String col1="ID";
    public static final String col2="Name";
    public static final String col3="Email";
    public static final String col4="Phone_Home";
    public static final String col5="Phone_Office";
    public static final String col6="img";
    public databasehelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ TABLE_NAME+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, EMAIL TEXT,PHONE_HOME TEXT,PHONE_OFFICE TEXT,IMG TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(String name, String email,String phone_home,String phone_office,String img){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(col2,name);
        contentValues.put(col3,email);
        contentValues.put(col4,phone_home);
        contentValues.put(col5,phone_office);
        contentValues.put(col6,img);
        long result=db.insert(TABLE_NAME,null,contentValues);
        if(result==-1)
        {
            return false;
        }
        else
            return true;

    }

    public Cursor getAllData(){
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from " +TABLE_NAME,null);
        return res;

    }

}
