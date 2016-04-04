package com.example.jonathan.labo2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jonathan on 2016-03-09.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "contacts.db";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String requeteNouvelleTable = "CREATE TABLE IF NOT EXISTS contacts("+
                                        "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                                        "prenom TEXT, "+
                                        "nom TEXT, "+
                                        "telephone TEXT)";
        db.execSQL(requeteNouvelleTable);

        ContentValues values = new ContentValues();

        values.put("prenom", "Jonathan");
        values.put("nom", "Bleau");
        values.put("telephone", "514-497-2000");
        db.insert("contacts", "nom", values);

        values.put("prenom","Jacinthe" );
        values.put("nom", "Desrochers");
        values.put("telephone", "438-879-2855");
        db.insert("contacts", "nom", values);

        values.put("prenom", "Simon");
        values.put("nom", "Petit");
        values.put("telephone", "555-555-5555");
        db.insert("contacts", "nom", values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS contacts");
        onCreate(db);
    }



}
