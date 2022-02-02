package com.example.photoapp

import android.net.Uri

interface OnPhotoClickListener {

    fun onPhotoClick(uri: Uri)
}