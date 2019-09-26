package com.example.app_video;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.app_video.Contact.ItemCategory;

import java.util.ArrayList;

public class SQLHelperSave extends SQLiteOpenHelper {
    private static final String TAG = "SQLHelperSave";
    static final String DB_NAME_TABLE ="SQLItemSave";
    static final String DB_NAME = "SQLItemSave.db";
    static final int VERSION =1;
    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
    Cursor cursor;

    public SQLHelperSave (Context context){
        super(context,DB_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQLItemSave = "CREATE TABLE SQLItemSave ( "+
                "image TEXT," +
                "title TEXT," +
                "time TEXT," +
                "url TEXT )";
        sqLiteDatabase.execSQL(SQLItemSave);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i!= i1){
            sqLiteDatabase.execSQL("drop table if exists "+ DB_NAME_TABLE);
            onCreate(sqLiteDatabase);
        }
    }
    public  void insertItem(ItemCategory itemCategory){
        sqLiteDatabase = getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put("image",itemCategory.getAvatar());
        contentValues.put("title",itemCategory.getName());
        contentValues.put("time",itemCategory.getDate());
        contentValues.put("url",itemCategory.getUrl());
        sqLiteDatabase.insert(DB_NAME_TABLE,null,contentValues);
        closeDB();

    }public ArrayList<ItemCategory> getALLItem(){
        ItemCategory itemCategory;
        ArrayList<ItemCategory> arrayList = new ArrayList<>();
        sqLiteDatabase = getReadableDatabase();
        cursor = sqLiteDatabase.query(false,DB_NAME_TABLE,null,null,null,null,null,null,null);
    while (cursor.moveToNext()){
        String image = cursor.getString(cursor.getColumnIndex("image"));
        String title = cursor.getString(cursor.getColumnIndex("title"));
        String time = cursor.getString(cursor.getColumnIndex("time"));
        String url = cursor.getString(cursor.getColumnIndex("url"));
        itemCategory = new ItemCategory(image,title,time,url);
        arrayList.add(itemCategory);
    }
    closeDB();
    return arrayList;
    }

    public int deleteItem (String title){
        sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete(DB_NAME_TABLE,"title=?",new String[]{title});

    }
    public boolean deleteAll(){
        int result;
        sqLiteDatabase = getWritableDatabase();
        result = sqLiteDatabase.delete(DB_NAME_TABLE,null,null);
        closeDB();
        if (result==1){
            return true;

        }else return false;
    }

    private void closeDB(){
        if (sqLiteDatabase != null) sqLiteDatabase.close();
        if (contentValues != null) contentValues.clear();
        if (cursor != null) cursor.close();

    }
}