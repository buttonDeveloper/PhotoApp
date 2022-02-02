package com.example.photoapp.room

import androidx.room.*

@Dao
interface GalleryDao {

    @Query("SELECT * FROM GALLERY_ITEM")
    suspend fun getAll(): List<GalleryItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePhotos(list: ArrayList<GalleryItem>)

    @Delete
    suspend fun delete(list: ArrayList<GalleryItem>)
}