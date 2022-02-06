package com.example.photoapp.model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.photoapp.repo.Repository
import com.example.photoapp.room.GalleryItem

class MainViewModel : ViewModel() {

    private var photosLiveData = MutableLiveData<List<GalleryItem>>()
    fun getPhotosLiveData(): LiveData<List<GalleryItem>> = photosLiveData

    private val photosObserver = Observer<List<GalleryItem>> { photosLiveData.value = it }

    private val repository = Repository()

    init {
        repository.listPhotosLiveData().observeForever(photosObserver)
    }

    fun observeModel() {
        repository.loadInitList {
            photosLiveData.value = it
        }
    }

    suspend fun getSection() = repository.getSection()

    fun updateSection(section: String) {
        repository.updateSection(section)
    }

    suspend fun getLocation() = repository.getLocation()

    fun updateLocation(location: String) {
        repository.updateLocation(location)
    }

    fun savePhotos(listPhotos: List<Uri>) {
        repository.savePhotos(listPhotos)
    }

    fun deletePhotos(list: ArrayList<GalleryItem>) {
        repository.deletePhotos(list)
    }

    override fun onCleared() {
        super.onCleared()
        repository.listPhotosLiveData().removeObserver(photosObserver)
        repository.destroy()
    }
}