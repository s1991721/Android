package com.ljf.contentprovider

import android.app.Activity
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        insertBt.setOnClickListener {
            if (sw.isChecked) {
                var uri = Uri.parse("content://com.ljf.contentprovider.MyContentProvider/user")
                var value = ContentValues()
                value.put("name", Et1.text.toString())
                value.put("age", Et2.text.toString())
                resultTv.text = contentResolver.insert(uri, value).toString()
            } else {
                var uri = Uri.parse("content://com.ljf.contentprovider.MyContentProvider/animal")
                var value = ContentValues()
                value.put("name", Et1.text.toString())
                value.put("type", Et2.text.toString())
                resultTv.text = contentResolver.insert(uri, value).toString()
            }
        }

        deleteBt.setOnClickListener {
            if (sw.isChecked) {
                var uri = Uri.parse("content://com.ljf.contentprovider.MyContentProvider/user")
                resultTv.text = contentResolver.delete(uri, "name = ?", arrayOf(Et1.text.toString())).toString()
            } else {
                var uri = Uri.parse("content://com.ljf.contentprovider.MyContentProvider/animal")
                resultTv.text = contentResolver.delete(uri, "name = ?", arrayOf(Et1.text.toString())).toString()
            }
        }

        updateBt.setOnClickListener {
            if (sw.isChecked) {
                var uri = Uri.parse("content://com.ljf.contentprovider.MyContentProvider/user")
                var value = ContentValues()
                value.put("name", Et1.text.toString())
                value.put("age", Et2.text.toString())
                resultTv.text = contentResolver.update(uri, value, "name = ?", arrayOf(Et1.text.toString())).toString()
            } else {
                var uri = Uri.parse("content://com.ljf.contentprovider.MyContentProvider/animal")
                var value = ContentValues()
                value.put("name", Et1.text.toString())
                value.put("type", Et2.text.toString())
                resultTv.text = contentResolver.update(uri, value, "name = ?", arrayOf(Et1.text.toString())).toString()
            }
        }

        queryBt.setOnClickListener {
            if (sw.isChecked) {
                var uri = Uri.parse("content://com.ljf.contentprovider.MyContentProvider/user")
                var value = ContentValues()
                value.put("name", Et1.text.toString())
                value.put("age", Et2.text.toString())
                var cursor = contentResolver.query(uri, arrayOf("name", "age"), "name = ?", arrayOf(Et1.text.toString()), null)
                var sb = StringBuilder()
                while (cursor.moveToNext()) {
                    sb.append(cursor.getString(0))
                    sb.append("----------")
                    sb.append(cursor.getString(1))
                    sb.append("\n")
                }
                resultTv.text = sb.toString()
                cursor.close()
            } else {
                var uri = Uri.parse("content://com.ljf.contentprovider.MyContentProvider/animal")
                var value = ContentValues()
                value.put("name", Et1.text.toString())
                value.put("type", Et2.text.toString())
                var cursor = contentResolver.query(uri, arrayOf("name", "type"), "name = ?", arrayOf(Et1.text.toString()), null)
                var sb = StringBuilder()
                while (cursor.moveToNext()) {
                    sb.append(cursor.getString(0))
                    sb.append("----------")
                    sb.append(cursor.getString(1))
                    sb.append("\n")
                }
                resultTv.text = sb.toString()
                cursor.close()
            }
        }

    }


}
