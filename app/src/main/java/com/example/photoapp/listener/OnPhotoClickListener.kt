package com.example.photoapp.listener

import android.net.Uri

interface OnPhotoClickListener {
    fun onPhotoClick(uri: Uri)
}