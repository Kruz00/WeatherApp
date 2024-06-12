package com.pam_228779.weatherapppro.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pam_228779.weatherapppro.data.db.entities.WeatherEntity

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weathers WHERE locationId = :locationId")
    fun getWeather(locationId: Int): LiveData<WeatherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: WeatherEntity)
}