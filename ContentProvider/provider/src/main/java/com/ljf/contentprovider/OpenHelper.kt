package com.ljf.contentprovider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by mr.lin on 2018/5/28.
 */
class OpenHelper(context: Context?) : SQLiteOpenHelper(context, "provider.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table User (name text,age integer)")
        db?.execSQL("create table Animal (name text,type text)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}