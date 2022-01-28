package com.example.photoapp.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Section::class, Location::class, GalleryItem::class], version = 1)
abstract class Database: RoomDatabase() {
    abstract fun galleryDao() : GalleryDao
}