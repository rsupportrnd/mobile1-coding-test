package com.rsupport.mobile1.test.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rsupport.mobile1.test.databinding.ItemImageBinding
import com.rsupport.mobile1.test.model.ImageSrc

class ImageAdapter: RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    private val items: MutableList<ImageSrc> = arrayListOf()

    class ImageViewHolder(val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageAdapter.ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageAdapter.ImageViewHolder, position: Int) {
        with(holder.binding) {
            Glide
                .with(root)
                .load(items[position].src)
                .into(imageView)
        }
    }

    override fun getItemCount(): Int = items.size

    fun addImageList(images: List<ImageSrc>) {
        items.addAll(images)
        notifyItemRangeInserted(items.size + 1, images.size)
    }
}