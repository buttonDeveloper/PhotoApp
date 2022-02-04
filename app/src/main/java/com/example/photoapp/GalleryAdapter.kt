package com.example.photoapp

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photoapp.databinding.PhotoCardBinding
import com.example.photoapp.room.GalleryItem
import timber.log.Timber


class GalleryAdapter(var list: ArrayList<GalleryItem>, var isDeleteMode: Boolean, private val deleteModeListener: OnDeleteModeListener,
                     private val onPhotoClickListener: OnPhotoClickListener) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    val deleteList = ArrayList<GalleryItem>()
    private val deleteIconOff = getDrawable(App.context(), R.drawable.photo_delete_off)
    private val deleteIconOn = getDrawable(App.context(), R.drawable.photo_delete_on)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Timber.d("onCreateViewHolder isDeleteMode = $isDeleteMode")
        val binding = PhotoCardBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.photo_card, parent, false))
        return ViewHolder(binding).apply {
            val listener = object : TouchCallback {
                override fun onLongClick() {
                    deleteModeListener.onDeleteMode(true)
                }

                override fun onClick() {
                    if (isDeleteMode) {
                        val model = list[bindingAdapterPosition]
                        val isSelected = deleteList.contains(model)
                        val toSelect = !isSelected
                        setDeleteIcon(binding.deleteIcon, toSelect)
                        if(toSelect) deleteList.add(model) else deleteList.remove(model)
                        if(toSelect) deleteModeListener.addToDeleteList(model) else deleteModeListener.removeFromDeleteList(model)
                    } else
                        onPhotoClickListener.onPhotoClick(Uri.parse(list[bindingAdapterPosition].photoUri))
                }
            }
            binding.photo.setOnTouchListener(ItemTouchListener(listener, isDeleteMode))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(App.context()).load(Uri.parse(list[position].photoUri)).centerCrop().into(holder.binding.photo)
        if (!isDeleteMode) holder.binding.deleteIcon.visibility = View.INVISIBLE else holder.binding.deleteIcon.visibility = View.VISIBLE
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



