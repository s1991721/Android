package com.ljf.greendao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.ljf.greendao.greendao.AnimalDao
import com.ljf.greendao.greendao.DaoMaster
import org.greenrobot.greendao.database.Database

/**
 * Created by mr.lin on 2018/5/29.
 */
class DaoOpenHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?) : DaoMaster.OpenHelper(context, name, factory) {

    override fun onUpgrade(db: Database?, oldVersion: Int, newVersion: Int) {
        if (oldVersion<newVersion){
            MigrationHelper.getInstance().migrate(db,AnimalDao::class.java)
        }
    }

}