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
import com.example.photoapp.*
import com.example.photoapp.databinding.ActivityMainBinding
import com.example.photoapp.room.GalleryItem
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), OnDeleteModeListener, OnPhotoClickListener {

    private lateinit var model: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private val adapter = GalleryAdapter(ArrayList(), false, this, this)
    private var deleteList = ArrayList<GalleryItem>()
    private var isDeleteMode = false
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
                includeGalleryCard.editTextLocation.setText(model.getLocation()?.location)
            }
        }

        //set editTexts listeners
        binding.apply {
            editTextSection.onSubmit { escapeEdition(editTextSection) }
            includeGalleryCard.editTextLocation.onSubmit { escapeEdition(includeGalleryCard.editTextLocation) }
        }

        //listener for enter gallery
        binding.includeGalleryCard.plus.setOnClickListener {
            if (!isDeleteMode) getContent.launch("image/*")
        }

        //init rv
        binding.includeGalleryCard.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, isDeleteMode)
            adapter = this@MainActivity.adapter
        }

        //observe photos list
        model.getPhotosLiveData().observe(this) {
//            adapter.updateList(it as ArrayList<GalleryItem>)
            adapter.list = it as ArrayList<GalleryItem>
            adapter.notifyDataSetChanged()
        }
    }

    //getting photos from gallery
    private val getContent = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
        model.savePhotos(it)
    }

    private fun setDeleteMode(isDeleteMode: Boolean) {
        this@MainActivity.isDeleteMode = isDeleteMode
        if (isDeleteMode && deleteButton == null) {
            deleteButton = binding.includeGalleryCard.deleteButton
            deleteButton?.apply {
                visibility = View.VISIBLE
                translationZ = 30F
                setOnClickListener {
                    if (isDeleteMode) {
                        binding.includeGalleryCard.recyclerView.adapter = adapter.apply { this.isDeleteMode = false }
                        this@MainActivity.isDeleteMode = false
                        if (deleteList.isNotEmpty()) model.deletePhotos(deleteList)
                        deleteButton?.visibility = View.GONE
                    }
                }
            }
        } else deleteButton?.visibility = View.VISIBLE
    }

    private fun escapeEdition(v: EditText) {
        when (v) {
            binding.editTextSection -> model.updateSection(v.text.toString())
            binding.includeGalleryCard.editTextLocation -> model.updateLocation(v.text.toString())
        }
        hideKeyboard()
        v.clearFocus()
    }

    private fun hideKeyboard() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onBackPressed() {
        if (this@MainActivity.isDeleteMode) {
            deleteList.clear()
            this.isDeleteMode = false
            deleteButton?.visibility = View.GONE
            adapter.apply {
                deleteList.clear()
                isDeleteMode = false
                notifyDataSetChanged()
            }
        } else super.onBackPressed()
    }

    override fun onDeleteMode(isDeleteMode: Boolean) {
        this@MainActivity.isDeleteMode = isDeleteMode
        adapter.isDeleteMode = isDeleteMode
        adapter.notifyDataSetChanged()
        setDeleteMode(isDeleteMode)
    }

    override fun deletePhotosList(list: ArrayList<GalleryItem>) {
        deleteList = list
    }

    override fun onPhotoClick(uri: Uri) {
        val intent = Intent(this, FullScreenImageActivity()::class.java)
        intent.putExtra("uri", uri.toString())
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

}
