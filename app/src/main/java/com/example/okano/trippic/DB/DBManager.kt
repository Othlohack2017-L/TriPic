package com.example.okano.trippic.DB

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by okano on 2017/10/07.
 */
class DBManager(context : Context) : SQLiteOpenHelper(context, "TripPicDB", null,1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("create table if not exists trip(" +
                "id serial PRIMARYKEY, " +
                "name text," +
                "category text," +
                "startTime text,"+
                "endTime text,"+
                "latitude real,"+
                "longitude real)"
        )

        db!!.execSQL("create table if not exists point(" +
                "id serial PRIMARYKEY, " +
                "latitude real,"+
                "longitude real,"+
                "tripId INTEGER,"+
                "foreign key (tripId) references trip(id))"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("drop table trip")
        db!!.execSQL("drop table point")
        onCreate(db)
    }
}