package com.example.photoapp

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.photoapp.databinding.PhotoCardBinding
import com.example.photoapp.room.GalleryItem
import kotlinx.coroutines.*
import timber.log.Timber

class GalleryAdapter(var list: List<GalleryItem>, var isDeleteMode: Boolean, private val modeListener: OnDeleteModeListener, private val deleteListener: OnDeletePhotosListener) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    private val deleteList = ArrayList<GalleryItem>()

    val listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
            Timber.d("e = $e")
            return false
        }

        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            Timber.d("resource = $resource")
            return false
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PhotoCardBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.photo_card, parent, false))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (!isDeleteMode) {
            Glide.with(App.context()).load(Uri.parse(list[position].photoUri)).centerCrop().into(holder.binding.photo)
            setClickBehavior(holder.binding.photo, Uri.parse(list[position].photoUri))
        } else {
            Glide.with(App.context()).load(Uri.parse(list[position].photoUri)).centerCrop().into(holder.binding.photo)
            holder.binding.viewStub.inflate().apply {
                var isSelected = false
                val icon = findViewById<LinearLayout>(R.id.view_stub_inflated).findViewById<ImageView>(R.id.delete_icon)
                setOnClickListener {
                    isSelected = !isSelected
                    icon.setImageDrawable(if (!isSelected) getDrawable(context, R.drawable.photo_delete_off)
                    else getDrawable(context, R.drawable.photo_delete_on))
                    if (isSelected) {
//                        Timber.d("add to deleteList = ${list[position]}")
                        deleteList.add(list[position])
                    } else {
                        deleteList.remove(list[position])
                    }
                    deleteListener.deletePhotosList(deleteList)
                }
            }
        }
    }

    override fun getItemCount() = list.size

    private fun setClickBehavior(v: View, uri: Uri) {
        var isLongClick = false
        val runnable = Runnable {
            this.isDeleteMode = true
            for (i in 0..list.size) this.notifyItemChanged(i)
            modeListener.onDeleteMode(isDeleteMode)
//            Timber.d("run")
            isLongClick = true
            vibrate()
        }
        v.apply {
            setOnTouchListener { v, event ->
//                Timber.d("event.action = ${event.action}")
                if (event.action == MotionEvent.ACTION_DOWN) {
                    Timber.d("down")
                    handler.postDelayed(runnable, 3000)
                    isLongClick = false
                    return@setOnTouchListener false
                } else if (event.action == MotionEvent.ACTION_UP) {
                    if (isLongClick) return@setOnTouchListener true
//                    Timber.d("up")
                    handler.removeCallbacks(runnable)
                    performClick()
                    return@setOnTouchListener false
                } else if (event.action == MotionEvent.ACTION_CANCEL) {
                    handler.removeCallbacks(runnable)
                    return@setOnTouchListener false
                }
                false
            }

            setOnClickListener {
                val intent = Intent(App.context(), FullScreenImage()::class.java)
                intent.putExtra("uri", uri.toString())
                intent.action = Intent.ACTION_OPEN_DOCUMENT
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                App.context().startActivity(intent)

            }
        }
    }

    private fun vibrate() {
        (App.context().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            else vibrate(100)
        }
    }

    class ViewHolder(val binding: PhotoCardBinding) : RecyclerView.ViewHolder(binding.root)


}