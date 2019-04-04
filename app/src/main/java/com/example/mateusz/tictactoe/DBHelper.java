package com.example.mateusz.tictactoe;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="Saves.db";
    private static final int DB_VER=1;
    private static final String TABLE_NAME = "Saves";
    private static final String KEY_DATA = "data";
    private static final String KEY_SCREENSHOOT = "screenshoot";
    private static final String KEY_WINNER = "winner";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_COMPUTER = "computer";
    private static final String KEY_GAME_TIME="game_time";


    private static DBHelper instance;

    public static synchronized DBHelper getInstance(Context context)
    {
        if(instance == null){
            instance = new DBHelper(context);
        }
        return instance;
    }

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Save> getAllSaves()
    {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+";", null);
        List<Save> saves = new ArrayList<>();
        if(cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                byte [] obraz=cursor.getBlob(cursor.getColumnIndex(KEY_SCREENSHOOT));

                Save save = new Save(
                        cursor.getString(cursor.getColumnIndex(KEY_DATA)),
                        BitmapFactory.decodeByteArray(obraz,0,obraz.length),
                        cursor.getString(cursor.getColumnIndex(KEY_WINNER)),
                        cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)),
                        cursor.getInt(cursor.getColumnIndex(KEY_COMPUTER)),
                        cursor.getString(cursor.getColumnIndex(KEY_GAME_TIME))
                );
                saves.add(save);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return saves;
    }

    public void addSave(Save save) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATA, save.getData());
        values.put(KEY_WINNER, save.getWinner());
        values.put(KEY_ADDRESS, save.getAddress());

        Bitmap obraz=save.getScreenshoot();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        obraz.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();
        values.put(KEY_SCREENSHOOT, bArray);

        values.put(KEY_COMPUTER, save.getComputer());
        values.put(KEY_GAME_TIME, save.getGameTime());



        // insert
        db.insert(TABLE_NAME,null, values);
        db.close();
    }

    public void deleteOne(Save save) {
        // Get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "data = ?", new String[] { String.valueOf(save.getData()) });
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Saves( "+" id INTEGER PRIMARY KEY AUTOINCREMENT,"+" data TEXT, "
                +" screenshoot BLOOB, "+" winner TEXT, "+" address TEXT, "+" computer INTEGER, "+" game_time TEXT "+"  )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

       // db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //this.onCreate(db);

    }
}
