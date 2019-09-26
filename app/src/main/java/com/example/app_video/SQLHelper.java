package com.example.app_video;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.app_video.Contact.ItemCategory;

import java.util.ArrayList;


public class SQLHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLHelper";
    static final String DB_NAME_TABLE = "SQLItemClick";
    static final String DB_NAME= "SQLItemClick.db";
    static final int VERSION= 1;
    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
    Cursor cursor;
    public SQLHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQLItemClick = "CREATE TABLE SQLItemClick ( "+
                "image TEXT," +
                "title TEXT," +
                "time TEXT," +
                "url TEXT )";
        sqLiteDatabase.execSQL(SQLItemClick);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i!= i1){
            sqLiteDatabase.execSQL("drop table if exists "+ DB_NAME_TABLE);
            onCreate(sqLiteDatabase);
        }
    }
   public void insertItem (ItemCategory itemCategory){
        sqLiteDatabase = getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put("image",itemCategory.getAvatar());
        contentValues.put("title",itemCategory.getName());
        contentValues.put("time",itemCategory.getDate());
        contentValues.put("url",itemCategory.getUrl());
        sqLiteDatabase.insert(DB_NAME_TABLE,null,contentValues);
        closeDB();
   }
    public ArrayList<ItemCategory> getAllItem(){
        ItemCategory itemCategory;
        ArrayList<ItemCategory> itemCategoryArrayList = new ArrayList<>();
        sqLiteDatabase = getReadableDatabase();
        cursor = sqLiteDatabase.query(false,DB_NAME_TABLE,null,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String image = cursor.getString(cursor.getColumnIndex("image"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            itemCategory = new ItemCategory(image,title,time,url);
            itemCategoryArrayList.add(itemCategory);
        }
        closeDB();
        return itemCategoryArrayList;
    }
    public int deleteItem(String title){
        sqLiteDatabase= getWritableDatabase();
        return sqLiteDatabase.delete(DB_NAME_TABLE, "title=?", new String[]{title});
    }
    public boolean deleteAll() {
        int result;
        sqLiteDatabase = getWritableDatabase();
        result = sqLiteDatabase.delete(DB_NAME_TABLE, null, null);
        closeDB();
        if (result == 1)
            return true;
        else return false;
    }

    public void closeDB(){
        if (sqLiteDatabase != null) sqLiteDatabase.close();
        if (contentValues != null) contentValues.clear();
        if (cursor != null) cursor.close();}


}
