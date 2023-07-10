package com.nqmgaming.demosqliteapp.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    // Database name and version
    private static final String DB_NAME = "databaseManagerProduct";
    private static final Integer DB_VERSION = 1;

    // Constructor
    public DataBaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table category including id and name
        String sql_create_table_category =
                "CREATE TABLE tbl_category (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)";
        db.execSQL(sql_create_table_category);

        // Create table product including id, name, price, category_id
        String sql_create_table_product =
                "CREATE TABLE tbl_product (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " name TEXT, price REAL, category_id INTEGER, " +
                        "FOREIGN KEY(category_id) REFERENCES category(id))";
        db.execSQL(sql_create_table_product);

    }

    // Upgrade tables
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Upgrade version...

    }
}
