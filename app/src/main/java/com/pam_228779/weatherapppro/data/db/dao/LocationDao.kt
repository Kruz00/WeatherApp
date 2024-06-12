package com.pam_228779.weatherapppro.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pam_228779.weatherapppro.data.db.entities.LocationEntity

@Dao
interface LocationDao {
    @Query("SELECT * FROM locations")
    fun getAllLocations(): LiveData<List<LocationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LocationEntity)

    @Delete
    suspend fun delete(location: LocationEntity)



    @Query("SELECT * FROM locations WHERE name = :name")
    suspend fun getLocation(name: String): LocationEntity?

    @Query("SELECT * FROM locations WHERE id = :id")
    suspend fun getLocation(id: Int): LocationEntity?

    @Update
    suspend fun updateLocations(location: LocationEntity)
}