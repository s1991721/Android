package com.ljf.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.content.UriMatcher.NO_MATCH
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri

/**
 * Created by mr.lin on 2018/5/30.
 *
 * MyContentProvider
 */
class MyContentProvider : ContentProvider() {

    companion object {
        val AUTHORITY = "com.ljf.contentprovider.MyContentProvider"
        val TABLE_USER = 1
        val TABLE_ANIMAL = 2
    }

    lateinit var dbHelper: SQLiteOpenHelper
    lateinit var db: SQLiteDatabase
    lateinit var match: UriMatcher

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        db.insert(getTableName(uri), null, values)
        return uri
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        return db.query(getTableName(uri), projection, selection, selectionArgs, null, null, sortOrder)
    }

    override fun onCreate(): Boolean {
        dbHelper = OpenHelper(context)
        db = dbHelper.writableDatabase
        match = UriMatcher(NO_MATCH)
        match.addURI(AUTHORITY, "user", TABLE_USER)
        match.addURI(AUTHORITY, "animal", TABLE_ANIMAL)
        return true
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        return db.update(getTableName(uri), values, selection, selectionArgs)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return db.delete(getTableName(uri), selection, selectionArgs)
    }

    override fun getType(uri: Uri?): String {
        return ""
    }

    private fun getTableName(uri: Uri): String {
        when (match.match(uri)) {
            TABLE_USER -> return "User"
            TABLE_ANIMAL -> return "Animal"
        }
        return ""
    }
}