package com.ljf.greendao

import android.app.Application

/**
 * Created by mr.lin on 2018/5/28.
 */
class App : Application() {

    companion object {
        private var instance: Application? = null
        fun instance(): Application {
            return instance!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}