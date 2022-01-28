package com.example.photoapp

import com.example.photoapp.room.GalleryItem

interface OnInitListLoadedCallback {
    fun onInitListLoaded(list: List<GalleryItem>)
}