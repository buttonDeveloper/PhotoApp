package com.example.photoapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.photoapp.room.GalleryItem
import com.example.photoapp.room.Location
import com.example.photoapp.room.Section
import kotlinx.coroutines.*
import timber.log.Timber

object Repository {

    private val scope = CoroutineScope(Job() + Dispatchers.Default)

    private val listPhotosLiveData = MutableLiveData<List<GalleryItem>>()
    fun getGalleryItem(): LiveData<List<GalleryItem>> = listPhotosLiveData

    init {
        Timber.d(" ")
    }

    fun loadInitList(callback: OnInitListLoadedCallback) {
        scope.launch(Dispatchers.Main) {
            callback.onInitListLoaded(DB.getPhotoList())
        }
    }

    fun savePhotos(photos: ArrayList<GalleryItem>) {
        scope.launch(Dispatchers.Main) {
            DB.save(photos)
            if (getPhotos() == null) return@launch
            listPhotosLiveData.value = getPhotos()!!
        }
    }

    fun deletePhotos(list: ArrayList<GalleryItem>) {
        scope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                DB.delete(list)
            }
            listPhotosLiveData.value = getPhotos()!!
        }
    }

    suspend fun getPhotos(): List<GalleryItem> {
        return DB.getPhotoList()
    }

    suspend fun getSection(): Section {
        return if (DB.getSection() == null) {
            Section()
        } else DB.getSection()
    }

    fun updateSection(section: Section) {
        scope.launch(Dispatchers.Main) {
            DB.updateSection(section)
        }
    }

    suspend fun getLocation(): Location {
        return if (DB.getLocation() == null) {
            Location()
        } else DB.getLocation()

    }

    fun updateLocation(location: Location) {
        scope.launch(Dispatchers.Main) {
            DB.updateLocation(location)
        }
    }

    fun updatePhotos(photos: ArrayList<GalleryItem>) {
        scope.launch(Dispatchers.IO) {
            DB.update(photos)
        }
    }


}