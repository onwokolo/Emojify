package com.example.emojify.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.emojify.ApplicationStart
import java.io.File

//Grab instance of me using "StorageSystem.storage"
class StorageSystem(androidContext: Context) {

    private var context = androidContext
    fun getBaseDirectory(): File {
        return(context.filesDir)
    }
    fun getDirectory(file: String): File {
        return context.getDir(file, MODE_PRIVATE);
    }
    fun getFile(file: String): File {
        return File(context.filesDir.absolutePath + file)
    }

    companion object {
        val storage = StorageSystem(ApplicationStart.context)
    }
}