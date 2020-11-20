package com.example.emojify.storage

import android.widget.ImageView
import java.io.Serializable

//Grab instance of me using "StorageSystem.storage"
class Entry(val thumbnail: ImageView, val date: Object, val emotion: String): Serializable {}