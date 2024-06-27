package com.pam_228779.weatherapppro.data.db.dao

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pam_228779.weatherapppro.data.db.entities.LocationEntity

@Dao
interface LocationDao {
    @Query("SELECT * FROM locations ORDER BY `order` ASC")
    fun getAllLocations(): LiveData<List<LocationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LocationEntity): Long

    @Delete
    suspend fun delete(location: LocationEntity)

    @Transaction
    suspend fun updateLocationListOrder(newOrderedList: List<LocationEntity>) {
        newOrderedList.forEachIndexed { index, location ->
            updateLocationOrder(location.id, index)
            Log.i("LocationDao", "dao update - id: ${location.id}, order: $index")
        }
    }

    @Query("UPDATE locations " +
            "SET `order`=:newOrder " +
            "WHERE `id` =:id")
    suspend fun updateLocationOrder(id: Int, newOrder: Int)

    @Query("SELECT * FROM locations WHERE rowId = :rowId")
    suspend fun getLocationByRowId(rowId: Long): LocationEntity?

    @Query("SELECT * FROM locations WHERE name = :name")
    suspend fun getLocation(name: String): LocationEntity?

    @Query("SELECT * FROM locations WHERE id = :id")
    suspend fun getLocationById(id: Int): LocationEntity?

    @Update
    suspend fun updateLocation(location: LocationEntity)
}