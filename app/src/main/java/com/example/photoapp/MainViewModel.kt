package com.example.photoapp

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.photoapp.room.GalleryItem
import com.example.photoapp.room.Location
import com.example.photoapp.room.Section
import timber.log.Timber

class MainViewModel : ViewModel() {

    private var photosLiveData = MutableLiveData<List<GalleryItem>>()
    fun getPhotosLiveData(): LiveData<List<GalleryItem>> = photosLiveData

    private val photosObserver = Observer<List<GalleryItem>> { photosLiveData.value = it }

    init {
        Timber.d("init")
        Repository.apply {
            getGalleryItem().observeForever(photosObserver)
        }
    }

    fun observeModel() {
        Repository.loadInitList(object : OnInitListLoadedCallback {
            override fun onInitListLoaded(list: List<GalleryItem>) {
                photosLiveData.value = list
            }
        })
    }

    suspend fun getSection(): Section {
        return Repository.getSection()
    }

    fun updateSection(section: String) {
        val i = Section().apply {
            this.section = section
        }
        Repository.updateSection(i)
    }

    suspend fun getLocation(): Location {
        return Repository.getLocation()
    }

    fun updateLocation(location: String) {
        val i = Location().apply {
            this.location = location
        }
        Repository.updateLocation(i)
    }

    fun savePhotos(listPhotos: List<Uri>) {
        val list = ArrayList<GalleryItem>()
        for (i in 0..listPhotos.size - 1) {
            val item = GalleryItem()
            item.photoUri = listPhotos[i].toString()
            list.add(item)
        }
        Repository.savePhotos(list)
    }

    fun deletePhotos(list: ArrayList<GalleryItem>) {
        Repository.apply {
            deletePhotos(list)
        }
    }


}