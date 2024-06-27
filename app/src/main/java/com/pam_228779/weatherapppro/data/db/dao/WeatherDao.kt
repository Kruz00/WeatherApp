package com.pam_228779.weatherapppro.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pam_228779.weatherapppro.data.db.entities.WeatherEntity

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weathers")
    suspend fun getAllWeathers(): List<WeatherEntity>

    @Query("SELECT * FROM weathers WHERE locationId = :locationId")
    fun getWeather(locationId: Int): LiveData<WeatherEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: WeatherEntity)

    @Query("DELETE FROM weathers WHERE locationId = :locationId")
    suspend fun deleteById(locationId: Int)

    @Query("SELECT EXISTS(SELECT * FROM weathers WHERE locationId = :locationId)")
    suspend fun isWeatherExist(locationId: Int): Boolean

    @Update
    suspend fun updateWeathers(weathers: List<WeatherEntity>)
}