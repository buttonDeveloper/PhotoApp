package com.example.photoapp

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.photoapp.room.GalleryItem
import com.example.photoapp.room.Location
import com.example.photoapp.room.Section
import kotlinx.coroutines.*

class Repository {

    private val scope = CoroutineScope(Job() + Dispatchers.Default)

    private val listPhotosLiveData = MutableLiveData<List<GalleryItem>>()
    fun listPhotosLiveData(): LiveData<List<GalleryItem>> = listPhotosLiveData

    fun loadInitList(callback: (list: List<GalleryItem>) -> Unit) {
        scope.launch(Dispatchers.Main) {
            callback.invoke(DB.getPhotoList())
        }
    }

    fun savePhotos(photos: List<Uri>) {
        scope.launch {
            val list = ArrayList<GalleryItem>()
            for (element in photos) {
                val item = GalleryItem().apply { photoUri = element.toString() }
                list.add(item)
            }
            DB.save(list)
            listPhotosLiveData.postValue(getPhotos()!!)
        }
    }

    fun deletePhotos(list: ArrayList<GalleryItem>) {
        scope.launch {
            DB.delete(list)
            listPhotosLiveData.postValue(getPhotos()!!)
        }
    }

    private suspend fun getPhotos() = DB.getPhotoList()

    suspend fun getSection() = if (DB.getSection() == null) DB.initSection() else DB.getSection()

    fun updateSection(section: String) {
        scope.launch {
            DB.updateSection(Section().apply { this.section = section })
        }
    }

    suspend fun getLocation() = if (DB.getLocation() == null) DB.initLocation() else DB.getLocation()

    fun updateLocation(location: String) {
        scope.launch {
            DB.updateLocation(Location().apply { this.location = location })
        }
    }

    fun destroy() {
        scope.cancel()
    }

}