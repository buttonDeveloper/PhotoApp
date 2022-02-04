package com.example.photoapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.photoapp.databinding.ActivityMainBinding
import com.example.photoapp.room.GalleryItem
import kotlinx.coroutines.launch
import timber.log.Timber


class MainActivity : AppCompatActivity(), OnDeleteModeListener, OnPhotoClickListener {

    private lateinit var model: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private val adapter = GalleryAdapter(ArrayList(), this, this)
    private var deleteButton: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this)[MainViewModel::class.java].apply { observeModel() }

        //set editTexts start value
        lifecycleScope.launch {
            binding.apply {
                editTextSection.setText(model.getSection()?.section)
                galleryCard.editTextLocation.setText(model.getLocation()?.location)
            }
        }

        //set editTexts listeners
        binding.apply {
            editTextSection.onSubmit { escapeEdition(editTextSection) }
            galleryCard.editTextLocation.onSubmit { escapeEdition(galleryCard.editTextLocation) }
        }

        //listener for enter gallery
        binding.galleryCard.plus.setOnClickListener {
            if (!adapter.isDeleteMode) getContent.launch("image/*")
        }

        //init rv
        binding.galleryCard.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = this@MainActivity.adapter
        }

        //observe photos list
        model.getPhotosLiveData().observe(this) {
            adapter.updateList(it as ArrayList<GalleryItem>)
        }
    }

    //getting photos from gallery
    private val getContent = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
        model.savePhotos(it)
    }

    private fun setDeleteMode(isDeleteMode: Boolean) {
        if (isDeleteMode && deleteButton == null) {
            deleteButton = binding.galleryCard.deleteButton
            deleteButton?.apply {
                visibility = View.VISIBLE
                translationZ = 30F
                setOnClickListener {
                    model.deletePhotos(adapter.deleteList)
                    binding.galleryCard.recyclerView.adapter = adapter.apply { this.isDeleteMode = false }
                    this.visibility = View.GONE
                }
            }
        } else deleteButton?.visibility = View.VISIBLE
    }

    private fun escapeEdition(v: EditText) {
        when (v) {
            binding.editTextSection -> model.updateSection(v.text.toString())
            binding.galleryCard.editTextLocation -> model.updateLocation(v.text.toString())
        }
        hideKeyboard()
        v.clearFocus()
    }

    private fun hideKeyboard() {
        this.currentFocus?.let { view ->
            (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onBackPressed() {
        if (adapter.isDeleteMode) {
            deleteButton?.visibility = View.GONE
            binding.galleryCard.recyclerView.adapter = adapter.apply { isDeleteMode = false }
        } else super.onBackPressed()
    }

    override fun onDeleteMode(isDeleteMode: Boolean) {
        setDeleteMode(isDeleteMode)
    }

    override fun onPhotoClick(uri: Uri) {
        val intent = Intent(this, FullScreenImageActivity()::class.java)
        intent.putExtra("uri", uri.toString())
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

}
