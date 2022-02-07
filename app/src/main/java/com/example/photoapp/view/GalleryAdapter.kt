package com.example.photoapp.view

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photoapp.App
import com.example.photoapp.R
import com.example.photoapp.databinding.PhotoCardBinding
import com.example.photoapp.getDrawable
import com.example.photoapp.listener.ItemTouchListener
import com.example.photoapp.listener.OnDeleteModeListener
import com.example.photoapp.listener.OnPhotoClickListener
import com.example.photoapp.listener.TouchCallback
import com.example.photoapp.room.GalleryItem
import timber.log.Timber


class GalleryAdapter(var list: ArrayList<GalleryItem>, private val deleteModeListener: OnDeleteModeListener,
                     private val onPhotoClickListener: OnPhotoClickListener) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    val deleteList = ArrayList<GalleryItem>()
    var isDeleteMode = false
    set(value) {
        if(value) {
            deleteList.clear()
            mode = value
            notifyDataSetChanged()
        } else {
            mode = value
            notifyDataSetChanged()
        }
        field = value
    }
    private val deleteIconOff = getDrawable(App.context(), R.drawable.photo_delete_off)
    private val deleteIconOn = getDrawable(App.context(), R.drawable.photo_delete_on)

    companion object {
        var mode = false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Timber.d("onCreateViewHolder isDeleteMode = $isDeleteMode")
        val binding = PhotoCardBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.photo_card, parent, false))
        return ViewHolder(binding).apply {
            val listener = object : TouchCallback {
                override fun onLongClick() {
                    deleteModeListener.onDeleteMode(true)
                    this@GalleryAdapter.isDeleteMode = true
                }

                override fun onClick() {
                    if (isDeleteMode) {
                        val item = list[bindingAdapterPosition]
                        val isSelected = deleteList.contains(item)
                        val toSelect = !isSelected
                        setDeleteIcon(binding.deleteIcon, toSelect)
                        if(toSelect) deleteList.add(item) else deleteList.remove(item)
                    } else
                        onPhotoClickListener.onPhotoClick(Uri.parse(list[bindingAdapterPosition].photoUri))
                }
            }
            binding.photo.setOnTouchListener(ItemTouchListener(listener))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(App.context()).load(Uri.parse(list[position].photoUri)).centerCrop().into(holder.binding.photo)
        if (!isDeleteMode) {
            holder.binding.deleteIcon.visibility = View.INVISIBLE
        } else {
            setDeleteIcon(holder.binding.deleteIcon, !isDeleteMode)
            holder.binding.deleteIcon.visibility = View.VISIBLE
        }
    }

    override fun getItemCount() = list.size

    private fun setDeleteIcon(v: ImageView, isSelected: Boolean) {
        v.setImageDrawable(if (!isSelected) deleteIconOff else deleteIconOn)
        if (isSelected) v.animate().rotation(180F).start() else v.animate().rotation(360F).start()
    }

    class DiffUtilCallback(var oldList: ArrayList<GalleryItem>, var newList: List<GalleryItem>) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].photoUri == newList[newItemPosition].photoUri
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    fun updateList(newList: List<GalleryItem>) {
        val diffCallback = DiffUtilCallback(this.list, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.list = ArrayList(newList)
        diffResult.dispatchUpdatesTo(this)
    }


    class ViewHolder(val binding: PhotoCardBinding) : RecyclerView.ViewHolder(binding.root)

}



