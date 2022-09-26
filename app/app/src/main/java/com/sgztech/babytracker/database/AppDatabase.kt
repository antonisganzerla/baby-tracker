package com.sgztech.babytracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sgztech.babytracker.dao.RegisterDao
import com.sgztech.babytracker.model.Register

@Database(entities = [Register::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun registerDao(): RegisterDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "my-db"
                ).build()
            }
            return instance!!
        }
    }
}
