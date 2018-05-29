package com.ljf.greendao

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {

        insertBt.setOnClickListener { resultTv.text = DataBaseManager.insert(Animal(nameEt.text.toString(), ageEt.text.toString().toInt())).toString() }
        deleteBt.setOnClickListener { resultTv.text = DataBaseManager.delete(Animal(nameEt.text.toString(), ageEt.text.toString().toInt())).toString() }
        updateBt.setOnClickListener { resultTv.text = DataBaseManager.update(Animal(nameEt.text.toString(), ageEt.text.toString().toInt())).toString() }
        selectBt.setOnClickListener {
            val list = DataBaseManager.select(Animal(nameEt.text.toString(), ageEt.text.toString().toInt()))
            val stringB = StringBuilder()

            for (animal in list) {
                stringB.append(animal.name + "---" + animal.age)
                stringB.append("\n")
            }

            resultTv.text = stringB.toString()
        }

        greenDapSw.setOnCheckedChangeListener { _, isChecked -> DataBaseManager.isGreenDao(isChecked) }

    }

}
