package com.example.photoapp

import androidx.room.Room
import com.example.photoapp.room.Database
import com.example.photoapp.room.GalleryItem
import com.example.photoapp.room.Location
import com.example.photoapp.room.Section
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DB {

    private val db = Room.databaseBuilder(App.context(), Database::class.java, "db").build()

    suspend fun save(photos: ArrayList<GalleryItem>) {
        return withContext(Dispatchers.IO) {
            db.galleryDao().savePhotos(photos)
        }
    }

    suspend fun getPhotoList(): List<GalleryItem> {
        return withContext(Dispatchers.IO) {
            db.galleryDao().getAll()
        }
    }

    suspend fun delete(list: ArrayList<GalleryItem>) {
        return withContext(Dispatchers.IO) {
            db.galleryDao().delete(list)
        }
    }

    suspend fun update(list: ArrayList<GalleryItem>) {
        return withContext(Dispatchers.IO) {
            db.galleryDao().update(list)
        }
    }

    suspend fun getSection(): Section {
        return withContext(Dispatchers.IO) {
            db.galleryDao().getSection()
        }
    }

    suspend fun updateSection(section: Section) {
        return withContext(Dispatchers.IO) {
            db.galleryDao().updateSection(section)
        }
    }

    suspend fun getLocation(): Location {
        return withContext(Dispatchers.IO) {
            db.galleryDao().getLocation()
        }
    }

    suspend fun updateLocation(location: Location) {
        return withContext(Dispatchers.IO) {
            db.galleryDao().updateLocation(location)
        }
    }


}