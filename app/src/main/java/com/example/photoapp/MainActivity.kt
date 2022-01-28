package com.example.photoapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewStub
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.photoapp.databinding.ActivityMainBinding
import com.example.photoapp.room.GalleryItem
import kotlinx.coroutines.*
import timber.log.Timber

class MainActivity : AppCompatActivity(), OnDeleteModeListener, OnDeletePhotosListener {

    private lateinit var model: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private val adapter = GalleryAdapter(ArrayList(), false, this, this)
    private var deleteList = ArrayList<GalleryItem>()
    private var isDeleteMode = false
    private var deleteButton: ViewStub? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this)[MainViewModel::class.java].apply { observeModel() }

        binding.editTextSection.apply {
            onSubmit { submitSection() }
            lifecycleScope.launch {
                if (model.getSection().section == " ") return@launch
                setText(model.getSection().section)
            }
        }

        binding.includeGalleryCard.editTextLocation.apply {
            onSubmit { submitLocation() }
            lifecycleScope.launch {
                if (model.getSection().section == " ") return@launch
                setText(model.getLocation().location)
            }
        }

        binding.includeGalleryCard.plus.setOnClickListener {
            if (!isDeleteMode) getContent.launch("image/*")
        }

        binding.includeGalleryCard.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, isDeleteMode)
            adapter = this@MainActivity.adapter
        }

        model.getPhotosLiveData().observe(this) {
            adapter.list = it
            adapter.notifyDataSetChanged()
        }
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {

        model.savePhotos(it)
    }

    override fun onDeleteMode(isDeleteMode: Boolean) {
        this@MainActivity.isDeleteMode = isDeleteMode
        setDeleteMode(isDeleteMode)
    }

    private fun setDeleteMode(isDeleteMode: Boolean) {
        if(isDeleteMode && deleteButton == null) {
            deleteButton = binding.includeGalleryCard.deleteButton
            deleteButton?.inflate()?.apply {
                elevation = 50F
                setOnClickListener {
                    if (isDeleteMode) {
                        binding.includeGalleryCard.recyclerView.adapter = adapter.apply { this.isDeleteMode = false }
                        this@MainActivity.isDeleteMode = false
                        if (deleteList.isNotEmpty()) model.deletePhotos(deleteList)
                        deleteButton?.visibility = View.GONE
                    }
                }
            }
        }   else deleteButton?.visibility = View.VISIBLE
    }

    private fun submitSection() {
        model.updateSection(binding.editTextSection.text.toString())
        hideKeyboard()
    }

    private fun submitLocation() {
        model.updateLocation(binding.includeGalleryCard.editTextLocation.text.toString())
        hideKeyboard()
    }

    private fun hideKeyboard() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onBackPressed() {
        if(this@MainActivity.isDeleteMode) {
            deleteList.clear()
            this.isDeleteMode = false
            deleteButton?.visibility = View.GONE
            adapter.apply {
                isDeleteMode = false
                notifyDataSetChanged()
            }
        }
        else super.onBackPressed()
    }

    override fun deletePhotosList(list: ArrayList<GalleryItem>) {
//        Timber.d("list = ${list[0].photoUri}")
        deleteList = list
    }

//    class DiffCallback: DiffUtil.Callback() {
//
//        var oldList = ArrayList<GalleryItem>()
//        var newList = ArrayList<GalleryItem>()
//
//        override fun getOldListSize(): Int {
//            return oldList.size
//        }
//
//        override fun getNewListSize(): Int {
//            return newList.size
//        }
//
//        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            return oldList[oldItemPosition].id == newList[newItemPosition].id
//        }
//
//        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            return oldList[oldItemPosition].photoUri == newList[newItemPosition].photoUri
//        }
//
//    }
}
