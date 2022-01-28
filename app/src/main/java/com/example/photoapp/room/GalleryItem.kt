package com.example.photoapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "GALLERY_ITEM")
class GalleryItem {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "photo_uri")
    lateinit var photoUri: String

}