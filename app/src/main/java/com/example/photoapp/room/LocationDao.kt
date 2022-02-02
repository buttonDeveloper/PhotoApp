package com.example.photoapp.room

import androidx.room.*

@Dao
interface LocationDao {

    @Query("SELECT * FROM LOCATION")
    suspend fun getLocation(): Location?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLocation(location: Location)

    @Transaction
    suspend fun initLocation(location: Location): Location? {
        updateLocation(location)
        return getLocation()
    }
}