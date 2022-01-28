package com.example.photoapp

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView

class FullScreenImage: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.full_screen_image)

        val uri = Uri.parse(intent.getStringExtra("uri"))
        val imageView = findViewById<PhotoView>(R.id.fullImageView)

        Glide.with(this).load(uri).into(imageView)
    }
}