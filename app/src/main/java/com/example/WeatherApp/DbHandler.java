package com.example.WeatherApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DbHandler extends SQLiteOpenHelper {


    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "weatherDB";
    private static final String TABLE_Users = "weatherdetails";
    private static final String KEY_ID = "id";
    private static final String KEY_CITY = "city";
    private static final String KEY_WEATHER = "weather";
    private static final String KEY_TEMP = "temperature";
    private static final String KEY_MIN = "min";
    private static final String KEY_MAX = "max";



    public DbHandler(Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_Users + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CITY + " TEXT,"
                + KEY_WEATHER + " TEXT," + KEY_TEMP + " TEXT," + KEY_MIN + " TEXT,"
                + KEY_MAX + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Users);
        // Create tables again
        onCreate(db);
    }


    // Adding new User Details
    void insertUserDetails(String city, String weather, String temperature, String min, String max){
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_CITY, city);
        cValues.put(KEY_WEATHER, weather);
        cValues.put(KEY_TEMP, temperature);
        cValues.put(KEY_MAX, max);
        cValues.put(KEY_MIN, min);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_Users,null, cValues);
        db.close();
    }

    // Get User Details
    public ArrayList<HashMap<String, String>> GetUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT  city,  weather,  temperature,  min,  max FROM "+ TABLE_Users;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("city",cursor.getString(cursor.getColumnIndex(KEY_CITY)));
            user.put("weather",cursor.getString(cursor.getColumnIndex(KEY_WEATHER)));
            user.put("temperature",cursor.getString(cursor.getColumnIndex(KEY_TEMP)));
            user.put("max",cursor.getString(cursor.getColumnIndex(KEY_MAX)));
            user.put("min",cursor.getString(cursor.getColumnIndex(KEY_MIN)));
            userList.add(user);
        }
        return  userList;
    }

    // Get User Details based on userid
    public ArrayList<HashMap<String, String>> GetUserByUserId(int userid){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT  city,  weather,  temperature,  min,  max FROM "+ TABLE_Users;
        Cursor cursor = db.query(TABLE_Users, new String[]{KEY_CITY, KEY_WEATHER, KEY_TEMP, KEY_MIN, KEY_MAX}, KEY_ID+ "=?",new String[]{String.valueOf(userid)},null, null, null, null);
        if (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("city",cursor.getString(cursor.getColumnIndex(KEY_CITY)));
            user.put("weather",cursor.getString(cursor.getColumnIndex(KEY_WEATHER)));
            user.put("temperature",cursor.getString(cursor.getColumnIndex(KEY_TEMP)));
            user.put("max",cursor.getString(cursor.getColumnIndex(KEY_MAX)));
            user.put("min",cursor.getString(cursor.getColumnIndex(KEY_MIN)));
            userList.add(user);
        }
        return  userList;
    }

    // Delete User Details
    public void ResetDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+TABLE_Users;
        db.execSQL(clearDBQuery);
    }


    // Delete User Details
    public void DeleteUser(int userid){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Users, KEY_ID+" = ?",new String[]{String.valueOf(userid)});
        db.close();
    }
    // Update User Details
    public int UpdateUserDetails(String city, String weather, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put(KEY_CITY, city);
        cVals.put(KEY_WEATHER, weather);
        int count = db.update(TABLE_Users, cVals, KEY_ID+" = ?",new String[]{String.valueOf(id)});
        return  count;
    }


}