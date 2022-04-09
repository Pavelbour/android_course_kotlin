package ru.gb.android_course_kotlin

import android.app.Application
import androidx.room.Room
import ru.gb.android_course_kotlin.data.db.HistoryDao
import ru.gb.android_course_kotlin.data.db.HistoryDataBase
import java.lang.IllegalStateException

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }
    companion object {
        private var appInstance: App? = null
        private var db: HistoryDataBase? = null
        private const val DB_NAME = "History.db"
        fun getHistoryDao(): HistoryDao {
                if (db == null) {
                    if (db == null) {
                        if (appInstance == null) throw
                        IllegalStateException("Application is null while creating DataBase")
                        db = Room.databaseBuilder(
                            appInstance!!.applicationContext,
                            HistoryDataBase::class.java,
                            DB_NAME
                        ).build()
                    }
                }
            return db!!.historyDao()
        }
    }
}