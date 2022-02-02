package com.example.photoapp

import com.example.photoapp.room.GalleryItem

interface OnDeleteModeListener {

    fun onDeleteMode(isDeleteMode: Boolean)
    fun deletePhotosList(list: ArrayList<GalleryItem>)
}