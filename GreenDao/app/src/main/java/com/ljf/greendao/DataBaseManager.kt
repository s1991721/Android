package com.ljf.greendao

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import com.ljf.greendao.greendao.DaoMaster

/**
 * Created by mr.lin on 2018/5/28.
 * 数据库操作
 */
object DataBaseManager {

    private var link: SQLiteOpenHelper = OpenHelper(App.instance(), "animal.db", null, 1)

    fun insert(animal: Animal): Long {
        if (isGreenDao) {
            return dao.insert(animal)
        } else {
            val values = ContentValues()
            values.put("name", animal.name)
            values.put("age", animal.age)
            return link.writableDatabase.insert("Animal", "", values)
        }
    }

    fun delete(animal: Animal): Int {
        if (isGreenDao) {
            dao.delete(animal)
            return 0
        } else {
            return link.writableDatabase.delete("Animal", "name=?", arrayOf(animal.name))
        }
    }

    fun update(animal: Animal): Int {
        if (isGreenDao) {
            dao.update(animal)
            return 0
        } else {
            val values = ContentValues()
            values.put("age", animal.age)
            return link.writableDatabase.update("Animal", values, "name=?", arrayOf(animal.name))
        }
    }

    fun select(animal: Animal): List<Animal> {
        if (isGreenDao) {
//            return dao.queryBuilder().where(AnimalDao.Properties.Name.eq(animal.name)).list()
//            return dao.queryRaw("where name = ?",animal.name)
            return dao.loadAll()
        } else {
            val list = ArrayList<Animal>()

            val cursor = link.writableDatabase.rawQuery("select * from Animal", null)
            if (cursor.moveToFirst()) {
                do {
                    list.add(Animal(cursor.getString(cursor.getColumnIndex("NAME")), cursor.getInt(cursor.getColumnIndex("AGE"))))
                } while (cursor.moveToNext())
            }

            return list
        }
    }

    //GreenDao

    private val helper = DaoOpenHelper(App.instance(), "animal.db", null)
    private val session = DaoMaster(helper.writableDb).newSession()
    private val dao = session.animalDao

    private var isGreenDao = false

    fun isGreenDao(b: Boolean) {
        isGreenDao = b
    }

}