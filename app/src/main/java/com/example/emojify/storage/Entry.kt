package com.example.emojify.storage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import java.io.ByteArrayOutputStream


//Grab instance of me using "StorageSystem.storage"
@kotlinx.serialization.Serializable
class Entry(private val thumbnail: ByteArray, private val date: String, private val emotion: String) {
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
        fun convertImageToByteArray(imageView: ImageView): ByteArray {
            val bitmapDrawable = imageView.drawable as BitmapDrawable
            val bitmap = bitmapDrawable.bitmap
            val stream = ByteArrayOutputStream()
            return stream.toByteArray()

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