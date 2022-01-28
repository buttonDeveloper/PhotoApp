package com.example.photoapp.room

import androidx.room.*

@Dao
interface GalleryDao {

    @Query("SELECT * FROM GALLERY_ITEM")
    fun getAll(): List<GalleryItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePhotos(list: ArrayList<GalleryItem>)

    @Update
    fun update(list: ArrayList<GalleryItem>)

    @Delete
    fun delete(list: ArrayList<GalleryItem>)

    @Query("SELECT * FROM SECTION_TABLE")
    suspend fun getSection(): Section

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSection(section: Section)

    @Query("SELECT * FROM LOCATION_TABLE")
    suspend fun getLocation(): Location

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLocation(location: Location)

}