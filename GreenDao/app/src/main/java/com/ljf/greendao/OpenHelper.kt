package com.ljf.greendao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by mr.lin on 2018/5/28.
 */
class OpenHelper(context: Context?, var name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table if not exists Animal (id integer primary key,name text,age integer)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (newVersion == 2) {
            db?.execSQL("alert table Animal add gender integer")
        }
    }
}