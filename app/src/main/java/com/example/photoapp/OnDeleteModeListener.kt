package com.example.photoapp

import com.example.photoapp.room.GalleryItem

interface OnDeleteModeListener {
    fun onDeleteMode(isDeleteMode: Boolean)
    fun addToDeleteList(item: GalleryItem)
    fun removeFromDeleteList(item: GalleryItem)
}