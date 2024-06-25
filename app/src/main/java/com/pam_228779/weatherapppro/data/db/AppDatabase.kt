package com.pam_228779.weatherapppro.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pam_228779.weatherapppro.data.db.dao.LocationDao
import com.pam_228779.weatherapppro.data.db.dao.WeatherDao
import com.pam_228779.weatherapppro.data.db.entities.LocationEntity
import com.pam_228779.weatherapppro.data.db.entities.WeatherEntity

@Database(entities = [LocationEntity::class, WeatherEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun weatherDao(): WeatherDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "weather_app_pro_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}