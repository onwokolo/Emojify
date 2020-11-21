package com.example.emojify.storage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import java.io.ByteArrayOutputStream


//Grab instance of me using "StorageSystem.storage"
@kotlinx.serialization.Serializable
class Entry(val thumbnail: ByteArray, val date: String, val emotion: String) {
    override fun equals(other: Any?): Boolean {
        other?.let {
            if (it.javaClass == this.javaClass) {
                val that = it as Entry
                return this.date == that.date
            }
        }
        return false
    }

    companion object {
        //grabs the image from the image view and converts it to a bitmap for storage
        //compression=0 means MAX compression, while compression=100 means no compression, preserve original image
        //It is recommended for 30-40 compression for minimum size while maintaining quality
        fun convertImageToByteArray(imageView: ImageView, compression: Int): ByteArray {
            imageView.drawable?.let {
                val bitmapDrawable = it as BitmapDrawable
                val bitmap = bitmapDrawable.bitmap
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, compression, stream);
                return stream.toByteArray()
            }
        return ByteArray(0)
        }
        private fun convertByteArrayToBitmap(b: ByteArray): Bitmap {
            return BitmapFactory.decodeByteArray(b, 0, b.size)
        }
        //Use me to set your image views from the byte array
        fun setImageViewFromByteArray(b: ByteArray, imageView: ImageView) {
            imageView.setImageBitmap(convertByteArrayToBitmap(b))
        }
    }
}