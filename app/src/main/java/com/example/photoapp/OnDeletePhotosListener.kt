package com.example.photoapp

import android.net.Uri
import com.example.photoapp.room.GalleryItem

interface OnDeletePhotosListener {

    fun deletePhotosList(list: ArrayList<GalleryItem>)
}