package ru.ifmo.ctddev.skripnikov.Weather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ru.ifmo.ctddev.skripnikov.Weather.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_CITIES = "cities";

    public static final String DROP_TABLE_CITIES = "drop table if exists " + TABLE_CITIES;

    private static final String CREATE_CITIES_QUERY = "create table "
            + TABLE_CITIES + "("
            + CityCols.ID + " integer not null primary key autoincrement, "
            + CityCols.OWM_ID + " integer not null unique on conflict ignore, "
            + CityCols.NAME + " text not null, "
            + CityCols.COUNTRY + " text not null, "
            + CityCols.LATITUDE + " real not null, "
            + CityCols.LONGITUDE + " real not null)";

    public static final class CityCols {
        public static final String ID = "_id";
        public static final String OWM_ID = "owmid";
        public static final String NAME = "name";
        public static final String COUNTRY = "country";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
    }


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CITIES_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_CITIES);
        onCreate(db);
    }
}