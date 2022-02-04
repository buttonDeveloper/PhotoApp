package com.example.photoapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "GALLERY_ITEM")
class GalleryItem {

    @PrimaryKey
    @ColumnInfo(name = "photo_uri", index = true)
    lateinit var photoUri: String
}