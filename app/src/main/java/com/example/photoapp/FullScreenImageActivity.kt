package com.example.photoapp

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.photoapp.databinding.FullScreenImageBinding

class FullScreenImageActivity : AppCompatActivity() {

    private lateinit var binding: FullScreenImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FullScreenImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("uri")?.let { uriString ->
            val uri = Uri.parse(uriString)
            Glide.with(this).load(uri).into(binding.fullImageView)
            binding.fullTextView.visibility = View.INVISIBLE
        } ?: run {
            binding.fullTextView.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}