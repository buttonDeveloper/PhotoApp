package com.example.photoapp

import androidx.room.Room
import com.example.photoapp.room.Database
import com.example.photoapp.room.GalleryItem
import com.example.photoapp.room.Location
import com.example.photoapp.room.Section

object DB {

    private val db = Room.databaseBuilder(App.context(), Database::class.java, "db").build()

    suspend fun save(photos: ArrayList<GalleryItem>) {
        db.galleryDao().savePhotos(photos)
    }

    suspend fun getPhotoList() = db.galleryDao().getAll()

    suspend fun delete(list: ArrayList<GalleryItem>) {
        db.galleryDao().delete(list)
    }

    suspend fun getSection() = db.sectionDao().getSection()


    suspend fun updateSection(section: Section) {
        db.sectionDao().updateSection(section)
    }

    suspend fun initSection(): Section? {
        return db.sectionDao().initSection(Section())
    }

    suspend fun getLocation() = db.locationDao().getLocation()

    suspend fun updateLocation(location: Location) {
        db.locationDao().updateLocation(location)
    }

    suspend fun initLocation(): Location? {
        return db.locationDao().initLocation(Location())
    }

}